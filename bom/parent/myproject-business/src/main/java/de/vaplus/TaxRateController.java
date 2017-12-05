package de.vaplus;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.TaxRate;
import de.vaplus.client.eao.TaxRateEao;
import de.vaplus.client.entity.TaxRateEntity;


@Stateless
public class TaxRateController implements TaxRateControllerInterface {

	private static final long serialVersionUID = -7460049920607730224L;
	
	@Inject
    private TaxRateEao eao;


	@Override
	public List<? extends TaxRate> getTaxRateList() {
		return eao.getTaxRateList();
	}

	public void clearDefaultTaxRate(){
		Iterator<TaxRateEntity> i = (Iterator<TaxRateEntity>) eao.getTaxRateList().iterator();
		TaxRateEntity tax;
		while(i.hasNext()){
			tax = i.next();
			tax.setDefaultTaxRate(false);
			
			eao.saveTaxRate(tax);
		}
	}

	@Override
	public TaxRate factoryNewTaxRate() {
		return eao.factoryNewTaxRate();
	}

	@Override
	public TaxRate saveTaxRate(TaxRate taxRate) {
		clearDefaultTaxRate();
		
		return eao.saveTaxRate(taxRate);
	}

	@Override
	public TaxRate getDefaultTaxRate() {
		return eao.getDefaultTaxRate();
	}

	@Override
	public TaxRate getTaxRateById(long id) {
		return eao.getTaxRateById(id);
	}

}
