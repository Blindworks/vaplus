package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.entity.TaxRate;

@Named
@SessionScoped
public class TaxRateConverter implements Converter{

	@EJB
	private TaxRateControllerInterface taxRateController;
	

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		long id;

		if(value == null)
			return null;
		if(value == "")
			return null;
		
		try{
			id = Long.valueOf(value);
		}
		catch(NumberFormatException e){
			return null;
		}

		return taxRateController.getTaxRateById(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof TaxRate))
			return null;

		return String.valueOf( ((TaxRate) value).getId() );
	}

}
