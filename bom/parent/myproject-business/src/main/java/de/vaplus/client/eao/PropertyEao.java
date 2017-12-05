package de.vaplus.client.eao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.vaplus.client.entity.PropertyEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.UserPropertyEntity;


@Stateless
public class PropertyEao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466161576414849348L;

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;
    
    public PropertyEntity getProperty(String name){
		PropertyEntity property = null;
		
		try{
	

			property = (PropertyEntity) em.createNamedQuery("Property.findByName")
		            .setParameter("name", name)
		            .getSingleResult();
			
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<PropertyEntity> cQuery = cb.createQuery(PropertyEntity.class);
//			Root<PropertyEntity> root = cQuery.from(PropertyEntity.class);
//			EntityType<PropertyEntity> type = em.getMetamodel().entity(PropertyEntity.class);
//			cQuery.select(root);
//			
//			cQuery.where(
//			        cb.equal(
//			                root.get(
//			                    type.getDeclaredSingularAttribute("name", String.class)
//			                ),
//			                name
//			        )
//				);
//			
//			property = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return property;
    }
    
    public PropertyEntity saveProperty(PropertyEntity property){
    	return em.merge(property);
    }

    
    public UserPropertyEntity getUserProperty(UserEntity user, String name){
    	UserPropertyEntity property = null;
		
		try{
			

			property = (UserPropertyEntity) em.createNamedQuery("UserProperty.findByName")
		            .setParameter("user", user)
		            .setParameter("name", name)
		            .getSingleResult();
	
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<UserPropertyEntity> cQuery = cb.createQuery(UserPropertyEntity.class);
//			Root<UserPropertyEntity> root = cQuery.from(UserPropertyEntity.class);
//			EntityType<UserPropertyEntity> type = em.getMetamodel().entity(UserPropertyEntity.class);
//			cQuery.select(root);
//			
//			cQuery.where(
//					cb.and(
//					        cb.equal(
//					                root.get(
//					                    type.getDeclaredSingularAttribute("name", String.class)
//					                ),
//					                name
//					        ),
//					        cb.equal(
//					                root.get(
//					                    type.getDeclaredSingularAttribute("user", UserEntity.class)
//					                ),
//					                user
//					        )
//				        )
//				);
//			
//			property = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return property;
    }
    
    public UserPropertyEntity saveUserProperty(UserPropertyEntity property){
    	return em.merge(property);
    }
}
