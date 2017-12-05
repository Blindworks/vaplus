package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VacationRequest;
import de.vaplus.client.entity.EventEntity;
import de.vaplus.client.entity.EventEntity_;
import de.vaplus.client.entity.EventTypeEntity;
import de.vaplus.client.entity.EventTypeEntity_;
import de.vaplus.client.entity.ShopEntity_;
import de.vaplus.client.entity.UserEntity_;
import de.vaplus.client.entity.VacationRequestEntity;
import de.vaplus.client.entity.VacationRequestEntity_;


@Stateless
public class EventEao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466161576414849348L;

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

    
    public long getEventCount(){
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(EventEntity.class)));
		
		return em.createQuery(cq).getSingleResult();
	}
    
    public EventEntity saveEvent(EventEntity event){
    	return em.merge(event);
    }

	public Event getEvent(long id) {
		return em.find(EventEntity.class, id);
	}

	public List<Event> getYearViewEvents(Date effectiveDate, Date expiryDate, User user) {
		
		List<? extends Event> list = new ArrayList<Event>();
			
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			Join <EventEntity,EventTypeEntity> join = root.join(EventEntity_.eventType);
			
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			criteria.add( 
					cb.or(
							cb.and(
									cb.greaterThanOrEqualTo(root.get(EventEntity_.effectiveDate),effectiveDate),
									cb.lessThanOrEqualTo( root.get(EventEntity_.effectiveDate), expiryDate)
							),
							cb.and(
									cb.greaterThanOrEqualTo(root.get(EventEntity_.expiryDate),effectiveDate),
									cb.lessThanOrEqualTo( root.get(EventEntity_.expiryDate), expiryDate)
							),
							cb.and(
									cb.lessThanOrEqualTo(root.get(EventEntity_.effectiveDate),effectiveDate),
									cb.greaterThanOrEqualTo( root.get(EventEntity_.expiryDate), expiryDate)
							)
					)
			);
			

			if(user == null)
				criteria.add(cb.isNull(root.get(EventEntity_.user)));
			else
				criteria.add(cb.equal(root.get(EventEntity_.user),user));

			criteria.add(
					cb.or(
							cb.equal(join.get(EventTypeEntity_.shortName), "U"),
							cb.equal(join.get(EventTypeEntity_.shortName), "K"),
							cb.equal(join.get(EventTypeEntity_.shortName), "EZ")
					)
			);
			
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return (List<Event>) list;
	}
	


	
	public List<Event> getAllEvents(User user){
			
		List<? extends Event> list = new ArrayList<Event>();
			
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			criteria.add(cb.equal(root.get(EventEntity_.user),user));

			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return (List<Event>) list;
	}
	
	public List<Event> getAllEvents(Shop shop){
			
		List<? extends Event> list = new ArrayList<Event>();
			
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			criteria.add(cb.equal(root.get(EventEntity_.shop),shop));

			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return (List<Event>) list;
	}
	


	public List<? extends Event> getEvents(Date from, Date to, Shop shop) {
		List<? extends Event> list = new ArrayList<Event>();
		
		try{
			
			list = em.createNamedQuery("Event.getEventsByShop",EventEntity.class)
		            .setParameter("shop", shop)
		            .setParameter("from", from)
		            .setParameter("from", from)
		            .setParameter("from", from)
		            .setParameter("to", to)
		            .setParameter("to", to)
		            .setParameter("to", to)
		            .getResultList();

		}
		catch(NoResultException e){
		}
		
		return (List<Event>) list;
	}
	
	public List<Event> getEvents(Date effectiveDate, Date expiryDate, User user, Shop shop){
			
		List<? extends Event> list = new ArrayList<Event>();
			
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			criteria.add( 
					cb.or(
							cb.and(
									cb.greaterThanOrEqualTo(root.get(EventEntity_.effectiveDate),effectiveDate),
									cb.lessThanOrEqualTo( root.get(EventEntity_.effectiveDate), expiryDate)
							),
							cb.and(
									cb.greaterThanOrEqualTo(root.get(EventEntity_.expiryDate),effectiveDate),
									cb.lessThanOrEqualTo( root.get(EventEntity_.expiryDate), expiryDate)
							),
							cb.and(
									cb.lessThanOrEqualTo(root.get(EventEntity_.effectiveDate),effectiveDate),
									cb.greaterThanOrEqualTo( root.get(EventEntity_.expiryDate), expiryDate)
							)
					)
			);
					

			if(user == null)
				criteria.add(cb.isNull(root.get(EventEntity_.user)));
			else
				criteria.add(cb.equal(root.get(EventEntity_.user).get(UserEntity_.id),user.getId()));

			if(shop != null){
				if(shop.getId() == 0)
					criteria.add(cb.isNull(root.get(EventEntity_.shop)));
				else
					criteria.add(cb.equal(root.get(EventEntity_.shop).get(ShopEntity_.id),shop.getId()));
			}
			
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return (List<Event>) list;
	}
	
	public Event getEventByUUID(String uuid){
		
		Event event = null;
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			cQuery.where(
			        cb.equal(
			                root.get(EventEntity_.uuid), uuid
			        )
				);
			
			event = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return event;
	}

	public void deleteEvent(EventEntity e) {
		EventEntity event = em.find(EventEntity.class, e.getId());
		if(event == null)
			return;
		em.remove(event);
	}

	public EventType getEventTypeById(long id) {
		return em.find(EventTypeEntity.class, id);
	}

	public List<? extends EventType> getSelectableEventTypeList() {
		List<? extends EventType> list = new ArrayList<EventType>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventTypeEntity> cQuery = cb.createQuery(EventTypeEntity.class);
			Root<EventTypeEntity> root = cQuery.from(EventTypeEntity.class);
			cQuery.select(root);
			
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			criteria.add( cb.equal(root.get(EventTypeEntity_.externalEvent),true));
			criteria.add( cb.equal(root.get(EventTypeEntity_.multiUserEvent),false));
			criteria.add( cb.notEqual(root.get(EventTypeEntity_.shortName),"U"));
			criteria.add( cb.notEqual(root.get(EventTypeEntity_.shortName),"EZ"));
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public EventType getEventTypeByShortName(String string) {
		
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventTypeEntity> cQuery = cb.createQuery(EventTypeEntity.class);
			Root<EventTypeEntity> root = cQuery.from(EventTypeEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get(EventTypeEntity_.shortName), string));
			
			return em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return null;
	}

	public List<? extends Event> getFullEventList() {
		
		List<? extends Event> list = new ArrayList<Event>();
			
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
	
		return list;
	}

	public List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, User user, EventType eventType) {
		return getEvents(effectiveDate, expiryDate, user, eventType, false);
	}

	public List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, EventType eventType) {
		return getEvents(effectiveDate, expiryDate, null, eventType, true);
	}

	private List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, User user, EventType eventType, boolean anyUser) {

		List<? extends Event> list = new ArrayList<Event>();
			
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			
			List<Predicate> criteria = new ArrayList<Predicate>();

			if(effectiveDate != null && expiryDate != null){
				criteria.add( 
						cb.or(
								cb.and(
										cb.greaterThanOrEqualTo(root.get(EventEntity_.effectiveDate),effectiveDate),
										cb.lessThanOrEqualTo( root.get(EventEntity_.effectiveDate), expiryDate)
								),
								cb.and(
										cb.greaterThanOrEqualTo(root.get(EventEntity_.expiryDate),effectiveDate),
										cb.lessThanOrEqualTo( root.get(EventEntity_.expiryDate), expiryDate)
								),
								cb.and(
										cb.lessThanOrEqualTo(root.get(EventEntity_.effectiveDate),effectiveDate),
										cb.greaterThanOrEqualTo( root.get(EventEntity_.expiryDate), expiryDate)
								)
						)
				);
			}
					
			if(anyUser == false){

				if(user == null)
					criteria.add(cb.isNull(root.get(EventEntity_.user)));
				else
					criteria.add(cb.equal(root.get(EventEntity_.user),user));
			}
			
			criteria.add(cb.equal(root.get(EventEntity_.eventType),eventType));
			
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			cQuery.orderBy(cb.asc(root.get(EventEntity_.effectiveDate)));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends EventType> getEventTypeList() {
		List<? extends EventType> list = new ArrayList<EventType>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventTypeEntity> cQuery = cb.createQuery(EventTypeEntity.class);
			Root<EventTypeEntity> root = cQuery.from(EventTypeEntity.class);
			cQuery.select(root);
			
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public EventType saveEventType(EventType selectedEventType) {
    	return em.merge(selectedEventType);
	}

	public Event getFirstEvent(User user) {

		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EventEntity> cQuery = cb.createQuery(EventEntity.class);
			Root<EventEntity> root = cQuery.from(EventEntity.class);
			cQuery.select(root);
			
			List<Predicate> criteria = new ArrayList<Predicate>();


			if(user != null)
				criteria.add(cb.equal(root.get(EventEntity_.user),user));
			
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			cQuery.orderBy(cb.asc(root.get(EventEntity_.effectiveDate)));
			
			
			return em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return null;
	}

	public VacationRequest saveVacationRequest(VacationRequest vacationRequest) {
		return em.merge(vacationRequest);
	}

	public List<? extends VacationRequest> getVacationRequestListByUser(User user) {
		List<? extends VacationRequest> list = new ArrayList<VacationRequest>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VacationRequestEntity> cQuery = cb.createQuery(VacationRequestEntity.class);
			Root<VacationRequestEntity> root = cQuery.from(VacationRequestEntity.class);
			cQuery.select(root);

			cQuery.where(cb.equal(root.get(VacationRequestEntity_.user), user));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends VacationRequest> getAllVacationRequestList() {
		List<? extends VacationRequest> list = new ArrayList<VacationRequest>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VacationRequestEntity> cQuery = cb.createQuery(VacationRequestEntity.class);
			Root<VacationRequestEntity> root = cQuery.from(VacationRequestEntity.class);
			cQuery.select(root);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public void detachEvent(EventEntity clone) {
		em.detach(clone);
	}
}
