package de.vaplus.client.eao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SalesEao implements Serializable{
	
	private static final long serialVersionUID = 8979747078346398330L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	
}
