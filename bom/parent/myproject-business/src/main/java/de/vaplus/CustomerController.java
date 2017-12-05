package de.vaplus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.Activity_old;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.Note;
import de.vaplus.api.entity.StatusBase;
import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.client.eao.ActivityEao;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.eao.CustomerEao;
import de.vaplus.client.entity.BaseContractCancellationEntity;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.CommissionActivityEntity;
import de.vaplus.client.entity.CustomerEntity;
import de.vaplus.client.entity.OrderEntity;


@Stateless
public class CustomerController implements CustomerControllerInterface {

	private static final long serialVersionUID = -3827031459923699365L;

	@Inject
    private CustomerEao customerEao;
	
	@Inject
    private ContractEao contractEao;
	
	@Inject
    private ActivityEao activityEao;

	@Override
	public Customer getCustomer(long id) {
		return customerEao.getCustomer(id);
	}

	@Override
	public List<? extends Customer> getCustomerList() {
		return customerEao.getCustomerList();
	}

	@Override
	public Customer saveCustomer(Customer shop) {
		return customerEao.saveCustomer((CustomerEntity) shop);
	}

	@Override
	public Customer factoryNewCustomer() {
		Customer customer = customerEao.factoryNewCustomer();
		customer.getAddress().setCountry("D");
		return customer;
	}

	@Override 
	public List<? extends Activity_old> getCustomerActivityList(Customer customer){
		
		List<? extends Activity_old> list = new ArrayList<Activity_old>();
		
		//FIXME
		//((List<StatusBase>)list).addAll(contractEao.getCustomerContractList(customer));
		//((List<Note>)list).addAll(customer.getNoteList());

		
		Collections.sort(list, new Comparator<Activity_old>() {
			  public int compare(Activity_old o1, Activity_old o2) {
				  if(o2.getCreationDate() == null)
					  return 1;
				  if(o1.getCreationDate() == null)
					  return -1;
			      return o2.getCreationDate().compareTo(o1.getCreationDate());
			  }
		});
		
		return list;
	}
	
	@Override
	public List<? extends Customer> getCustomer(String firstname, String lastname){
		return customerEao.findCustomer(firstname, lastname, true);
	}
	
	@Override
	public List<? extends Customer> getCustomerByCompanyName(String companyName){
		return customerEao.findCustomerByCompanyName(companyName);
	}

	@Override
	public Note factoryNewNote() {
		return customerEao.factoryNewNote();
	}
	
	@Override
	public String getCityByZip(String country, String zip){
		return customerEao.getCityByZip(country, zip);
	}

	@Override
	public List<? extends ExternalCustomer> getExternalCustomerList(
			Customer customer) {
		return customerEao.getExternalCustomerList(customer);
	}

	@Override
	public ExternalCustomer getExternalCustomer(long id) {
		return customerEao.getExternalCustomer(id);
	}

	@Override
	public List<? extends ExternalCustomer> findExternalCustomerByCustomerId(String customerId) {
		return customerEao.findExternalCustomerByCustomerId(customerId);
	}

	@Override
	public ExternalCustomer getExternalCustomerByCustomerId(String customerId) {
		return customerEao.getExternalCustomerByCustomerId(customerId);
	}

	@Override
	public List<? extends BaseContract> getContractMap(Customer selectedCustomer) {
		return contractEao.getContractList(null, null, null, selectedCustomer, null, null, false);
	}

	@Override
	public Customer getCustomerOrCreate(String firstname, String lastname,
			String companyName, boolean company, String street,
			String streetNumber, String addressline, String zip, String city, Date creationDate) {

		
		if(firstname != null)
			firstname = firstname.trim();
		
		if(lastname != null)
			lastname = lastname.trim();
		
		if(companyName != null)
			companyName = companyName.trim();
		
		if(street != null)
			street = street.trim();
		
		if(streetNumber != null)
			streetNumber = streetNumber.trim();
		
		if(addressline != null)
			addressline = addressline.trim();
		
		if(zip != null)
			zip = zip.trim();
		
		if(city != null)
			city = city.trim();
		
		
		CustomerEntity c = (CustomerEntity) customerEao.getCustomer( firstname, lastname,  companyName, company, street, streetNumber, addressline, zip, city);
		
		if(c == null){
			c = (CustomerEntity) factoryNewCustomer();
			c.setFirstname(firstname);
			c.setLastname(lastname);
			c.setCompanyName(companyName);
			c.setCompany(company);
			Address a = c.getAddress();
			a.setStreet(street);
			a.setStreetNumber(streetNumber);
			a.setAddressline(addressline);
			a.setZip(zip);
			a.setCity(city);
			
			if(company)
				c.setTitle("Firma");
			
			if(creationDate != null)
				c.setCreationDate(creationDate);
				
		}
		
		return c;
	}

	@Override
	public BigDecimal getContractPoints(Customer customer) {
		BigDecimal sum = new BigDecimal(0);
		
		Iterator<? extends Activity> i = activityEao.getActivities(customer, 0, 0).iterator();
		Activity a;
		
		while(i.hasNext()){
			a = i.next();
			
			if(a instanceof BaseContractEntity){
				sum = sum.add(((BaseContractEntity) a).getFinalCommission().getPoints());
			}
			else if(a instanceof BaseContractCancellationEntity){
				sum = sum.add(((BaseContractCancellationEntity) a).getCommission().getPoints());
			}
		}
		
		return sum;
	}

	@Override
	public BigDecimal getRetailPoints(Customer customer) {
		BigDecimal sum = new BigDecimal(0);
		
		Iterator<? extends Activity> i = activityEao.getActivities(customer, 0, 0).iterator();
		Activity a;
		
		while(i.hasNext()){
			a = i.next();
			
			if(a instanceof OrderEntity){
				sum = sum.add(((OrderEntity) a).getCommission().getPoints());
			}
		}
		
		return sum;
	}

	@Override
	public BigDecimal getMonthlyTotal(Customer customer) {
		BigDecimal sum = new BigDecimal(0);
		
		Iterator<? extends Activity> i = activityEao.getActivities(customer, 0, 0).iterator();
		Activity a;
		BaseContractEntity contract;
		
		while(i.hasNext()){
			a = i.next();
			
			if(a instanceof BaseContractEntity){
				
				contract = (BaseContractEntity) a;
				
				if(contract.getDaysTillExpiration() > 0 && contract.isExtensionOfTheTermPermission()){
					sum = sum.add(((BaseContractEntity) a).getFinalCommission().getPrice());
				}
				
				
			}
		}
		
		return sum;
	}

	@Override
	public ExternalCustomer saveSelectedExternalCustomer(
			ExternalCustomer externalCustomer) {
		return customerEao.saveExternalCustomer(externalCustomer);
	}

	@Override
	public ExternalCustomer reload(ExternalCustomer externalCustomer) {
		return customerEao.reload(externalCustomer);
	}

	@Override
	public List<? extends Customer> getCustomerList(int currentPage, int pageSize, String sortField, String sortOrder,
			String searchQuery) {
		return customerEao.getCustomerList(currentPage, pageSize, sortField, sortOrder, searchQuery);
	}

	@Override
	public long getCustomerCount() {
		return customerEao.getCustomerCount();
	}

	@Override
	public long getCustomerCount(int currentPage, int pageSize, String sortField, String sortOrder,
			String searchQuery) {
		return customerEao.getCustomerCount(currentPage, pageSize, sortField, sortOrder, searchQuery);
	}

	@Override
	public Customer refresh(Customer customer) {
		return customerEao.refreshCustomer(customer);
	}
    

}
