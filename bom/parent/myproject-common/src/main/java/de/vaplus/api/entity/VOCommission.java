package de.vaplus.api.entity;

import java.math.BigDecimal;


public interface VOCommission extends Base{

	VO getVo();

	void setVo(VO vo);

	int getYear();

	void setYear(int year);

	int getMonth();

	void setMonth(int month);

	BigDecimal getBaseAirtime();

	void setBaseAirtime(BigDecimal baseAirtime);

	BigDecimal getBonusAirtime();

	void setBonusAirtime(BigDecimal bonusAirtime);

	BigDecimal getRepairs();

	void setRepairs(BigDecimal repairs);

	BigDecimal getServicePackages();

	void setServicePackages(BigDecimal servicePackages);

	void clear();


}
