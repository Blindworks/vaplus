package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.vaplus.api.entity.PhoneContract;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="PhoneContract")
public class PhoneContractEntity extends BaseContractEntity implements PhoneContract {
	
	private static final long serialVersionUID = 4533494371280900797L;
	
	@Column(name="callingNumber", nullable = false)
	private String callingNumber;

	@Column(name="frameworkContractId")
	private String frameworkContractId;

	@Column(name="userPassword")
	private String userPassword;
	
	public PhoneContractEntity() {
		super();
	}


	@Override
	public String getFrameworkContractId() {
		return frameworkContractId;
	}

	@Override
	public void setFrameworkContractId(String frameworkContractId) {
		this.frameworkContractId = frameworkContractId;
	}

	@Override
	public String getUserPassword() {
		return userPassword;
	}

	@Override
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	@Override
	public String getCallingNumber() {
		return callingNumber;
	}

	@Override
	public void setCallingNumber(String callingNumber) {
		this.callingNumber = callingNumber;
	}


	@Override
	public String getName() {
		return this.getCachedTariff().getProductName();
	}



}
