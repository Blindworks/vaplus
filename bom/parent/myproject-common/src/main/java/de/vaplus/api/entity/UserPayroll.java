package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface UserPayroll extends Base{


	public User getUser();

	public void setUser(User user);

	public int getMonth();

	public void setMonth(int month);

	public int getYear();

	public void setYear(int year);

	public BigDecimal getBasicSalary();

	public void setBasicSalary(BigDecimal basicSalary);

	public BigDecimal getBonusSalary();

	public void setBonusSalary(BigDecimal bonusSalary);

	public BigDecimal getMinimalBonusPointGoal();

	public void setMinimalBonusPointGoal(BigDecimal minimalBonusPointGoal);

	public EmploymentForm getFormOfEmployment();

	public void setFormOfEmployment(EmploymentForm formOfEmployment);

	public BigDecimal getCarGrossCatalogPrice();

	public void setCarGrossCatalogPrice(BigDecimal carGrossCatalogPrice);

	public boolean isHomeOffice();

	public void setHomeOffice(boolean homeOffice);

	public int getDistanceToWorkplace();

	public void setDistanceToWorkplace(int distanceToWorkplace);

	public boolean isPrivateCarUsage();

	public void setPrivateCarUsage(boolean privateCarUsage);

	public BigDecimal getPointGoal();

	public void setPointGoal(BigDecimal pointGoal);

	public BigDecimal getPointsPerCommission();

	public void setPointsPerCommission(BigDecimal pointsPerCommission);

	public int getWeeklyWorkingTime();

	public void setWeeklyWorkingTime(int weeklyWorkingTime);

	public int getWeeklyWorkingDays();

	public void setWeeklyWorkingDays(int weeklyWorkingDays);

	public BigDecimal getYieldedCommission();

	public void setYieldedCommission(BigDecimal yieldedCommission);

	public BigDecimal getCommission();

	public void setCommission(BigDecimal commission);

	public BigDecimal getExpenses();

	public void setExpenses(BigDecimal expenses);

	public BigDecimal getTravelExpenses();

	public void setTravelExpenses(BigDecimal travelExpenses);

	public BigDecimal getHoursWorked();

	public void setHoursWorked(BigDecimal hoursWorked);

	public BigDecimal getPoints();

	public void setPoints(BigDecimal points);

	public String getNote();

	public void setNote(String note);

	BigDecimal getGoalAttainment();

	void updateUserValues();



}
