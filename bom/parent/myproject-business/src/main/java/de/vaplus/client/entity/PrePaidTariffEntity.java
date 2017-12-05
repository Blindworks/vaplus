package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.vaplus.api.entity.PrePaidTariff;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="PrePaidTariff")
public class PrePaidTariffEntity extends TariffEntity implements PrePaidTariff {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8900635241319355930L;

	@Column(name="generatedRevenue", precision = 10, scale = 4, nullable = false)
	private BigDecimal generatedRevenue;


	public PrePaidTariffEntity() {
		super();
	}

	@Override
	public BigDecimal getGeneratedRevenue() {
		if(generatedRevenue == null)
			generatedRevenue = new BigDecimal(0);
		return generatedRevenue;
	}

	@Override
	public void setGeneratedRevenue(BigDecimal generatedRevenue) {
		this.generatedRevenue = generatedRevenue;
	}

	@Override
	public boolean askForOrderNumber() {
		return false;
	}

}
