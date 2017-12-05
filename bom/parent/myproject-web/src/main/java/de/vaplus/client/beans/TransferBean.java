package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.ContractRetailItem;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.backingbeans.RetailProductBean;
import de.vaplus.client.entity.ExternalCustomerEntity;

@ManagedBean(name="transferBean")
@SessionScoped
public class TransferBean implements Serializable {

	private static final long serialVersionUID = 1771882396880118133L;

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private ContractControllerInterface contractController;
	
	@EJB
	private CommissionControllerInterface commissionController;
	
	@EJB
	private CustomerControllerInterface customerController;
	
	@EJB
	private TaxRateControllerInterface taxRateController;
	
	@EJB
	private OrderControllerInterface orderController;


	@Inject
	private FacesContext facesContext;
	
	private BaseContract selectedContract;
	
	private ExternalCustomer targetExternalCustomer;
	
	private List<? extends ExternalCustomer> targetExternalCustomerList;
	
	private String targetExternalCustomerQuery;
	
	
	public TransferBean() {
	}
	

	public BaseContract getSelectedContract() {
		return selectedContract;
	}
	

	public void setSelectedContract(BaseContract selectedContract) {
		this.selectedContract = selectedContract;
	}


	public ExternalCustomer getTargetExternalCustomer() {
		return targetExternalCustomer;
	}


	public void setTargetExternalCustomer(ExternalCustomer targetExternalCustomer) {
		
		System.out.println("setTargetExternalCustomer "+targetExternalCustomer.getCustomerId());
		
		this.targetExternalCustomer = targetExternalCustomer;
	}


	public String getTargetExternalCustomerQuery() {
		return targetExternalCustomerQuery;
	}


	public void setTargetExternalCustomerQuery(
			String targetExternalCustomerQuery) {
		this.targetExternalCustomerQuery = targetExternalCustomerQuery;
	}
	
	public void searchExternalCustomer(){
		
		if(targetExternalCustomerQuery.length() < 2){
			setTargetExternalCustomerList(null);
			return;
		}
		
		targetExternalCustomer = null;
		
		setTargetExternalCustomerList(customerController.findExternalCustomerByCustomerId(targetExternalCustomerQuery));
	}


	public List<? extends ExternalCustomer> getTargetExternalCustomerList() {
		if(targetExternalCustomerList == null)
			targetExternalCustomerList = new LinkedList<ExternalCustomer>();
		return targetExternalCustomerList;
	}


	public void setTargetExternalCustomerList(
			List<? extends ExternalCustomer> targetExternalCustomerList) {
		this.targetExternalCustomerList = targetExternalCustomerList;
	}

	
	public void transferContractToTargetCustomer(){
		if(this.targetExternalCustomer == null)
			return;
		if(this.selectedContract == null)
			return;
		
		selectedContract.setCustomer(targetExternalCustomer.getCustomer());
		selectedContract.setExternalCustomer(targetExternalCustomer);
		
		selectedContract = contractController.saveContract(selectedContract);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Vertrag transferiert.", null));
	}
}
