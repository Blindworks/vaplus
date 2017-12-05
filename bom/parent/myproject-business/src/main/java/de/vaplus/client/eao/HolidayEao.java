package de.vaplus.client.eao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.vaplus.api.entity.State;
import de.vaplus.client.entity.HolidayEntity;


@Stateless
public class HolidayEao implements Serializable {

	private static final long serialVersionUID = 8198714928745230335L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;


	public HolidayEntity getHoliday(State state, String day){
		
		HolidayEntity holiday = null;
		
		try{
			
			holiday = (HolidayEntity) em.createNamedQuery("Holiday.getHoliday")
		            .setParameter("day", day)
		            .setParameter("state", state)
		            .getSingleResult();

		}
		catch(NoResultException e){
		}
		
		return holiday;
	}
	
	public HolidayEntity saveHoliday(HolidayEntity holiday){
		return em.merge(holiday);
	}

	public List<HolidayEntity> getHolidayList(State state, String day) {
		
		List<HolidayEntity> list = new ArrayList<HolidayEntity>();
		
		try{
			
			if(day != null && day.length() > 0){
				TypedQuery<HolidayEntity> tQuery = em.createNamedQuery("Holiday.getHolidaysByStateAndDay", HolidayEntity.class);
				tQuery.setParameter("state", state);
				tQuery.setParameter("day", day);
				list = tQuery.getResultList();
			}else{
				TypedQuery<HolidayEntity> tQuery = em.createNamedQuery("Holiday.getHolidaysByState", HolidayEntity.class);
				tQuery.setParameter("state", state);
				list = tQuery.getResultList();
			}
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<HolidayEntity> getAllHolidayCalendar() {
		
		List<HolidayEntity> list = new ArrayList<HolidayEntity>();
		
		try{
			
			list = em.createNamedQuery("Holiday.getAllHolidays", HolidayEntity.class)
		            .getResultList();

		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<HolidayEntity> getHolidayList(Date from, Date to, State state) {
		DateFormat inputFormatter = new SimpleDateFormat("yyyyMMdd");
		
		List<HolidayEntity> list = new ArrayList<HolidayEntity>();
		
		try{
			
			list = em.createNamedQuery("Holiday.getHolidaysByStateAndTime", HolidayEntity.class)
		            .setParameter("state", state)
		            .setParameter("from", inputFormatter.format(from))
		            .setParameter("to", inputFormatter.format(to))
		            .getResultList();

		}
		catch(NoResultException e){
		}
		
		return list;
	}
    

}
