package de.vaplus.client.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.vaplus.api.entity.StatusBase;

/**
 * Entity implementation class for Entity: User
 *
 */
@MappedSuperclass
public abstract class StatusBaseEntity extends BaseEntity implements StatusBase {

	private static final long serialVersionUID = -926752501992429448L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="effectiveDate", nullable = true)
	private Date effectiveDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expiryDate", nullable = true)
	private Date expiryDate;
	
	@Column(name="enabled", nullable = false)
	private boolean enabled;

	public StatusBaseEntity() {
		super();
	}
	
	@Override
	@Transient
	public void setStatusBaseEntityValues(StatusBase statusBase){
		
		this.setEffectiveDate(statusBase.getEffectiveDate());
		this.setExpiryDate(statusBase.getExpiryDate());
		this.setEnabled(statusBase.isEnabled());
	}


	@Override
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	@Override
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public Date getExpiryDate() {
		return expiryDate;
	}

	@Override
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Transient
	@Override
	public boolean isEnabledAndInTime() {
		if(! enabled){
			return false;
		}else if(effectiveDate != null && effectiveDate.compareTo(new Date())>0){
    		return false;
    	}else if(expiryDate != null && expiryDate.compareTo(new Date())<0){
    		return false;
    	}
		
		return true;
	}

	@Transient
	@Override
	public long getDaysTillExpiration(){
		
		if(getExpiryDate() == null)
			return 0;
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		
		Date today = c.getTime();

		c.setTime(getExpiryDate());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		
		Date expiryDate = c.getTime();
		
		long days = 0;
		
	    long diff = expiryDate.getTime() - today.getTime();
	    
	    days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		    
		if(days < 0)
			return 0;
		
		return days;
	}
}
