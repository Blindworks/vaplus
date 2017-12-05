package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.entity.Supplier;

@Named
//@FacesConverter(value="supplierConverter")
//@SessionScoped
public class SupplierConverter implements Converter{

	@EJB
	private ProductControllerInterface productController;
	

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

		return productController.getSupplierById(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value == null)
			return null;
		if(! (value instanceof Supplier))
			return null;

		return String.valueOf( ((Supplier) value).getId() );
	}

}
