/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.vaplus.client.picketlink;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.omnifaces.component.output.cache.CacheFactory;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.authentication.UserAlreadyLoggedInException;
import org.picketlink.credential.DefaultLoginCredentials;
//import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;




import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.User;
import de.vaplus.client.beans.HelperBean;
import de.vaplus.client.beans.MailBean;

/**
 * We control the authentication process from this action bean, so that in the event of a failed authentication we can add an
 * appropriate FacesMessage to the response.
 *
 * @author Shane Bryzak
 *
 */

@Named
@Stateless
public class LoginController {

    @Inject
    private Identity identity;

    @Inject
    private DefaultLoginCredentials loginCredentials;

    @Inject
    private FacesContext facesContext;

	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private UserControllerInterface userController;

	@EJB
	private PropertyControllerInterface propertyController;

	public LoginController(){
//		 System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}

    /* (non-Javadoc)
	 * @see picketlink.LoginControllerx#login()
	 *
	 */
	public String login() {
		User u = null;

    	try{
    		u = userController.getUserByLoginName(loginCredentials.getUserId());

    		if(u == null){
        		facesContext.addMessage( null, new FacesMessage("Anmeldung fehlgeschlagen."));
        		return "";
        	}
    		else if(u.getFailedLogins() > 3){
        		facesContext.addMessage( null, new FacesMessage("Ihr Konto wurde aufgrund zu vieler erfolgloser Login- Versuche gesperrt."));
        		return "";
        	}
    		else if(! u.isEnabled() || u.isDeleted()){
        		facesContext.addMessage( null, new FacesMessage("Ihr Zugang wurde gesperrt"));
        		return "";
        	}

	        AuthenticationResult result = identity.login();
	        if (AuthenticationResult.FAILED.equals(result)) {
	            facesContext.addMessage(
	                    null,
	                    new FacesMessage("Anmeldung fehlgeschlagen."));

	            userController.loginFailed(u);

	            userController.createSupervisorUser();
	            
	            return "";

	        }else {
	        	u = userController.getUserByAccountTypeId(identity.getAccount().getId());

	        	// login successfull
	        	userController.clearFailedLogins(u);

	        	CacheFactory.getCache(facesContext, "session").remove("pageNavCache");
	        	CacheFactory.getCache(facesContext, "session").remove("sideBarCustomerHistoryCache");
	        	CacheFactory.getCache(facesContext, "session").remove("sideBarTopProductsCache");
	        }
    	}catch(UserAlreadyLoggedInException e){
    	}


    	if(u != null && propertyController.getBooleanUserProperty(u, "ShowShopGroupDashboard", false)){
            return "dashboardShopGroup";
    	}else{
            return "dashboard";
    	}

    }




    /* (non-Javadoc)
	 * @see picketlink.LoginControllerx#logout()
	 */
	public String logout() {
    	identity.logout();
    	facesContext.getExternalContext().invalidateSession();
    	return "login";
    }




}
