package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.VO;

@Named
@SessionScoped
public class VOConverter implements Converter{

	@EJB
	private VOControllerInterface voController;
	

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

		return voController.getVO(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if(value == null)
			return null;
		if(! (value instanceof VO))
			return null;

		return String.valueOf( ((VO) value).getId() );
	}

}
