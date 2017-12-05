package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
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
import de.vaplus.api.entity.ContractStatus;
import de.vaplus.api.entity.ContractStatusChange;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.PhoneContract;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.applicationbeans.ResourceBean;
import de.vaplus.client.backingbeans.PaginationBean;
import de.vaplus.client.backingbeans.RetailProductBean;

@ManagedBean(name="contractBean")
@SessionScoped
public class ContractBean implements Serializable {

	private static final long serialVersionUID = 1771882396880118133L;
	
//	public static final String PAGINATION_KEY = "RenewableContractList";

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
	
	@EJB
	private ResourceBean resourceBean;

	@ManagedProperty(value="#{propertyBean}")
	private PropertyBean propertyBean;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;

	@ManagedProperty(value="#{shopBean}")
	private ShopBean shopBean;

	@ManagedProperty(value="#{customerBean}")
	private CustomerBean customerBean;

	@ManagedProperty(value="#{retailProductBean}")
	private RetailProductBean retailProductBean;

	@ManagedProperty(value="#{orderBean}")
	private OrderBean orderBean;

	@ManagedProperty(value="#{saleBean}")
	private SaleBean saleBean;

	@ManagedProperty(value="#{paginationBean}")
	private PaginationBean paginationBean;


	@Inject
	private FacesContext facesContext;

	private BaseContract selectedContract;

	private Product selectedProduct;

	private List<? extends BaseContract> renewableContractList;
	
	private ContractStatus selectedContractStatus;
	
	private ContractStatus newContractStatus;
	
	private String newContractStatusNote;
	
//
//	private Shop renewableContractList_Filter_Shop;
//	
//	private User renewableContractList_Filter_User;
//	
//	private ContractStatus renewableContractList_Filter_Status;
	
	private long ownRenewableContractCount;

	public ContractBean() {
		clearCachedValues();
	}
	
//	@PostConstruct
//	public void init(){
//		renewableContractList_Filter_Status = contractController.getContractStatus_ACTIVE();
//		renewableContractList_Filter_User = userBean.getActiveUser();
//	}

	public List<? extends Tariff> getTariffList(){
		return getTariffList(false);
	}
	
	@Schedule( hour="1", persistent=false)
	public void clearCachedValues(){
		renewableContractList = null;
		ownRenewableContractCount = -1;
	}

	private void createNewOrder(){
		getSelectedContract().setSubsidyOrder(orderController.factoryOrder());
		getSelectedContract().getSubsidyOrder().setEnabled(true);
		getSelectedContract().getSubsidyOrder().setEffectiveDate(new Date());
		getSelectedContract().getSubsidyOrder().setUser(userBean.getActiveUser());
		getSelectedContract().getSubsidyOrder().setShop(shopBean.getActiveShop());
		getSelectedContract().getSubsidyOrder().setCustomer(customerBean.getCustomer());
	}
	
	private void clearForms(){
		newContractStatus = null;
		newContractStatusNote = null;
	}

	public List<? extends Tariff> getTariffList(boolean showAll){
		VO vo = null;

		if(! showAll){

			if(selectedContract == null || selectedContract.getVo() == null)
				return null;

			vo = selectedContract.getVo();
		}

		return productController.getTariffList(vo);
	}

	public List<? extends ProductOption> getProductOptionList(){

		if(selectedContract.getVo() == null)
			return null;

		return productController.getProductOptionList(selectedContract.getVo());
	}

	public BaseContract getSelectedContract() {

		if(selectedContract != null){

			if(selectedContract.getId() == 0){
				if(!selectedContract.getShop().equals(shopBean.getActiveShop()) || !(selectedContract.getCustomer().equals(customerBean.getCustomer()))){
					// new Contract with wrong customer or active shop
					clearSelectedContract();
				}

			}else{
				// view contract set active customer if needed
				if(!(selectedContract.getCustomer().equals(customerBean.getCustomer()))){
					customerBean.setSelectedCustomer(selectedContract.getCustomer());
				}
			}


		}

		if(selectedContract == null){
			selectedContract = contractController.factoryCellPhoneContract();
			selectedContract.setStatus(contractController.getContractStatus_ACTIVE());
			selectedContract.setEnabled(true);
			selectedContract.setEffectiveDate(new Date());
			selectedContract.setUser(userBean.getActiveUser());
			selectedContract.setShop(shopBean.getActiveShop());
			selectedContract.setCustomer(customerBean.getCustomer());
		}
		return selectedContract;
	}

	public BigDecimal getPointSumOfContractAndInvoice(){

		if(getSelectedContract().getSubsidyInvoice() != null)
			return this.getSelectedContract().getFinalCommission().getPoints().add(getSelectedContract().getSubsidyInvoice().getCommission().getPoints());
		else
			return this.getSelectedContract().getFinalCommission().getPoints();
	}

	public List<? extends BaseContract> getContractList() {

		Shop shop = null;
		VO vo = null;
		User user = null;
		Date from = null;
		Date to = null;
		return contractController.getContractList(shop, vo, user, from, to);
	}


	public long getOwnRenewableContractCount() {
		if(ownRenewableContractCount < 0 ){
			
			ownRenewableContractCount = contractController.countRenewableContractList(null, userBean.getActiveUser(), contractController.getContractStatus_ACTIVE(), null);
		}
		return ownRenewableContractCount;
	}

//	public List<? extends BaseContract> getOwnRenewableContractList() {
//		return getRenewableContractList(null, userBean.getActiveUser(), contractController.getContractStatus_ACTIVE(), false);
//	}
//
//	public List<? extends BaseContract> getRenewableContractList() {
//
//		if(renewableContractList == null){
//			renewableContractList = getRenewableContractList(getRenewableContractList_Filter_Shop(), getRenewableContractList_Filter_User(), getRenewableContractList_Filter_Status(), false);
//		}
//		return renewableContractList;
//	}
//
//	public List<? extends BaseContract> getRenewableContractList(Shop shopFilter, User userFilter, ContractStatus statusFilter, boolean refresh) {
//
//		if(userFilter != null && userFilter.getId() == 1){
//			// no Filter for supervisors
//			userFilter = null;
//		}
//		
//
//		if(!refresh){
//			long count = contractController.countRenewableContractList(shopFilter, userFilter, statusFilter);
//	
//			this.getPaginationBean().setMaxEntries(PAGINATION_KEY, count);
//			this.getPaginationBean().setLimit(PAGINATION_KEY, 20);
//		}
//		
//
//		List<? extends BaseContract> list =  contractController.getRenewableContractList(shopFilter, userFilter, statusFilter, this.getPaginationBean().getLimit(PAGINATION_KEY),this.getPaginationBean().getOffset(PAGINATION_KEY));
//		
//		
//		return list;
//	}
	
//	public void refreshRenewableContractList(){
//		renewableContractList = getRenewableContractList(getRenewableContractList_Filter_Shop(), getRenewableContractList_Filter_User(), getRenewableContractList_Filter_Status(), true);
//	}

	public void setSelectedContract(BaseContract selectedContract) {
		clearForms();
		this.selectedContract = selectedContract;
	}

	public void viewContract(BaseContract selectedContract) {
		customerBean.setSelectedCustomer(selectedContract.getCustomer());
		setSelectedContract(selectedContract);
	}

	public String getContractTitle(){
		if(getSelectedContract().getId() == 0)
			return "Neuer Vertrag";
		else{
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			return "Vertrag vom "+ formatter.format( getSelectedContract().getCreationDate() );
		}
	}

	public String saveContractAndCreateNew(){
		BaseContract contract = selectedContract;
		saveContract();

		contract = contractController.cloneContract(contract);

		if(contract instanceof PhoneContract){
			((PhoneContract) contract).setCallingNumber(null);
		}
		selectedContract = contract;
		
		return "";
	}

	public String saveContract(){
		
		String returnPage = "/customer/activities";

//		if( selectedContract.getExternalCustomer().getCustomerId().length() == 0)
//			selectedContract.setExternalCustomer(null);

//		if((selectedContract.getExternalCustomer() == null || selectedContract.getExternalCustomer().getCustomerId().length() == 0) && (selectedContract.getOrderNumber() == null || selectedContract.getOrderNumber().length() == 0)){
//			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte Kundennummer oder Auftragsnummer angeben.", null));
//			return "";
//		}

		if(saleBean.getInvoice() == null || saleBean.getInvoice().getInvoiceItemList() == null ||  saleBean.getInvoice().getInvoiceItemList().size() == 0){
			saleBean.setInvoice(null);
			selectedContract.setSubsidyInvoice(null);
		}else{
			saleBean.getInvoice().setSubsidyContract(null);
			saleBean.saveInvoice();
			selectedContract.setSubsidyInvoice(saleBean.getSelectedInvoice());
			saleBean.getSelectedInvoice().setSubsidyContract(selectedContract);
			returnPage = "/customer/invoiceView";
		}

		if(saleBean.getInvoice() != null){
		System.out.println("SAVE INVOICE: "+saleBean.getInvoice());
		System.out.println("SAVE INVOICE ID: "+saleBean.getInvoice().getId());
		System.out.println("SAVE INVOICE NR: "+saleBean.getInvoice().getNumber());
		}
		
		calculateCommission();
		calculateExpiryDate();

		selectedContract = contractController.saveContract(selectedContract);

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Vertrag erfolgreich gespeichert.", null));

		clearSelectedContract();

		customerBean.clearCache();
		
		clearCachedValues();

		return returnPage;
	}

	public String cancelContract(){

		contractController.cancelContract(selectedContract);

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Vertrag erfolgreich storniert.", null));

		clearSelectedContract();

		return "/customer/activities";
	}

	public void clearSelectedContract(){
		this.selectedContract = null;
		this.selectedProduct = null;
	}

	public void newContract() throws IOException {
		clearSelectedContract();
		this.getSelectedContract().newExternalCustomer();
		
		saleBean.newInvoice();
		saleBean.getInvoice().setSubsidyContract(this.getSelectedContract());
		this.getSelectedContract().setSubsidyInvoice(saleBean.getInvoice());

		facesContext.getExternalContext().redirect("/customer/contract.xhtml");
	}

	public void changeExternalCustomerId(){

		String customerId = this.getSelectedContract().getExternalCustomer().getCustomerId();

		ExternalCustomer externalCustomer = (ExternalCustomer) customerController.getExternalCustomerByCustomerId(customerId);

		if(externalCustomer == null){
			this.getSelectedContract().newExternalCustomer();
			this.getSelectedContract().getExternalCustomer().setCustomerId(customerId);
			this.getSelectedContract().getExternalCustomer().setCustomer(this.getSelectedContract().getCustomer());
			externalCustomer = this.getSelectedContract().getExternalCustomer();
		}


//		if(! externalCustomer.getCustomer().equals(this.getSelectedContract().getCustomer())){
//
//			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kundennummer in anderem Kunden gefunden!", null));
//			return;
//		}

		this.getSelectedContract().setExternalCustomer(externalCustomer);
	}

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public PropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(PropertyBean propertyBean) {
		this.propertyBean = propertyBean;
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

	public CustomerBean getCustomerBean() {
		return customerBean;
	}

	public void setCustomerBean(CustomerBean customerBean) {
		this.customerBean = customerBean;
	}

	public void calculateCommission(){

		if(selectedContract == null)
			return;

		commissionController.calculateCommission(selectedContract);

		if(selectedContract.getSubsidyOrder() != null)
			orderBean.calculateCommission(selectedContract.getSubsidyOrder());

	}

	private void calculateExpiryDate(){
		if(selectedContract.getEffectiveDate() == null)
			return;
		if(selectedContract.getTariff() == null)
			return;


		Calendar c = Calendar.getInstance();
		c.setTime(selectedContract.getEffectiveDate());
		c.add(Calendar.MONTH, selectedContract.getLeadTime());
		c.add(Calendar.MONTH, selectedContract.getTariff().getMinimumTermOfContract());
		selectedContract.setExpiryDate(c.getTime());
	}

	public boolean isContractAbleToExtensionOfTheTerm(BaseContract contract){
		return contractController.isContractAbleToExtensionOfTheTerm(contract);
	}

//	public long getContractTermInDays(BaseContract contract){
//
////		if(isContractAbleToExtensionOfTheTerm(contract)){
//
//		contract.getDaysTillExpiration();
//
//			Date today = new Date();
//			Date valueDate = contract.getExpiryDate();
//
//			long diffInMillies = valueDate.getTime() - today.getTime();
//
//
//			return TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);
//
////		}
////
////		return 0;
//	}

	public RetailProductBean getRetailProductBean() {
		return retailProductBean;
	}

	public void setRetailProductBean(RetailProductBean retailProductBean) {
		this.retailProductBean = retailProductBean;
	}

	public OrderBean getOrderBean() {
		return orderBean;
	}

	public void setOrderBean(OrderBean orderBean) {
		this.orderBean = orderBean;
	}

	public SaleBean getSaleBean() {
		return saleBean;
	}

	public void setSaleBean(SaleBean saleBean) {
		this.saleBean = saleBean;
	}

	public PaginationBean getPaginationBean() {
		return paginationBean;
	}

	public void setPaginationBean(PaginationBean paginationBean) {
		this.paginationBean = paginationBean;
	}

	public void addRetailProduct(){
		
		if(retailProductBean.getProduct() == null)
			return;

		if(retailProductBean.isForeignWare()){
			// add to contract
			contractController.addRetailItemToContract(selectedContract, retailProductBean.getProduct(), retailProductBean.getSerial());
		}else{
			// add to order

			if(selectedContract.getSubsidyOrder() == null)
				this.createNewOrder();

			orderBean.addRetailProduct(selectedContract.getSubsidyOrder());

		}

		retailProductBean.clearProduct();
	}


	public void removeRetailItemFromContract(ContractRetailItem contractRetailItem){
		contractController.removeRetailItemFromContract(selectedContract, contractRetailItem);
	}

	public void recalculateCommission(){
		BaseContract contract;
		Iterator<? extends BaseContract> i = contractController.getContractList(null, null, null, null, null).iterator();
		while(i.hasNext()){
			contract = i.next();
			commissionController.calculateCommission(contract);
			contractController.saveContract(contract);
		}
	}
	
	public List<? extends ContractStatus> getContractStatusList(){
		return contractController.getContractStatusList();
	}

	public ContractStatus getSelectedContractStatus() {
		if(selectedContractStatus == null)
			selectedContractStatus = contractController.factoryNewContractStatus();
		return selectedContractStatus;
	}

	public void setSelectedContractStatus(ContractStatus selectedContractStatus) {
		this.selectedContractStatus = selectedContractStatus;
	}

	public ContractStatus getNewContractStatus() {
		return newContractStatus;
	}

	public void setNewContractStatus(ContractStatus newContractStatus) {
		this.newContractStatus = newContractStatus;
	}

	public String getNewContractStatusNote() {
		return newContractStatusNote;
	}

	public void setNewContractStatusNote(String newContractStatusNote) {
		this.newContractStatusNote = newContractStatusNote;
	}

	public String saveContractStatus() {
		selectedContractStatus = contractController.saveContractStatus(selectedContractStatus);
		clearCachedValues();
		return "lists";
	}
	
	public String saveNewContractStatus(){
		if(this.selectedContract == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kein Vertrag selektiert.", null));
			return "";
		}
		if(this.newContractStatus == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kein neuer Vertragstatus gewählt.", null));
			return "";
		}
		if(this.newContractStatusNote == null || this.newContractStatusNote.length() == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keins Vertragstatuswechsel Information angegeben.", null));
			return "";
		}
		
		ContractStatusChange contractStatusChange = contractController.factoryNewContractStatusChange();

		contractStatusChange.setCustomer(selectedContract.getCustomer());
		contractStatusChange.setUser(userBean.getActiveUser());
		contractStatusChange.setShop(shopBean.getActiveShop());
		
		contractStatusChange.setContract(selectedContract);
		contractStatusChange.setOldStatus(selectedContract.getStatus().getName());
		contractStatusChange.setNewStatus(newContractStatus.getName());
		contractStatusChange.setNote(newContractStatusNote);
		
		contractStatusChange = contractController.saveContractStatusChange(contractStatusChange);
		
		selectedContract.setStatus(newContractStatus);
		
		selectedContract = contractController.saveContract(selectedContract);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuer Vertragstatus hinterlegt.", null));
		
		clearCachedValues();
		
		return "contractView";
	}

//	public Shop getRenewableContractList_Filter_Shop() {
//		return renewableContractList_Filter_Shop;
//	}
//
//	public void setRenewableContractList_Filter_Shop(Shop renewableContractList_Filter_Shop) {
//		renewableContractList = null;
//		this.renewableContractList_Filter_Shop = renewableContractList_Filter_Shop;
//	}
//
//	public User getRenewableContractList_Filter_User() {
//		return renewableContractList_Filter_User;
//	}
//
//	public void setRenewableContractList_Filter_User(User renewableContractList_Filter_User) {
//		renewableContractList = null;
//		this.renewableContractList_Filter_User = renewableContractList_Filter_User;
//	}
//
//	public ContractStatus getRenewableContractList_Filter_Status() {
//		return renewableContractList_Filter_Status;
//	}
//
//	public void setRenewableContractList_Filter_Status(ContractStatus renewableContractList_Filter_Status) {
//		renewableContractList = null;
//		this.renewableContractList_Filter_Status = renewableContractList_Filter_Status;
//	}
}
