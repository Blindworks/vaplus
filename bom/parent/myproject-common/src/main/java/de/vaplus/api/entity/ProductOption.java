package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface ProductOption extends BaseProduct{

	boolean isRevenueCommissionRelevant();

	void setRevenueCommissionRelevant(boolean revenueCommissionRelevant);

	boolean isPercentagePrice();

	int getWeight();

	void setWeight(int weight);

	boolean isForceExtensionOfTheTerm();

	void setForceExtensionOfTheTerm(boolean forceExtensionOfTheTerm);

	BigDecimal getGeneratedRevenue();

	void setGeneratedRevenue(BigDecimal generatedRevenue);
}
