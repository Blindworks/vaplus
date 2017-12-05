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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.security.identity.IdentityType;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.PermissionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.permission.Permission;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
//import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;

/**
 * This startup bean creates a number of default users, groups and roles when the application is started.
 * 
 * @author Shane Bryzak
 */
@Singleton
@Startup
public class SecurityInitializer {

    @Inject
    private PartitionManager partitionManager;


	
	private boolean hasPermission(IdentityType assignee, Object resource, String operation){
		
		PermissionManager permissionManager = partitionManager.createPermissionManager();
		List<Permission> list = permissionManager.listPermissions(resource, operation);
		
		if(list.size() > 0)
			return true;
		else
			return false;
	}
	
    @PostConstruct
    public void checkRights() {

    	IdentityManager identityManager = this.partitionManager.createIdentityManager();
		PermissionManager permissionManager = this.partitionManager.createPermissionManager();
      RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

		
    	// Supervisor
	        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
	        IdentityQuery<Role> roleQuery = builder.createIdentityQuery(Role.class);
	        roleQuery.where( builder.equal(Role.NAME, "Supervisor") );
	
	        List<Role> roleResult = roleQuery.getResultList();
	        Role supervisorRole = null;
	        
	        if(roleResult != null && roleResult.size() > 0)
	        	supervisorRole = roleResult.get(0);

	        IdentityQuery<User> userQuery = builder.createIdentityQuery(User.class);
	        userQuery.where( builder.equal(User.LOGIN_NAME, "supervisor") );
	
	        List<User> userResult = userQuery.getResultList();
	        User supervisorUser = null;
	        
	        if(userResult != null && userResult.size() > 0)
	        	supervisorUser = userResult.get(0);
		
	        if(supervisorRole != null && supervisorUser != null){
	        	
	        	BasicModel.grantRole(relationshipManager, supervisorUser, supervisorRole);
	        	
	    		permissionManager.grantPermission(supervisorRole, "customer", "create");
	    		permissionManager.grantPermission(supervisorRole, "customer", "read");
	    		permissionManager.grantPermission(supervisorRole, "customer", "edit");
	    		permissionManager.grantPermission(supervisorRole, "customer", "delete");
	    		permissionManager.grantPermission(supervisorRole, "contract", "create");
	    		permissionManager.grantPermission(supervisorRole, "contract", "read");
	    		permissionManager.grantPermission(supervisorRole, "contract", "edit");
	    		permissionManager.grantPermission(supervisorRole, "contract", "delete");
	    		permissionManager.grantPermission(supervisorRole, "order", "create");
	    		permissionManager.grantPermission(supervisorRole, "order", "read");
	    		permissionManager.grantPermission(supervisorRole, "order", "edit");
	    		permissionManager.grantPermission(supervisorRole, "order", "delete");
	    		permissionManager.grantPermission(supervisorRole, "calendar", "read");
	    		permissionManager.grantPermission(supervisorRole, "controlling", "read");
	    		permissionManager.grantPermission(supervisorRole, "management", "read");
	    		permissionManager.grantPermission(supervisorRole, "settings", "read");
	    		permissionManager.grantPermission(supervisorRole, "support", "read");
    		
	        }
    		
    	
    	
    	
//
//        IdentityManager identityManager = this.partitionManager.createIdentityManager();
//        PermissionManager permissionManager = this.partitionManager.createPermissionManager();
//        
//        IdentityQueryBuilder builder = identityManager.getQueryBuilder();
//        
//        IdentityQuery<Group> query = builder.createIdentityQuery(Group.class);
//        		
//        List<Group> result = query.getResultList();
//        Iterator<Group> i = result.iterator();
//        while(i.hasNext()){
//        	System.out.println(i.next().getName());
//        }
//        
//      User supervisorUser = identityManager.lookupById(User.class, "a64cd43b-4cd5-4321-a7b2-2eb11da22d2c");
//      Role supervisorRole = identityManager.lookupById(Role.class, "534bbe65-febc-4aa1-9fbd-1114c5566d3d");
//      
//      Group sales = new Group("sales");
//      identityManager.add(sales);
//      
//      RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
//      addToGroup(relationshipManager, supervisorUser, sales);
//      
//
//      permissionManager.grantPermission(sales, "test3", "read");
        
//
        
        
//        User john = identityManager.lookupById(User.class, "8b96ee5b-4aa0-4cd6-be69-b3ee9b3108d7");
//        User jane = identityManager.lookupById(User.class, "7d54fa81-75b2-4750-8bb5-684ece89c618");
//        Role superuser = identityManager.lookupById(Role.class, "2a151409-1e31-40aa-88d8-31b9328a22c2");
//        
        

//      Role superuser = identityManager.lookupById(Role.class, "2a151409-1e31-40aa-88d8-31b9328a22c2");
//
//        permissionManager.grantPermission(john, "ShopStatistics", "read");
//        permissionManager.revokePermission(john, "ShopStatistics", "write");
//        permissionManager.grantPermission(supervisorUser, "test1", "read");
//        permissionManager.grantPermission(supervisorRole, "test2", "read");
        
        
//        
//        ActionType actionType = new ActionType();  
//        AttributeType attActionID = RequestAttributeFactory.createStringAttributeType(  
//              "urn:oasis:names:tc:xacml:1.0:action:action-id", issuer, "read");  
//        actionType.getAttribute().add(attActionID);  
//        return actionType;  
        
        
        
        
//        Permission shopStatistics = new Permission();
//        
//        
//        permissionManager.grantPermission(john, resource, operation);
//        
//        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
//        
//        relationshipManager.
//        
//        grantRole(relationshipManager, john, superuser);
    	
    	/*

        // Create user john
        User demo = new User("Demo2");
        demo.setEmail("demo@demo.dexx");
        demo.setFirstName("Max");
        demo.setLastName("Mustermann");

        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        

        identityManager.add(demo);
        identityManager.updateCredential(demo, new Password("demo"));

//        // Create user mary
//        User mary = new User("mary");
//        mary.setEmail("mary@acme.com");
//        mary.setFirstName("Mary");
//        mary.setLastName("Jones");
//        identityManager.add(mary);
//        identityManager.updateCredential(mary, new Password("demo"));
//
//        // Create user jane
//        User jane = new User("jane");
//        jane.setEmail("jane@acme.com");
//        jane.setFirstName("Jane");
//        jane.setLastName("Doe");
//        identityManager.add(jane);
//        identityManager.updateCredential(jane, new Password("demo"));
//
//        // Create role "manager"
//        Role manager = new Role("manager");
//        identityManager.add(manager);

        // Create application role "superuser"
        Role superuser = new Role("superuser");
        identityManager.add(superuser);

//        // Create group "sales"
//        Group sales = new Group("sales");
//        identityManager.add(sales);

        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
//
//        // Make john a member of the "sales" group
        addToGroup(relationshipManager, john, sales);
//
//        // Make mary a manager of the "sales" group
//        grantGroupRole(relationshipManager, mary, manager, sales);

        // Grant the "superuser" application role to jane
        grantRole(relationshipManager, demo, superuser);
        
        */
        
    }
}
