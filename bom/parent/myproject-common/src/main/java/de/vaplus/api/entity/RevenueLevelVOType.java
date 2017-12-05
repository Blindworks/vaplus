package de.vaplus.api.entity;

import java.util.List;


public interface RevenueLevelVOType extends VOType{

	VOTypeCommissionRevenueLevel getVoTypeCommissionRevenueLevel(
			RevenueLevel revenueLevel);

	List<? extends VOTypeCommissionRevenueLevel> getVoTypeCommissionRevenueLevelList();


}
