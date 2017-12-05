package de.vaplus.client.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.picketlink.Identity;

import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.User;

@ManagedBean(name="userHelperBean")
@SessionScoped
public class UserHelperBean implements Serializable {

	private static final long serialVersionUID = -3560069326505270599L;

	@EJB
	private UserControllerInterface userController;

    @Inject
    private FacesContext facesContext;
	
    @ManagedProperty(value="#{mailBean}")
    private MailBean mailBean;

	private String username;
	
	private String email;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public MailBean getMailBean() {
		return mailBean;
	}
	
	public void setMailBean(MailBean mailBean) {
		this.mailBean = mailBean;
	}
	

	public String sendPasswordResetMail() {
		
    	System.out.println(this.username);
    	System.out.println(mailBean);
    	
		
		if(this.username == null || this.username.length() == 0){
    		facesContext.addMessage( null, new FacesMessage("Bitte Benutzername angeben."));
    		return "";
    	}

    	User u = userController.getUserByLoginName(this.username);
    	
    	if(u == null){
    		facesContext.addMessage( null, new FacesMessage("Benutzername nicht gefunden."));
    		return "";
    	}

    	
    	mailBean.sendChangePwdLink(u);
    	
    	this.username = null;

		return "login";
	}


	public String sendUsernameMail() {

		
		if(this.email == null || this.email.length() == 0){
    		facesContext.addMessage( null, new FacesMessage("Bitte Email angeben."));
    		return "";
    	}

    	User u = userController.getUserByEmail(this.email);
    	
    	if(u == null){
    		facesContext.addMessage( null, new FacesMessage("Benutzername nicht gefunden."));
    		return "";
    	}
    	
    	mailBean.sendUsernameEmail(u);

    	
    	this.email = null;
    	
		return "login";
	}

}
