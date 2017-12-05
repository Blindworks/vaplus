package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;

import de.vaplus.api.ModulControllerInterface;
import de.vaplus.api.PermissionControllerInterface;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.Permission;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserGroup;

//@Named
@ManagedBean(name = "permissionBean")
@SessionScoped
public class PermissionBean implements Serializable {

	private static final long serialVersionUID = -3889822100241856397L;

	@Inject
	private Identity identity;

//	@EJB
//    private UserControllerInterface userController;

	@ManagedProperty(value="#{userBean}")
	private UserBean userBean;

	private Map<String, Boolean> permissionCache;

	@EJB
    private PermissionControllerInterface permissionController;

	@EJB
    private ModulControllerInterface modulController;

	public PermissionBean(){
//		 System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
		permissionCache = new HashMap<String, Boolean> ();
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public void grantPermission(User user, String resource, String command){
		permissionController.grantPermission(user, resource, command, Permission.ALLOW);
	}

	public void revokePermission(User user, String resource, String command){
		permissionController.revokePermission(user, resource, command);
	}

	public void grantPermission(UserGroup userGroup, String resource, String command){
		permissionController.grantPermission(userGroup, resource, command, Permission.ALLOW);
	}

	public void revokePermission(UserGroup userGroup, String resource, String command){
		permissionController.revokePermission(userGroup, resource, command);
	}

	public boolean hasOwnPermission(User user, String resource, String command){
		return permissionController.hasOwnPermission(user, resource, command);
	}

	public boolean hasOwnPermission(UserGroup userGroup, String resource, String command){
		return permissionController.hasOwnPermission(userGroup, resource, command);
	}

	public boolean hasPermission(String resource, String command){
//		System.out.println("PermissionBean hasPermission "+resource+" "+command);

		if(identity == null)
			return false;

		if(identity.getAccount() == null)
			return false;

		
		// File check
		if(resource.equalsIgnoreCase("file")){
			if(! modulController.isFileModulLicensed())
				return false;
		}
		
		

		String key = identity.getAccount().getId()+"_"+resource+"_"+command;

		if(permissionCache.containsKey(key)){
			return permissionCache.get(key);
		}

		User user = permissionController.getUserByAccountTypeId(identity.getAccount().getId());

		if(user == null)
			return false;

		permissionCache.put(key, permissionController.hasPermission(user, resource, command));

		return permissionCache.get(key);
	}

	public boolean isAllowedToChangeContract(BaseContract contract){

		if(contract == null)
			return true;

		if(contract.getId() == 0)
			return true;

		if(hasPermission("contract", "edit"))
			return true;

		if(contract.getUser() == null)
			return false;


		if(identity.getAccount() == null)
			return false;

		User user = permissionController.getUserByAccountTypeId(identity.getAccount().getId());

		if(user == null)
			return false;

		if(contract.getUser().equals(user)){
			return hasPermission("contract", "edit_own");
		}

		return false;
	}

	public boolean isAllowedToCancelContract(BaseContract contract){

		if(contract == null)
			return true;

		if(contract.getId() == 0)
			return false;

		if(contract.isCanceled())
			return false;

		if(hasPermission("contract", "cancel"))
			return true;

		return false;
	}

}
