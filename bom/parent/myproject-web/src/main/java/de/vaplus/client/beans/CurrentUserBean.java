package de.vaplus.client.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import org.picketlink.Identity;
import org.picketlink.idm.model.basic.Group;

import de.vaplus.api.ActivityControllerInterface;
import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.HolidayControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.SecureLinkControllerInterface;
import de.vaplus.api.TestControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.JobTitle;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserCustomerHistory;
import de.vaplus.api.entity.UserStats;
import de.vaplus.client.mail.Mailer;
import de.vaplus.client.mail.Theming;
import de.vaplus.client.picketlink.LoginController;

@ManagedBean(name="currentUserBean")
@RequestScoped
public class CurrentUserBean implements Serializable {
	
	private static final long serialVersionUID = 6342785680135899398L;


	@EJB
	private UserControllerInterface userController;

	@Inject
	private FacesContext facesContext;
	
    @Inject
    private Identity credentials;
	
    private User user;

	public CurrentUserBean() {
		// System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}
	
    public User getUser() {
    	if(user != null)
    		return user;
    	
//    	Date d = new Date();
    	
    	org.picketlink.idm.model.basic.User loggedInUser = (org.picketlink.idm.model.basic.User) credentials.getAccount();
    	
    	if(loggedInUser == null){
    		try {
    			if(!facesContext.getExternalContext().isResponseCommitted())
    				facesContext.getExternalContext().redirect("/login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return null;
    	}

    	user = userController.getUserByAccountTypeId(loggedInUser.getId());
    	

//    	Date d2 = new Date();
//
//    	long diff = d2.getTime() - d.getTime();
//    	
//    	System.out.println("getActiveUser: duration: "+diff+"ms");
    	
        return user;
    }
    
}
