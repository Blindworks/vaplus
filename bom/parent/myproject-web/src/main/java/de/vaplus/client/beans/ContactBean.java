package de.vaplus.client.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.MailControllerInterface;
import de.vaplus.api.PropertyControllerInterface;

@ManagedBean(name="contactBean")
@SessionScoped
public class ContactBean implements Serializable {
	
	private static final long serialVersionUID = -5290990922023483183L;

	@EJB
	private MailControllerInterface mailController;

	@EJB
	private PropertyControllerInterface propertyController;
	
	@Inject
	private FacesContext facesContext;


    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

	private String subject;
	private String body;
	
	
	public ContactBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String sendMailToSupport(){

		String preBody = "Support-Anfrage von: \n"+userBean.getActiveUser().getName()+"<br/>";
		preBody += userBean.getActiveUser().getEmail()+"<br/><br/>";
		preBody += propertyController.getLicenseName()+"<br/>";
		preBody += "App Version: "+ propertyController.getAppVerson()+"<br/>";
		preBody += "DB Version: "+ propertyController.getDBVersion()+"<br/>";
		
		
		mailController.sendMailToSupport(subject, preBody+"<br/><br/>"+body.replaceAll("\n", "<br/>"), userBean.getActiveUser().getEmail());
		subject = null;
		body = null;
		

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Nachricht erfolgreich versandt.", null));
		
		return "/support/faq";
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	

}
