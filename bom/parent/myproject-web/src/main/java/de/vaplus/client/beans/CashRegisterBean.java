package de.vaplus.client.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
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

@ManagedBean(name="cashRegisterBean")
@ViewScoped
public class CashRegisterBean implements Serializable {
	
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

    private Shop selectedShop;

    private Payment selectedPayment;
    
    private int selectedYear;
    
    private int selectedMonth;
    
	public CashRegisterBean() {
		Calendar c = Calendar.getInstance();
		selectedMonth = c.get(Calendar.MONTH);
		selectedYear = c.get(Calendar.YEAR);
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

	public BigDecimal getLastCumulativeSum(Payment payment) {
		return paymentController.getPaymentLastCumulativeSum(payment, selectedShop);
	}

	public PaymentStatus getLastPaymentStatus(Payment payment) {
		return paymentController.getLastPaymentStatus(payment, selectedShop);
	}

	public boolean getPaymentStatus(Payment payment) {
		
		return paymentController.getPaymentStatus(payment, selectedShop);
	}

	public List<? extends PaymentStatus> getPaymentStatusList() {
		
		return paymentController.getPaymentStatus(selectedPayment, selectedShop, selectedMonth, selectedYear);
	}

	public Shop getSelectedShop() {
		if(selectedShop == null)
			selectedShop = shopBean.getActiveShop();
		return selectedShop;
	}

	public void setSelectedShop(Shop selectedShop) {
		this.selectedShop = selectedShop;
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
	}

	public int getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(int selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public Payment getSelectedPayment() {
		if(selectedPayment == null)
			selectedPayment = paymentController.getPaymentById(1);
		return selectedPayment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

}
