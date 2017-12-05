package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.TaxRate;

public interface TaxRateControllerInterface extends Serializable{

	List<? extends TaxRate> getTaxRateList();

	TaxRate saveTaxRate(TaxRate selectedTaxRate);

	TaxRate factoryNewTaxRate();

	TaxRate getTaxRateById(long id);

	TaxRate getDefaultTaxRate();


}
