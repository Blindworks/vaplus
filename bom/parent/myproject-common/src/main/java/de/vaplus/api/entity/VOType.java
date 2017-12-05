package de.vaplus.api.entity;

import java.math.BigDecimal;


public interface VOType extends StatusBase{

	String getName();

	void setName(String name);

	Vendor getVendor();

	void setVendor(Vendor vendor);

	String getShortName();

	void setShortName(String shortName);

	void setPointsPerCommission(BigDecimal pointsPerCommission);

	BigDecimal getPointsPerCommission();

}
