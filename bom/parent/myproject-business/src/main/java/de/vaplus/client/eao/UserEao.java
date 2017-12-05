package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.JobTitle;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;
import de.vaplus.api.entity.UserCustomerHistory;
import de.vaplus.api.entity.UserGroup;
import de.vaplus.client.entity.EmploymentFormEntity;
import de.vaplus.client.entity.JobTitleEntity;
import de.vaplus.client.entity.PhoneContractEntity_;
import de.vaplus.client.entity.ShopAliasEntity;
import de.vaplus.client.entity.ShopAliasEntityPK;
import de.vaplus.client.entity.ShopAliasEntity_;
import de.vaplus.client.entity.UserAliasEntity;
import de.vaplus.client.entity.UserAliasEntityPK;
import de.vaplus.client.entity.UserAliasEntity_;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.UserEntity_;
import de.vaplus.client.entity.UserGroupEntity;


@Stateless
public class UserEao extends BaseEao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466161576414849348L;

//    @Inject
//    private EntityManager super;
    
    public UserEntity saveUser(UserEntity user){
    	return super.merge(user);
    }
    
    public UserEntity factoryNewUser(){
    	return new UserEntity();
    }

	public List<UserEntity> getUserList(){
		return getUserList(false);
	}

	public List<UserEntity> getUserList(boolean showDisabled){
		
		List<UserEntity> list = new ArrayList<UserEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<UserEntity> cQuery = cb.createQuery(UserEntity.class);
			Root<UserEntity> root = cQuery.from(UserEntity.class);
			cQuery.select(root);
			
			
			if(showDisabled){
				list = super.createNamedQuery("User.listUser", UserEntity.class).getResultList();
			}
			else{
				list = super.createNamedQuery("User.listEnabledUserNotSupervisor", UserEntity.class).getResultList();
			}
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<UserEntity> getSalesUserList() {
		
		List<UserEntity> list = new ArrayList<UserEntity>();
		
		try{
			list = super.createNamedQuery("User.listSalesUser", UserEntity.class).getResultList();
	
//			CriteriaBuilder cb = super.getCriteriaBuilder();
//			CriteriaQuery<UserEntity> cQuery = cb.createQuery(UserEntity.class);
//			Root<UserEntity> root = cQuery.from(UserEntity.class);
//			cQuery.select(root);
//
//			cQuery.where(
//					cb.and(
//				        cb.greaterThan(
//				                root.get(
//				                		UserEntity_.pointGoal
//				                ),
//				                new BigDecimal(0)
//				        ),
//				        cb.equal(
//				                root.get(
//				                		UserEntity_.enabled
//				                ),
//				                true
//				        ),
//				        cb.equal(
//				                root.get(
//				                		UserEntity_.deleted
//				                ),
//				                false
//				        ),
//				        cb.gt(
//				                root.get(
//				                		UserEntity_.id
//				                ),
//				                1
//				        )
//				     )
//				);
//			
//			
//			list = super.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
//			Systsuper.out.println("No User found.");
		}
		
		return list;
	}

	public UserEntity getUserByUUID(String uuid){
		
		UserEntity user = null;
		
		try{
			
			user = (UserEntity) super.createNamedQuery("User.findByUUID", UserEntity.class)
		            .setParameter("uuid", uuid)
		            .getSingleResult();
	
//			CriteriaBuilder cb = super.getCriteriaBuilder();
//			CriteriaQuery<UserEntity> cQuery = cb.createQuery(UserEntity.class);
//			Root<UserEntity> root = cQuery.from(UserEntity.class);
//			EntityType<UserEntity> type = super.getMetamodel().entity(UserEntity.class);
//			cQuery.select(root);
//			
//			cQuery.where(
//				cb.and(
//			        cb.equal(
//			                root.get(
//			                    type.getDeclaredSingularAttribute("uuid", String.class)
//			                ),
//			                uuid
//			        ),
//			        cb.equal(
//			                root.get(
//			                		UserEntity_.enabled
//			                ),
//			                true
//			        ),
//			        cb.equal(
//			                root.get(
//			                		UserEntity_.deleted
//			                ),
//			                false
//			        )
//			    )
//			);
//			
//			user = super.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return user;
	}

	public List<UserEntity> findUser(String query){
		
		
		List<UserEntity> list = new ArrayList<UserEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<UserEntity> cQuery = cb.createQuery(UserEntity.class);
			Root<UserEntity> root = cQuery.from(UserEntity.class);
//			EntityType<UserEntity> type = super.getMetamodel().entity(UserEntity.class);
			cQuery.select(root);
			
			List<Predicate> criteria = new ArrayList<Predicate>();
			
			if (query.matches("-?\\d+(\\.\\d+)?")) {
			    // numeric
				
				try{
					long numericQuery = Long.valueOf(query);
					
					criteria.add(cb.equal( root.get( UserEntity_.id) , numericQuery ));
				}catch(NumberFormatException e){
					
				}
				
				
			}
			
			criteria.add(cb.like(
		            cb.lower(
			                root.get(UserEntity_.firstname)
			            ), "%" + query.toLowerCase() + "%"
			        ));
			
			criteria.add(cb.like(
		            cb.lower(
			                root.get(UserEntity_.lastname)
			            ), "%" + query.toLowerCase() + "%"
			        ));
				
			cQuery.where(
					
					cb.and(
							cb.equal(
					                root.get(
					                		UserEntity_.enabled
					                ),
					                true
					        ),
					        cb.equal(
					                root.get(
					                		UserEntity_.deleted
					                ),
					                false
					        ),
						    cb.or(
						    	criteria.toArray(new Predicate[0])
						    ),
					        cb.gt(
					                root.get(
					                		UserEntity_.id
					                ),
					                1
					        )
					)
				);
			
			
			list = super.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}
	
	public Long getUserCount(){
		
		CriteriaBuilder cb = super.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<UserEntity> root = cq.from(UserEntity.class);
		cq.select(cb.count(root));
		
		cq.where(
				
				cb.and(
					cb.equal(
			                root.get(
			                		UserEntity_.enabled
			                ),
			                true
			        ),
			        cb.equal(
			                root.get(
			                		UserEntity_.deleted
			                ),
			                false
			        )
			    )
				);
		
		return super.createQuery(cq).getSingleResult();
	}
	

	public UserEntity getUserByAccountTypeId(String attributedTypeId) {
		
		
		UserEntity user = null;
		
		try{
			
			user = (UserEntity) super.createNamedQuery("User.findByAccoundType")
		            .setParameter("id", attributedTypeId)
		            .getSingleResult();

//			AccountTypeEntity ate = super.find(AccountTypeEntity.class, attributedTypeId);
//			if(ate == null)
//				return null;
//	
//			CriteriaBuilder cb = super.getCriteriaBuilder();
//			CriteriaQuery<UserEntity> cQuery = cb.createQuery(UserEntity.class);
//			Root<UserEntity> root = cQuery.from(UserEntity.class);
//			cQuery.select(root);
//			
//			cQuery.where(cb.equal(root.get("accountTypeEntity"), ate));
//			
//			user = super.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return user;
	}

	public JobTitleEntity getJobTitleByName(String name, boolean createIfNotExisting) {
		
		JobTitleEntity jobTitle = null;
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<JobTitleEntity> cQuery = cb.createQuery(JobTitleEntity.class);
			Root<JobTitleEntity> root = cQuery.from(JobTitleEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get("name"), name));
			
			jobTitle = super.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
			
		}
		
		return jobTitle;
	}

	public JobTitleEntity saveJobTitle(JobTitleEntity jobTitle) {
		return super.merge(jobTitle);
	}

	public User getUserById(long id) {
		return super.find(UserEntity.class, id);
	}

	public List<? extends UserGroup> getUserGroupList() {
		List<UserGroupEntity> list = new ArrayList<UserGroupEntity>();
		
		try{
	
//			CriteriaBuilder cb = super.getCriteriaBuilder();
//			CriteriaQuery<UserGroupEntity> cQuery = cb.createQuery(UserGroupEntity.class);
//			Root<UserGroupEntity> root = cQuery.from(UserGroupEntity.class);
//			cQuery.select(root);
//			
//			list = super.createQuery(cQuery).getResultList();
			
			list = super.createNamedQuery("UserGroup.getAll", UserGroupEntity.class).getResultList();
		}
		catch(NoResultException e){
//			Systsuper.out.println("No User found.");
		}
		
		return list;
	}

	public UserGroup saveUserGroup(UserGroupEntity userGroup) {
    	return super.merge(userGroup);
	}

	public UserGroup getUserGroupById(long id) {
		UserGroupEntity group = super.find(UserGroupEntity.class, id);
		
		if(group.isDeleted())
			return null;
		
		return group;
	}

	public void flushUserCache(){
		super.getEntityManagerFactory().getCache().evict(UserEntity.class);
	}

	public void flushUserGroupCache(){
		super.getEntityManagerFactory().getCache().evict(UserGroupEntity.class);
	}

	public User getUserByAlias(String alias) {
		
		UserEntity user = null;
		
		try{
			
			user = (UserEntity) super.createNamedQuery("User.findByAlias")
		            .setParameter("alias", alias)
		            .setMaxResults(1)
		            .getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return user;
	}

	public UserAlias saveUserAlias(UserAlias userAlias) {
    	return super.merge(userAlias);
	}

	public Object getEmploymentFormById(long id) {
		return super.find(EmploymentFormEntity.class, id);
	}

	public List<? extends EmploymentForm> getEmploymentFormList() {
		
		List<EmploymentFormEntity> list = new ArrayList<EmploymentFormEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<EmploymentFormEntity> cQuery = cb.createQuery(EmploymentFormEntity.class);
			Root<EmploymentFormEntity> root = cQuery.from(EmploymentFormEntity.class);
			cQuery.select(root);
			
			list = super.createNamedQuery("EmploymentForm.getList", EmploymentFormEntity.class).getResultList();
			
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public Object getJobTitleById(long id) {
		return super.find(JobTitleEntity.class, id);
	}

	public List<? extends JobTitle> getJobTitleList() {
		
		List<JobTitleEntity> list = new ArrayList<JobTitleEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<JobTitleEntity> cQuery = cb.createQuery(JobTitleEntity.class);
			Root<JobTitleEntity> root = cQuery.from(JobTitleEntity.class);
			cQuery.select(root);
			
			list = super.createNamedQuery("JobTitle.getList", JobTitleEntity.class).getResultList();
			
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public User getUserByEmail(String email) {
		UserEntity user = null;
		
		try{
			
			user = (UserEntity) super.createNamedQuery("User.findByEmail")
		            .setParameter("email", email)
		            .setMaxResults(1)
		            .getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return user;
	}

	public User getUserByName(String firstname, String lastname) {
		UserEntity user = null;
		
		try{
			
			user = (UserEntity) super.createNamedQuery("User.findByName")
		            .setParameter("firstname", firstname)
		            .setParameter("lastname", lastname)
		            .setMaxResults(1)
		            .getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return user;
	}

	public UserCustomerHistory saveUserCustomerHistory(
			UserCustomerHistory history) {
		return super.merge(history);
	}

	public EmploymentForm saveEmploymentForm(EmploymentForm employmentForm) {
		return super.merge(employmentForm);
	}



	public List<? extends UserAlias> getUserAliasList(){
		
		List<? extends UserAlias> list = new ArrayList<UserAlias>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<UserAliasEntity> cQuery = cb.createQuery(UserAliasEntity.class);
			Root<UserAliasEntity> root = cQuery.from(UserAliasEntity.class);
			cQuery.select(root);
			
			cQuery.orderBy(cb.asc(root.get(UserAliasEntity_.user)));
			
			return super.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public void deleteUserAlias(UserAlias userAlias) {
		UserAliasEntityPK pk = new UserAliasEntityPK();
		
		pk.setAlias(userAlias.getAlias());
		pk.setUser(userAlias.getUser().getId());
		
		UserAliasEntity a = super.find(UserAliasEntity.class, pk);
		
		super.remove(a);
	}
}
