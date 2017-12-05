package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.UserGroup;


@Named
@SessionScoped
public class UserGroupConverter implements Converter{

	@EJB
	private UserControllerInterface userController;

	
	public UserGroupConverter(){
		// System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
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

		return userController.getUserGroupById(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof UserGroup))
			return null;

		return String.valueOf( ((UserGroup) value).getId() );
	}

}
