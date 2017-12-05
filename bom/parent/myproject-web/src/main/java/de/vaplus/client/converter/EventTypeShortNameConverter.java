package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.entity.EventType;

@Named
@SessionScoped
public class EventTypeShortNameConverter implements Converter{

	@EJB
	private EventControllerInterface eventController;
	

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		long id;

		if(value == null)
			return null;
		if(value == "")
			return null;
		
		return eventController.getEventTypeByShortName(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof EventType))
			return null;

		return ((EventType) value).getShortName();
	}

}
