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
import de.vaplus.api.entity.Holiday;
import de.vaplus.api.entity.State;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Holiday")
@NamedQueries({
    @NamedQuery(
        name = "Holiday.getHoliday",
        query = "SELECT h FROM HolidayEntity h WHERE h.day = :day AND h.state = :state",
        hints = {
                @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
            }
    ),
    @NamedQuery(
            name = "Holiday.getHolidaysByState",
            query = "SELECT h FROM HolidayEntity h WHERE h.state = :state ORDER BY h.day ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "Holiday.getHolidaysByStateAndDay",
            query = "SELECT h FROM HolidayEntity h WHERE h.state = :state AND h.day LIKE :day ORDER BY h.day ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "Holiday.getHolidaysByStateAndTime",
            query = "SELECT h FROM HolidayEntity h WHERE h.state = :state AND h.day >= :from AND h.day <= :to ORDER BY h.day ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "Holiday.getAllHolidays",
            query = "SELECT h FROM HolidayEntity h GROUP BY h.day ORDER BY h.day ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    )
})
public class HolidayEntity extends BaseEntity implements Holiday {
	
	private static final long serialVersionUID = 4795338155693602358L;

	@Column(name="name")
	private String name;
	
	@Column(name="day")
	private String day;
	
	@ManyToOne
    @JoinColumn(name="state_id", nullable = false)
	private StateEntity state;

	public HolidayEntity() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public StateEntity getState() {
		return state;
	}

	public void setState(StateEntity state) {
		this.state = state;
	}
	
	private Calendar getDayCalendar(){
		if(day == null || day.length() != 8)
			return null;

		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, Integer.valueOf( day.substring(0, 4)));
		c.set(Calendar.MONTH, Integer.valueOf( day.substring(4, 6)) -1);
		c.set(Calendar.DAY_OF_MONTH, Integer.valueOf( day.substring(6, 8)));

		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);
		
		return c;
	}

	@Transient
	public Date getStartDate(){
		Calendar c = getDayCalendar();
		if(c == null)
			return null;

		return c.getTime();
	}

	@Transient
	public Date getEndDate(){
		Calendar c = getDayCalendar();
		if(c == null)
			return null;

		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.HOUR, 23);

		return c.getTime();
	}
}
