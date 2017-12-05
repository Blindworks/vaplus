package de.vaplus.client.eao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.vaplus.api.entity.Invoice;
import de.vaplus.client.entity.InvoiceEntity;

@Stateless
public class InvoiceEao implements Serializable{
	
	private static final long serialVersionUID = -1122520670313568007L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public InvoiceEntity saveInvoice(InvoiceEntity invoice){
		return em.merge(invoice);
	}

	public Invoice getInvoice(String number) {
		InvoiceEntity invoice = null;
		
		try{

			invoice = em.createNamedQuery("Invoice.getInvoiceByNumber", InvoiceEntity.class)
		            .setParameter("number", number)
		            .getSingleResult();

		}
		catch(NoResultException e){
		}
		
		return invoice;
	}

	
}
