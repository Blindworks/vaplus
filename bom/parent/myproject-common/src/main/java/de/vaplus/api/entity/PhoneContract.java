package de.vaplus.api.entity;


public interface PhoneContract extends BaseContract{

	String getFrameworkContractId();

	void setFrameworkContractId(String frameworkContractId);

	String getCallingNumber();

	void setCallingNumber(String callingNumber);

	String getUserPassword();

	void setUserPassword(String userPassword);

}
