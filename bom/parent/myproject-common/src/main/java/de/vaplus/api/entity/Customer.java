package de.vaplus.api.entity;

import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.embeddable.Address;


public interface Customer extends Base, ActivityOwner{

	String getFirstname();

	String getLastname();

	void setFirstname(String firstname);

	void setLastname(String lastname);

	String getEmail();

	void setEmail(String email);

	String getName();

	Address getAddress();

	void setAddress(Address address);

	String getTitle();

	void setTitle(String title);

	List<? extends Note> getNoteList();

	void setNoteList(List<? extends Note> noteList);

	String getCompanyName();

	void setCompanyName(String companyName);

	Date getBday();

	void setBday(Date bday);

	boolean isCompany();

	String getContactPerson();

	void setContactPerson(String contactPerson);

	Note getNote();

	void setNote(Note note);

	String getTel();

	void setTel(String tel);

	String getFax();

	void setFax(String fax);

	String getCommercialRegisterId();

	void setCommercialRegisterId(String commercialRegisterId);

	List<? extends ExternalCustomer> getExternalCustomerList();

	void setExternalCustomerList(
			List<? extends ExternalCustomer> externalCustomerList);

	String getNameLF();

	Shop getShop();

	void setShop(Shop shop);

	void setCompany(boolean company);

	User getAccountManager();

	void setAccountManager(User user);

	Country getNationality();

	void setNationality(Country nationality);

	PaymentAccount getPaymentAccount();

}
