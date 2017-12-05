package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CountryControllerInterface;
import de.vaplus.api.entity.ContractStatus;

@Named
public class ContractStatusConverter implements Converter{

	@EJB
	private ContractControllerInterface contractController;
	

	public ContractStatusConverter(){
	}

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

		return contractController.getContractStatusById(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof ContractStatus))
			return null;

		return String.valueOf( ((ContractStatus) value).getId() );
	}

}
