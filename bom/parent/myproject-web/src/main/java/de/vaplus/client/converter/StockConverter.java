package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.entity.Stock;

@Named
//@FacesConverter(value="stockConverter")
//@SessionScoped
public class StockConverter implements Converter{

	@EJB
	private StockControllerInterface stockController;
	

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

		return stockController.getStock(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value == null)
			return null;
		if(! (value instanceof Stock))
			return null;

		return String.valueOf( ((Stock) value).getId() );
	}

}
