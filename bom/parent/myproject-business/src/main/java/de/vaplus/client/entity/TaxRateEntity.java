package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.TaxRate;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="TaxRate")
public class TaxRateEntity extends BaseEntity implements TaxRate {

	private static final long serialVersionUID = 8828197819536869662L;

	@Column(name="tax", precision = 5, scale = 2, nullable = false)
	private BigDecimal tax;

	@Column(name="defaultTaxRate", nullable = false)
	private boolean defaultTaxRate;
	
	public TaxRateEntity() {
		super();
	}

	@Override
	public BigDecimal getTax() {
		if(tax == null)
			tax = new BigDecimal(0);
		return tax;
	}

	@Override
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	@Override
	public boolean isDefaultTaxRate() {
		return defaultTaxRate;
	}

	@Override
	public void setDefaultTaxRate(boolean defaultTaxRate) {
		this.defaultTaxRate = defaultTaxRate;
	}
	
	@Transient
	@Override
	public String toString(){
		return String.valueOf(tax.setScale(1, RoundingMode.HALF_UP)) + "%" ;
	}
}
