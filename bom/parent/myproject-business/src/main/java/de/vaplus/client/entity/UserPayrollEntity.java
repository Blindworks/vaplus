package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserPayroll;

@Entity
@Table(name="UserPayroll")
@NamedQueries({
    @NamedQuery(
            name = "UserPayroll.findByDate",
            query = "SELECT p FROM UserPayrollEntity p Where p.year = :year AND p.month = :month AND p.deleted = false ORDER BY p.user.lastname, p.user.firstname ASC",
            hints = {
            		@QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "UserPayroll.findByDateAndUser",
            query = "SELECT p FROM UserPayrollEntity p Where p.year = :year AND p.month = :month AND p.user = :user AND p.deleted = false ORDER BY p.user.lastname, p.user.firstname ASC",
            hints = {
            		@QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        )
})
public class UserPayrollEntity extends BaseEntity implements UserPayroll{

	private static final long serialVersionUID = -2043793741129927565L;

	@Column(name="month", nullable = false)
	private int month;

	@Column(name="year", nullable = false)
	private int year;

	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

	@Column(name="basicSalary", nullable = false, precision = 10, scale = 2)
	private BigDecimal basicSalary;

	@Column(name="bonusSalary", nullable = false, precision = 10, scale = 2)
	private BigDecimal bonusSalary;

	@Column(name="minimalBonusPointGoal", nullable = false, precision = 10, scale = 4)
	private BigDecimal minimalBonusPointGoal;

	@ManyToOne
    @JoinColumn(name="employmentForm_id", nullable = false)
	private EmploymentFormEntity formOfEmployment;
	
	@Column(name="carGrossCatalogPrice", nullable = false, precision = 10, scale = 2)
	private BigDecimal carGrossCatalogPrice;

	@Column(name="homeOffice")
	private boolean homeOffice;

	@Column(name="distanceToWorkplace")
	private int distanceToWorkplace;
	
	@Column(name="privateCarUsage")
	private boolean privateCarUsage;

	@Column(name="pointGoal", nullable = false, precision = 10, scale = 4)
	private BigDecimal pointGoal;

	@Column(name="pointsPerCommission", nullable = false)
	private BigDecimal pointsPerCommission;

	@Column(name="weeklyWorkingTime", nullable = false)
	private int weeklyWorkingTime;

	@Column(name="weeklyWorkingDays", nullable = false)
	private int weeklyWorkingDays;
	
	/*
	 * monthly dates
	 */

	// erwirtschaftete Kommission
	@Column(name="yieldedCommission", nullable = false, precision = 10, scale = 2)
	private BigDecimal yieldedCommission;
	
	// ausbezahlte Kommission
	@Column(name="commission", nullable = false, precision = 10, scale = 2)
	private BigDecimal commission;
	
	// Spesen
	@Column(name="expenses", nullable = false, precision = 10, scale = 2)
	private BigDecimal expenses;
	
	// Reisekosten
	@Column(name="travelExpenses", nullable = false, precision = 10, scale = 2)
	private BigDecimal travelExpenses;
	
	// Reisekosten
	@Column(name="hoursWorked", nullable = false, precision = 10, scale = 2)
	private BigDecimal hoursWorked;

	@Column(name="points", precision = 10, scale = 4, nullable = false)
	private BigDecimal points;
	
	@Lob 
	@Column(name="note")
	private String note;
	
	
	
//
//	-      Provision Ja/Nein
//
//	-      Gutschein(e) erhalten Ja/Nein
//
//	-          Anzahl Aktivierungen je Tarif mit ausgewiesenen Punkten
//
//	-          Anzahl VVL je Tarif mit ausgewiesenen Punkten
//
//	-          Anzahl CallYa je Tarif mit ausgewiesenen Punkten
//
//	-          Anzahl DSL/Kabel je Tarif mit ausgewiesenen Punkten
//
//	-          Auswertung Leistungsziele
//
//	-          Auszeichnung Sonderprovisionen


	public UserPayrollEntity(){
		init();
	}
	
	public UserPayrollEntity(User user, int month, int year){
		this.setUser(user);
		this.setMonth(month);
		this.setYear(year);
		init();
	}
	
	@Override
	public void updateUserValues(){
		
		if(user == null)
			return;
		
		this.setBasicSalary(user.getBasicSalary());
		this.setBonusSalary(user.getBonusSalary());
		this.setMinimalBonusPointGoal(user.getMinimalBonusPointGoal());
		this.setFormOfEmployment(user.getFormOfEmployment());
		this.setCarGrossCatalogPrice(user.getCarGrossCatalogPrice());
		this.setHomeOffice(user.isHomeOffice());
		this.setDistanceToWorkplace(user.getDistanceToWorkplace());
		this.setPrivateCarUsage(user.isPrivateCarUsage());
		this.setPointGoal(user.getPointGoal());
		this.setPointsPerCommission(user.getPointsPerCommission());
		this.setWeeklyWorkingDays(user.getWeeklyWorkingDays());
		this.setWeeklyWorkingTime(user.getWeeklyWorkingTime());
	}
	
	private void init(){
		this.getBasicSalary();
		this.getBonusSalary();
		this.getMinimalBonusPointGoal();
		this.getCarGrossCatalogPrice();
		this.getPointGoal();
		this.getPointsPerCommission();
		this.getYieldedCommission();
		this.getCommission();
		this.getExpenses();
		this.getTravelExpenses();
		this.getHoursWorked();
		this.getPoints();
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = (UserEntity) user;
		updateUserValues();
	}
	

	@Override
	public int getMonth() {
		return month;
	}

	@Override
	public void setMonth(int month) {
		this.month = month;
	}

	@Override
	public int getYear() {
		return year;
	}

	@Override
	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public BigDecimal getBasicSalary() {
		if(basicSalary == null)
			basicSalary = new BigDecimal(0);
		return basicSalary;
	}

	@Override
	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}

	@Override
	public BigDecimal getBonusSalary() {
		if(bonusSalary == null)
			bonusSalary = new BigDecimal(0);
		return bonusSalary;
	}

	@Override
	public void setBonusSalary(BigDecimal bonusSalary) {
		this.bonusSalary = bonusSalary;
	}

	@Override
	public BigDecimal getMinimalBonusPointGoal() {
		if(minimalBonusPointGoal == null)
			minimalBonusPointGoal = new BigDecimal(0);
		return minimalBonusPointGoal;
	}

	@Override
	public void setMinimalBonusPointGoal(BigDecimal minimalBonusPointGoal) {
		this.minimalBonusPointGoal = minimalBonusPointGoal;
	}

	@Override
	public EmploymentForm getFormOfEmployment() {
		return formOfEmployment;
	}

	@Override
	public void setFormOfEmployment(EmploymentForm formOfEmployment) {
		this.formOfEmployment = (EmploymentFormEntity) formOfEmployment;
	}

	@Override
	public BigDecimal getCarGrossCatalogPrice() {
		if(carGrossCatalogPrice == null)
			carGrossCatalogPrice = new BigDecimal(0);
		return carGrossCatalogPrice;
	}

	@Override
	public void setCarGrossCatalogPrice(BigDecimal carGrossCatalogPrice) {
		this.carGrossCatalogPrice = carGrossCatalogPrice;
	}

	@Override
	public boolean isHomeOffice() {
		return homeOffice;
	}

	@Override
	public void setHomeOffice(boolean homeOffice) {
		this.homeOffice = homeOffice;
	}

	@Override
	public int getDistanceToWorkplace() {
		return distanceToWorkplace;
	}

	@Override
	public void setDistanceToWorkplace(int distanceToWorkplace) {
		this.distanceToWorkplace = distanceToWorkplace;
	}

	@Override
	public boolean isPrivateCarUsage() {
		return privateCarUsage;
	}

	@Override
	public void setPrivateCarUsage(boolean privateCarUsage) {
		this.privateCarUsage = privateCarUsage;
	}

	@Override
	public BigDecimal getPointGoal() {
		if(pointGoal == null)
			pointGoal = new BigDecimal(0);
		return pointGoal;
	}

	@Override
	public void setPointGoal(BigDecimal pointGoal) {
		this.pointGoal = pointGoal;
	}

	@Override
	public BigDecimal getPointsPerCommission() {
		if(pointsPerCommission == null)
			pointsPerCommission = new BigDecimal(0);
		return pointsPerCommission;
	}

	@Override
	public void setPointsPerCommission(BigDecimal pointsPerCommission) {
		this.pointsPerCommission = pointsPerCommission;
	}

	@Override
	public int getWeeklyWorkingTime() {
		return weeklyWorkingTime;
	}

	@Override
	public void setWeeklyWorkingTime(int weeklyWorkingTime) {
		this.weeklyWorkingTime = weeklyWorkingTime;
	}

	@Override
	public int getWeeklyWorkingDays() {
		return weeklyWorkingDays;
	}

	@Override
	public void setWeeklyWorkingDays(int weeklyWorkingDays) {
		this.weeklyWorkingDays = weeklyWorkingDays;
	}

	@Override
	public BigDecimal getYieldedCommission() {
		if(yieldedCommission == null)
			yieldedCommission = new BigDecimal(0);
		return yieldedCommission;
	}

	@Override
	public void setYieldedCommission(BigDecimal yieldedCommission) {
		this.yieldedCommission = yieldedCommission;
	}

	@Override
	public BigDecimal getCommission() {
		if(commission == null)
			commission = new BigDecimal(0);
		return commission;
	}

	@Override
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	@Override
	public BigDecimal getExpenses() {
		if(expenses == null)
			expenses = new BigDecimal(0);
		return expenses;
	}

	@Override
	public void setExpenses(BigDecimal expenses) {
		this.expenses = expenses;
	}

	@Override
	public BigDecimal getTravelExpenses() {
		if(travelExpenses == null)
			travelExpenses= new BigDecimal(0);
		return travelExpenses;
	}

	@Override
	public void setTravelExpenses(BigDecimal travelExpenses) {
		this.travelExpenses = travelExpenses;
	}

	@Override
	public BigDecimal getHoursWorked() {
		if(hoursWorked == null)
			hoursWorked = new BigDecimal(0);
		return hoursWorked;
	}

	@Override
	public void setHoursWorked(BigDecimal hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	@Override
	public BigDecimal getPoints() {
		if(points == null)
			points = new BigDecimal(0);
		return points;
	}

	@Override
	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}
	



	@Override
	@Transient
	public BigDecimal getGoalAttainment() {
		if(getPointGoal() == null || getPointGoal().compareTo(new BigDecimal(0)) == 0)
			return new BigDecimal(0);
		return getPoints().divide( getPointGoal(), 4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
}
