package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.entity.ExternalCustomer;


@Named
@SessionScoped
public class ExternalCustomerConverter implements Converter{
	
	@EJB
	private CustomerControllerInterface customerController;

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

		return customerController.getExternalCustomer(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof ExternalCustomer))
			return null;

		return String.valueOf( ((ExternalCustomer) value).getId() );
	}

}
