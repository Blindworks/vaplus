package de.vaplus.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VacationRequest;

public interface EventControllerInterface extends Serializable{

	long getEventCount();

	Event factoryNewEvent();

	Event saveEvent(Event event);

	List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, User user, Shop shop);

	Event getEventByUUID(String uuid);

	void deleteEvent(Event event);

	EventType getEventTypeById(long id);

	List<? extends EventType> getSelectableEventTypeList();

	EventType getEventTypeByShortName(String string);

	List<? extends Event> getFullEventList();

	List<? extends Event> getYearViewEvents(Date effectiveDate, Date expiryDate, User user);

	List<? extends Event> getEventsByType(Date from, Date to, User user, EventType eventType);

	List<? extends EventType> getEventTypeList();

	EventType saveEventType(EventType selectedEventType);

	EventType factoryNewEventType();

	Event getFirstEvent(User user);

	List<? extends Event> getAllEvents(User user);

	List<? extends Event> getAllEvents(Shop shop);

	VacationRequest saveVacationRequest(VacationRequest vacationRequest);

	VacationRequest factoryNewVacationRequest(User user);

	List<? extends VacationRequest> getOwnVacationRequestList(User user);

	List<? extends VacationRequest> getAllVacationRequestList();

	List<? extends Event> getEventsByType(Date from, Date to, EventType eventType);

	Event cloneEvent(Event event);

	List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, Shop shop);

	List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, User user);

	void correctVacationRequestDate(VacationRequest vacationRequest);

}
