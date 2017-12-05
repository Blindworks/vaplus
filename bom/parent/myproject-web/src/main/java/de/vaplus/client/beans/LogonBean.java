package de.vaplus.client.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.picketlink.Identity;

import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.User;

@ManagedBean(name="logonBean")
@SessionScoped
public class LogonBean implements Serializable {

	private static final long serialVersionUID = -7367710550466893205L;

	@EJB
	private UserControllerInterface userController;
    
    @Inject
    private Identity credentials;

    public User getCurrentUser() {
    	
    	org.picketlink.idm.model.basic.User loggedInUser = (org.picketlink.idm.model.basic.User) credentials.getAccount();
    	
    	if(loggedInUser == null)
    		return null;

    	User user = userController.getUserByAccountTypeId(loggedInUser.getId());

        return user;
    }
    

}
