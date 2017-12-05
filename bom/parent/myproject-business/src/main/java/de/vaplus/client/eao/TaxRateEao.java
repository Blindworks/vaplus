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

import de.vaplus.api.entity.TaxRate;
import de.vaplus.client.entity.TaxRateEntity;
import de.vaplus.client.entity.TaxRateEntity_;

@Stateless
public class TaxRateEao implements Serializable{
	
	private static final long serialVersionUID = 4217968178654919761L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;


	public List<? extends TaxRate> getTaxRateList() {
		List<? extends TaxRate> list = new ArrayList<TaxRate>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TaxRateEntity> cQuery = cb.createQuery(TaxRateEntity.class);
			Root<TaxRateEntity> root = cQuery.from(TaxRateEntity.class);
			cQuery.select(root);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}


	public TaxRate saveTaxRate(TaxRate taxRate) {
		return em.merge(taxRate);
	}


	public TaxRateEntity factoryNewTaxRate() {
		return new TaxRateEntity();
	}


	public TaxRate getTaxRateById(long id) {
		return em.find(TaxRateEntity.class, id);
	}


	public TaxRate getDefaultTaxRate() {
		TaxRate taxRate = null;
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TaxRateEntity> cQuery = cb.createQuery(TaxRateEntity.class);
			Root<TaxRateEntity> root = cQuery.from(TaxRateEntity.class);
			cQuery.select(root);
			
			cQuery.where(
				cb.equal(root.get(TaxRateEntity_.defaultTaxRate), true)
			);
			
			
			taxRate = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return taxRate;
	}


}
