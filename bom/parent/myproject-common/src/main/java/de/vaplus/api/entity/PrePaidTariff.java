package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface PrePaidTariff extends BaseProduct{

	BigDecimal getGeneratedRevenue();

	void setGeneratedRevenue(BigDecimal generatedRevenue);

}
