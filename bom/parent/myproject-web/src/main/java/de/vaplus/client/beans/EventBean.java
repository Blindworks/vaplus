package de.vaplus.client.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VacationRequest;

@ManagedBean(name = "eventBean")
@SessionScoped
public class EventBean implements Serializable {


	@EJB
	private EventControllerInterface eventController;
	
	@EJB
	private ShopControllerInterface shopController;
	
	@EJB
	private UserControllerInterface userController;
	
	@Inject
	private FacesContext facesContext;

	private Event selectedEvent;
	
	private VacationRequest selectedVacationRequest;
	
//	private String selectedShopUuid;
	
//	private String selectedUserUuid;
	
	private String selectedEventUuid;
	
	private String selectedEventTypeShortName;
	
	private UIInput selectedEventTypeShortNameInput;
	
	private String tempEventUuid;
	
	private String selectedCalendarId;
	
	private String alert, warning, cause;
	
	private boolean deleteOtherEvents;

	private boolean reloadAllEvents;
	
    @ManagedProperty(value="#{propertyBean}")
    private PropertyBean propertyBean;
	
    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;
    

    
    private boolean copyEventToMonday;
    
    private boolean copyEventToTuesday;
    
    private boolean copyEventToWednesday;
    
    private boolean copyEventToThursday;
    
    private boolean copyEventToFriday;
    
    private boolean copyEventToSaturday;
    
    private boolean copyEventToSunday;
    
    private Date copyEventTill;
	
	public EventBean() {
		// System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}

	public PropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(PropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public Event getSelectedEvent() {
		if(selectedEvent == null){
			selectedEvent = eventController.factoryNewEvent();
		}
		return selectedEvent;
	}

	public void setSelectedEvent(Event selectedEvent) {
		this.selectedEvent = selectedEvent;
	}
	
	public String getSelectedEventShopUuid() {
		if(getSelectedEvent().getShop() == null)
			return null;
		return getSelectedEvent().getShop().getUuid();
	}

	@SuppressWarnings("unused") // no dead code
	public void setSelectedEventShopUuid(String selectedEventShopUuid) throws Exception {
			
//		System.out.println("setSelectedEventShopUuid: "+selectedEventShopUuid);
		
		if(getSelectedEvent().getId() > 0)
			return;
		
		if(selectedEventShopUuid.equalsIgnoreCase(propertyBean.getExternalCalendarId())){
			// external Calendar ID
			getSelectedEvent().setShop(shopController.factoryExternalDummyShop());
		}else if( selectedEventShopUuid != null){
			getSelectedEvent().setShop( shopController.getShopByUUID(selectedEventShopUuid) );
			
			if(getSelectedEvent().getShop() == null)
				throw new Exception("No Shop given.");
		}else{ // no dead code
			throw new Exception("No Shop given.");
		}
	}

	public String getSelectedEventUserUuid() {
		
		if(getSelectedEvent().getUser() == null)
			return null;
		return getSelectedEvent().getUser().getUuid();
	}

	public void setSelectedEventUserUuid(String selectedEventUserUuid) {
		
		if(getSelectedEvent().getId() > 0)
			return;
		
		getSelectedEvent().setUser( userController.getUserByUUID(selectedEventUserUuid) );
	}
	public void saveSelectedEvent() throws Exception{
		
		System.out.println("saveSelectedEvent - START "+(new Date()).getTime());

		selectedEvent = saveEvent(selectedEvent);
		selectedEventUuid = selectedEvent.getUuid();
		
		if(copyEventToMonday || copyEventToTuesday || copyEventToWednesday || copyEventToThursday || copyEventToFriday || copyEventToSaturday || copyEventToSunday){
			copyEvent(selectedEvent);
		}

		System.out.println("saveSelectedEvent - END "+(new Date()).getTime());
	}
	
	private void copyEvent(Event event){

		Calendar c = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		if(copyEventTill == null){

			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte geben Sie ein Enddatum zum kopieren eines Termins an.", null));
			return;
		}
		
		if(!copyEventToMonday && !copyEventToTuesday && !copyEventToWednesday && !copyEventToThursday && !copyEventToFriday && !copyEventToSaturday && !copyEventToSunday){
			return;
		}
		
		if(copyEventTill.compareTo(event.getEffectiveDate()) <= 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Termine können nicht in die Vergangenheit kopiert werden.", null));
			return;
		}

		c.setTime(event.getEffectiveDate());
		c2.setTime(event.getExpiryDate());
		

		if(c.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR) && c.get(Calendar.YEAR) != c2.get(Calendar.YEAR)){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Nur 1-Tages-Termine können kopiert werden", null));
			return;
		}

		c.setTime(event.getEffectiveDate());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		c.add(Calendar.DAY_OF_YEAR, 1);
		
		while(c.getTime().compareTo(copyEventTill) <= 0){
			
			switch(c.get(Calendar.DAY_OF_WEEK)){
			case Calendar.MONDAY:
				if(copyEventToMonday){
					copyEventTo(event, c.getTime());
				}
				break;
			case Calendar.TUESDAY:
				if(copyEventToTuesday){
					copyEventTo(event, c.getTime());
				}
				break;
			case Calendar.WEDNESDAY:
				if(copyEventToWednesday){
					copyEventTo(event, c.getTime());
				}
				break;
			case Calendar.THURSDAY:
				if(copyEventToThursday){
					copyEventTo(event, c.getTime());
				}
				break;
			case Calendar.FRIDAY:
				if(copyEventToFriday){
					copyEventTo(event, c.getTime());
				}
				break;
			case Calendar.SATURDAY:
				if(copyEventToSaturday){
					copyEventTo(event, c.getTime());
				}
				break;
			case Calendar.SUNDAY:
				if(copyEventToSunday){
					copyEventTo(event, c.getTime());
				}
				break;
			}

			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		reloadAllEvents = true;
		copyEventTill = null;
		
		copyEventToMonday = false;
		copyEventToTuesday = false;
		copyEventToWednesday = false;
		copyEventToThursday = false;
		copyEventToFriday  = false;
		copyEventToSaturday = false;
		copyEventToSunday = false;
			
	}
	
	private void copyEventTo(Event event, Date targetDate){

		Calendar c_target = Calendar.getInstance();
		c_target.setTime(targetDate);
		
		Calendar c = Calendar.getInstance();
		
		
		Event clone = eventController.cloneEvent(event);


		c.setTime(event.getEffectiveDate());
		c.set(Calendar.YEAR, c_target.get(Calendar.YEAR));
		c.set(Calendar.DAY_OF_YEAR, c_target.get(Calendar.DAY_OF_YEAR));
		
		clone.setEffectiveDate(c.getTime());

		c.setTime(event.getExpiryDate());
		c.set(Calendar.YEAR, c_target.get(Calendar.YEAR));
		c.set(Calendar.DAY_OF_YEAR, c_target.get(Calendar.DAY_OF_YEAR));
		
		clone.setExpiryDate(c.getTime());
		
		clone = eventController.saveEvent(clone);
		
	}
	
	public Event saveEvent(Event event) throws Exception{
//		System.out.println("saveSelectedEvent");
//		System.out.println("selectedEventUuid: >"+selectedEventUuid+"<");
//		System.out.println("tempEventUuid: >"+tempEventUuid+"<");
		
		
		if(event.getId() == 0){
			// NEW EVENT
			
			if(selectedEventTypeShortName != null && selectedEventTypeShortName.length() > 0){
//				System.out.println("selectedEventTypeShortName: "+selectedEventTypeShortName);
				event.setEventType(eventController.getEventTypeByShortName(selectedEventTypeShortName));
				event.setWorkingTime(event.getEventType().isWorkingTime());
			}

			if(event.getShop() == null){
				throw new Exception("No Shop given.");
			}
			if(event.getShop().getId() <= 0){
				event.setShop(null);
			}
			
			if(event.getEventType() == null){
				event.setEventType(eventController.getEventTypeByShortName("A"));
				event.setWorkingTime(event.getEventType().isWorkingTime());
			}

			event.setHideInTimeline(true);
			
			if(isDeleteOtherEvents()){
				deleteOtherEventsInTimerange(event);
				this.setReloadAllEvents(true);
			}
			
			// check other events on this day
			try{
				checkForIntersectionsForUser(event);
			}catch(Exception e){
				// send exception to GUI
//				e.getCause().getMessage()
				this.setWarning(e.getMessage());
				if(e.getCause() != null)
					this.setCause(e.getCause().getMessage());
				
				//facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}

			
		}else{
			
			// check updates
			Event tmp = eventController.getEventByUUID(event.getUuid());
			
			if(tmp == null){
				throw new Exception("No Event found with ID: "+event.getUuid());
			}
			
			if(event.getShop() == null){
				if(tmp.getShop() != null){
					throw new Exception("Shop changing not allowed.");
				}
			}else if(! event.getShop().equals(tmp.getShop())){
				throw new Exception("Shop changing not allowed.");
			}
			
			if(! event.getUser().equals(tmp.getUser())){
				throw new Exception("User changing not allowed.");
			}
			
			if(tmp.getEventType().getShortName().equalsIgnoreCase("A") && ! event.getEventType().getShortName().equalsIgnoreCase("A")){
				throw new Exception("EventType changing not allowed for Work Events.");
			}
			
		}


		this.setDeleteOtherEvents(false);
		updatePauseLength(event);
		

//		System.out.println("Event:");
//		System.out.println("isWorkingTime:"+event.isWorkingTime());
//		System.out.println("getEventType:"+event.getEventType().getShortName());
		
		
		event = eventController.saveEvent(event);
		
		return event;
	}
	

	
	private void deleteOtherEventsInTimerange(Event event) throws Exception{

		// get other events in timerange
		List<? extends Event> list = eventController.getEvents(event.getEffectiveDate(), event.getExpiryDate(), event.getUser(), null);
		Iterator<? extends Event> i = list.iterator();
		Event other;
		
		while(i.hasNext()){
			other = i.next();
			
			if(other.getEffectiveDate().getTime() < event.getEffectiveDate().getTime())
				continue;
			
			if(other.getExpiryDate().getTime() > event.getExpiryDate().getTime())
				continue;
			
			eventController.deleteEvent(other);
		}

	}

	
	private void checkForIntersectionsForUser(Event event) throws Exception{
		
		Calendar c = Calendar.getInstance();
		TimeUnit timeUnit = TimeUnit.MINUTES;
		long diffInMillies;
		
		// get other events in timerange
		List<? extends Event> list = eventController.getEvents(event.getEffectiveDate(), event.getExpiryDate(), event.getUser(), null);
		
		if(list.size() > 0){
			// Intersection found!
			
			
			if(list.size() == 1){
				
				// try to reduce time range for new event
				Event intersectionEvent = list.get(0);
				

				if(intersectionEvent.isAllDay()){
					throw new Exception("Achtung Termin-Überschneidung!");
				}
				if(event.getEventType().getShortName().equalsIgnoreCase("U")){
					// Holiday Event
					throw new Exception("Achtung Termin-Überschneidung!");
				}
				
				// free time span before intersectionEvent
				diffInMillies = intersectionEvent.getEffectiveDate().getTime() - event.getEffectiveDate().getTime();
				long spaceBefore = timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);

				// free time span after intersectionEvent
				diffInMillies = event.getExpiryDate().getTime() - intersectionEvent.getExpiryDate().getTime();
				long spaceAfter = timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
				
				long marginTime = event.getEventType().getMarginTime() > intersectionEvent.getEventType().getMarginTime() ? event.getEventType().getMarginTime() : intersectionEvent.getEventType().getMarginTime(); 
				
				if(spaceBefore > spaceAfter){
					// try to shrink event to fit before intersection
					if(spaceBefore - marginTime < 0){
						// space ist to small -> abort
						throw new Exception("Achtung Termin-Überschneidung!");
					}
					
					
					
					c.setTime( intersectionEvent.getEffectiveDate() );
					c.add(Calendar.MINUTE, (int) (marginTime * -1));
					
					
					if(c.getTime().getTime() - event.getEffectiveDate().getTime() < 0){
						// time to short for event -> abort
						throw new Exception("Achtung Termin-Überschneidung!");
					}
					
					event.setExpiryDate( c.getTime() );
					

					throw new Exception("Achtung Termin-Überschneidung!", new Exception("Termin wurde angepasst."));
					
				}else{
					// try to shrink event to fit after intersection
					if(spaceAfter - marginTime < 0){
						// space ist to small -> abort
						throw new Exception("Achtung Termin-Überschneidung!");
					}
					
					c.setTime( intersectionEvent.getExpiryDate() );
					c.add(Calendar.MINUTE, (int) (marginTime));
					

					if(event.getExpiryDate().getTime() - c.getTime().getTime() < 0){
						// time to short for event -> abort
						throw new Exception("Achtung Termin-Überschneidung!");
					}
					
					event.setEffectiveDate( c.getTime() );

					throw new Exception("Achtung Termin-Überschneidung!", new Exception("Termin wurde angepasst."));
				}
				
			}else{
				throw new Exception("Achtung Termin-Überschneidung!");
			}
		}
		
		

	}

//	private void saveNewEvent() throws Exception{
//		
//		System.out.println("save NEW Event");
//		
//		String tempUuid = tempEventUuid;
//		String calendarId = selectedCalendarId;
//		
//
//		System.out.println("tempUuid: "+tempUuid);
//		
//
//		
//		event.setHideInTimeline(true);
//		
//
//		if(selectedEvent.getEventType() == null){
//			selectedEvent.setEventType(eventController.getEventTypeByShortName("A"));
//		}
//		
//		
//		
////		updatePauseLength(event);
//		
////		event = eventController.saveEvent(event);
//		
////		selectedEventUuid = event.getUuid();
//		
//
//		System.out.println("selectedEventUuid: "+selectedEventUuid);
//		
//		
//	}

//	private void updateSelectedEvent(){
//
//		String calendarId = selectedCalendarId;
//		
//		Event event = eventController.getEventByUUID(selectedEventUuid);
//		event.setEffectiveDate(selectedEvent.getEffectiveDate());
//		event.setExpiryDate(selectedEvent.getExpiryDate());
//		event.setAllDay(selectedEvent.isAllDay());
//		event.setNote(selectedEvent.getNote());
//		event.setShop(selectedEvent.getShop());
//		
//		event.setWorkingTime(event.getEventType().isWorkingTime());
//		updatePauseLength(event);
//
//		eventController.saveEvent(event);
//
//		
//		selectedEventUuid = event.getUuid();
//		selectedCalendarId = calendarId;
//
//	}

	public String getSelectedEventUuid() {
		return selectedEventUuid;
	}

	public void setSelectedEventUuid(String selectedEventUuid) {

//		System.out.println("setSelectedEventUuid: "+selectedEventUuid);
		
		this.selectedEventUuid = selectedEventUuid;
	}

	public String getSelectedCalendarId() {
		return selectedCalendarId;
	}

	public void setSelectedCalendarId(String selectedCalendarId) {

//		System.out.println("setSelectedCalendarId: "+selectedCalendarId);
		
		this.selectedCalendarId = selectedCalendarId;
	}
	
	public String getTempEventUuid() {
		return tempEventUuid;
	}

	public void setTempEventUuid(String tempEventUuid) {
		this.tempEventUuid = tempEventUuid;
	}

	public List<? extends EventType> getSelectableEventTypeList(){
		return eventController.getSelectableEventTypeList();
	}
	
	
	public void loadSelectedEvent(){
		selectedEvent = eventController.getEventByUUID(selectedEventUuid);
		this.setDeleteOtherEvents(false);
		this.setReloadAllEvents(false);
	}
	
	public void deleteSelectedEvent(){
		eventController.deleteEvent(selectedEvent);
	}
	
	public void clearSelectedEvent(){
//		System.out.println("clearSelectedEvent");
		setSelectedEvent(null);
		setSelectedEventUuid(null);
		setSelectedEventTypeShortName(null);
		setTempEventUuid(null);
		setSelectedCalendarId(null);
		setDeleteOtherEvents(false);
		setReloadAllEvents(false);
	}

	
	public void presetSelectedEventTime(){
		if(selectedEvent == null)
			return;
		if(selectedEvent.getEventType() == null)
			return;
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 1970);
		c.set(Calendar.DAY_OF_YEAR, 1);
		
		selectedEvent.setAllDay(selectedEvent.getEventType().isAllDay());
		
		if(selectedEvent.getEventType().isAllDay()){
			selectedEvent.setAllDay(true);

			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			
			selectedEvent.setEffectiveDate(c.getTime());

			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MILLISECOND, -1);
			
			
			selectedEvent.setExpiryDate(c.getTime());
		}else{
			selectedEvent.setAllDay(false);

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			
			try{
			
				if(selectedEvent.getEventType().getPreferedEventStartTime() != null){
					selectedEvent.setEffectiveDate(sdf.parse(selectedEvent.getEventType().getPreferedEventStartTime()));
				}
				
				if(selectedEvent.getEventType().getPreferedEventEndTime() != null){
					selectedEvent.setExpiryDate(sdf.parse(selectedEvent.getEventType().getPreferedEventEndTime()));
				}
			}catch(ParseException e){
				e.printStackTrace();
			}
			
		}
		
		selectedEvent.setWorkingTime(selectedEvent.getEventType().isWorkingTime());
		
		updatePauseLength();
	}
	
	public void updatePauseLength(){
		updatePauseLength(this.selectedEvent);
	}
	
	private void updatePauseLength(Event event){
		if(event == null)
			return;
		
		if(event.isWorkingTime()){
			
			if(event.isAllDay())
				if(event.isSingleDay())
					event.setPauseLength(calculatePauseLength(event.getUser().getFullDayWorkingTime()));
				else
					event.setPauseLength(0);
			else
				event.setPauseLength(calculatePauseLength(event.getDuration()));
		}else{
			event.setPauseLength(0);
		}
		
	}
	
	private int calculatePauseLength(long duration){
		if(duration > 9*60){
			return (int) propertyBean.getPauseLength9h();
		}else if(duration > 6*60){
			return (int) propertyBean.getPauseLength6h();
		}else{
			return 0;
		}
	}
	
	public void updateAllEvents(){
		Event event;
		Iterator<? extends Event> i = eventController.getFullEventList().iterator();
		while(i.hasNext()){
			event = i.next();
			updatePauseLength(event);
			
			event = eventController.saveEvent(event);
		}
	}
	
	public int calculateWorkingTime(Event e){
		return userController.calculateWorkingTime(e, null, null);
	}
	
	public boolean isSelectedEventTypeChangeable(){
		if(selectedEvent == null)
			return false;
		if(selectedEvent.getEventType() == null)
			return true;
		if(selectedEvent.getEventType().getShortName().equalsIgnoreCase("A"))
			return false;
		if(selectedEvent.getEventType().getShortName().equalsIgnoreCase("U"))
			return false;
		if(selectedEvent.getEventType().getShortName().equalsIgnoreCase("EZ"))
			return false;
		
		return true;
	}
	
	public void prepareNewEvent(){
		
	}
	
	public void validateEventType(FacesContext context, UIComponent component, Object value) {
//		
//		System.out.println("call validateEventType");
//		System.out.println("validate EventTypeShortName: "+selectedEventTypeShortNameInput.getLocalValue());
//		System.out.println("validate EventType: "+value);
		if(value == null && ((String)selectedEventTypeShortNameInput.getLocalValue()).length() == 0)
			throw new ValidatorException(new FacesMessage("Fehlender Event Typ"));
	}

	public String getSelectedEventTypeShortName() {
		return selectedEventTypeShortName;
	}

	public void setSelectedEventTypeShortName(String selectedEventTypeShortName) {
//		System.out.println("setSelectedEventTypeShortName: "+selectedEventTypeShortName);
		this.selectedEventTypeShortName = selectedEventTypeShortName;
	}

	public UIInput getSelectedEventTypeShortNameInput() {
		return selectedEventTypeShortNameInput;
	}

	public void setSelectedEventTypeShortNameInput(
			UIInput selectedEventTypeShortNameInput) {
		this.selectedEventTypeShortNameInput = selectedEventTypeShortNameInput;
	}

	public String getAlert() {
		String a = alert;
		alert = null;
		
		return a;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getWarning() {
		String w = warning;
		warning = null;
		
		return w;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getCause() {
		String c = cause;
		cause = null;
		
		return c;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public boolean isDeleteOtherEvents() {
		return deleteOtherEvents;
	}

	public void setDeleteOtherEvents(boolean deleteOtherEvents) {
		this.deleteOtherEvents = deleteOtherEvents;
	}

	public boolean isReloadAllEvents() {
		return reloadAllEvents;
	}

	public void setReloadAllEvents(boolean reloadAllEvents) {
		this.reloadAllEvents = reloadAllEvents;
	}

	public VacationRequest getSelectedVacationRequest() {
		if(selectedVacationRequest == null)
			selectedVacationRequest = eventController.factoryNewVacationRequest(userBean.getActiveUser());
		return selectedVacationRequest;
	}

	public void setSelectedVacationRequest(VacationRequest selectedVacationRequest) {
		this.selectedVacationRequest = selectedVacationRequest;
	}
	
	public void clearVacationRequest(){
		setSelectedVacationRequest(null);
	}
	
	public void saveVacationRequest(){
		eventController.saveVacationRequest(selectedVacationRequest);
		clearVacationRequest();
	}
	
	public void deleteVacationRequest(){
		selectedVacationRequest.setDeleted();
		selectedVacationRequest = eventController.saveVacationRequest(selectedVacationRequest);
	}

	public List< ? extends VacationRequest> getOwnVacationRequestList() {
		return eventController.getOwnVacationRequestList(userBean.getActiveUser());
	}

	public List< ? extends VacationRequest> getAllVacationRequestList() {
		return eventController.getAllVacationRequestList();
	}
	
	public float getVacationDays(VacationRequest vacationRequest){
		
		if(vacationRequest.getVacationFrom() == null || vacationRequest.getVacationTo() == null)
			return 0;
		
		eventController.correctVacationRequestDate(vacationRequest);
		
		Event event = eventController.factoryNewEvent();
		
		event.setEventType(eventController.getEventTypeByShortName("U"));
		event.setWorkingTime(true);
		event.setEnabled(true);
		event.setUser(vacationRequest.getUser());
		event.setEffectiveDate(vacationRequest.getVacationFrom());
		event.setExpiryDate(vacationRequest.getVacationTo());

		if(!selectedVacationRequest.isEndHalfDay() && !selectedVacationRequest.isStartHalfDay()){
			event.setAllDay(true);
		}
		
		Calendar c = Calendar.getInstance();
		
		c.setTime(vacationRequest.getVacationFrom());
		if(c.get(Calendar.HOUR_OF_DAY) != 0)
			event.setAllDay(false);
		
		c.setTime(vacationRequest.getVacationTo());
		if(c.get(Calendar.HOUR_OF_DAY) != 23)
			event.setAllDay(false);
		
		
		return userController.calculateVacationDuration(event, 0);
	}

	public void ackVacationRequest() {
		
		Event vacationEvent = eventController.factoryNewEvent();
		vacationEvent.setEventType(eventController.getEventTypeByShortName("U"));
		vacationEvent.setWorkingTime(true);
		vacationEvent.setEnabled(true);

		vacationEvent.setEffectiveDate(selectedVacationRequest.getVacationFrom());
		vacationEvent.setExpiryDate(selectedVacationRequest.getVacationTo());
		
		vacationEvent.setUser(selectedVacationRequest.getUser());
		
		vacationEvent.setAllDay(true);

		Calendar c = Calendar.getInstance();
		
		c.setTime(selectedVacationRequest.getVacationFrom());
		if(c.get(Calendar.HOUR_OF_DAY) != 0)
			vacationEvent.setAllDay(false);
		
		c.setTime(selectedVacationRequest.getVacationTo());
		if(c.get(Calendar.HOUR_OF_DAY) != 23)
			vacationEvent.setAllDay(false);
		
		vacationEvent = eventController.saveEvent(vacationEvent);
		
		selectedVacationRequest.setEvent(vacationEvent);
		
		
		selectedVacationRequest.setStatus(VacationRequest.VACATION_REQUEST_STATUS_ACKNOWLEDGED);
		selectedVacationRequest = eventController.saveVacationRequest(selectedVacationRequest);
		clearVacationRequest();
	}

	public void denyVacationRequest() {
		Event oldEvent = selectedVacationRequest.getEvent();
		
		selectedVacationRequest.setEvent(null);
		selectedVacationRequest.setStatus(VacationRequest.VACATION_REQUEST_STATUS_DENIED);
		selectedVacationRequest = eventController.saveVacationRequest(selectedVacationRequest);
		clearVacationRequest();

		if(oldEvent != null){
			eventController.deleteEvent(oldEvent);
		}
	}
	
	public List<? extends Event> getParallelVacations(){
		EventType eventType = eventController.getEventTypeByShortName("U");
		
		return eventController.getEventsByType(selectedVacationRequest.getVacationFrom(), selectedVacationRequest.getVacationTo(), eventType);
	}

	public boolean isCopyEventToMonday() {
		return copyEventToMonday;
	}

	public void setCopyEventToMonday(boolean copyEventToMonday) {
		this.copyEventToMonday = copyEventToMonday;
	}

	public boolean isCopyEventToTuesday() {
		return copyEventToTuesday;
	}

	public void setCopyEventToTuesday(boolean copyEventToTuesday) {
		this.copyEventToTuesday = copyEventToTuesday;
	}

	public boolean isCopyEventToWednesday() {
		return copyEventToWednesday;
	}

	public void setCopyEventToWednesday(boolean copyEventToWednesday) {
		this.copyEventToWednesday = copyEventToWednesday;
	}

	public boolean isCopyEventToThursday() {
		return copyEventToThursday;
	}

	public void setCopyEventToThursday(boolean copyEventToThursday) {
		this.copyEventToThursday = copyEventToThursday;
	}

	public boolean isCopyEventToFriday() {
		return copyEventToFriday;
	}

	public void setCopyEventToFriday(boolean copyEventToFriday) {
		this.copyEventToFriday = copyEventToFriday;
	}

	public boolean isCopyEventToSaturday() {
		return copyEventToSaturday;
	}

	public void setCopyEventToSaturday(boolean copyEventToSaturday) {
		this.copyEventToSaturday = copyEventToSaturday;
	}

	public boolean isCopyEventToSunday() {
		return copyEventToSunday;
	}

	public void setCopyEventToSunday(boolean copyEventToSunday) {
		this.copyEventToSunday = copyEventToSunday;
	}

	public Date getCopyEventTill() {
		return copyEventTill;
	}

	public void setCopyEventTill(Date copyEventTill) {
		this.copyEventTill = copyEventTill;
	}
}
