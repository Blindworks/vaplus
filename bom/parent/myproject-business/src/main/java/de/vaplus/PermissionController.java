package de.vaplus;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.PermissionControllerInterface;
import de.vaplus.api.entity.Permission;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserGroup;
import de.vaplus.client.eao.UserEao;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.UserGroupEntity;
import de.vaplus.client.entity.UserGroupPermissionEntity;
import de.vaplus.client.entity.UserPermissionEntity;


@Stateless
public class PermissionController implements PermissionControllerInterface {

	private static final long serialVersionUID = -5341178992971582245L;

    @Inject
    private UserEao userEao;
    
//	@EJB
//	private UserControllerInterface userController;

	@Override
	public boolean hasOwnPermission(UserGroup userGroup, String resource, String command){
		return hasPermission(((UserGroupEntity)userGroup).getPermissionList(), resource, command);
	}

	@Override
	public boolean hasOwnPermission(User user, String resource, String command){
		return hasPermission(((UserEntity)user).getPermissionList(), resource, command);
	}

	@Override
	public boolean hasPermission(User user, String resource, String command){
		
//		System.out.println("check permission: "+resource+" "+command);
		
		if(user.isSupervisor()){
//			System.out.println("Supervisor!");
			return true;
		}
		
		if(user.getUserGroup() != null && hasPermission(((UserGroupEntity)user.getUserGroup()).getPermissionList(), resource, command)){
//			System.out.println("User Group Permission is true");
			return true;
		}
		
		
		if(hasPermission(((UserEntity)user).getPermissionList(), resource, command)){
//			System.out.println("User Permission is true");
			return true;
		}
		
//		System.out.println("Permission is false");
		
		return false;
	}
	
	private boolean hasPermission(List<? extends Permission> list, String resource, String command){
		if(list == null)
			return false;
		
		Iterator<? extends Permission> i = list.iterator();
		Permission p;
		while(i.hasNext()){
			p = i.next();
			
			if(! p.getResource().equalsIgnoreCase(resource))
				continue;
			
			if(! p.getCommand().equalsIgnoreCase(command))
				continue;
			
			if(p.getPermission() == Permission.ALLOW)
				return true;
			
			if(p.getPermission() == Permission.DENY)
				return false;
			
		}
		
		return false;
	}

	@Override
	public void grantPermission(User user, String resource, String command, int permission) {
		
		List<UserPermissionEntity> list = ((UserEntity)user).getPermissionList();
		Iterator<? extends Permission> i = list.iterator();
		Permission p;
		while(i.hasNext()){
			p = i.next();
			
			if(! p.getResource().equalsIgnoreCase(resource))
				continue;
			
			if(! p.getCommand().equalsIgnoreCase(command))
				continue;
			
			p.setPermission(permission);
			return;
			
		}
		
		list.add(new UserPermissionEntity(user, resource, command, permission));
	}

	@Override
	public void grantPermission(UserGroup userGroup, String resource, String command, int permission) {
		
		List<UserGroupPermissionEntity> list = ((UserGroupEntity)userGroup).getPermissionList();
		
		Iterator<? extends Permission> i = list.iterator();
		Permission p;
		while(i.hasNext()){
			p = i.next();
			
			if(! p.getResource().equalsIgnoreCase(resource))
				continue;
			
			if(! p.getCommand().equalsIgnoreCase(command))
				continue;
			
			p.setPermission(permission);
			return;
			
		}
		
		list.add(new UserGroupPermissionEntity(userGroup, resource, command, permission));
		
	}

	@Override
	public void revokePermission(User user, String resource, String command) {
		grantPermission(user, resource, command, Permission.DENY);
	}

	@Override
	public void revokePermission(UserGroup userGroup, String resource, String command) {
		grantPermission(userGroup, resource, command, Permission.DENY);
	}

	@Override
	public User getUserByAccountTypeId(String attributedTypeId) {
		return userEao.getUserByAccountTypeId(attributedTypeId);
	}

//	private void revokePermission(List<? extends Permission> list, String resource, String command) {
//		
//		Iterator<? extends Permission> i = list.iterator();
//		Permission p;
//		while(i.hasNext()){
//			p = i.next();
//			
//			if(! p.getResource().equalsIgnoreCase(resource))
//				continue;
//			
//			if(! p.getCommand().equalsIgnoreCase(command))
//				continue;
//			
//			p.setPermission(Permission.DENY);
//			return;
//			
//		}
//	}
	
}