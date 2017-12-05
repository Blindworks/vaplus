package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.TaxRate;

@ManagedBean(name="taxRateBean")
@SessionScoped
public class TaxRateBean implements Serializable {

	private static final long serialVersionUID = 6659445347297651638L;

	@Inject
	private FacesContext facesContext;
	
	@EJB
	private TaxRateControllerInterface taxRateController;
	
	private TaxRate selectedTaxRate;
	
	private boolean taxRateListEditable;
	
	public TaxRateBean() {
		// TODO Auto-generated constructor stub
		
	}

	public boolean isTaxRateListEditable() {
		return taxRateListEditable;
	}

	public void toggleTaxRateListEditable() {
		taxRateListEditable = taxRateListEditable ? false : true;
	}

	public TaxRate getSelectedTaxRate() {
		if(selectedTaxRate == null)
			selectedTaxRate = taxRateController.factoryNewTaxRate();
		return selectedTaxRate;
	}

	public void setSelectedTaxRate(TaxRate selectedTaxRate) {
		this.selectedTaxRate = selectedTaxRate;
	}
	
	public List<? extends TaxRate> getTaxRateList(){
		return taxRateController.getTaxRateList();
	}
	
	public String saveTaxRate(){
		
		selectedTaxRate = taxRateController.saveTaxRate(selectedTaxRate);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "MwSt. Satz erfolgreich gespeichert.", null));
		
		return "lists";
	}

}
