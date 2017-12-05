package de.vaplus.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.Activity_old;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.Note;

public interface CustomerControllerInterface extends Serializable {

	List<? extends Customer> getCustomerList();

	Customer saveCustomer(Customer shop);

	Customer factoryNewCustomer();

	Customer getCustomer(long id);

	List<? extends Activity_old> getCustomerActivityList(Customer customer);

	Note factoryNewNote();

	List<? extends Customer> getCustomer(String firstname, String lastname);

	String getCityByZip(String country, String zip);

	List<? extends ExternalCustomer> getExternalCustomerList(Customer customer);

	ExternalCustomer getExternalCustomer(long id);

	List<? extends ExternalCustomer> findExternalCustomerByCustomerId(String customerId);

	ExternalCustomer getExternalCustomerByCustomerId(String customerId);

	List<? extends Customer> getCustomerByCompanyName(String companyName);

	List<? extends BaseContract> getContractMap(Customer selectedCustomer);

	Customer getCustomerOrCreate(String firstname, String lastname, String companyName,
			boolean company, String street, String streetNumber,
			String addressLine, String zip, String city, Date creationDate);

	BigDecimal getContractPoints(Customer customer);

	BigDecimal getRetailPoints(Customer customer);

	BigDecimal getMonthlyTotal(Customer customer);

	ExternalCustomer saveSelectedExternalCustomer(
			ExternalCustomer selectedExternalCustomer);

	ExternalCustomer reload(ExternalCustomer selectedExternalCustomer);

	List<? extends Customer> getCustomerList(int currentPage, int pageSize, String sortField, String sortOrder,
			String searchQuery);

	long getCustomerCount();

	long getCustomerCount(int currentPage, int pageSize, String sortField, String sortOrder, String searchQuery);

	Customer refresh(Customer customer);

}
