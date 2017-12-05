package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface BaseProduct extends StatusBase{

	String getName();

	void setName(String name);

	ProductCategory getProductCategory();

	void setProductCategory(ProductCategory productCategory);

	Commissionable getCommission();

	void setCommission(Commissionable commission);

	Vendor getVendor();

	void setVendor(Vendor vendor);

	List<? extends VOType> getVoTypePermissionList();

	void setVoTypePermissionList(List<? extends VOType> voTypePermissionList);

	void setBaseProductValues(BaseProduct product);

	TaxRate getTaxRate();

	void setTaxRate(TaxRate taxRate);

	BigDecimal getVat();
}
