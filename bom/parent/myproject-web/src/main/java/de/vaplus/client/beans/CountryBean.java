package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.CountryControllerInterface;
import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Country;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.User;

@ManagedBean(name="countryBean")
@SessionScoped
public class CountryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8954626904715145653L;

	@EJB
	private CountryControllerInterface countryController;
	
	@Inject
	private FacesContext facesContext;
	
	public List<? extends Country> getCountryList() {
		return countryController.getCountryList();
	}

}
