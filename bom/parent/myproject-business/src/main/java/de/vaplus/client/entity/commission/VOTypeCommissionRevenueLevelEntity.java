package de.vaplus.client.entity.commission;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.RevenueLevel;
import de.vaplus.api.entity.RevenueLevelVOType;
import de.vaplus.api.entity.VOTypeCommissionRevenueLevel;
import de.vaplus.client.entity.BaseEntity;
import de.vaplus.client.entity.RevenueLevelVOTypeEntity;

/**
 * Entity implementation class for Entity: VOTypeCommissionRevenueLevelEntity
 *
 */
@Entity
@Table(name = "VOTypeCommissionRevenueLevel")
public class VOTypeCommissionRevenueLevelEntity extends BaseEntity implements VOTypeCommissionRevenueLevel {

	private static final long serialVersionUID = 5419886176961684994L;

	@ManyToOne
    @JoinColumn(name="revenueLevelVOType_id", nullable = false)
	private RevenueLevelVOTypeEntity revenueLevelVOType;

	@ManyToOne
    @JoinColumn(name="revenueLevel_id", nullable = false)
	private RevenueLevelEntity revenueLevel;

	@Column(name="commission", precision = 10, scale = 4, nullable = false)
	private BigDecimal commission;

	@Column(name="points", precision = 10, scale = 4, nullable = false)
	private BigDecimal points;

	public VOTypeCommissionRevenueLevelEntity() {
		super();
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
	public RevenueLevelVOType getRevenueLevelVOType() {
		return revenueLevelVOType;
	}

	@Override
	public void setRevenueLevelVOType(RevenueLevelVOType revenueLevelVOType) {
		this.revenueLevelVOType = (RevenueLevelVOTypeEntity) revenueLevelVOType;
	}

	@Override
	public RevenueLevel getRevenueLevel() {
		return revenueLevel;
	}

	@Override
	public void setRevenueLevel(RevenueLevel revenueLevel) {
		this.revenueLevel = (RevenueLevelEntity) revenueLevel;
	}

	@Override
	public BigDecimal getPoints() {
		return points;
	}

	@Override
	public void setPoints(BigDecimal points) {
		this.points = points;
	}
   
}
