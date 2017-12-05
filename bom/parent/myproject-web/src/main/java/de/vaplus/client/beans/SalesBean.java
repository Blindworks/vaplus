package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.SalesControllerInterface;
import de.vaplus.api.SearchControllerInterface;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.client.pojo.Icon;
import de.vaplus.client.pojo.Page;

@ManagedBean(name="salesBean")
@SessionScoped
public class SalesBean implements Serializable {

	private static final long serialVersionUID = 5055261920162746676L;
	
	@EJB
	private SalesControllerInterface salesController;
	
	@Inject
	private FacesContext facesContext;

	public void updateAllSalesCache() {
		//salesController.updateAllSalesCache();
		//facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Neuberechnung des Kategorie Sales Cache gestartet.", null));
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_ERROR, "Neuberechnung des Kategorie Sales Cache DEAKTIVIERT!!!.", null));
	}
}
