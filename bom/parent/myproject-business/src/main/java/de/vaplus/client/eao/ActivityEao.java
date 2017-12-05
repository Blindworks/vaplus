package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.client.entity.ActivityEntity;
import de.vaplus.client.entity.EventEntity_;
import de.vaplus.client.entity.FileActivityEntity;


@Stateless
public class ActivityEao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466161576414849348L;

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;
    
	public List<? extends Activity> getActivities( Object owner, int limit, int offset){
			
		List<? extends Activity> list = new ArrayList<ActivityEntity>();
			
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ActivityEntity> cQuery = cb.createQuery(ActivityEntity.class);
			Root<ActivityEntity> root = cQuery.from(ActivityEntity.class);
			cQuery.select(root);
			

			Predicate ownerPredicate = null;
			
			if(owner instanceof User){
				ownerPredicate = cb.equal(root.get(EventEntity_.user), owner);
			}else if(owner instanceof Shop){
				ownerPredicate = cb.equal(root.get(EventEntity_.shop), owner);
			}else if(owner instanceof Customer){
				ownerPredicate = cb.equal(root.get(EventEntity_.customer), owner);
			}	
			
			if(ownerPredicate == null)
				return list;
			
			cQuery.where(
					cb.and(
							ownerPredicate,
							cb.equal(root.get(EventEntity_.hideInTimeline), false)
					)
			);
			
			cQuery.orderBy(cb.desc(root.get(EventEntity_.id)));
			
			TypedQuery <ActivityEntity> tQuery = em.createQuery(cQuery);
			
			if(limit > 0){
				
				tQuery.setMaxResults(limit);
				
				if(offset > 0){
					tQuery.setFirstResult(offset);
				}
			}
			
			list = tQuery.getResultList();
		}
		catch(NoResultException e){
		}
		return list;
	}

	public FileActivityEntity saveActivity(ActivityEntity fileActivity) {
		return (FileActivityEntity) em.merge(fileActivity);
	}
	

}
