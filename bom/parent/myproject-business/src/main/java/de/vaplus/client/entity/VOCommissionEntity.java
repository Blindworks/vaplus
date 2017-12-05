package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOCommission;

@Entity
@Table(name="VOCommission")
public class VOCommissionEntity extends BaseEntity implements VOCommission {

	private static final long serialVersionUID = -7303514288558648057L;

	@ManyToOne
    @JoinColumn(name="vo_id", nullable = false)
    private VOEntity vo;

	@Column(name="year", nullable = false)
	private int year;

	@Column(name="month", nullable = false)
	private int month;
	
	@Column(name="baseAirtime", nullable = false)
	private BigDecimal baseAirtime; 
	
	@Column(name="bonusAirtime", nullable = false)
	private BigDecimal bonusAirtime; 
	
	@Column(name="repairs", nullable = false)
	private BigDecimal repairs; 
	
	@Column(name="servicePackages", nullable = false)
	private BigDecimal servicePackages;

	@Override
	public VO getVo() {
		return vo;
	}

	@Override
	public void setVo(VO vo) {
		this.vo = (VOEntity) vo;
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
	public BigDecimal getBaseAirtime() {
		if(baseAirtime == null)
			baseAirtime = new BigDecimal(0);
		return baseAirtime;
	}

	@Override
	public void setBaseAirtime(BigDecimal baseAirtime) {
		this.baseAirtime = baseAirtime;
	}

	@Override
	public BigDecimal getBonusAirtime() {
		if(bonusAirtime == null)
			bonusAirtime = new BigDecimal(0);
		return bonusAirtime;
	}

	@Override
	public void setBonusAirtime(BigDecimal bonusAirtime) {
		this.bonusAirtime = bonusAirtime;
	}

	@Override
	public BigDecimal getRepairs() {
		if(repairs == null)
			repairs = new BigDecimal(0);
		return repairs;
	}

	@Override
	public void setRepairs(BigDecimal repairs) {
		this.repairs = repairs;
	}

	@Override
	public BigDecimal getServicePackages() {
		if(servicePackages == null)
			servicePackages = new BigDecimal(0);
		return servicePackages;
	}

	@Override
	public void setServicePackages(BigDecimal servicePackages) {
		this.servicePackages = servicePackages;
	} 

	@Override
	public void clear() {
		this.baseAirtime = new BigDecimal(0);
		this.bonusAirtime = new BigDecimal(0);
		this.repairs = new BigDecimal(0);
		this.servicePackages = new BigDecimal(0);
	} 
	
}
