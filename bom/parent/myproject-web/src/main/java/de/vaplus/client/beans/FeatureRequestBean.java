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
import de.vaplus.api.controller.stub.FeatureRequestStub;

@ManagedBean(name="featureRequestBean")
@SessionScoped
public class FeatureRequestBean implements Serializable {
	
	private static final long serialVersionUID = -5290990922023483183L;
	
	@EJB
	private ControllerClientInterface controllerClient;

	@Inject
	private FacesContext facesContext;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;
	
	private FeatureRequestStub selectedFeatureRequest;
	
//	@Inject 
//	@Named
//	private User user;
	
	
	
	
	public FeatureRequestBean() {
		// TODO Auto-generated constructor stub
	}

	public FeatureRequestStub getSelectedFeatureRequest() {
		if(selectedFeatureRequest == null){
			selectedFeatureRequest = controllerClient.factoryNewFeatureRequest();
			selectedFeatureRequest.setOwner(userBean.getActiveUser().getName());
		}
		return selectedFeatureRequest;
	}
	
	public String saveSelectedFeatureRequest() throws Exception{
		
		if(selectedFeatureRequest.getTitle().length() < 10){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte geben Sie einen treffenden Titel an.", null));
			return null;
		}
		
		if(selectedFeatureRequest.getDescription().length() < 10){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte geben Sie eine treffende Beschreibung an.", null));
			return null;
		}
		
		controllerClient.saveFeatureRequest(selectedFeatureRequest);
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "FeatureRequest wurde erfolgreich gemeldet.", null));
		
		selectedFeatureRequest = null;
		
		return "/support/featureRequestTracker";
	}

	public void setSelectedFeatureRequest(FeatureRequestStub selectedFeatureRequest) {
		this.selectedFeatureRequest = selectedFeatureRequest;
	}
	
	
	public List<FeatureRequestStub> getFeatureRequestList() throws Exception{
		return controllerClient.getFeatureRequestList();
	}
	
	public String newFeatureRequest(){
		selectedFeatureRequest = null;
		
		return "/support/newFeatureRequest";
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
}
