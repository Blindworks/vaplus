package de.vaplus.client.backingbeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
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
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentAccountTransaction;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.helper.TaxHelper;

@ManagedBean(name="paymentAccountBean")
@SessionScoped
public class PaymentAccountBean implements Serializable {
	
	@EJB
	private PaymentControllerInterface paymentController;

	@Inject
	private FacesContext facesContext;

	private PaymentAccount selectedPaymentAccount;
	
	private BigDecimal accountBalance;
	
	private BigDecimal accountSumIncoming;
	
	private BigDecimal accountSumOutgoing;
	
	private List<? extends PaymentAccountTransaction> accountTransactions;
	
	public PaymentAccountBean() {
		// TODO Auto-generated constructor stub
	}
	
	private void cleanCache(){
		setAccountBalance(null);
		setAccountTransactions(null); 
		setAccountSumIncoming(null);
		setAccountSumOutgoing(null);
	}

	public PaymentAccount getPseudoCustomerAccount(){
		return paymentController.getPseudoCustomerAccount();
	}

	public PaymentAccount getCustomerAccount(Customer c){
		return paymentController.getCustomerAccount(c);
	}

	public PaymentAccount getSelectedPaymentAccount() {
		return selectedPaymentAccount;
	}

	public void setSelectedPaymentAccount(PaymentAccount selectedPaymentAccount) {
		this.selectedPaymentAccount = selectedPaymentAccount;
		cleanCache();
	}

	public BigDecimal getAccountBalance() {
		if(accountBalance == null)
			accountBalance = paymentController.getAccountBalance(getSelectedPaymentAccount());
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public List<? extends PaymentAccountTransaction> getAccountTransactions() {
		if(accountTransactions == null)
			accountTransactions = paymentController.getAccountTransactions(getSelectedPaymentAccount());
		return accountTransactions;
	}

	public void setAccountTransactions(List<? extends PaymentAccountTransaction> accountTransactions) {
		this.accountTransactions = accountTransactions;
	}

	public BigDecimal getAccountSumIncoming() {
		if(accountSumIncoming == null)
			accountSumIncoming = paymentController.getAccountIncoming(getSelectedPaymentAccount());
		return accountSumIncoming;
	}

	public void setAccountSumIncoming(BigDecimal accountSumIncoming) {
		this.accountSumIncoming = accountSumIncoming;
	}

	public BigDecimal getAccountSumOutgoing() {
		if(accountSumOutgoing == null)
			accountSumOutgoing = paymentController.getAccountOutgoing(getSelectedPaymentAccount());
		return accountSumOutgoing;
	}

	public void setAccountSumOutgoing(BigDecimal accountSumOutgoing) {
		this.accountSumOutgoing = accountSumOutgoing;
	}
}
