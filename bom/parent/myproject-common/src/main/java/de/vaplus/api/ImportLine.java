package de.vaplus.api;

import java.util.Date;

public interface ImportLine {

	Date getCreationDate();

	void setCreationDate(Date creationDate);

	boolean isCompany();

	void setCompany(boolean company);

	String getCompanyName();

	void setCompanyName(String companyName);

	String getFirstname();

	void setFirstname(String firstname);

	String getLastname();

	void setLastname(String lastname);

	String getAddressLine1();

	void setAddressLine1(String addressLine1);

	String getAddressLine2();

	void setAddressLine2(String addressLine2);

	String getZip();

	void setZip(String zip);

	String getCity();

	void setCity(String city);

	String getEmail();

	void setEmail(String email);

	Date getDayOfBirth();

	void setDayOfBirth(Date dayOfBirth);

	String getPassword();

	void setPassword(String password);

	String getDdi();

	void setDdi(String ddi);

	String[] getProductNames();

	void setProductNames(String[] productNames);

	String getInfo();

	void setInfo(String info);

	String getCategoryName();

	void setCategoryName(String categoryName);

	String getUserName();

	void setUserName(String userName);

	String getShopName();

	void setShopName(String shopName);

	String getVoNumber();

	void setVoNumber(String voNumber);

	String getStreet();

	String getStreetNumber();

	void setStreet(String street);

	void setStreetNumber(String streetNumber);
	
	
}
