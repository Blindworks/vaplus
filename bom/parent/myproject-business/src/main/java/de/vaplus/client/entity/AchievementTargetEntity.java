package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;

@Entity
@Table(name="AchievementTarget")
public class AchievementTargetEntity extends BaseEntity implements AchievementTarget{

	private static final long serialVersionUID = 227211135857647891L;

	@Column(name="target")
	private BigDecimal target;
	
	@Column(name="payout")
	private BigDecimal payout;
	
	@Column(name="payoutText")
	private String payoutText;
	
	@Column(name="commission")
	private BigDecimal commission;
	
	public AchievementTargetEntity(){
		
	}

	public AchievementTargetEntity(BigDecimal target, BigDecimal payout, String payoutText, BigDecimal commission){
		setTarget(target);
		setPayout(payout);
		setPayoutText(payoutText);
		setCommission(commission);
	}
	
	@Override
	public BigDecimal getTarget() {
		if(target == null)
			target = new BigDecimal(0);
		return target;
	}

	@Override
	public void setTarget(BigDecimal target) {
		this.target = target;
	}

	@Override
	public BigDecimal getPayout() {
		if(payout == null)
			payout = new BigDecimal(0);
		return payout;
	}

	@Override
	public void setPayout(BigDecimal payout) {
		this.payout = payout;
	}

	@Override
	public String getPayoutText() {
		return payoutText;
	}

	@Override
	public void setPayoutText(String payoutText) {
		this.payoutText = payoutText;
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
	public boolean isTargetReached(BigDecimal attainment){
		if(attainment == null || getTarget() == null)
			return false;
		
		if(attainment.compareTo(getTarget()) >= 0)
			return true;
		
		return false;
	}
	
}
