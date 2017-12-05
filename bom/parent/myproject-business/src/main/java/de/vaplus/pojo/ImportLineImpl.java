package de.vaplus.pojo;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.vaplus.api.ImportLine;

public class ImportLineImpl implements ImportLine{

	private Date creationDate;
	
	private boolean company;
	
	private String companyName;
	
	private String firstname;
	
	private String lastname;
	
	private String addressLine1;
	
	private String addressLine2;
	
	private String zip;

	private String city;
	
	private String email;

	private Date dayOfBirth;

	private String password;

	private String ddi;
	
	private String[] productNames;

	private String info;

	private String categoryName;

	private String userName;

	private String shopName;

	private String voNumber;

	private String street;

	private String streetNumber;
	
	private String cleanString(String str){
		if(str == null)
			return null;
		return str.replaceAll("[^A-Za-z0-9äüöÄÜÖ\\+\\-\\_\\,\\.\\(\\)\\%\\&\\=\\@\\<\\> ]", "");
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public boolean isCompany() {
		return company;
	}

	@Override
	public void setCompany(boolean company) {
		this.company = company;
	}

	@Override
	public String getCompanyName() {
		return companyName;
	}

	@Override
	public void setCompanyName(String companyName) {
		this.companyName = cleanString(companyName);
	}

	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public void setFirstname(String firstname) {
		this.firstname = cleanString(firstname);
	}

	@Override
	public String getLastname() {
		return lastname;
	}

	@Override
	public void setLastname(String lastname) {
		this.lastname = cleanString(lastname);
	}

	@Override
	public String getAddressLine1() {
		return addressLine1;
	}

	@Override
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = cleanString(addressLine1);
	}

	@Override
	public String getAddressLine2() {
		return addressLine2;
	}

	@Override
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = cleanString(addressLine2);
	}

	@Override
	public String getZip() {
		return zip;
	}

	@Override
	public void setZip(String zip) {
		this.zip = cleanString(zip);
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = cleanString(city);
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = cleanString(email);
	}

	@Override
	public Date getDayOfBirth() {
		return dayOfBirth;
	}

	@Override
	public void setDayOfBirth(Date dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getDdi() {
		return ddi;
	}

	@Override
	public void setDdi(String ddi) {
		this.ddi = cleanString(ddi);
	}

	@Override
	public String[] getProductNames() {
		return productNames;
	}

	@Override
	public void setProductNames(String[] productNames) {
		this.productNames = new String[productNames.length];
		
		for(int i=0; i<productNames.length; i++){
			this.productNames[i] = cleanString(productNames[i]);
		}
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String info) {
		this.info = cleanString(info);
	}

	@Override
	public String getCategoryName() {
		return categoryName;
	}

	@Override
	public void setCategoryName(String categoryName) {
		this.categoryName = cleanString(categoryName);
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public void setUserName(String userName) {
		this.userName = cleanString(userName);
	}

	@Override
	public String getShopName() {
		return shopName;
	}

	@Override
	public void setShopName(String shopName) {
		this.shopName = cleanString(shopName);
	}

	@Override
	public String getVoNumber() {
		return voNumber;
	}

	@Override
	public void setVoNumber(String voNumber) {
		this.voNumber = cleanString(voNumber);
	}
	


	@Override
	public String getStreet() {
		
		if(street != null && street.length() > 0)
			return street;
		
		if(getAddressLine1() == null)
			return null;
		
	    Pattern pattern = Pattern.compile("[0-9].*");
	    Matcher matcher = pattern.matcher(getAddressLine1());
	    if (matcher.find())
	    {
	    	return getAddressLine1().replace(matcher.group(0), "").trim();
	    }
		
		return addressLine1;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String getStreetNumber() {
		
		if(streetNumber != null && streetNumber.length() > 0)
			return streetNumber;
		
		if(getAddressLine1() == null)
			return null;
		
	    Pattern pattern = Pattern.compile("[0-9].*");
	    Matcher matcher = pattern.matcher(getAddressLine1());
	    if (matcher.find())
	    {
	    	return matcher.group(0).trim();
	    }
		
		return null;
	}

	@Override
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = cleanString(streetNumber);
	}
}
