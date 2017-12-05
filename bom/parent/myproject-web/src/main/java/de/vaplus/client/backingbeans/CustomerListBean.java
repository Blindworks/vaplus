package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.entity.Customer;

@ManagedBean(name = "customerListBean")
@SessionScoped
public class CustomerListBean implements Serializable {

	private static final long serialVersionUID = 1581305026776053866L;
	
	private int pageSize = 15;
	
	private int currentPage = 1;
	
	private String sortField;
	
	private String sortOrder;
	
	private String searchQuery;
	
	private List<? extends Customer> customerListPart;
	
	long customerCount;
	
	long filteredCustomerCount;
	
	@EJB
	private CustomerControllerInterface customerController;
	
	private void clearData(){
		customerListPart = null;
		customerCount = 0;
		filteredCustomerCount = 0;
	}
	
	private void loadCustomerListPart(){
		customerListPart = customerController.getCustomerList(currentPage, pageSize, sortField, sortOrder, searchQuery);
	}
	
	public long getCustomerCount(){
		if(customerCount == 0)
			customerCount = customerController.getCustomerCount();
		
		return customerCount;
	}
	
	public long getFilteredCustomerCount(){
		if(filteredCustomerCount == 0)
			filteredCustomerCount = customerController.getCustomerCount(currentPage, pageSize, sortField, sortOrder, searchQuery);
		return filteredCustomerCount;
	}
	
	public long getPageCount(){
		long pageCount = (long) Math.ceil((double)getFilteredCustomerCount() / (double)pageSize);
		
		return pageCount;
	}
	
	public List<? extends Customer> getCustomerListPart(){
		if(customerListPart == null)
			loadCustomerListPart();
		return customerListPart;
	}
	
	public void setPageSize(int size){
		pageSize = size;
		clearData();
	}
	
	public int getPageSize(){
		return pageSize;
	}
	
	public void setCurrentPage(int page){
		currentPage = page;
		clearData();
	}
	
	public int getCurrentPage(){
		return currentPage;
	}
	
	public void setSearchQuery(String query){
		searchQuery = query;
		setCurrentPage(1);
		clearData();
	}
	
	public String getSearchQuery(){
		return searchQuery;
	}
	
	public void setSortOrder(String order){
		sortOrder = order;
		clearData();
	}
	
	private void toggleSortOrder(){
		System.out.println("toggleSortOrder from "+getSortOrder());
		
		if(sortOrder.equalsIgnoreCase("asc"))
			setSortOrder("desc");
		else
			setSortOrder("asc");
	}
	
	public String getSortOrder(){
		return sortOrder;
	}
	
	public void setSortField(String field){
		System.out.println("setSortField "+field);
		
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
	
	public void toggleSortField_name(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("name")){
			setSortField("name");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_firstname(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("firstname")){
			setSortField("firstname");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void toggleSortField_address(){
		
		if(getSortField() == null || ! getSortField().equalsIgnoreCase("address")){
			setSortField("address");
			setSortOrder("asc");
		}
		else{
			toggleSortOrder();
		}
		
	}
	
	public void prevPage(){
		if(currentPage > 1)
			setCurrentPage(currentPage - 1);
	}
	
	public void nextPage(){
		if(currentPage < getPageCount())
			setCurrentPage(currentPage + 1);
	}
	
	public long getFirstCustomerNumber(){
		return (currentPage -1) * pageSize + 1;
	}
	
	public long getLastCustomerNumber(){
		long lastCustomerNumber = currentPage * pageSize;
		if(getFilteredCustomerCount() < lastCustomerNumber)
			lastCustomerNumber = getFilteredCustomerCount();
		return lastCustomerNumber;
	}
	
	public long getPrevPrevPageNumber(){
		long p = currentPage - 2;
		if(p > 0)
			return p;
		return 0;
	}
	
	public long getPrevPageNumber(){
		long p = currentPage - 1;
		if(p > 0)
			return p;
		return 0;
	}

	public long getNextPageNumber(){
		long p = currentPage + 1;
		if(p <= getPageCount())
			return p;
		return 0;
	}

	public long getNextNextPageNumber(){
		long p = currentPage + 2;
		if(p < getPageCount())
			return p;
		return 0;
	}
}
