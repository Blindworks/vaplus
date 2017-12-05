package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.State;
import de.vaplus.client.entity.StateEntity;


@Stateless
public class StateEao implements Serializable {

	private static final long serialVersionUID = -6375173529173621595L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public List<StateEntity> getStateList(){
		
		List<StateEntity> list = new ArrayList<StateEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<StateEntity> cQuery = cb.createQuery(StateEntity.class);
			Root<StateEntity> root = cQuery.from(StateEntity.class);
			cQuery.select(root);
			
			list = em.createNamedQuery("State.getStates", StateEntity.class).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public State getStateById(long id) {
		return em.find(StateEntity.class, id);
	}
}
