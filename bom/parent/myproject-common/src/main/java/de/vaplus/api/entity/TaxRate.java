package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface TaxRate extends Base{

	BigDecimal getTax();

	void setTax(BigDecimal tax);

	boolean isDefaultTaxRate();

	void setDefaultTaxRate(boolean defaultTaxRate);


}
