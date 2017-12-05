package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.User;


@Named
public class StateConverter implements Converter{

	@EJB
	private StateControllerInterface stateController;
	

	public StateConverter(){
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

		return stateController.getStateById(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof State))
			return null;

		return String.valueOf( ((State) value).getId() );
	}

}
