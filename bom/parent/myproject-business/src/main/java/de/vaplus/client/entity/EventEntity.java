package de.vaplus.client.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Event", uniqueConstraints = @UniqueConstraint(columnNames = {"uuid"}))
@NamedQueries({
    @NamedQuery(
        name = "Event.getEventsByShop",
        query = "SELECT e FROM EventEntity e WHERE "
        		+ "e.shop = :shop "
        		+ "AND( "
        		+ "(e.effectiveDate >= :from AND e.effectiveDate <= :to) "
        		+ "OR "
        		+ "(e.expiryDate >= :from AND e.expiryDate <= :to) "
        		+ "OR"
        		+ "(e.effectiveDate >= :from AND e.expiryDate <= :to) "
        		+ ") "
//        ,hints = {
//                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
//            }
    )
})
public class EventEntity extends ActivityEntity implements Event {

	private static final long serialVersionUID = -3588292856756196069L;
	
	@Column(name="uuid", nullable = false)
	private String uuid;

	@Column(name="title")
	private String title;

	@Column(name="allDay", nullable = false)
	private boolean allDay;

	@Column(name="workingTime", nullable = false)
	private boolean workingTime;

	@Column(name="pauseLength", nullable = false)
	private int pauseLength;

	@ManyToOne
    @JoinColumn(name="eventType_id", nullable = false)
    private EventTypeEntity eventType;
	
	public EventEntity() {
		super();
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean isAllDay() {
		return allDay;
	}

	@Override
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	@Override
	public EventType getEventType() {
		return eventType;
	}

	@Override
	public void setEventType(EventType eventType) {
		this.eventType = (EventTypeEntity) eventType;
	}
	
	@Override
	public void setEffectiveDate(Date effectiveDate) {

		if(effectiveDate == null)
			return;
		
		// if only time value set only time values
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(effectiveDate);
		if(c1.get(Calendar.YEAR) == 1970 && c1.get(Calendar.DAY_OF_YEAR) == 1){
			c2.setTime(super.getEffectiveDate());
			c2.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
			c2.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
			c2.set(Calendar.SECOND, c1.get(Calendar.SECOND));
			super.setEffectiveDate(c2.getTime());
		}else
			super.setEffectiveDate(effectiveDate);
	}

	@Override
	public void setExpiryDate(Date expiryDate) {
		
		if(expiryDate == null)
			return;
		
		// if only time value set only time values
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(expiryDate);
		if(c1.get(Calendar.YEAR) == 1970 && c1.get(Calendar.DAY_OF_YEAR) == 1){
			c2.setTime(super.getExpiryDate());
			c2.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
			c2.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
			c2.set(Calendar.SECOND, c1.get(Calendar.SECOND));
			super.setExpiryDate(c2.getTime());
		}else if(c1.get(Calendar.HOUR_OF_DAY) == 0 && c1.get(Calendar.MINUTE) == 0 && c1.get(Calendar.SECOND) == 0){
			
//			System.out.println("SET DAY ONLY");
			
			c2.setTime(expiryDate);
			c2.set(Calendar.HOUR_OF_DAY, 23);
			c2.set(Calendar.MINUTE, 59);
			c2.set(Calendar.SECOND, 59);
			super.setExpiryDate(c2.getTime());
		}else
			super.setExpiryDate(expiryDate);
	}

	@Override
	public boolean isWorkingTime() {
		return workingTime;
	}

	@Override
	public void setWorkingTime(boolean workingTime) {
		this.workingTime = workingTime;
	}

	@Override
	public int getPauseLength() {
		return pauseLength;
	}

	@Override
	public void setPauseLength(int pauseLength) {
		this.pauseLength = pauseLength;
	}
	
	@Transient
	@Override
	public long getDuration(){

		long diff = getExpiryDate().getTime() - getEffectiveDate().getTime();

		return TimeUnit.MILLISECONDS.toMinutes(diff);
	}

	@Transient
	@Override
	public long getDurationInDays(){

		long diff = getExpiryDate().getTime() - getEffectiveDate().getTime() + 1000;

		return TimeUnit.MILLISECONDS.toDays(diff);
	}

	@Transient
	@Override
	public boolean isSingleDay(){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(getEffectiveDate());
		c2.setTime(getExpiryDate());
		
		if(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
			return false;
		
		if(c1.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR))
			return false;
		else
			return true;
				
	}
	
	@Transient
	@Override
	public long getWorkingDuration() {
		
		if(! isWorkingTime())
			return 0;

		if(this.getUser() == null)
			return 0;
		
		if(this.isAllDay() && isSingleDay()){
			return this.getUser().getFullDayWorkingTime();
		}
		else if(isSingleDay()){
			return getDuration() - getPauseLength();
		}
		
		else return 0;
	}
	


}
