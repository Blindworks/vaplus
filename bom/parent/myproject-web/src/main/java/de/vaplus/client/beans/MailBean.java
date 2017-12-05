package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.ImportLine;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.controller.ImportControllerInterface;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.pojo.ImportResult;
import de.vaplus.client.mail.Mailer;
import de.vaplus.client.mail.Theming;

@ManagedBean(name = "mailBean")
@SessionScoped
public class MailBean implements Serializable {

	private static final long serialVersionUID = -6251483338108616254L;

	@Inject
	private FacesContext facesContext;
	
	@EJB
	private UserControllerInterface userController;
	
    @ManagedProperty(value="#{helperBean}")
    private HelperBean helperBean;

	@EJB
	private Mailer mailer;
	
    @EJB
    private PicketLinkBean picketLinkBean;

	public void sendChangePwdLink(User user){
		
		if(user.getEmail() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Keine E-Mail Adresse im Kundenkonto gefunden!", null));
			return;
		}
		
		SecureUserLink link = userController.getPasswordResetLink(user);
		
		String baseURL = helperBean.getBaseURL();

    	Properties prop = new Properties();
    	prop.put("baseURL", baseURL);
    	prop.put("userName", user.getName());
    	prop.put("secureLink", baseURL+"/l.xhtml?c="+link.getCode());
    	
    	String mailBody = Theming.theme("/WEB-INF/mailTemplate/resetPassword.xhtml", prop);

    	mailer.sendMail(user.getEmail(), "Kennwort zurücksetzen", mailBody, null);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "E-Mail zum Zurücksetzen des Kennworts wurde versandt.", null));

	}
	
	public void sendWelcomeMessage(User user){
		
		if(user.getEmail() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Keine E-Mail Adresse im Kundenkonto gefunden!", null));
			return;
		}
		
		SecureUserLink link = userController.getPasswordResetLink(user);
		
		String baseURL = helperBean.getBaseURL();

    	Properties prop = new Properties();
    	prop.put("baseURL", baseURL);
    	prop.put("userName", user.getName());
    	prop.put("secureLink", baseURL+"/l.xhtml?c="+link.getCode());
    	
    	String mailBody = Theming.theme("/WEB-INF/mailTemplate/welcome.xhtml", prop);

    	mailer.sendMail(user.getEmail(), "Willkommen bei VA+", mailBody, null);

	}
	
	public void sendUsernameEmail(User user){
		
		if(user.getEmail() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Keine E-Mail Adresse im Kundenkonto gefunden!", null));
			return;
		}
		
		if(picketLinkBean.getUserLoginName(user) == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Kein Benutzernamen im Kundenkonto gefunden!", null));
			return;
		}
		
    	Properties prop = new Properties();
    	prop.put("name", user.getName());
    	prop.put("userName", picketLinkBean.getUserLoginName(user));
    	
    	String mailBody = Theming.theme("/WEB-INF/mailTemplate/sendUsername.xhtml", prop);

    	mailer.sendMail(user.getEmail(), "Ihr VA+ Benutzername", mailBody, null);

		facesContext.addMessage( null, new FacesMessage("Eine Email mit Ihrem Benuternamen wurde Ihnen zugesandt."));

	}

	public HelperBean getHelperBean() {
		return helperBean;
	}

	public void setHelperBean(HelperBean helperBean) {
		this.helperBean = helperBean;
	}
}
