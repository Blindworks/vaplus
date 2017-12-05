package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.ControllerClientInterface;
import de.vaplus.api.controller.stub.BugStub;

@ManagedBean(name="bugBean")
@SessionScoped
public class BugBean implements Serializable {
	
	private static final long serialVersionUID = -5290990922023483183L;

	@Inject
	private FacesContext facesContext;
	
	@EJB
	private ControllerClientInterface controllerClient;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	private BugStub selectedBug;
	
	public BugBean() {
		// TODO Auto-generated constructor stub
		
	}

	public BugStub getSelectedBug() {
		if(selectedBug == null){
			selectedBug = controllerClient.factoryNewBug();
			selectedBug.setOwner(userBean.getActiveUser().getName());
		}
		return selectedBug;
	}
	
	public String saveSelectedBug() throws Exception{
		
		if(selectedBug.getTitle().length() < 10){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte geben Sie einen treffenden Titel an.", null));
			return null;
		}
		
		if(selectedBug.getDescription().length() < 10){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte geben Sie eine treffende Beschreibung an.", null));
			return null;
		}
		
		controllerClient.saveBug(selectedBug);
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Bug wurde erfolgreich gemeldet.", null));
		
		selectedBug = null;
		
		return "/support/bugTracker";
	}

	public void setSelectedBug(BugStub selectedBug) {
		this.selectedBug = selectedBug;
	}
	
	
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public List<BugStub> getBugList() throws Exception{
		return controllerClient.getBugList();
	}
	
	public String newBug(){
		selectedBug = null;
		
		return "/support/newBug";
	}
}
