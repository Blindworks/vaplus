package de.vaplus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.PaymentControllerInterface;
import de.vaplus.api.PdfControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.FileSystemFile;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.InvoiceItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentShopAccount;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.TaxRate;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.eao.InvoiceEao;
import de.vaplus.client.eao.NumberRangeEao;
import de.vaplus.client.eao.OrderEao;
import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.InvoiceItemEntity;
import de.vaplus.client.entity.OrderEntity;
import de.vaplus.client.entity.OrderItemEntity;


@Stateless
public class OrderController implements OrderControllerInterface {

	private static final long serialVersionUID = -5234734486100703013L;

	@EJB
	private CommissionControllerInterface commissionController;
	
	@EJB
	private PropertyControllerInterface propertyController;
	
	@EJB
	private PaymentControllerInterface paymentController;

	@EJB
	private TaxRateControllerInterface taxRateController;
	
	@EJB
	private PdfControllerInterface pdfController;
	
	@EJB
	private FileControllerInterface fileController;
	
	@EJB
	private ShopControllerInterface shopController;
	
	@Inject
    private OrderEao orderEao;
	
	@Inject
    private InvoiceEao invoiceEao;

	@Inject
    private NumberRangeEao numberRangeEao;

	@Override
	public Order factoryOrder(){
		return new OrderEntity();
	}

	@Override
	public Invoice factoryInvoice(){
		return new InvoiceEntity();
	}
	
	@Override
	public InvoiceItem factoryInvoiceItem(){
		return new InvoiceItemEntity();
	}

	@Override
	public Order saveOrder(Order order) throws Exception {

		setNextOrderNumber(order);
		
		order = orderEao.saveOrder((OrderEntity) order);

		commissionController.updateUserComissionHistory(order.getUser());
		commissionController.updateShopComissionHistory(order.getShop());
		
		return order;
	}


	@Override
	public Invoice saveInvoice(Invoice invoice) throws Exception {
		
		
		calculateCommission(invoice);
		
		if(invoice.getId() != 0)
			throw new Exception("Rechnung nicht änderbar!");
		

		// Recipient Lines
		String[] recipientLines = new String[5];
		int line = 0;
		
		if(invoice.getCustomer() == null){
			recipientLines[line] = "BAR Kunde";
		}else{

			recipientLines[line++] = invoice.getCustomer().getName();
			recipientLines[line++] = invoice.getCustomer().getAddress().getStreet()+" "+invoice.getCustomer().getAddress().getStreetNumber();
			
			if(invoice.getCustomer().getAddress().getAddressline() != null && invoice.getCustomer().getAddress().getAddressline().length() > 0){
				recipientLines[line++] = invoice.getCustomer().getAddress().getAddressline();
			}
			
			recipientLines[line++] = invoice.getCustomer().getAddress().getCountry() + " " + invoice.getCustomer().getAddress().getZip() + " " + invoice.getCustomer().getAddress().getCity();
		}

		invoice.setRecipientLine1(recipientLines[0]);
		invoice.setRecipientLine2(recipientLines[1]);
		invoice.setRecipientLine3(recipientLines[2]);
		invoice.setRecipientLine4(recipientLines[3]);
		invoice.setRecipientLine5(recipientLines[4]);
		
		
		// Invoice text
//		invoice.setDrafterLine(propertyController.getStringProperty("drafterLine", ""));
		invoice.setIntroduction(propertyController.getStringProperty("invoiceIntroduction", ""));
		invoice.setClosing(propertyController.getStringProperty("invoiceClosing", ""));
		
		
		// Payment Info
		invoice.setPaymentInformation(invoice.getPayment().getInvoiceText());
		
		
		// Payment Transactions preparations
		if(invoice.getCustomer() == null)
			invoice.setCustomerAccount(paymentController.getPseudoCustomerAccount());
		else
			invoice.setCustomerAccount(paymentController.getCustomerAccount(invoice.getCustomer()));
		
		
		
		
		// SET Due Date for invoice
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, invoice.getPayment().getTermOfPayment());
		invoice.setDueDate(c.getTime());
		
		setNextInvoiceNumber(invoice);
		
		invoice = orderEao.saveInvoice((InvoiceEntity) invoice);

		commissionController.updateUserComissionHistory(invoice.getUser());
		commissionController.updateShopComissionHistory(invoice.getShop());
		
		
		
		
		// Payment Transactions 
		PaymentShopAccount paymentShopAccount = paymentController.getPaymentShopAccount(invoice.getPayment(), invoice.getShop());
		PaymentAccount customerAccount = invoice.getCustomerAccount();
		

		if(! invoice.isCancelation()){
			// transfer amount from customer to payment
			paymentController.newPaymentAccountTransaction(invoice.getCommission().getGrossPrice().setScale(2, RoundingMode.HALF_UP), customerAccount, paymentShopAccount.getAccount(), invoice);
			
			// settle account if allowed
			if(invoice.getPayment().isDirect())
				paymentController.newPaymentAccountTransaction(invoice.getCommission().getGrossPrice().setScale(2, RoundingMode.HALF_UP), paymentShopAccount.getSettlingAccount(), customerAccount, invoice);
		}else{
			// transfer amount back to customer
			paymentController.newPaymentAccountTransaction(invoice.getCommission().getGrossPrice().setScale(2, RoundingMode.HALF_UP), paymentShopAccount.getAccount(), customerAccount, invoice);
			
			// settle account if allowed
			if(invoice.getPayment().isDirect())
				paymentController.newPaymentAccountTransaction(invoice.getCommission().getGrossPrice().setScale(2, RoundingMode.HALF_UP), customerAccount, paymentShopAccount.getSettlingAccount(), invoice);
			
		}

		
		java.io.File tmpFile = pdfController.writeInvoiceTmpFile(invoice);
        
        FileSystemFile invoiceFile = fileController.saveInvoice(tmpFile, invoice.getNumber());
        
        tmpFile.delete();
		
        invoice.setInvoiceFile(invoiceFile);
        
        invoice = orderEao.saveInvoice((InvoiceEntity) invoice);
		
		return invoice;
	}
	
	private void setNextInvoiceNumber(Invoice invoice) throws Exception{
		if(invoice.getNumber() != null)
			return;
		
		String prefix;
		
		Shop shop = shopController.refreshShop(invoice.getShop());
		
		long nextNumber = numberRangeEao.getNextInvoiceNumber(shop);
		
		if(invoice.isCancelation())
			prefix = "GU";
		else
			prefix = "RE";
		
		String invoiceNumber = prefix+"-"+shop.getId()+"-"+nextNumber;
		
		Invoice i = getInvoiceByNumber(invoiceNumber);
		if(i != null){
			throw new Exception("Rechnungsnummer "+invoiceNumber+" bereits vorhanden. Bitte prüfen Sie Ihre Einstellungen.");
		}

//		shopController.saveShop(shop);
		
		invoice.setNumber(invoiceNumber);
	}
	
	private void setNextOrderNumber(Order order) throws Exception{
		if(order.getNumber() != null)
			return;
		
		Shop shop = shopController.refreshShop(order.getShop());
		
		long nextNumber = numberRangeEao.getNextOrderNumber(shop);
		
		String orderNumber = "BE-"+shop.getId()+"-"+nextNumber;
		
		Order o = getOrderByNumber(orderNumber);
		if(o != null){
			throw new Exception("Bestellnummer "+orderNumber+" bereits vorhanden. Bitte prüfen Sie Ihre Einstellungen.");
		}

//		shopController.saveShop(shop);
		
		order.setNumber(orderNumber);
	}
	
	@Override
	public void addRetailItemToOrder(Order order, Product product, String serial, BigDecimal quantity) throws Exception{
		
		Iterator<OrderItemEntity> i = (Iterator<OrderItemEntity>) order.getOrderItemList().iterator();
		OrderItemEntity oi;
		
		if(product.isSerialRequired()){

			while(i.hasNext()){
				oi = i.next();
				if(oi.getProduct() != null && oi.getProduct().equals(product)){
					if(oi.getSerial().equalsIgnoreCase(serial)){
						throw new Exception("Produkt mit Seriennummer "+serial+" bereits in Liste.");
					}
				}
			}
			
		}else{
			while(i.hasNext()){
				oi = i.next();
				if(oi.getProduct() != null && oi.getProduct().equals(product)){
					oi.setQuantity(oi.getQuantity().add(new BigDecimal(1)));
					return;
				}
			}
		}
		
		((List<OrderItemEntity>)order.getOrderItemList()).add( new OrderItemEntity(order, product, serial, quantity));
	}
	
	@Override
	public void addOrderItemToOrder(Order order, String title, BigDecimal quantity, String serial, Commissionable commission, BigDecimal unitPrice, boolean blockedItem) {
		
		OrderItemEntity oi = new OrderItemEntity();
		oi.setTitle(title);
		oi.setQuantity(quantity);
		oi.setSerial(serial);
		oi.setCommission(commission);
		oi.setUnitPrice(unitPrice);
		oi.setBlockedItem(blockedItem);
		
		((List<OrderItemEntity>)order.getOrderItemList()).add( oi);
	}
	


	@Override
	public void removeInvoiceItemFromInvoice(Invoice invoice, InvoiceItem invoiceItem) {
		invoice.getInvoiceItemList().remove(invoiceItem);
	}
	
	@Override
	public InvoiceItem addInvoiceItemToInvoice(Invoice invoice, Product product, BigDecimal quantity, String serial) throws Exception {
		
		Iterator<? extends InvoiceItem> i = invoice.getInvoiceItemList().iterator();
		InvoiceItemEntity invoiceItem = null;
		while(i.hasNext()){
			invoiceItem = (InvoiceItemEntity) i.next();
			
			if(invoiceItem.getProduct() != null && invoiceItem.getProduct().getId() == product.getId()){

				// single product
				if(invoiceItem.getProduct().isSerialRequired()){
					
					if(invoiceItem.getSerial().equalsIgnoreCase(serial)){
						throw new Exception("Artikel mit dieser IMEI/Seriennummer bereits in Liste!");
					}else{
						invoiceItem = null;
						break;
					}
					
				}else{
					// add more items after loop so break out
					break;
				}
				
			}else{
				invoiceItem = null;
			}
		}
		
		if(invoiceItem != null){
			invoiceItem.setQuantity(invoiceItem.getQuantity().add(quantity));
		}else{
			invoiceItem = new InvoiceItemEntity(invoice, product, serial, quantity);
			invoiceItem.setUnitPurchasePrice(product.getAvergePurchasePrice());
			((List<InvoiceItemEntity>)invoice.getInvoiceItemList()).add(invoiceItem);
		}

		
		return invoiceItem;
		
	}
	

	@Override
	public void addInvoiceItemToInvoice(Invoice invoice, InvoiceItem invoiceItem) throws Exception {
		((List<InvoiceItemEntity>)invoice.getInvoiceItemList()).add((InvoiceItemEntity) invoiceItem);
	}

	@Override
	public void removeRetailItemFromOrder(Order order,
			OrderItem orderItem) {
		
		order.getOrderItemList().remove(orderItem);
	}
	
	@Override
	public void calculateCommission(Invoice invoice) {
		commissionController.calculateCommission(invoice);
	}

	@Override
	public void calculateCommission(Order selectedOrder) {
		commissionController.calculateCommission(selectedOrder);
	}
	
	@Override
	public void calculteSubsidy(Invoice invoice){
		
		try{
		
		if(invoice.getSubsidyContract() != null && invoice.getSubsidyContract().getSubsidyBudgetary().compareTo(new BigDecimal(0)) > 0){
			
			Iterator<? extends InvoiceItem> i = invoice.getInvoiceItemList().iterator();
			InvoiceItem ii;
			BigDecimal retailSum = new BigDecimal(0);
			BigDecimal subsidy = new BigDecimal(0);
			while(i.hasNext()){
				ii = i.next();
				if(ii.getProduct() == null && ii.getTitle().equalsIgnoreCase(propertyController.getStringProperty("subsidyTitle", "Vertrags-Subvention"))){
					i.remove();
					continue;
				}	
				
				retailSum = retailSum.add(ii.getCommission().getPrice());
			}

			if(retailSum.compareTo(new BigDecimal(0)) == 0)
				return;
			
			if(invoice.getSubsidyContract().getSubsidyBudgetary().compareTo(retailSum) > 0){
				// retail sum SMALER than budget
				subsidy = retailSum;
			}else{
				// retail sum GREATER than budget
				subsidy = invoice.getSubsidyContract().getSubsidyBudgetary();
			}
			
			// flip sign
			BigDecimal subsidy_negated = subsidy.multiply(new BigDecimal(-1));
			
			// points
			BigDecimal points = commissionController.calculatePointsPerProfit(subsidy_negated);
			
			// Special User Points
			BigDecimal userPoints = commissionController.calculateUserPointsPerCommission(invoice.getUser(), subsidy_negated);
			if(userPoints != null && userPoints.compareTo(new BigDecimal(0)) > 0){
				// Special User Points
				points = userPoints; 
			}
			
			// create subsidy
			
			TaxRate taxRate = taxRateController.getDefaultTaxRate();
			
			Commissionable commission = commissionController.factoryNewCommission();
			commission.setCommission(subsidy_negated);
			commission.setPoints(points);
			commission.setPrice(subsidy_negated);
			commission.setTax(taxRate.getTax());
			
			if(invoice.isNoSubsidy()){
				return;
			}
			
			InvoiceItem subsidyInvoiceItem = factoryInvoiceItem();
			
			subsidyInvoiceItem.setTitle(propertyController.getStringProperty("subsidyTitle", "Vertrags-Subvention"));
			subsidyInvoiceItem.setQuantity(new BigDecimal(1));
			subsidyInvoiceItem.setBlockedItem(true);
			subsidyInvoiceItem.setCommission(commission);
			subsidyInvoiceItem.setUnitPrice(subsidy_negated);
			subsidyInvoiceItem.setInvoice(invoice);
			
			addInvoiceItemToInvoice(invoice, subsidyInvoiceItem);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public List<? extends Order> getOrderList(Shop shop, User user, Date from,
			Date to) {
		
		return orderEao.getOrderList(shop, user, null, from, to, false);
	}


	@Override
	public List<? extends Invoice> getInvoiceList(Shop shop, User user, Date from, Date to) {
		
		return orderEao.getInvoiceList(shop, user, null, from, to, false);
	}

	@Override
	public Invoice getInvoiceByNumber(String number) {
		return invoiceEao.getInvoice(number);
	}

	@Override
	public Order getOrderByNumber(String number) {
		return orderEao.getOrder(number);
	}

}
