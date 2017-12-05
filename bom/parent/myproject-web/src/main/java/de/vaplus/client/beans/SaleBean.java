package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.PaymentControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.InvoiceItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.backingbeans.RetailProductBean;
import de.vaplus.client.entity.InvoiceItemEntity;
import de.vaplus.client.entity.StockPickslipEntity;

@ManagedBean(name = "saleBean")
@SessionScoped
public class SaleBean implements Serializable {
	
	private static final long serialVersionUID = -8661127756355675513L;
	
	private static final String FORM_FIELD_EAN = "ean";
	private static final String FORM_FIELD_SERIAL = "serial";
	private static final String FORM_FIELD_QUANTITY = "quantity";

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private StockControllerInterface stockController;

	@EJB
	private OrderControllerInterface orderController;

	@EJB
	private PaymentControllerInterface paymentController;
	
	@EJB
	private CommissionControllerInterface commissionController;
	
	@EJB
	private TaxRateControllerInterface taxRateControllerController;

	@Inject
	private FacesContext facesContext;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	@ManagedProperty(value="#{shopBean}")
	private ShopBean shopBean;
	
	@ManagedProperty(value="#{customerBean}")
	private CustomerBean customerBean;

	@ManagedProperty(value="#{productBean}")
	private ProductBean productBean;

	@ManagedProperty(value="#{paymentBean}")
	private PaymentBean paymentBean;

	private Invoice invoice;
	private Invoice selectedInvoice;
	private InvoiceItem invoiceItem;
	private InvoiceItem manualInvoiceItem;
	
	private List<? extends Payment> paymentList;

	private String ean;
	private boolean eanProductFound;
	private Product product;
	private String serial;
	private BigDecimal quantity;
	
	private String nextFormField;

	private List<? extends BaseProduct> orderableProductList;
	
	public String saveInvoice(){

		Iterator<? extends InvoiceItem> i = invoice.getInvoiceItemList().iterator();
		InvoiceItem invoiceItem = null;
		
		if(invoice.getInvoiceItemList().size() == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rechnung enthält keine Artikel!", null));
			return "";
		}
		
//		invoice.setStockPickslip(pickslip);

		invoice.setEnabled(true);
		invoice.setEffectiveDate(new Date());
		
		
		//check every item on STOCK
		
		Order order = orderController.factoryOrder();
		order.setEnabled(true);
		order.setEffectiveDate(new Date());
		order.setShop(invoice.getShop());
		order.setUser(invoice.getUser());
		order.setCustomer(invoice.getCustomer());
		order.setInfo(invoice.getNote());
		
		while(i.hasNext()){
			invoiceItem = i.next();
			
			if(invoiceItem.getProduct() == null || ! invoiceItem.getProduct().isStockControlled() )
				continue;

			try {
				orderController.addRetailItemToOrder(order, invoiceItem.getProduct(), invoiceItem.getSerial(), invoiceItem.getQuantity());
			} catch (Exception e) {
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
				return "";
			}
			
		}
		try {
			order = orderController.saveOrder(order);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage(), null));
			return "";
		}
		
		
		StockPickslip pickslip = stockController.factoryNewStockPickslip();
		pickslip.setEnabled(true);
		pickslip.setEffectiveDate(new Date());
		pickslip.setCustomer(invoice.getCustomer());
		pickslip.setShop(invoice.getShop());
		pickslip.setUser(invoice.getUser());
		pickslip.setStock(shopBean.getActiveShop().getStock());
		pickslip.setSrc_stock(shopBean.getActiveShop().getStock());
		pickslip.setEnabled(true);
		pickslip.setOrder(order);
		

		i = invoice.getInvoiceItemList().iterator();
		StockMovement movement;
		while(i.hasNext()){
			invoiceItem = i.next();
			
			if(invoiceItem.getProduct() == null || ! invoiceItem.getProduct().isStockControlled())
				continue;

			movement = stockController.factoryNewStockMovement();
			movement.setProduct(invoiceItem.getProduct());
			movement.setQuantity(invoiceItem.getQuantity());
			movement.setSerial(invoiceItem.getSerial());
			movement.setPurchasePrice(invoiceItem.getProduct().getAvergePurchasePrice());
			
			((List<StockMovement>)pickslip.getStockMovementList()).add(movement);
			
		}
		
		try {
			pickslip = stockController.saveStockPickslip(pickslip);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return "";
		}
		
		invoice.setStockPickslip(pickslip);
		
		try {
			invoice = orderController.saveInvoice(invoice);
			selectedInvoice = invoice;
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return "";
		}
		
		invoice = null;
		
		customerBean.refreshCustomer();
		
		return "invoiceView";
	}
	

	public String cancelInvoice(Invoice invoice) throws Exception {
		Invoice cancelationInvoice = orderController.factoryInvoice();
		invoice.setCancelationInvoice(cancelationInvoice);
		cancelationInvoice.setCanceledinvoice(invoice);
		

		Iterator<? extends InvoiceItem> i = invoice.getInvoiceItemList().iterator();
		InvoiceItem invoiceItem = null;
		
		if(invoice.getInvoiceItemList().size() == 0){
			return null;
		}

		cancelationInvoice.setEnabled(true);
		cancelationInvoice.setEffectiveDate(new Date());
		cancelationInvoice.setShop(invoice.getShop());
		cancelationInvoice.setUser(invoice.getUser());
		cancelationInvoice.setCustomer(invoice.getCustomer());
		cancelationInvoice.setNote(invoice.getNote());
		
		cancelationInvoice.setPayment(invoice.getPayment());
		
		Order order = orderController.factoryOrder();
		order.setEnabled(true);
		order.setEffectiveDate(new Date());
		order.setShop(invoice.getShop());
		order.setUser(invoice.getUser());
		order.setCustomer(invoice.getCustomer());
		order.setInfo(invoice.getNote());

		
		while(i.hasNext()){
			invoiceItem = i.next();
			
			if(invoiceItem.getProduct() != null && ! invoiceItem.getProduct().isStockControlled())
				continue;

			try {
				
				if(invoiceItem.getProduct() != null)
					orderController.addRetailItemToOrder(order, invoiceItem.getProduct(), invoiceItem.getSerial(), invoiceItem.getQuantity().multiply(new BigDecimal(-1)));
				
				InvoiceItem item = orderController.factoryInvoiceItem();
				
				item.setInvoice(cancelationInvoice);
				
				if(invoiceItem.getProduct() != null)
					item.setProduct(invoiceItem.getProduct());
				
				item.setTitle(invoiceItem.getTitle());
				item.setUnitPrice(invoiceItem.getUnitPrice());
				item.setSerial(invoiceItem.getSerial());
				item.setQuantity(invoiceItem.getQuantity());
				item.setUnitPurchasePrice(invoiceItem.getUnitPurchasePrice());
				
				item.setCommission(null);
				item.setCommission(item.getCommission().addCommissionable(invoiceItem.getCommission()));
				item.getCommission().setPoints(item.getCommission().getPoints().multiply(new BigDecimal(-1)));

				
				orderController.addInvoiceItemToInvoice(cancelationInvoice, item);
				
			} catch (Exception e) {
				return null;
			}
			
		}
		try {
			order = orderController.saveOrder(order);
		} catch (Exception e) {
			return null;
		}


		
		cancelationInvoice.setCommission(null);
		
		cancelationInvoice.setCommission(cancelationInvoice.getCommission().addCommissionable(invoice.getCommission()));
		cancelationInvoice.getCommission().setPoints(cancelationInvoice.getCommission().getPoints().multiply(new BigDecimal(-1)));

		
		StockPickslip pickslip = stockController.factoryNewStockPickslip();
		pickslip.setEnabled(true);
		pickslip.setEffectiveDate(new Date());
		pickslip.setCustomer(invoice.getCustomer());
		pickslip.setShop(invoice.getShop());
		pickslip.setUser(invoice.getUser());
		pickslip.setStock(invoice.getStockPickslip().getStock());
		pickslip.setDst_stock(invoice.getStockPickslip().getSrc_stock());
		pickslip.setEnabled(true);
		pickslip.setOrder(order);
		

		i = invoice.getInvoiceItemList().iterator();
		StockMovement movement;
		while(i.hasNext()){
			invoiceItem = i.next();
			
			if(invoiceItem.getProduct() == null)
				continue;
			
			if(! invoiceItem.getProduct().isStockControlled())
				continue;

			movement = stockController.factoryNewStockMovement();
			movement.setProduct(invoiceItem.getProduct());
			movement.setQuantity(invoiceItem.getQuantity());
			movement.setSerial(invoiceItem.getSerial());
			movement.setPurchasePrice(invoiceItem.getProduct().getAvergePurchasePrice());
			
			((List<StockMovement>)pickslip.getStockMovementList()).add(movement);
			
		}
		
		try {
			pickslip = stockController.saveStockPickslip(pickslip);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
		
		cancelationInvoice.setStockPickslip(pickslip);
		

		
		try {
			cancelationInvoice = orderController.saveInvoice(cancelationInvoice);
//			invoice = orderController.saveInvoice(invoice);
			selectedInvoice = cancelationInvoice;
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return "";
		}
		
		customerBean.refreshCustomer();
		
		return "invoiceView";
	}
	
	public void newInvoice(){

		invoice = orderController.factoryInvoice();
		
//		pickslip = stockController.factoryNewStockPickslip();
//		order = orderController.factoryOrder();
		
		invoice.setCustomer(customerBean.getCustomer());
		invoice.setShop(shopBean.getActiveShop());
		invoice.setUser(userBean.getActiveUser());
		invoice.setIntroduction("");
//		invoice.setDrafterLine("");
		invoice.setClosing("");
		invoice.setRecipientLine1("");
		invoice.setRecipientLine2("");
		invoice.setRecipientLine3("");
		invoice.setRecipientLine4("");
		invoice.setRecipientLine5("");
		
	}
	
	public void newCashInvoice(){

		invoice = orderController.factoryInvoice();
		
//		pickslip = stockController.factoryNewStockPickslip();
//		order = orderController.factoryOrder();
		
//		invoice.setCustomer(customerBean.getCustomer());
		invoice.setCustomerAccount(paymentController.getCashCustomerAccount());
		invoice.setShop(shopBean.getActiveShop());
		invoice.setUser(userBean.getActiveUser());
		invoice.setIntroduction("");
//		invoice.setDrafterLine("");
		invoice.setClosing("");
		invoice.setRecipientLine1("");
		invoice.setRecipientLine2("");
		invoice.setRecipientLine3("");
		invoice.setRecipientLine4("");
		invoice.setRecipientLine5("");
		
	}
	
	public void clearProduct(){
		this.ean = null;
		this.eanProductFound = false;
		this.product = null;
		this.serial = null;
		this.quantity = null;
		nextFormField = FORM_FIELD_EAN;
	}
	
	public void loadProductByEan(){
		
		if(ean == null || ean.length() == 0){
			clearProduct();
			return;
		}
		
		product = productController.getProductByEAN(ean);
		
		if(product == null){
			setEan(null);
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Artikel nicht gefunden!", null));
			setEanProductFound(false);
			nextFormField = FORM_FIELD_EAN;
		}else{
			setEanProductFound(true);
		}
		
		initProduct();
	}
	
	public void initProduct(){
		
		if(product != null){

			this.setQuantity(new BigDecimal(1));
			this.setSerial(null);
			
			if(this.product.isSerialRequired()){
				nextFormField = FORM_FIELD_SERIAL;
			}else{
				nextFormField = FORM_FIELD_QUANTITY;
			}
		}
	}
	

	public boolean checkSerial(){
		return checkSerial(true);
	}
		
	public boolean checkSerial(boolean autoInsert){
		
		if(product == null){
			return true;
		}
		
		if(!product.isSerialRequired()){
			return true;
		}

		if(this.serial == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Keine Seriennummer angegeben!", null));
			this.setSerial(null);
			nextFormField = FORM_FIELD_SERIAL;
			return false;
		}

		if(this.serial.length() < 4){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Seriennummer muss mind. 4 Zeichen lang sein!", null));
			this.setSerial(null);
			nextFormField = FORM_FIELD_SERIAL;
			return false;
		}
		
		// check if serial on Stock
		if(!stockController.isProductOnStock(invoice.getShop().getStock(), this.product, this.serial)){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Das Produkt mit dieser Seriennummer ist in diesem Lager nicht vorhanden!", null));
			this.setSerial(null);
			nextFormField = FORM_FIELD_SERIAL;
			return false;
		}
			
			

		
		
		//check if serial already found
		
		Iterator<? extends InvoiceItem> i = invoice.getInvoiceItemList().iterator();
		InvoiceItem invoiceItem = null;
		while(i.hasNext()){
			invoiceItem = i.next();
			
			if(invoiceItem.getProduct() != null && invoiceItem.getProduct().getId() == this.product.getId()){
				
				if(invoiceItem.getProduct().isSerialRequired()){
					
					if(invoiceItem.getSerial().equalsIgnoreCase(this.getSerial())){
						facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Artikel mit dieser IMEI/Seriennummer bereits hinzugefügt!", null));
						nextFormField = FORM_FIELD_SERIAL;
						this.setSerial(null);
						return false;
					}
				}
			}
		}

		nextFormField = FORM_FIELD_EAN;
		
		if(autoInsert){
			addProductToInvoice();
		}
		
		return true;
	}


	public boolean checkQuantity(){
		return checkQuantity(true);
	}
	
	public boolean checkQuantity(boolean autoInsert){

		if(this.quantity == null || this.quantity.compareTo(new BigDecimal(0)) == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine Anzahl angegeben!", null));
			nextFormField = FORM_FIELD_QUANTITY;
			return false;
		}

		if(this.quantity.compareTo(new BigDecimal(0)) < 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anzahl muss positiv sein!", null));
			nextFormField = FORM_FIELD_QUANTITY;
			return false;
		}

		nextFormField = FORM_FIELD_EAN;
		
		if(autoInsert){
			addProductToInvoice();
		}

		return true;
	}
	

	
	public void checkInvoiceItem(InvoiceItem invoiceItem){

		BigDecimal minPrice = productBean.getMinimumGrossPrice(invoiceItem.getProduct());
		BigDecimal maxPrice = invoiceItem.getProduct().getCommission().getGrossPrice();
		
		if(invoiceItem.getUnitGrossPrice().compareTo(maxPrice) > 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preis grösser als VK.", null));
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Preis wurde angepasst.", null));
			invoiceItem.setUnitGrossPrice(maxPrice);
		}
		
		if(invoiceItem.getUnitGrossPrice().compareTo(minPrice) < 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preis kleiner als Mindestpreis", null));
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Preis wurde angepasst.", null));
			invoiceItem.setUnitGrossPrice(minPrice);
		}
		
	}

	
	private void addProductToInvoice(){
		
		
		if(this.product == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kein Produkt gewählt!", null));
			return;
		}
		
		if(!checkSerial(false)){
			return;
		}
		
		if(!checkQuantity(false)){
			return;
		}
		
		// check if quantity on stock
		if(this.product.isStockControlled() && stockController.getStockLevel(invoice.getShop().getStock(), this.product).compareTo(new BigDecimal(0)) == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Dieses Produkt ist in diesem Lager nicht vorhanden.", null));
			this.clearProduct();
			return;
		}
		
		Commissionable commission = null;
		BigDecimal unitPrice = null;
		
		InvoiceItem invoiceItem;
		
		try {
			
			invoiceItem = orderController.addInvoiceItemToInvoice(getInvoice(), getProduct(), getQuantity(), getSerial());
			
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, e.getLocalizedMessage(), null));
			this.setSerial(null);
			return;
		}

		if(this.product.isStockControlled() && stockController.getStockLevel(invoice.getShop().getStock(), this.product).compareTo(invoiceItem.getQuantity()) < 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Dieses Produkt ist nicht in genügender Menge in diesem Lager vorhanden. Die Anzahl wurde angepasst.", null));
			invoiceItem.setQuantity(stockController.getStockLevel(invoice.getShop().getStock(), this.product));
		}
		

		clearProduct();
		calculateCommission(invoice);
	}

	public void calculateCommission(){
		calculateCommission(this.getInvoice());
	}
	
	public void calculateCommission(Invoice invoice){
		orderController.calculateCommission(invoice);
		orderController.calculteSubsidy(invoice);
		orderController.calculateCommission(invoice);
	}
	
	public void disableAutomaticSubsidy(){
		this.invoice.setNoSubsidy(true);
		calculateCommission(invoice);
	}
	
	public void removeInvoiceItemFromInvoice(InvoiceItem invoiceItem){
		orderController.removeInvoiceItemFromInvoice(getInvoice(), invoiceItem);
		calculateCommission(invoice);
	}
	
	public List<? extends BaseProduct> getOrderableProductList(){
		if(orderableProductList == null){
			orderableProductList = productController.getRetailProductList();
		}
		
		return orderableProductList;
	}

	public void setOrderableProductList(List<? extends BaseProduct> orderableProductList) {
		this.orderableProductList = orderableProductList;
	}

	public String getNextFormField() {
		if(nextFormField == null)
			nextFormField = FORM_FIELD_EAN;
		return nextFormField;
	}

	public void setNextFormField(String s) {
		
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public ShopBean getShopBean() {
		return shopBean;
	}

	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}
	
	public ProductBean getProductBean() {
		return productBean;
	}

	public void setProductBean(ProductBean productBean) {
		this.productBean = productBean;
	}

	public PaymentBean getPaymentBean() {
		return paymentBean;
	}

	public void setPaymentBean(PaymentBean paymentBean) {
		this.paymentBean = paymentBean;
	}

	public CustomerBean getCustomerBean() {
		return customerBean;
	}

	public void setCustomerBean(CustomerBean customerBean) {
		this.customerBean = customerBean;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Invoice getSelectedInvoice() {
		return selectedInvoice;
	}

	public void setSelectedInvoice(Invoice selectedInvoice) {
		this.selectedInvoice = selectedInvoice;
	}

	public InvoiceItem getInvoiceItem() {
		return invoiceItem;
	}

	public void setInvoiceItem(InvoiceItem invoiceItem) {
		this.invoiceItem = invoiceItem;
	}

	public List<? extends Payment> getPaymentList() {
		if(paymentList == null){
			paymentList = paymentController.getPaymentList();
		}
		return paymentList;
	}

	public void setPaymentList(List<? extends Payment> paymentList) {
		this.paymentList = paymentList;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public boolean isEanProductFound() {
		return eanProductFound;
	}

	public void setEanProductFound(boolean eanProductFound) {
		this.eanProductFound = eanProductFound;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public BigDecimal getQuantity() {
		if(quantity == null)
			quantity = new BigDecimal(0);
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}


	public InvoiceItem getManualInvoiceItem() {
		if(manualInvoiceItem == null){
			manualInvoiceItem = orderController.factoryInvoiceItem();
			
			manualInvoiceItem.setManualItem(true);
			manualInvoiceItem.setQuantity(new BigDecimal(1));
			manualInvoiceItem.getCommission().setTax(taxRateControllerController.getDefaultTaxRate().getTax());
			
		}
		return manualInvoiceItem;
	}


	public void setManualInvoiceItem(InvoiceItem manualInvoiceItem) {
		this.manualInvoiceItem = manualInvoiceItem;
	}

	
	public void saveManualInvoiceItem(){
		
		System.out.println("save manual invoice Item: "+this.getManualInvoiceItem().getTitle());
		
		this.getManualInvoiceItem().setUnitPrice(
				this.getManualInvoiceItem().getCommission().getPrice()
		);
		
		this.getManualInvoiceItem().setUnitPurchasePrice(
				this.getManualInvoiceItem().getCommission().getPrice()
		);
		
//		
//		this.getManualInvoiceItem().getCommission().setGrossPrice(
//				this.getManualInvoiceItem().getUnitPrice().multiply(this.getManualInvoiceItem().getQuantity())
//		);
//		
//		this.getManualInvoiceItem().setUnitPurchasePrice(this.getManualInvoiceItem().getUnitPrice());
		
		if(getManualInvoiceItem().getInvoice() == null){
			try {
				getManualInvoiceItem().setInvoice(invoice);
				orderController.addInvoiceItemToInvoice(invoice, getManualInvoiceItem());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		calculateCommission(invoice);
	}
}
