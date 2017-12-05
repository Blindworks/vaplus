package de.vaplus.client.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.DBControllerInterface;


@SessionScoped
@ManagedBean(name="systemBean")
public class SystemBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5730259920331561839L;
	
	@EJB
	private DBControllerInterface dbController;

	@Inject
	private FacesContext facesContext;
	
//	public void resetTrigger() throws Exception{
//		
//		dbController.resetTrigger();
//
//		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "DB Trigger erneuert.", null));
//	}
	
}