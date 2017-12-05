package de.vaplus.api.entity.embeddable;

import java.io.Serializable;
import java.math.BigDecimal;

public interface Commissionable extends Serializable {

	BigDecimal getPoints();

	void setPoints(BigDecimal points);

	BigDecimal getCommission();

	void setCommission(BigDecimal commission);

	BigDecimal getPrice();

	void setPrice(BigDecimal price);


	Commissionable addCommissionable(Commissionable c2);

	Commissionable addCommissionable(Commissionable c2, boolean percentagePrice);

	BigDecimal getVat();

	void setVat(BigDecimal vat);

	BigDecimal getGrossPrice();

	void setGrossPrice(BigDecimal grossPrice);

	BigDecimal getTax();

	void setTax(BigDecimal tax);

	void invert();

}
