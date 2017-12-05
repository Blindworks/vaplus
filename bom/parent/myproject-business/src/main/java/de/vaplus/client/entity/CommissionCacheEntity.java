package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;


@Entity
@Table(name="CommissionCache")
public class CommissionCacheEntity extends BaseEntity implements CommissionCache{

	private static final long serialVersionUID = -3739034248203521184L;

	@Column(name="year", nullable = false)
	private int year;

	@Column(name="month", nullable = false)
	private int month;

	@Column(name="extensionOfTheTermSum", nullable = false)
	private int extensionOfTheTermSum;

	@Column(name="newContractSum", nullable = false)
	private int newContractSum;

	@Column(name="pointGoal", precision = 10, scale = 4, nullable = false)
	private BigDecimal pointGoal;
	
	@Embedded
	private CommissionableEmbeddable commission;

	
	public CommissionCacheEntity(){
		pointGoal = new BigDecimal(0);
		commission = new CommissionableEmbeddable();
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
	public int getMonth() {
		return month;
	}

	@Override
	public void setMonth(int month) {
		this.month = month;
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
	public Commissionable getCommission() {
		if(commission == null)
			commission = new CommissionableEmbeddable();
		return commission;
	}

	@Override
	public void setCommission(Commissionable commission) {
		if(commission == null)
			commission = new CommissionableEmbeddable();
		else
			this.commission = (CommissionableEmbeddable) commission;
	}

	@Override
	public int getExtensionOfTheTermSum() {
		return extensionOfTheTermSum;
	}

	@Override
	public void setExtensionOfTheTermSum(int extensionOfTheTermSum) {
		this.extensionOfTheTermSum = extensionOfTheTermSum;
	}

	@Override
	public int getNewContractSum() {
		return newContractSum;
	}

	@Override
	public void setNewContractSum(int newContractSum) {
		this.newContractSum = newContractSum;
	}

	@Override
	@Transient
	public BigDecimal getGoalAttainment() {
		if(getPointGoal() == null || getPointGoal().compareTo(new BigDecimal(0)) == 0)
			return new BigDecimal(0);
		return getCommission().getPoints().divide( getPointGoal(), 4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
	}


}