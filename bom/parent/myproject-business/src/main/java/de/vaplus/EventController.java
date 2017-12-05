package de.vaplus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VacationRequest;
import de.vaplus.client.eao.EventEao;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.EventEntity;
import de.vaplus.client.entity.EventTypeEntity;
import de.vaplus.client.entity.VacationRequestEntity;


@Stateless
public class EventController implements EventControllerInterface {

	private static final long serialVersionUID = 7410728476404635370L;

	
	@EJB
	private UserControllerInterface userController;
	
	@EJB
	private PropertyControllerInterface propertyController;

	@Inject
    private EventEao eventEao;

	
	public EventController(){
		// System.out.println("### INIT EventController ###");
	}

	@Override
    public long getEventCount(){
		return eventEao.getEventCount();
	}

    @Override
    public Event saveEvent(Event event){
    	
    	if(event == null)
    		return null;
    	
    	if(! (event instanceof EventEntity))
    		return null;
    	
    	EventEntity e = (EventEntity) event;
    	
    	e = eventEao.saveEvent(e);
    	
    	if(e.isWorkingTime()){
    		Calendar c = Calendar.getInstance();
    		c.setTime(e.getEffectiveDate());
    		userController.updateUserStats(e.getUser(), c.get(Calendar.YEAR), c.get(Calendar.MONDAY));
    		
    		if(! e.isSingleDay()){
        		c.setTime(e.getExpiryDate());
        		userController.updateUserStats(e.getUser(), c.get(Calendar.YEAR), c.get(Calendar.MONDAY));
    		}
    	}

    	return eventEao.saveEvent(e);
    }

    @Override
    public void deleteEvent(Event event){
    	
    	if(event == null)
    		return;
    	
    	if(! (event instanceof EventEntity))
    		return;
    	
    	EventEntity e = (EventEntity) event;

    	eventEao.deleteEvent(e);
    	
    	
    	if(e.isWorkingTime()){
    		Calendar c = Calendar.getInstance();
    		c.setTime(e.getEffectiveDate());
    		userController.updateUserStats(e.getUser(), c.get(Calendar.YEAR), c.get(Calendar.MONDAY));
    		
    		if(! e.isSingleDay()){
        		c.setTime(e.getExpiryDate());
        		userController.updateUserStats(e.getUser(), c.get(Calendar.YEAR), c.get(Calendar.MONDAY));
    		}
    	}
    }


	@Override
	public Event cloneEvent(Event event) {
		EventEntity clone = (EventEntity) event;
		eventEao.detachEvent(clone);
		clone.setId(0);
		clone.setUuid(UUID.randomUUID().toString());
		
		return clone;
	}
    
	@Override
	public Event factoryNewEvent(){
		EventEntity event = new EventEntity();
		event.setUuid(UUID.randomUUID().toString());
		
    	event.setEffectiveDate(new Date());
    	event.setExpiryDate(new Date());
		
		return event;
	}
	
	@Override
	public List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, User user, Shop shop){
		return eventEao.getEvents(effectiveDate, expiryDate, user, shop);
	}

	// get all shop events
	@Override
	public List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, Shop shop) {
		return eventEao.getEvents(effectiveDate, expiryDate, shop);
	}
	
	@Override
	public List<? extends Event> getYearViewEvents(Date effectiveDate, Date expiryDate, User user) {
		return eventEao.getYearViewEvents(effectiveDate, expiryDate, user);
	}

	@Override
	public List<? extends Event> getEventsByType(Date from, Date to, User user, EventType eventType) {
		return eventEao.getEvents(from, to, user, eventType);
	}

	@Override
	public List<? extends Event> getEventsByType(Date from, Date to, EventType eventType) {
		return eventEao.getEvents(from, to, eventType);
	}
	
	@Override
	public List<? extends Event> getAllEvents(User user){
		return eventEao.getAllEvents(user);
	}
	
	@Override
	public List<? extends Event> getAllEvents(Shop shop){
		return eventEao.getAllEvents(shop);
	}

	@Override
	public Event getEventByUUID(String uuid) {
		return eventEao.getEventByUUID(uuid);
	}

	@Override
	public EventType getEventTypeById(long id) {
		return eventEao.getEventTypeById(id);
	}


	@Override
	public Event getFirstEvent(User user) {
		return eventEao.getFirstEvent(user);
	}

	@Override
	public List<? extends EventType> getSelectableEventTypeList() {
		return eventEao.getSelectableEventTypeList();
	}

	@Override
	public EventType getEventTypeByShortName(String string) {
		return eventEao.getEventTypeByShortName(string);
		
	}

	@Override
	public List<? extends Event> getFullEventList() {
		return eventEao.getFullEventList();
	}

	@Override
	public List<? extends EventType> getEventTypeList() {
		return eventEao.getEventTypeList();
	}

	@Override
	public EventType saveEventType(EventType selectedEventType) {
    	
    	if(selectedEventType == null)
    		return null;
    	
    	if(! (selectedEventType instanceof EventTypeEntity))
    		return null;
    	
    	EventTypeEntity et = (EventTypeEntity) selectedEventType;

    	return eventEao.saveEventType(selectedEventType);
	}

	@Override
	public EventType factoryNewEventType() {
		
		EventTypeEntity eventType = new EventTypeEntity();
		eventType.setUuid(UUID.randomUUID().toString());
    	eventType.setEditable(true);
		
		return eventType;
	}



	@Override
	public VacationRequest factoryNewVacationRequest(User user) {
		
		VacationRequestEntity vacationRequest = new VacationRequestEntity();
		vacationRequest.setStatus(VacationRequest.VACATION_REQUEST_STATUS_NEW);
		vacationRequest.setUser(user);
		
		return vacationRequest;
	}
	
	@Override
	public void correctVacationRequestDate(VacationRequest vacationRequest){
		
		if(vacationRequest.getId() > 0)
			return;
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(vacationRequest.getVacationFrom());
		if(vacationRequest.isStartHalfDay()){
			c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0] ));
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		}else{
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		}
		vacationRequest.setVacationFrom(c.getTime());
		
		c.setTime(vacationRequest.getVacationTo());
		if(vacationRequest.isEndHalfDay()){
			c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(propertyController.getStringProperty("calendarChangingTime", "14:00").split(":")[0]));
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 59);
		}else{
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 59);
		}
		vacationRequest.setVacationTo(c.getTime());
	}

	@Override
	public VacationRequest saveVacationRequest(VacationRequest vacationRequest) {
		
		correctVacationRequestDate(vacationRequest);

		return eventEao.saveVacationRequest(vacationRequest);
	}


	@Override
	public List< ? extends VacationRequest> getOwnVacationRequestList(User user) {
		return eventEao.getVacationRequestListByUser(user);
	}

	@Override
	public List<? extends VacationRequest> getAllVacationRequestList() {
		return eventEao.getAllVacationRequestList();
	}

	@Override
	public List<? extends Event> getEvents(Date effectiveDate, Date expiryDate, User user) {
		// TODO Auto-generated method stub
		return null;
	}
}
