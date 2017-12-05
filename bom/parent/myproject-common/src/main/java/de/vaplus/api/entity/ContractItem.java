package de.vaplus.api.entity;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface ContractItem extends StatusBase{

	String getProductName();

	Commissionable getProductCommission();

	BaseProduct getBaseProduct();

	Commissionable getVoCommission();

	void setVoCommission(Commissionable voCommission);

	Vendor getVendor();

	ProductCategory getProductCategory();

}
