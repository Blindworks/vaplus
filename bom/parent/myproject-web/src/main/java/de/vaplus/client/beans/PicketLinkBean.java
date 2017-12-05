package de.vaplus.client.beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.PermissionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Credentials.Status;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.permission.acl.spi.PersistentPermissionVoter;
import org.picketlink.idm.permission.spi.PermissionResolver;
import org.picketlink.idm.permission.spi.PermissionVoter.VotingResult;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;


//@ManagedBean(name="picketLinkBean")
@Stateless
public class PicketLinkBean {

    @Inject
    private PartitionManager partitionManager;
    
    @Inject
    private Identity identity;

    @Inject
    private Instance<PermissionResolver> permissionResolver;
    



	public boolean hasRole(User plUser, String roleName){
		
		RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
		
		Role role = getRole(roleName);
		
		return BasicModel.hasRole(relationshipManager, plUser, role);
	}

	public boolean hasRole(de.vaplus.api.entity.User user, String roleName){
		
		if(user == null || user.getAccountTypeId() == null)
			return false;

		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		
		User plUser = identityManager.lookupById(User.class, user.getAccountTypeId());
		
		return hasRole(plUser, roleName);
	}
    
    public boolean hasRole(String roleName){
    	
    	return hasRole((User) identity.getAccount(), roleName);
    }

	public boolean hasGroup(User plUser, Group group){
		
		RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
		
		return BasicModel.isMember(relationshipManager, plUser, group);
	}

	public boolean hasGroup(User plUser, String groupName){
		
		Group group = getGroup(groupName);
		
		return hasGroup(plUser, group);//
	}

	public boolean hasGroup(de.vaplus.api.entity.User user, Group group){
		
		if(user == null || user.getAccountTypeId() == null)
			return false;

		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		
		User plUser = identityManager.lookupById(User.class, user.getAccountTypeId());
		
		return hasGroup(plUser, group);
	}

	public String getUserLoginName(de.vaplus.api.entity.User user){
		
		if(user == null || user.getAccountTypeId() == null)
			return "";

		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		
		User plUser = identityManager.lookupById(User.class, user.getAccountTypeId());
		
		return plUser.getLoginName();
	}
    
    public boolean hasGroup(String groupName){
    	
    	return hasGroup((User) identity.getAccount(), groupName);
    }

	public void revokeRole(de.vaplus.api.entity.User user, String roleName){
		
		if(user.getAccountTypeId() == null)
			return;
		
		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
		
		User plUser = identityManager.lookupById(User.class, user.getAccountTypeId());
		
		Role role = getRole(roleName);
		if(role == null)
			return;
		
		BasicModel.revokeRole(relationshipManager, plUser, role);
	}

	public void grantRole(de.vaplus.api.entity.User user, String roleName){
		
		if(user.getAccountTypeId() == null)
			return;
		
		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
		
		User plUser = identityManager.lookupById(User.class, user.getAccountTypeId());
		
		Role role = getRole(roleName);
		if(role == null)
			return;
		
		BasicModel.grantRole(relationshipManager, plUser, role);
		
		
	}

	public void addToGroup(de.vaplus.api.entity.User user, Group group){
		
		if(user.getAccountTypeId() == null)
			return;
		
		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
		
		User member = identityManager.lookupById(User.class, user.getAccountTypeId());
		
		if(group == null)
			return;
		
		BasicModel.addToGroup(relationshipManager, member, group);
		
	}

	public void removeFromGroup(de.vaplus.api.entity.User user, Group group){
		
		if(user.getAccountTypeId() == null)
			return;
		
		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
		
		User member = identityManager.lookupById(User.class, user.getAccountTypeId());
		
		if(group == null)
			return;
		
		BasicModel.removeFromGroup(relationshipManager, member, group);
		
	}
	
	public Role getRole(String roleName){
		
        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        
        
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        IdentityQuery<Role> query = builder.createIdentityQuery(Role.class);
        query.where( builder.equal(Role.NAME, roleName) );

        List<Role> result = query.getResultList();
        
        if(result != null && result.size() > 0)
        	return result.get(0);
        else{
        	Role role = new Role(roleName);
        	identityManager.add(role);
        	return role;
        }
	}

	public Group createGroup(String groupName){
		return getGroup(groupName);
	}

	public void updateGroup(Group group){

        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        
        identityManager.update(group);
	}
	
	public Group getGroup(String groupName){
		
        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        
        
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        IdentityQuery<Group> query = builder.createIdentityQuery(Group.class);
        query.where( builder.equal(Group.NAME, groupName) );

        List<Group> result = query.getResultList();
        
        if(result != null && result.size() > 0)
        	return result.get(0);
        else{
        	Group role = new Group(groupName);
        	identityManager.add(role);
        	return role;
        }
	}
	
	public Group getGroupById(String groupID){
		
        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        
        
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        IdentityQuery<Group> query = builder.createIdentityQuery(Group.class);
        query.where( builder.equal(Group.ID, groupID) );

        List<Group> result = query.getResultList();
        
        if(result != null && result.size() > 0)
        	return result.get(0);
        
        return null;
	}
	
	public boolean checkOldPassword(User user, String password){
			
		return false;
	}
	
	public void changeUserLoginName(de.vaplus.api.entity.User user, String loginName){
		
		if(user.getAccountTypeId() == null)
			return;

	    IdentityManager identityManager = this.partitionManager.createIdentityManager();
		User plUser = identityManager.lookupById(User.class, user.getAccountTypeId());
	
		
		plUser.setLoginName(loginName);
		
		identityManager.update(plUser);
	}
	
	public void changeUserPassword(de.vaplus.api.entity.User user, String password){

		if(user.getAccountTypeId() == null)
			return;

	    IdentityManager identityManager = this.partitionManager.createIdentityManager();
		User plUser = identityManager.lookupById(User.class, user.getAccountTypeId());
	    
	    identityManager.updateCredential(plUser, new Password(password));
		
	}

	public boolean checkUserPassword(String userId, String password) {

	    IdentityManager identityManager = this.partitionManager.createIdentityManager();
	    
		Credentials creds = new UsernamePasswordCredentials(userId, new Password(password));

		identityManager.validateCredentials(creds);

		if (Status.VALID.equals(creds.getStatus()))
			return true;
		else
			return false;
	}
	
	public List<Role> getRoleList(){
		
        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        
        IdentityQuery<Role> query = builder.createIdentityQuery(Role.class);
        		
        return query.getResultList();
	}
	
	public List<Group> getGroupList(){
		
        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        
        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
        
        IdentityQuery<Group> query = builder.createIdentityQuery(Group.class);
        		
        return query.getResultList();
	}
	
	public void grantPermission(IdentityType assignee, Object resource, String operation){
		PermissionManager permissionManager = this.partitionManager.createPermissionManager();
		permissionManager.grantPermission(assignee, resource, operation);
	}
	
	public void revokePermission(IdentityType assignee, Object resource, String operation){
		PermissionManager permissionManager = this.partitionManager.createPermissionManager();
		permissionManager.revokePermission(assignee, resource, operation);
	}
	
	public boolean hasPermission(IdentityType assignee, Object resource, String operation){
		
		PersistentPermissionVoter voter = new PersistentPermissionVoter(partitionManager);
		
		if(voter.hasPermission(assignee, resource, operation) == VotingResult.ALLOW)
			return true;
		return false;
		
//		return permissionResolver.get().resolvePermission(assignee, resource, operation);
	}
}
