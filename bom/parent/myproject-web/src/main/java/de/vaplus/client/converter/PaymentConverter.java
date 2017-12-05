package de.vaplus.client.converter;

import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import de.vaplus.api.PaymentControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.entity.Payment;

@Named
public class PaymentConverter implements Converter{

	@EJB
	private PaymentControllerInterface paymentController;
	

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

		return paymentController.getPaymentById(id);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value == null)
			return null;
		if(! (value instanceof Payment))
			return null;

		return String.valueOf( ((Payment) value).getId() );
	}

}
