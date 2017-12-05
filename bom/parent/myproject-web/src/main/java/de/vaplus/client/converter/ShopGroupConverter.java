package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;

@Named
//@SessionScoped
public class ShopGroupConverter implements Converter{

	@EJB
	private ShopControllerInterface shopController;
	

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

		return shopController.getShopGroup(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof ShopGroup))
			return null;

		return String.valueOf( ((ShopGroup) value).getId() );
	}

}
