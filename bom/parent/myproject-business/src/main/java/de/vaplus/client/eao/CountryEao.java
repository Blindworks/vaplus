package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.vaplus.api.entity.Country;
import de.vaplus.client.entity.CountryEntity;


@Stateless
public class CountryEao implements Serializable {

	private static final long serialVersionUID = -6375173529173621595L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public List<CountryEntity> getCountryList(){
		
		List<CountryEntity> list = new ArrayList<CountryEntity>();
		
		try{
	
			list = em.createNamedQuery("Country.getCountries", CountryEntity.class).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public Country getCountryById(long id) {
		return em.find(CountryEntity.class, id);
	}
}
