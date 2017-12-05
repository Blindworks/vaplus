package de.vaplus;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.PaymentControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentAccountTransaction;
import de.vaplus.api.entity.PaymentShopAccount;
import de.vaplus.api.entity.PaymentStatus;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.eao.PaymentEao;
import de.vaplus.client.eao.ShopEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.CustomerEntity;
import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.PaymentAccountEntity;
import de.vaplus.client.entity.PaymentAccountTransactionEntity;
import de.vaplus.client.entity.PaymentEntity;
import de.vaplus.client.entity.PaymentShopAccountEntity;
import de.vaplus.client.entity.PaymentStatusEntity;
import de.vaplus.client.entity.ShopAliasEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.ShopGroupEntity;
import de.vaplus.client.entity.VOEntity;


@Stateless
public class PaymentController implements PaymentControllerInterface {

	private static final long serialVersionUID = -3393575099213115473L;

	@EJB
	private CustomerControllerInterface customerController;

	@EJB
	private PropertyControllerInterface propertyController;

	@EJB
	private ShopControllerInterface shopController;
	
	@Inject
    private PaymentEao paymentEao;

	@Override
	public List<? extends Payment> getPaymentList(){
		return getPaymentList(false);
	}

	@Override
	public Payment getPaymentById(long id) {
		return paymentEao.getPaymentById(id);
	}

	@Override
	public List<? extends Payment> getPaymentList(boolean showDisabled) {
		return paymentEao.getPaymentList(showDisabled);
	}

	@Override
	public Payment savePayment(Payment payment) {
		return paymentEao.savePayment(payment);
	}

	@Override
	public Payment factoryNewPayment() {
		
		PaymentEntity payment = new PaymentEntity();
		payment.setSystemPayment(false);
		payment.setEnabled(true);
		payment.setDirect(true);

		return payment;
	}
	
	@Override
	public PaymentAccount getPseudoCustomerAccount(){
		PaymentAccount paymentAccount = null;
		
		long id = propertyController.getLongProperty("PSEUDO_CUSTOMER_ACCOUNT", 0);
		
		paymentAccount = getPaymentAccount(id);
		
		if(id == 0){
			paymentAccount = new PaymentAccountEntity();
			paymentAccount.setName("Buchungskonto von Pseudokunde");
			paymentAccount = paymentEao.savePaymentAccount(paymentAccount);
			paymentEao.flush();
			
			propertyController.updateLongProperty("PSEUDO_CUSTOMER_ACCOUNT", paymentAccount.getId());
		}
		
		return paymentAccount;
	}
	
	@Override
	public PaymentAccount getPaymentAccount(long id){
		return paymentEao.getPaymentAccountById(id);
	}
	
	@Override
	public PaymentAccount getCustomerAccount(Customer c){
		CustomerEntity customer = (CustomerEntity) customerController.refresh(c);
		
		PaymentAccountEntity paymentAccount = (PaymentAccountEntity) customer.getPaymentAccount();
		
		if(paymentAccount == null){
			paymentAccount = new PaymentAccountEntity();
			paymentAccount.setName("Buchungskonto von "+customer.toString());
			customer.setPaymentAccount(paymentAccount);
			
			customer = (CustomerEntity) customerController.saveCustomer(customer);
		}
		
		return paymentAccount;
	}
	
	@Override
	public PaymentShopAccount getPaymentShopAccount(Payment p, Shop s){
		PaymentEntity payment = (PaymentEntity) p;
		ShopEntity shop = (ShopEntity) s;
		
		PaymentShopAccountEntity paymentShopAccount = (PaymentShopAccountEntity) paymentEao.getPaymentShopAccount(p,s);
		
		if(paymentShopAccount == null){
			paymentShopAccount = new PaymentShopAccountEntity();


			PaymentAccountEntity account = new PaymentAccountEntity();
			account.setName(p.getName() + " Konto für "+s.getName());

			PaymentAccountEntity settlingAccount = new PaymentAccountEntity();
			settlingAccount.setName(p.getName() + " Ausgleichskonto für "+s.getName());
			
			paymentShopAccount.setShop(shop);
			paymentShopAccount.setPayment(payment);
			paymentShopAccount.setAccount(account);
			paymentShopAccount.setSettlingAccount(settlingAccount);
			
			paymentShopAccount = paymentEao.savePaymentShopAccount(paymentShopAccount);
			
		}
		
		return paymentShopAccount;
	}
	
	@Override
	public PaymentAccountTransaction newPaymentAccountTransaction(BigDecimal amount, PaymentAccount sourceAccount, PaymentAccount destinationAccount, Invoice invoiceReference){
		
		PaymentAccountTransaction transaction = new PaymentAccountTransactionEntity();
		transaction.setAmount(amount);
		transaction.setSourceAccount(sourceAccount);
		transaction.setDestinationAccount(destinationAccount);
		transaction.setInvoiceReference(invoiceReference);
		
		
		transaction = paymentEao.savePaymentAccountTransaction(transaction);
		
		return transaction;
	}
	
	@Override
	public PaymentAccountTransaction newPaymentAccountTransaction(BigDecimal amount, PaymentAccount sourceAccount, PaymentAccount destinationAccount, String note){
		
		PaymentAccountTransaction transaction = new PaymentAccountTransactionEntity();
		transaction.setAmount(amount);
		transaction.setSourceAccount(sourceAccount);
		transaction.setDestinationAccount(destinationAccount);
		transaction.setNote(note);
		
		
		transaction = paymentEao.savePaymentAccountTransaction(transaction);
		
		return transaction;
	}

	@Override
	public List<? extends PaymentAccountTransaction> getAccountTransactions(PaymentAccount paymentAccount) {
		return paymentEao.getAccountTransactions(paymentAccount);
	}

	@Override
	public BigDecimal getAccountBalance(PaymentAccount paymentAccount) {
		return paymentEao.getAccountBalance(paymentAccount);
	}

	@Override
	public BigDecimal getAccountIncoming(PaymentAccount paymentAccount) {
		return paymentEao.getAccountIncoming(paymentAccount);
	}

	@Override
	public BigDecimal getAccountOutgoing(PaymentAccount paymentAccount) {
		return paymentEao.getAccountOutgoing(paymentAccount);
	}
	
//	@Override
//	public boolean getCashRegisterStatus(Shop shop){
//		Payment payment = this.getPaymentById(1);
//		return getPaymentStatus(payment, shop);
//	}
	
	@Override
	public boolean getPaymentStatus(Payment payment, Shop shop){
		
		List<? extends PaymentStatus> list = getPaymentStatusList(payment, shop);
		
		if(list.isEmpty())
			return false;
		
		PaymentStatus paymentStatus = list.get(0);
		
		if(paymentStatus == null)
			return false;
		
		return paymentStatus.isOpen();
	}
	
	@Override
	public List<? extends PaymentStatus> getPaymentStatus(Payment payment, Shop shop, int month, int year){
		
		List<? extends PaymentStatus> list = getPaymentStatusList(payment, shop, month, year);
		
		return list;
	}
	
	@Override
	public PaymentStatus getLastPaymentStatus(Payment payment, Shop shop){
		
		List<? extends PaymentStatus> list = getPaymentStatusList(payment, shop);
		
		if(list.isEmpty())
			return null;
		
		PaymentStatus paymentStatus = list.get(0);
		
		if(paymentStatus == null)
			return null;
		
		return paymentStatus;
	}
	
	@Override
	public BigDecimal getPaymentLastCumulativeSum(Payment payment, Shop shop){
		System.out.println("getPaymentLastCumulativeSum: shop: "+shop.getId()+" payment: "+payment.getName()+ " "+payment.getId());
		
		List<? extends PaymentStatus> list = getPaymentStatusList(payment, shop);
		
		if(list.isEmpty())
			return new BigDecimal(0);
		
		PaymentStatus paymentStatus = list.get(0);
		
		if(paymentStatus == null)
			return new BigDecimal(0);

		System.out.println("paymentStatus: "+paymentStatus.getId());
		System.out.println("paymentStatus: "+paymentStatus.getCumulativeSum());
		
		return paymentStatus.getCumulativeSum();
	}
	
	@Override
	public BigDecimal getPaymentAccountBalance(Payment payment, Shop shop){
		
		PaymentShopAccount paymentShopAccount = getPaymentShopAccount(payment, shop);
		
		return paymentEao.getAccountBalance(paymentShopAccount.getAccount());
		
	}
	
	@Override
	public List<? extends PaymentStatus> getPaymentStatusList(Payment payment, Shop shop){
		return paymentEao.getPaymentStatusList(payment, shop);
	}
	
	@Override
	public List<? extends PaymentStatus> getPaymentStatusList(Payment payment, Shop shop, int month, int year){
		return paymentEao.getPaymentStatusList(payment, shop, month, year);
	}
	
	
	@Override
	public BigDecimal getCurrentPaymentCumulativeSum(Payment payment, Shop shop){
		PaymentStatus paymentStatus = getPaymentStatusList(payment, shop).get(0);
		BigDecimal currentCumulativeSum = new BigDecimal(0);
		
		if(paymentStatus != null)
			currentCumulativeSum = paymentStatus.getCumulativeSum();
		
		currentCumulativeSum.add(paymentEao.getCumulativeSumForToday(payment, shop));
		
		return currentCumulativeSum;
	}
	
	private PaymentStatus openPayment(User user, Payment payment, Shop shop, String note, BigDecimal sum){
		PaymentStatus paymentStatus = new PaymentStatusEntity();
		
		paymentStatus.setUser(user);
		paymentStatus.setPayment(payment);
		paymentStatus.setShop(shop);
		paymentStatus.setOpen(true);
		paymentStatus.setEffectiveDate(new Date());
		paymentStatus.setEnabled(true);
		paymentStatus.setNote(note);
		
		paymentStatus.setCumulativeSum(sum);
		
		return paymentEao.savePaymentStatus(paymentStatus);
	}
	
	private PaymentStatus closePayment(User user, Payment payment, Shop shop, String note, BigDecimal sum){
		PaymentStatus paymentStatus = new PaymentStatusEntity();
		
		paymentStatus.setUser(user);
		paymentStatus.setPayment(payment);
		paymentStatus.setShop(shop);
		paymentStatus.setOpen(false);
		paymentStatus.setEffectiveDate(new Date());
		paymentStatus.setEnabled(true);
		paymentStatus.setNote(note);
		
		paymentStatus.setCumulativeSum(sum);
		
		return paymentEao.savePaymentStatus(paymentStatus);
	}

//	private PaymentStatus factoryNewPaymentStatus(Payment payment, Shop shop) {
//		PaymentStatus ps = new PaymentStatusEntity();
//		
//		ps.setPayment(payment);
//		ps.setShop(shop);
//		
//		return ps;
//	}

//	@Override
//	public void bookCorrection(Payment payment, Shop shop, BigDecimal amount, String comment) {
//		
//		
//	}
	
	@Override
	public void openPayments(Shop shop, User user, String comment, BigDecimal cashRegisterCountSum) throws Exception{
		
		Payment cashRegisterPayment = this.getPaymentById(1);
		
		BigDecimal accountBalance = getPaymentAccountBalance(cashRegisterPayment, shop);
		
		if(accountBalance.compareTo(cashRegisterCountSum) != 0){
			throw new Exception("Kasseninhalt nicht korrekt!");
		}
		
		Iterator<? extends Payment> i = this.getPaymentList().iterator();
		Payment payment;
		String note;
		while(i.hasNext()){
			payment = i.next();
			note = "";
			
			if(payment.getId() == 1)
				note = comment;
			
			this.openPayment(user, payment, shop, note, getPaymentAccountBalance(payment, shop));
		}
		
	}
	
	@Override
	public void closePayments(Shop shop, User user, String comment, BigDecimal cashRegisterCountSum) throws Exception{
		
		Payment cashRegisterPayment = this.getPaymentById(1);
		
		BigDecimal accountBalance = getPaymentAccountBalance(cashRegisterPayment, shop);
		
		if(accountBalance.compareTo(cashRegisterCountSum) != 0){
			throw new Exception("Kasseninhalt nicht korrekt!");
		}
		
		Iterator<? extends Payment> i = this.getPaymentList().iterator();
		Payment payment;
		String note;
		while(i.hasNext()){
			payment = i.next();
			note = "";
			
			if(payment.getId() == 1)
				note = comment;
			
			this.closePayment(user, payment, shop, note, getPaymentAccountBalance(payment, shop));
		}
		
	}
//	
//	@Override
//	public void closePayments(){
//		
//	}

	@Override
	public PaymentAccount getSettlingAccount(Shop shop) {
		ShopEntity shopEntity = (ShopEntity)shop;
		
		PaymentAccountEntity settlingAccount = shopEntity.getSettlingAccount();
		
		if(settlingAccount == null){
			
			settlingAccount = new PaymentAccountEntity();
			settlingAccount.setName("Ausgleichskonto Konto für "+shop.getName());
			
			shopEntity.setSettlingAccount(settlingAccount);
			
			shopController.saveShop(shopEntity);
		}
		
		return settlingAccount;
		
	}

	@Override
	public PaymentAccount getCashCustomerAccount() {
		long cashCustomerAccountID = propertyController.getLongProperty("CASH_CUSTOMER_ACCOUNT", 0);
		PaymentAccountEntity cashCustomerAccount;
		if(cashCustomerAccountID == 0){

			cashCustomerAccount = new PaymentAccountEntity();
			cashCustomerAccount.setName("BAR-Kunden Gegen-Konto");
			
			cashCustomerAccount = (PaymentAccountEntity) paymentEao.savePaymentAccount(cashCustomerAccount);
			
			paymentEao.flush();
			
			propertyController.updateLongProperty("CASH_CUSTOMER_ACCOUNT", cashCustomerAccount.getId());
		}else{
			cashCustomerAccount = (PaymentAccountEntity) this.getPaymentAccount(cashCustomerAccountID);
		}
		
		return cashCustomerAccount;
	}
}
