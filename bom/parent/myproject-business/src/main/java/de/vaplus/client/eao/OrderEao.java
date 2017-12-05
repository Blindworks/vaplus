package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.OrderEntity;
import de.vaplus.client.entity.OrderEntity_;

@Stateless
public class OrderEao implements Serializable{
	
	private static final long serialVersionUID = -1122520670313568007L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public OrderEntity saveOrder(OrderEntity order){
		order = em.merge(order);
		em.flush();
		// refresh do get per trigger generated number values
		OrderEntity o = em.find(OrderEntity.class, order.getId());
		
		em.refresh(o);
		return o;
	}

	public List<? extends Order> getOrderList(Shop shop, User user, Customer customer, Date from, Date to, boolean onlyActive) {

		List<? extends Order> list = new LinkedList<Order>();

		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderEntity> cQuery = cb.createQuery(OrderEntity.class);
			Root<OrderEntity> root = cQuery.from(OrderEntity.class);
			cQuery.select(root);
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			if(customer != null)
				criteria.add(cb.equal(root.get(OrderEntity_.customer), customer));

			if(user != null)
				criteria.add(cb.equal(root.get(OrderEntity_.user), user));

			if(shop != null)
				criteria.add(cb.equal(root.get(OrderEntity_.shop), shop));

			if(from != null)
				criteria.add(cb.greaterThanOrEqualTo(root.get(OrderEntity_.effectiveDate), from));
			
			if(to != null)
				criteria.add(cb.lessThanOrEqualTo(root.get(OrderEntity_.effectiveDate), to));
			
			if(onlyActive){
				criteria.add(
						cb.equal(root.get(OrderEntity_.enabled), true)
					);
				criteria.add(
						cb.greaterThan(root.get(OrderEntity_.expiryDate), new Date())
					);
			}
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			cQuery.orderBy(cb.desc(root.get(OrderEntity_.effectiveDate)));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		
		return list;
	}

	public List<? extends Invoice> getInvoiceList(Shop shop, User user, Customer customer, Date from, Date to, boolean onlyActive) {
		List<? extends Invoice> list = new LinkedList<Invoice>();

		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceEntity> cQuery = cb.createQuery(InvoiceEntity.class);
			Root<InvoiceEntity> root = cQuery.from(InvoiceEntity.class);
			cQuery.select(root);
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			if(customer != null)
				criteria.add(cb.equal(root.get(OrderEntity_.customer), customer));

			if(user != null)
				criteria.add(cb.equal(root.get(OrderEntity_.user), user));

			if(shop != null)
				criteria.add(cb.equal(root.get(OrderEntity_.shop), shop));

			if(from != null)
				criteria.add(cb.greaterThanOrEqualTo(root.get(OrderEntity_.effectiveDate), from));
			
			if(to != null)
				criteria.add(cb.lessThanOrEqualTo(root.get(OrderEntity_.effectiveDate), to));
			
			if(onlyActive){
				criteria.add(
						cb.equal(root.get(OrderEntity_.enabled), true)
					);
				criteria.add(
						cb.greaterThan(root.get(OrderEntity_.expiryDate), new Date())
					);
			}
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			cQuery.orderBy(cb.desc(root.get(OrderEntity_.effectiveDate)));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		
		return list;
	}

	public Invoice saveInvoice(InvoiceEntity invoice) {
		invoice = em.merge(invoice);
		em.flush();
		// refresh do get per trigger generated number values
		Invoice i = em.find(InvoiceEntity.class, invoice.getId());
		
		em.refresh(i);
		return i;
	}

	public Order getOrder(String number) {
		OrderEntity order = null;
		
		try{

			order = em.createNamedQuery("Order.getOrderByNumber", OrderEntity.class)
		            .setParameter("number", number)
		            .getSingleResult();

		}
		catch(NoResultException e){
		}
		
		return order;
	}
	
}
