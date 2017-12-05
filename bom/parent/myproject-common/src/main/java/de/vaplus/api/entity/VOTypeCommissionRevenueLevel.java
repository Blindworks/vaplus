package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.Date;


public interface VOTypeCommissionRevenueLevel extends Base{

	BigDecimal getCommission();
	RevenueLevelVOType getRevenueLevelVOType();

	void setRevenueLevelVOType(
			de.vaplus.api.entity.RevenueLevelVOType revenueLevelVOType);

	RevenueLevel getRevenueLevel();

	void setRevenueLevel(RevenueLevel revenueLevel);

	Date getCreationDate();

	Date getUpdateDate();

	void setCommission(BigDecimal commission);
	BigDecimal getPoints();
	void setPoints(BigDecimal points);

}
