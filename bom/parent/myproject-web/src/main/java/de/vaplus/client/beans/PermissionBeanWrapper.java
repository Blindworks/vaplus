package de.vaplus.client.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;

import de.vaplus.api.PermissionControllerInterface;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.Permission;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserGroup;

@Named
@ManagedBean(name = "PermissionBeanWrapper")
@SessionScoped
public class PermissionBeanWrapper implements Serializable {

	private static final long serialVersionUID = -3889822100241856397L;

	@Inject
    private PermissionBean permissionBean;

	public PermissionBeanWrapper(){
//		 System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}

//	public void setPermissionBean(PermissionBean permissionBean){
//		this.permissionBean = permissionBean;
//	}
//
//	public PermissionBean getPermissionBean(){
//		return permissionBean;
//	}

	public boolean hasPermission(String resource, String command){
		return permissionBean.hasPermission(resource, command);
	}

}
