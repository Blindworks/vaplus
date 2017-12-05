package de.vaplus.client.util.picketlink;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.picketlink.idm.jpa.model.sample.simple.AbstractCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.AttributedTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.GroupTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PartitionTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.PasswordCredentialTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipIdentityTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RelationshipTypeEntity;
import org.picketlink.idm.jpa.model.sample.simple.RoleTypeEntity;


@Stateless
public class PicketlinkEao {


	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;
    


    public AttributedTypeEntity saveAttributedTypeEntity(AttributedTypeEntity entity){
    	return em.merge(entity);
    }
    
    public AbstractCredentialTypeEntity saveAbstractCredentialTypeEntity(AbstractCredentialTypeEntity entity){
    	return em.merge(entity);
    }
    

	public PartitionTypeEntity getPartitionTypeEntity(String configurationName) {
		return getPartitionTypeEntity(configurationName, false);
	}

	public PartitionTypeEntity getPartitionTypeEntity(String configurationName, boolean createIfNotExists) {
		
		PartitionTypeEntity partitionTypeEntity = null;
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PartitionTypeEntity> cQuery = cb.createQuery(PartitionTypeEntity.class);
			Root<PartitionTypeEntity> root = cQuery.from(PartitionTypeEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get("configurationName"), configurationName));
			
			partitionTypeEntity = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
			partitionTypeEntity = new PartitionTypeEntity();

			partitionTypeEntity.setId(UUID.randomUUID().toString());
			partitionTypeEntity.setName(configurationName);
			partitionTypeEntity.setConfigurationName(configurationName);
			partitionTypeEntity.setTypeName("org.picketlink.idm.model.basic.Realm");
			
			em.merge(partitionTypeEntity);
			
		}
		
		return partitionTypeEntity;
	}

	public RoleTypeEntity getRoleTypeEntity(String name) {
		return getRoleTypeEntity(name, false, null);
	}

	public RoleTypeEntity getRoleTypeEntity(String name, boolean createIfNotExists, PartitionTypeEntity pte) {
		
		RoleTypeEntity rte = null;
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<RoleTypeEntity> cQuery = cb.createQuery(RoleTypeEntity.class);
			Root<RoleTypeEntity> root = cQuery.from(RoleTypeEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get("name"), name));
			
			rte = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
			rte = new RoleTypeEntity();

			rte.setId(UUID.randomUUID().toString());
			rte.setName(name);
			rte.setTypeName("org.picketlink.idm.model.basic.Role");
			rte.setPartition(pte);
			rte.setCreatedDate(new Date());
			rte.setEnabled(true);
			
			em.merge(rte);
			
		}
		
		return rte;
	}
	
	public List<RoleTypeEntity> getGrantedRoles(IdentityTypeEntity ate){
		
		
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<RoleTypeEntity> cQuery = cb.createQuery(RoleTypeEntity.class);
//		Root<RoleTypeEntity> root = cQuery.from(RoleTypeEntity.class);
//
//		Join<RelationshipIdentityTypeEntity> rite_r = root.join("id");
//		Join<RelationshipIdentityTypeEntity> rite_u = rite_r.join("owner_id");
//		
//		cQuery.select(root);
//		
//		cQuery.where(cb.equal(rite_u.get("identityType_id"), ate.getId()));
//		
//
//		return em.createQuery(cQuery).getResultList();

		
		
		return em.createQuery("SELECT rte FROM RelationshipIdentityTypeEntity rite_u, RelationshipIdentityTypeEntity rite_r , RoleTypeEntity rte "
				+ "WHERE rite_u.identityType.id = :identityTypeId "
				+ "AND rite_u.owner.id = rite_r.owner.id "
				+ "AND rte.id = rite_r.identityType.id",
				RoleTypeEntity.class)
			       .setParameter("identityTypeId", ate.getId())
			       .getResultList();
		
		
//		return em.createQuery("SELECT rte FROM RoleTypeEntity rte "
//				+ "LEFT JOIN RelationshipIdentityTypeEntity rite_r on rite_r.identityType_id = rte.id "
//				+ "LEFT JOIN RelationshipIdentityTypeEntity rite_u on rite_r.owner_id = rite_u.owner_id "
//				+ "WHERE rite_u.identityType_id = :identityId",
//				RoleTypeEntity.class)
//			       .setParameter("identityId", ate.getId())
//			       .getResultList();
	}
	
	public boolean isRoleGranted(IdentityTypeEntity ate, RoleTypeEntity rte){
		
		List<RoleTypeEntity> roles = getGrantedRoles(ate);
		Iterator<RoleTypeEntity> i = roles.iterator();
		RoleTypeEntity rte2;
		while(i.hasNext()){
			rte2 = i.next();
			if(rte2.getName().compareTo(rte.getName()) == 0)
				return true;
		}
		return false;
	}
	
	public void grantRole(IdentityTypeEntity ate, RoleTypeEntity rte){
		// check existing Roles
		if(! isRoleGranted(ate, rte)){
			RelationshipIdentityTypeEntity rite_role = new RelationshipIdentityTypeEntity();
			RelationshipIdentityTypeEntity rite_assignee = new RelationshipIdentityTypeEntity();
			
			RelationshipTypeEntity relation = new RelationshipTypeEntity();
			relation.setTypeName("org.picketlink.idm.model.basic.Grant");
			relation.setId(UUID.randomUUID().toString());
			em.merge(relation);

			rite_role.setOwner(relation);
			rite_assignee.setOwner(relation);
			
			rite_role.setDescriptor("role");
			rite_assignee.setDescriptor("assignee");

			rite_role.setIdentityType(rte);
			rite_assignee.setIdentityType(ate);
			

			em.merge(rite_role);
			em.merge(rite_assignee);
		}
	}
	
	public void refuseRole(IdentityTypeEntity ate, RoleTypeEntity rte){
		// check existing Roles
		if(isRoleGranted(ate, rte)){
			RelationshipIdentityTypeEntity rite_role = new RelationshipIdentityTypeEntity();
			RelationshipIdentityTypeEntity rite_assignee = new RelationshipIdentityTypeEntity();
			
			RelationshipTypeEntity relation = new RelationshipTypeEntity();
			relation.setTypeName("org.picketlink.idm.model.basic.Grant");
			relation.setId(UUID.randomUUID().toString());
			em.merge(relation);

			rite_role.setOwner(relation);
			rite_assignee.setOwner(relation);
			
			rite_role.setDescriptor("role");
			rite_assignee.setDescriptor("assignee");

			rite_role.setIdentityType(rte);
			rite_assignee.setIdentityType(ate);
			

			em.merge(rite_role);
			em.merge(rite_assignee);
		}
		
		List<RoleTypeEntity> roles = getGrantedRoles(ate);
		Iterator<RoleTypeEntity> i = roles.iterator();
		RoleTypeEntity rte2;
		while(i.hasNext()){
			rte2 = i.next();
			if(rte2.getName().compareTo(rte.getName()) == 0){
				// Role found try to remove it
				
				
			}
		}
	}

	public AccountTypeEntity getAccountTypeEntityById(String id) {
		return em.find(AccountTypeEntity.class, id);
	}

	public AccountTypeEntity getAccountTypeEntity(String loginName) {
		
		AccountTypeEntity accountTypeEntity = null;
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AccountTypeEntity> cQuery = cb.createQuery(AccountTypeEntity.class);
			Root<AccountTypeEntity> root = cQuery.from(AccountTypeEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get("loginName"), loginName));
			
			accountTypeEntity = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return accountTypeEntity;
	}

	public PasswordCredentialTypeEntity getPasswordCredentialTypeEntity(AccountTypeEntity ate) {
		
		PasswordCredentialTypeEntity pcte = null;
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PasswordCredentialTypeEntity> cQuery = cb.createQuery(PasswordCredentialTypeEntity.class);
			Root<PasswordCredentialTypeEntity> root = cQuery.from(PasswordCredentialTypeEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get("owner"), ate));
			
			pcte = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return pcte;
	}

	public List<RoleTypeEntity> getRoleList() {
		List<RoleTypeEntity> list = null;
		
		return list;
	}

	public List<GroupTypeEntity> getGroupList() {
		List<GroupTypeEntity> list = null;
		
		return list;
	}

	public List<String> getPermissionResourceList() {
		List<String> list = null;
		
		return list;
	}
	
	
}