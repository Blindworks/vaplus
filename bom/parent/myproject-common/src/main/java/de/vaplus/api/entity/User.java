package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.api.interfaces.RankableByCommission;

public interface User extends StatusBase, RankableByCommission, ActivityOwner{

	String getFirstname();

	String getLastname();

	void setFirstname(String firstname);

	void setLastname(String lastname);

	String getEmail();

	void setEmail(String email);

	String getName();

	JobTitle getJobTitle();

	void setJobTitle(JobTitle jobTitle);

	Address getAddress();

	void setAddress(Address address);

	String getTitle();

	void setTitle(String title);

	BigDecimal getPointGoal();

	void setPointGoal(BigDecimal pointGoal);

	List<? extends CommissionCache> getCommissionCacheList();

	CommissionCache getLiveCommissionCache();

	CommissionCache getCurrentCommissionCache();

	CommissionCache[] getCommissionCacheList(int year);

	CommissionCache[] getCurrentRangeCommissionCacheList();

	CommissionCache getLastCommissionCache();

	CommissionCache getNextCommissionCache();

	String getAccountTypeId();

	void setUserImage(DBFile userImage);

	DBFile getUserImage();

	String getColor();

	void setColor(String color);

	BigDecimal getPointsPerCommission();

	void setPointsPerCommission(BigDecimal pointsPerCommission);

	CommissionCache getCommissionCache(int year, int month);

	int getWeeklyWorkingTime();

	void setWeeklyWorkingTime(int weeklyWorkingTime);

	String getUuid();

	int getVacationDays();

	void setVacationDays(int vacationDays);

	int getWeeklyWorkingDays();

	void setWeeklyWorkingDays(int weeklyWorkingDays);

	UserGroup getUserGroup();

	void setUserGroup(UserGroup userGroup);

	boolean isSupervisor();

	void setSupervisor(boolean supervisor);

	int getFailedLogins();

	void setFailedLogins(int failedLogins);

	List<? extends UserStats> getStatsList();

	UserStats getStats(int year, int month);

	int getFullDayWorkingTime();

	Date getEntranceDate();

	void setEntranceDate(Date entranceDate);

	int getPlaningStartMonth();

	void setPlaningStartMonth(int planingStartMonth);

	int getPlaningStartYear();

	void setPlaningStartYear(int planingStartYear);

	List<? extends UserAlias> getAliasList();

	void setAliasList(List<? extends UserAlias> aliasList);


	ProductCategorySalesCache getProductCategorySalesCache(
			ProductCategory category, int year, int month);

	List<? extends ProductCategorySalesCache> getProductCategorySalesCacheList();

	void setProductCategorySalesCacheList(
			List<? extends ProductCategorySalesCache> productCategorySalesCacheList);

	Date getBday();

	void setBday(Date bday);

	String getStuffNumber();

	void setStuffNumber(String stuffNumber);

	String getTel();

	void setTel(String tel);

	BigDecimal getBasicSalary();

	void setBasicSalary(BigDecimal basicSalary);

	BigDecimal getBonusSalary();

	void setBonusSalary(BigDecimal bonusSalary);

	BigDecimal getMinimalBonusPointGoal();

	void setMinimalBonusPointGoal(BigDecimal minimalBonusPointGoal);

	EmploymentForm getFormOfEmployment();

	void setFormOfEmployment(EmploymentForm formOfEmployment);

	BigDecimal getCarGrossCatalogPrice();

	void setCarGrossCatalogPrice(BigDecimal carGrossCatalogPrice);

	boolean isHomeOffice();

	void setHomeOffice(boolean homeOffice);

	int getDistanceToWorkplace();

	void setDistanceToWorkplace(int distanceToWorkplace);

	boolean isPrivateCarUsage();

	void setPrivateCarUsage(boolean privateCarUsage);

	List<? extends Shop> getAllowedShops();

	void setAllowedShops(List<? extends Shop> allowedShops);

	List<? extends UserCustomerHistory> getCustomerHistoryList();

	List<? extends ProductCategorySalesCache> getCurrentRangeProductCategorySalesCacheList(
			ProductCategory category);

	State getState();

	void setState(State state);

}
