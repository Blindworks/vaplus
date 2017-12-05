package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.ContractStatus;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.client.beans.UserBean;
import de.vaplus.client.entity.BaseProductEntity;

@ManagedBean(name = "renewableContractListBean")
@SessionScoped
public class RenewableContractListBean implements Serializable {
	
	public static final String PAGINATION_KEY = "RenewableContractList";
	
	private static final long serialVersionUID = 4026273734085918770L;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;

	@ManagedProperty(value="#{paginationBean}")
	private PaginationBean paginationBean;

//	private int pageSize = 15;
	
//	private int currentPage = 1;
	
	private String sortField;
	
	private String sortOrder;
	
	private List<? extends BaseContract> contractListPart;
	
	long contractCount;
	
	long filteredContractCount;

	private Shop shopFilter;
	
	private User userFilter;
	
	private ContractStatus statusFilter;
	
	private BaseProductEntity tariffFilter;
	
	@EJB
	private ContractControllerInterface contractController;
	
	
	@PostConstruct
	public void init(){
		statusFilter = contractController.getContractStatus_ACTIVE();
		userFilter = userBean.getActiveUser();
		
		setSortField("expiryDate");
		setSortOrder("asc");
	}
	
	private void clearData(){
		contractListPart = null;
		contractCount = 0;
		filteredContractCount = 0;
		
		this.getPaginationBean().setLimit(PAGINATION_KEY, 20);
		
	}
	
	public void loadContractListPart(){
		
		filteredContractCount = contractController.countRenewableContractList(shopFilter, userFilter, statusFilter, tariffFilter);

//		System.out.println("filteredContractCount "+filteredContractCount);
		
		this.getPaginationBean().setMaxEntries(PAGINATION_KEY, filteredContractCount);
		
		
		contractListPart =  contractController.getRenewableContractList(shopFilter, userFilter, statusFilter, tariffFilter, this.getPaginationBean().getLimit(PAGINATION_KEY),this.getPaginationBean().getOffset(PAGINATION_KEY), sortField, sortOrder);
		
		
//		System.out.println("loadContractListPart sortField: "+sortField+" sortOrder:"+sortOrder);
		
	}
	
	public long getContractCount(){
//		if(contractCount == 0)
//			contractCount = contractController.getCustomerCount();
		
		return contractCount;
	}
	
	public long getFilteredContractCount(){
		if(filteredContractCount == 0)
			loadContractListPart();
		return filteredContractCount;
	}
	
//	public long getPageCount(){
//		if(getFilteredContractCount() == 0)
//			return 0;
//		
//		long pageCount = (long) Math.ceil((double)getFilteredContractCount() / (double)pageSize);
//		
//		return pageCount;
//	}
	
	public List<? extends BaseContract> getContractListPart(){
		if(contractListPart == null)
			loadContractListPart();
		return contractListPart;
	}
	
//	public void setPageSize(int size){
//		pageSize = size;
//		clearData();
//	}
//	
//	public int getPageSize(){
//		return pageSize;
//	}
//	
//	public void setCurrentPage(int page){
//		currentPage = page;
//		clearData();
//	}
//	
//	public int getCurrentPage(){
//		return currentPage;
//	}
	
//	public void setSearchQuery(String query){
//		searchQuery = query;
//		setCurrentPage(1);
//		clearData();
//	}
	
//	public String getSearchQuery(){
//		return searchQuery;
//	}
	
	public void setSortOrder(String order){
		sortOrder = order;
		clearData();
	}
	
	private void toggleSortOrder(){
//		System.out.println("toggleSortOrder from "+getSortOrder());
		
		if(sortOrder.equalsIgnoreCase("asc"))
			setSortOrder("desc");
		else
			setSortOrder("asc");
	}
	
	public String getSortOrder(){
		return sortOrder;
	}
	
	public void setSortField(String field){
//		System.out.println("setSortField "+field);
		
		sortField = field;
		clearData();
	}
	
	public String getSortField(){
		return sortField;
	}
	
	public void toggleSortField_id(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("id")){
			setSortField("id");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_customer(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("customer")){
			setSortField("customer");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_shop(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("shop")){
			setSortField("shop");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_user(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("user")){
			setSortField("user");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_callingNumber(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("callingNumber")){
			setSortField("callingNumber");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_tariff(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("tariff")){
			setSortField("tariff");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_expiryDate(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("expiryDate")){
			setSortField("expiryDate");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_status(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("status")){
			setSortField("status");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
//	public void prevPage(){
//		if(currentPage > 1)
//			setCurrentPage(currentPage - 1);
//	}
	
//	public void nextPage(){
//		if(currentPage < getPageCount())
//			setCurrentPage(currentPage + 1);
//	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public Shop getShopFilter() {
		return shopFilter;
	}

	public void setShopFilter(Shop shopFilter) {
		this.shopFilter = shopFilter;
		clearData();
	}

	public User getUserFilter() {
		return userFilter;
	}

	public void setUserFilter(User userFilter) {
		this.userFilter = userFilter;
		clearData();
	}

	public ContractStatus getStatusFilter() {
		return statusFilter;
	}

	public void setStatusFilter(ContractStatus statusFilter) {
		this.statusFilter = statusFilter;
		clearData();
	}

	public PaginationBean getPaginationBean() {
		return paginationBean;
	}

	public void setPaginationBean(PaginationBean paginationBean) {
		this.paginationBean = paginationBean;
	}

	public BaseProductEntity getTariffFilter() {
		return tariffFilter;
	}

	public void setTariffFilter(BaseProductEntity tariffFilter) {
		this.tariffFilter = tariffFilter;
		clearData();
	}
	
//	public long getFirstContractNumber(){
//		return (currentPage -1) * pageSize + 1;
//	}
//	
//	public long getLastContractNumber(){
//		long lastContractNumber = currentPage * pageSize;
//		if(getFilteredContractCount() < lastContractNumber)
//			lastContractNumber = getFilteredContractCount();
//		return lastContractNumber;
//	}
//	
//	public long getPrevPrevPageNumber(){
//		long p = currentPage - 2;
//		if(p > 0)
//			return p;
//		return 0;
//	}
//	
//	public long getPrevPageNumber(){
//		long p = currentPage - 1;
//		if(p > 0)
//			return p;
//		return 0;
//	}
//
//	public long getNextPageNumber(){
//		long p = currentPage + 1;
//		if(p <= getPageCount())
//			return p;
//		return 0;
//	}
//
//	public long getNextNextPageNumber(){
//		long p = currentPage + 2;
//		if(p < getPageCount())
//			return p;
//		return 0;
//	}
}
