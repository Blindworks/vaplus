package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;


public interface RevenueLevel extends Base{

	String getName();

	void setName(String name);

	BigDecimal getScaleFrom();

	void setScaleFrom(BigDecimal scaleFrom);

	BigDecimal getScaleTo();

	void setScaleTo(BigDecimal scaleTo);

	Vendor getVendor();

	void setVendor(Vendor vendor);

//	RevenueLevelVOType getRevenueStepVOType();
//
//	void setRevenueStepVOType(RevenueLevelVOType revenueStepVOType);

	List<? extends VOTypeCommissionRevenueLevel> getVoTypeCommissionRevenueLevelList();


}
