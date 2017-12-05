package de.vaplus.client.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.PaymentControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.CellPhoneTariff;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.LandlineTariff;
import de.vaplus.api.entity.PrePaidTariff;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentShopAccount;
import de.vaplus.api.entity.PaymentStatus;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.helper.TaxHelper;
import javafx.util.Pair;

@ManagedBean(name="cashRegisterStatusBean")
@ViewScoped
public class CashRegisterStatusBean implements Serializable {
	
	private static final long serialVersionUID = 1078629762941689449L;

	@EJB
	private PaymentControllerInterface paymentController;
	
	@Inject
	private FacesContext facesContext;

    @ManagedProperty(value="#{shopBean}")
    private ShopBean shopBean;

    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

    @ManagedProperty(value="#{paymentBean}")
    private PaymentBean paymentBean;

    private int countHelper_001;
    private int countHelper_002;
    private int countHelper_005;
    private int countHelper_010;
    private int countHelper_020;
    private int countHelper_050;
    private int countHelper_1;
    private int countHelper_2;
    private int countHelper_5;
    private int countHelper_10;
    private int countHelper_20;
    private int countHelper_50;
    private int countHelper_100;
    private int countHelper_200;
    private int countHelper_500;
    
    private BigDecimal countHelper_sum;
    
    private String comment;
    
    private boolean show_correction_booking = false;
    
    private BigDecimal correction_sum;
    
    private String correction_comment;
    
    private boolean book_correction;
    
	public CashRegisterStatusBean() {
	}
	
	private void cleanStatusCache(){
		paymentBean.cleanPaymentStatusCache();
	}

	public ShopBean getShopBean() {
		return shopBean;
	}

	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public PaymentBean getPaymentBean() {
		return paymentBean;
	}

	public void setPaymentBean(PaymentBean paymentBean) {
		this.paymentBean = paymentBean;
	}
	

	public int getCountHelper_001() {
		return countHelper_001;
	}

	public void setCountHelper_001(int countHelper_001) {
		this.countHelper_001 = countHelper_001;
	}

	public int getCountHelper_002() {
		return countHelper_002;
	}

	public void setCountHelper_002(int countHelper_002) {
		this.countHelper_002 = countHelper_002;
	}

	public int getCountHelper_005() {
		return countHelper_005;
	}

	public void setCountHelper_005(int countHelper_005) {
		this.countHelper_005 = countHelper_005;
	}

	public int getCountHelper_010() {
		return countHelper_010;
	}

	public void setCountHelper_010(int countHelper_010) {
		this.countHelper_010 = countHelper_010;
	}

	public int getCountHelper_020() {
		return countHelper_020;
	}

	public void setCountHelper_020(int countHelper_020) {
		this.countHelper_020 = countHelper_020;
	}

	public int getCountHelper_050() {
		return countHelper_050;
	}

	public void setCountHelper_050(int countHelper_050) {
		this.countHelper_050 = countHelper_050;
	}

	public int getCountHelper_1() {
		return countHelper_1;
	}

	public void setCountHelper_1(int countHelper_1) {
		this.countHelper_1 = countHelper_1;
	}

	public int getCountHelper_2() {
		return countHelper_2;
	}

	public void setCountHelper_2(int countHelper_2) {
		this.countHelper_2 = countHelper_2;
	}

	public int getCountHelper_5() {
		return countHelper_5;
	}

	public void setCountHelper_5(int countHelper_5) {
		this.countHelper_5 = countHelper_5;
	}

	public int getCountHelper_10() {
		return countHelper_10;
	}

	public void setCountHelper_10(int countHelper_10) {
		this.countHelper_10 = countHelper_10;
	}

	public int getCountHelper_20() {
		return countHelper_20;
	}

	public void setCountHelper_20(int countHelper_20) {
		this.countHelper_20 = countHelper_20;
	}

	public int getCountHelper_50() {
		return countHelper_50;
	}

	public void setCountHelper_50(int countHelper_50) {
		this.countHelper_50 = countHelper_50;
	}

	public int getCountHelper_100() {
		return countHelper_100;
	}

	public void setCountHelper_100(int countHelper_100) {
		this.countHelper_100 = countHelper_100;
	}

	public int getCountHelper_200() {
		return countHelper_200;
	}

	public void setCountHelper_200(int countHelper_200) {
		this.countHelper_200 = countHelper_200;
	}

	public int getCountHelper_500() {
		return countHelper_500;
	}

	public void setCountHelper_500(int countHelper_500) {
		this.countHelper_500 = countHelper_500;
	}

	public BigDecimal getCountHelper_sum() {
		return countHelper_sum;
	}

	public void setCountHelper_sum(BigDecimal countHelper_sum) {
		this.countHelper_sum = countHelper_sum;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isShow_correction_booking() {
		return show_correction_booking;
	}

	public void setShow_correction_booking(boolean show_correction_booking) {
		this.show_correction_booking = show_correction_booking;
	}

	public BigDecimal getCorrection_sum() {
		return correction_sum;
	}

	public void setCorrection_sum(BigDecimal correction_sum) {
		this.correction_sum = correction_sum;
	}

	public String getCorrection_comment() {
		return correction_comment;
	}

	public void setCorrection_comment(String correction_comment) {
		this.correction_comment = correction_comment;
	}

	public boolean isBook_correction() {
		return book_correction;
	}

	public void setBook_correction(boolean book_correction) {
		this.book_correction = book_correction;
	}

	public BigDecimal getLastCumulativeSum() {
		Payment payment = paymentController.getPaymentById(1);
		
		return paymentController.getPaymentLastCumulativeSum(payment, shopBean.getActiveShop());
	}

	public PaymentStatus getLastCashRegisterStatus() {
		Payment payment = paymentController.getPaymentById(1);
		return paymentController.getLastPaymentStatus(payment, shopBean.getActiveShop());
	}

	public boolean getCashRegisterStatus() {
		Payment payment = paymentController.getPaymentById(1);
		
		return paymentController.getPaymentStatus(payment, shopBean.getActiveShop());
	}
	
	private void calHelperSum(){

		countHelper_sum = new BigDecimal(0);
		
		// 1 Cent
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_001).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

		// 2 Cent
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_002).multiply(new BigDecimal(2)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

		// 5 Cent
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_005).multiply(new BigDecimal(5)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

		// 10 Cent
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_010).multiply(new BigDecimal(10)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

		// 20 Cent
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_020).multiply(new BigDecimal(20)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

		// 50 Cent
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_050).multiply(new BigDecimal(50)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

		// 1 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_1));

		// 2 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_2).multiply(new BigDecimal(2)));

		// 5 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_5).multiply(new BigDecimal(5)));

		// 10 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_10).multiply(new BigDecimal(10)));

		// 20 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_20).multiply(new BigDecimal(20)));

		// 50 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_50).multiply(new BigDecimal(50)));

		// 100 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_100).multiply(new BigDecimal(100)));

		// 200 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_200).multiply(new BigDecimal(200)));

		// 500 Euro
		countHelper_sum = countHelper_sum.add(new BigDecimal(countHelper_500).multiply(new BigDecimal(500)));
		
	}

	public void open(){
		cleanStatusCache();
		
		calHelperSum();
		
		Payment cashRegisterPayment = paymentController.getPaymentById(1);

		BigDecimal accountBalance = paymentController.getPaymentAccountBalance(cashRegisterPayment, shopBean.getActiveShop());
		
		if(countHelper_sum.compareTo(accountBalance) != 0 && book_correction == false){
			show_correction_booking = true;
			
			correction_sum = countHelper_sum.subtract(accountBalance);
			
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gezählter Kasseninhalt stimmt nicht mit letztem Kasseninhalt überein! Bitte neu zählen oder mit Korrekturbuchung öffnen. Differenz: "+correction_sum.setScale(2, RoundingMode.HALF_UP)+" €", null));
			
			return;
			
		}else if(countHelper_sum.compareTo(accountBalance) != 0 && book_correction == true){
			
			PaymentAccount settlingAccount = paymentController.getSettlingAccount(shopBean.getActiveShop());

			PaymentShopAccount paymentShopAccount = paymentController.getPaymentShopAccount(cashRegisterPayment, shopBean.getActiveShop());
			
			BigDecimal amount;
			
			String note = "Korrekturbuchung des Kassenbestands.\n\n\n";
			
			note += correction_comment;
			
			PaymentAccount sourceAccount, destinationAccount;
			
			if(correction_sum.compareTo(new BigDecimal(0)) > 0){
				amount = correction_sum;

				sourceAccount = settlingAccount;
				destinationAccount = paymentShopAccount.getAccount();
			}else{
				amount = correction_sum.multiply(new BigDecimal(-1));
				
				destinationAccount = settlingAccount;
				sourceAccount = paymentShopAccount.getAccount();
			}
			
			paymentController.newPaymentAccountTransaction(amount, sourceAccount, destinationAccount, note);
			
			System.out.println("BOOK CORRECTION: "+correction_sum);

			comment += "\n\n----------------------------------\n\n";
			comment += note;
			
		}
		
		
		show_correction_booking = false;
		

		System.out.println("OPEN CASH REGISTER");
		try {
			paymentController.openPayments(shopBean.getActiveShop(), userBean.getActiveUser(), comment, countHelper_sum);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim öffnen der Kasse: "+e.getMessage(), null));
			return;
		}
		
	}

	public void close(){
		cleanStatusCache();
		
		calHelperSum();
		
		Payment cashRegisterPayment = paymentController.getPaymentById(1);

		BigDecimal accountBalance = paymentController.getPaymentAccountBalance(cashRegisterPayment, shopBean.getActiveShop());
		
		if(countHelper_sum.compareTo(accountBalance) != 0 && book_correction == false){
			show_correction_booking = true;
			
			correction_sum = countHelper_sum.subtract(accountBalance);
			
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gezählter Kasseninhalt stimmt nicht mit Kassensumme überein! Bitte neu zählen oder mit Korrekturbuchung öffnen. Differenz: "+correction_sum.setScale(2, RoundingMode.HALF_UP)+" €", null));
			
			return;
			
		}else if(countHelper_sum.compareTo(accountBalance) != 0 && book_correction == true){
			
			PaymentAccount settlingAccount = paymentController.getSettlingAccount(shopBean.getActiveShop());

			PaymentShopAccount paymentShopAccount = paymentController.getPaymentShopAccount(cashRegisterPayment, shopBean.getActiveShop());
			
			BigDecimal amount;
			
			String note = "Korrekturbuchung des Kassenbestands:\n\n";
			
			note += correction_comment;
			
			PaymentAccount sourceAccount, destinationAccount;
			
			if(correction_sum.compareTo(new BigDecimal(0)) > 0){
				amount = correction_sum;

				sourceAccount = settlingAccount;
				destinationAccount = paymentShopAccount.getAccount();
			}else{
				amount = correction_sum.multiply(new BigDecimal(-1));
				
				destinationAccount = settlingAccount;
				sourceAccount = paymentShopAccount.getAccount();
			}
			
			paymentController.newPaymentAccountTransaction(amount, sourceAccount, destinationAccount, note);
			
			System.out.println("BOOK CORRECTION: "+correction_sum);

			comment += "\n\n----------------------------------\n\n";
			comment += note;
			
		}
		
		
		show_correction_booking = false;
		

		System.out.println("CLOSE CASH REGISTER");
		try {
			paymentController.closePayments(shopBean.getActiveShop(), userBean.getActiveUser(), comment, countHelper_sum);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim schliessen der Kasse: "+e.getMessage(), null));
			return;
		}
		
	}

}
