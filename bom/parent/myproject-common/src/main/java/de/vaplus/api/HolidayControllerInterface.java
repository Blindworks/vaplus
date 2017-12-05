package de.vaplus.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.State;

public interface HolidayControllerInterface extends Serializable {

	void importHolidayCalendar() throws Exception;

	boolean isHoliday(Date date, State state);

	List<? extends Event> getHolidayEvents(State state);

	List<? extends Event> getAllHolidayEvents();

	List<? extends Event> getHolidayEvents(Date effectiveDate, Date expiryDate, State state);

}
