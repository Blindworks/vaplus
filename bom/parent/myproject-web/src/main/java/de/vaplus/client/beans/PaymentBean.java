package de.vaplus.client.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentStatus;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.helper.TaxHelper;
import javafx.util.Pair;

@ManagedBean(name="paymentBean")
@SessionScoped
public class PaymentBean implements Serializable {
	
	private static final long serialVersionUID = -5961840365407223159L;

	@EJB
	private PaymentControllerInterface paymentController;

	@Inject
	private FacesContext facesContext;

	private Payment selectedPayment;
	
	private boolean paymentListEditable;

    @ManagedProperty(value="#{shopBean}")
    private ShopBean shopBean;

    private Map<Integer,Boolean> cashRegisterStatusCache;
    
//    private Map<Pair<Shop,Payment>,PaymentStatus> newPaymentStatus;
	

	public PaymentBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void cleanPaymentStatusCache(){
		cashRegisterStatusCache = null;
	}
	
	public Map<Integer, Boolean> getCashRegisterStatusCache() {
		if(cashRegisterStatusCache == null)
			cashRegisterStatusCache = new HashMap<Integer,Boolean>();
		
		return cashRegisterStatusCache;
	}


	public List<? extends Payment> getPaymentList(){
		return paymentController.getPaymentList();
	}
	
	public List<? extends Payment> getPaymentList(boolean showDisabled){
		return paymentController.getPaymentList(showDisabled);
	}

	public String savePayment(){
		
		selectedPayment = paymentController.savePayment(selectedPayment);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Zahlungsart erfolgreich gespeichert.", null));
		
		return "paymentList";
	}
	
	public Payment getSelectedPayment() {
		if(selectedPayment == null)
			selectedPayment = paymentController.factoryNewPayment();
		return selectedPayment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	
	public String deletePayment(){
		
		selectedPayment.setDeleted();
		selectedPayment = paymentController.savePayment(selectedPayment);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Zahlungsart gelöscht.", null));
		
		return "/settings/paymentList";
	}

	public boolean isPaymentListEditable() {
		return paymentListEditable;
	}

	public void togglePaymentListEditable() {
		paymentListEditable = paymentListEditable ? false : true;
	}
	
	public boolean getPaymentStatus(Payment payment){
		
		
		return paymentController.getPaymentStatus(payment, shopBean.getActiveShop());
	}
	
	public BigDecimal getPaymentLastCumulativeSum(Payment payment){
		return paymentController.getPaymentLastCumulativeSum(payment, shopBean.getActiveShop());
	}


	public ShopBean getShopBean() {
		return shopBean;
	}

	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}

	public boolean getCashRegisterStatus(){
		
		Payment payment = paymentController.getPaymentById(1);
		Shop shop = shopBean.getActiveShop();

		if(shop == null || payment == null)
			return false;
		
		int key = payment.hashCode()+shop.hashCode();
		
		if(! getCashRegisterStatusCache().containsKey(key))
			getCashRegisterStatusCache().put(key, paymentController.getPaymentStatus(payment, shopBean.getActiveShop()));
		

		return getCashRegisterStatusCache().get(key);
	}

	

//	private Map<Pair<Shop,Payment>,PaymentStatus> getNewPaymentStatus(){
//		if(newPaymentStatus == null)
//			newPaymentStatus = new HashMap<Pair<Shop,Payment>,PaymentStatus>();
//		return newPaymentStatus;
//	}
//
//	public PaymentStatus getNewPaymentStatus(Shop shop, Payment payment) {
//		return getNewPaymentStatus().get(new Pair<Shop,Payment>(shop, payment));
//	}
//
//
//	public void setNewPaymentStatus(Shop shop, Payment payment, PaymentStatus paymentStatus) {
//		getNewPaymentStatus().put(new Pair<Shop,Payment>(shop, payment), paymentStatus);
//	}
}
