package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.omnifaces.component.output.cache.CacheFactory;

import de.vaplus.api.ActivityControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.Activity_old;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.Note;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.pojo.ExternalCustomerList;

@ManagedBean(name = "customerBean")
@SessionScoped
public class CustomerBean implements Serializable {

	private static final long serialVersionUID = -5290990922023483183L;


	@EJB
	private CustomerControllerInterface customerController;

	@EJB
	private ContractControllerInterface contractController;

    @EJB
    private ActivityControllerInterface activityController;

	@Inject
	private FacesContext facesContext;

    @ManagedProperty(value="#{propertyBean}")
    private PropertyBean propertyBean;

    @ManagedProperty(value="#{shopBean}")
    private ShopBean shopBean;

    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

	private Customer selectedCustomer;

	private ExternalCustomer selectedExternalCustomer;

	private Customer currentCustomer;

	private boolean selectedCustomerMailNotSet;

	private boolean selectedExternalCustomerEditable;

	private Note selectedNote;

	private ExternalCustomerList externalCustomerMap;

	@PostConstruct
	public void init() {
		if(propertyBean != null)
			selectedCustomer = propertyBean.getDefaultCustomer();
	}

	public void clearCache(){
		externalCustomerMap = null;
		selectedExternalCustomer = null;
		setSelectedExternalCustomerEditable(false);
	}


    public Customer getCustomer(){
    	return getSelectedCustomer();
    }

	public void refreshCustomer() {
		if(getSelectedCustomer() == null)
			return;
		
		setSelectedCustomer(customerController.refresh(getSelectedCustomer()));
	}

	public List<? extends Customer> getCustomerList(){
		return customerController.getCustomerList();
	}

	public void checkIfCustomerLogged() throws IOException {
		
	    if (this.getSelectedCustomer() == null || this.getSelectedCustomer().getId() == 0) {
			try {
    			if(!facesContext.getExternalContext().isResponseCommitted()){
					facesContext.getExternalContext().redirect("/dashboard.xhtml");
					facesContext.responseComplete();
    			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
	}
	

	public String deleteCustomer(){
		System.out.println("deleteCustomer");
		
		selectedCustomer.setDeleted();
		customerController.saveCustomer(selectedCustomer);
		selectedCustomer = null;
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Kunde gelöscht.", null));

		System.out.println("DONE");
		return "/";
	}

	public String saveCustomer(){

		boolean newCustomer = selectedCustomer.getId() == 0 ? true : false;

		selectedCustomer = customerController.saveCustomer(selectedCustomer);

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Kunde erfolgreich gespeichert.", null));

		if(newCustomer)
			return "/customer/contract";
		else
			return "/customer/profile";
	}

	public Customer getSelectedCustomer() {

		if(selectedCustomer == null){
			selectedCustomer = customerController.factoryNewCustomer();
			selectedCustomer.setShop(shopBean.getActiveShop());
			selectedCustomer.setAccountManager(userBean.getActiveUser());
		}

		return selectedCustomer;
	}

	public void setSelectedCustomer(Customer customer) {
		clearCache();
		
		if(customer != null && customer.isDeleted()){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kunde ist gelöscht!", null));
			this.selectedCustomer = null;
			return;
		}
		


		if(customer != null && customer.getId() > 0)
			userBean.setLastSeenCustomer(customer);


    	CacheFactory.getCache(facesContext, "session").remove("pageNavCache");
    	CacheFactory.getCache(facesContext, "session").remove("sideBarCustomerHistoryCache");

		this.selectedCustomer = customer;
	}

	public List<? extends Activity_old> getCustomerActivityList(){
		return customerController.getCustomerActivityList(selectedCustomer);
	}

	public List<? extends Activity_old> getCustomerActivityListSmall(){
		if(customerController.getCustomerActivityList(selectedCustomer).size() > 5)
			return customerController.getCustomerActivityList(selectedCustomer).subList(0, 5);
		else
			return customerController.getCustomerActivityList(selectedCustomer);
	}


	public Note getSelectedNote() {
		if(selectedNote == null)
				selectedNote = customerController.factoryNewNote();
		return selectedNote;
	}


	public void setSelectedNote(Note selectedNote) {
		this.selectedNote = selectedNote;
	}


	public String saveNote(){

		((List<Note>)selectedCustomer.getNoteList()).add(selectedNote);

		selectedCustomer = customerController.saveCustomer(selectedCustomer);

		selectedNote = null;

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Aktivität erfolgreich gespeichert.", null));

		return "activities";
	}


	public List<? extends Customer> getCustomerSearchResult() {
		List<? extends Customer> list = new ArrayList<Customer>();
		if(getSelectedCustomer().getId() > 0)
			return list;

		if(getSelectedCustomer().isCompany()){
			if(getSelectedCustomer().getCompanyName() == null || getSelectedCustomer().getCompanyName().length() == 0)
				return list;

			list = customerController.getCustomerByCompanyName(getSelectedCustomer().getCompanyName());
		}
		else{
			if(getSelectedCustomer().getFirstname() == null || getSelectedCustomer().getFirstname().length() == 0)
				return list;
			if(getSelectedCustomer().getLastname() == null || getSelectedCustomer().getLastname().length() == 0)
				return list;

			list = customerController.getCustomer(getSelectedCustomer().getFirstname(), getSelectedCustomer().getLastname());
		}


		Iterator<? extends Customer> i = list.iterator();
		Customer c;
		while(i.hasNext()){
			c = i.next();
		}

		return list;
	}


	public boolean isSelectedCustomerMailNotSet() {
		return selectedCustomerMailNotSet;
	}


	public void setSelectedCustomerMailNotSet(boolean selectedCustomerMailNotSet) {
		this.selectedCustomerMailNotSet = selectedCustomerMailNotSet;
	}

	public PropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(PropertyBean propertyBean) {
		this.propertyBean = propertyBean;
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

	public void newCustomer() throws IOException {

		this.selectedCustomer = null;
		setSelectedCustomerMailNotSet(false);

		facesContext.getExternalContext().redirect("/newCustomer.xhtml");
	}

	public void searchForCustomerCity(){

		if(getSelectedCustomer().getId() > 0)
			return;
		if(getSelectedCustomer().getAddress().getCountry() == null || getSelectedCustomer().getAddress().getCountry().length() == 0)
			return;
		if(getSelectedCustomer().getAddress().getZip() == null || getSelectedCustomer().getAddress().getZip().length() == 0)
			return;

		if(getSelectedCustomer().getAddress().getCity() != null && getSelectedCustomer().getAddress().getCity().length() > 0)
			return;

		getSelectedCustomer().getAddress().setCity(
				customerController.getCityByZip(getSelectedCustomer().getAddress().getCountry(), getSelectedCustomer().getAddress().getZip())
		);

	}

	public List<? extends ExternalCustomer> findExternalCustomerByCustomerId(String customerId){
		return customerController.findExternalCustomerByCustomerId(customerId);
	}

	public List<? extends ExternalCustomer> getExternalCustomerList(){
		return customerController.getExternalCustomerList(this.selectedCustomer);
	}

	public List<? extends ExternalCustomer> getExternalCustomerList(Customer customer){
		return customerController.getExternalCustomerList(customer);
	}

	public List<? extends Activity> getTimeline(){
		return activityController.getCustomerTimeline(this.getSelectedCustomer(), 10, 0);
	}

	public List<? extends Activity> getTimeline(Customer customer){
		return activityController.getCustomerTimeline(customer, 10, 0);
	}

	public ExternalCustomerList getContractMap(){

		if(externalCustomerMap == null){

			externalCustomerMap = new ExternalCustomerList();
			List<? extends BaseContract> contractList = customerController.getContractMap(this.selectedCustomer);

			externalCustomerMap.setContractCount(contractList.size());

			Iterator<? extends BaseContract> i = contractList.iterator();
			BaseContract contract;
			String key, password;
			while(i.hasNext()){
				contract = i.next();
				key = null;
				password = null;

				if(contract.getExternalCustomer() != null && contract.getExternalCustomer().getCustomerId() != null && contract.getExternalCustomer().getCustomerId().length() > 0){
					key = contract.getExternalCustomer().getCustomerId();
					password = contract.getExternalCustomer().getPassword();
				}else if(contract.getOrderNumber() != null && contract.getOrderNumber().length() > 0){
					key = contract.getOrderNumber();
				}else{
					key = "#";
				}

				if(key == null)
					continue;

				if(contractController.isContractAbleToExtensionOfTheTerm(contract)){
					externalCustomerMap.getCustomerList(key).setContainsExtensionOfTheTermContract(true);
				}

				externalCustomerMap.getCustomerList(key).setCustomerId(key);
				externalCustomerMap.getCustomerList(key).setPassword(password);

				externalCustomerMap.getCustomerList(key).getContractList().add(contract);
			}
		}

		return externalCustomerMap;

	}

	public List<? extends BaseContract> getContractList(Customer customer){

		List<BaseContractEntity> contractList = new LinkedList<BaseContractEntity>();

		Iterator<? extends BaseContract> i = customerController.getContractMap(customer).iterator();
		BaseContract contract;
		while(i.hasNext()){
			contract = i.next();

			if(! contract.isEnabledAndInTime())
				continue;

			contractList.add((BaseContractEntity) contract);
		}

		return contractList;

	}

	public Customer getCurrentCustomer() {
		return currentCustomer;
	}

	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	public BigDecimal getContractPoints(Customer customer){
		return customerController.getContractPoints(customer);
	}

	public BigDecimal getRetailPoints(Customer customer){
		return customerController.getRetailPoints(customer);
	}

	public BigDecimal getMonthlyTotal(Customer customer){
		return customerController.getMonthlyTotal(customer);
	}

	public ExternalCustomer getSelectedExternalCustomer() {
		return selectedExternalCustomer;
	}

	public void setSelectedExternalCustomer(ExternalCustomer selectedExternalCustomer) {
		this.selectedExternalCustomer = customerController.reload(selectedExternalCustomer);;
	}

	public boolean isSelectedExternalCustomerEditable() {
		return selectedExternalCustomerEditable;
	}

	public void setSelectedExternalCustomerEditable(
			boolean selectedExternalCustomerEditable) {
		this.selectedExternalCustomerEditable = selectedExternalCustomerEditable;
	}

	public void toggleSelectedExternalCustomerEditable() {
		selectedExternalCustomerEditable = selectedExternalCustomerEditable ? false : true;
	}

	public void saveSelectedExternalCustomer() {
		selectedExternalCustomer = customerController.saveSelectedExternalCustomer(selectedExternalCustomer);
		setSelectedExternalCustomerEditable(false);
	}



}
