package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VacationRequest;

@ManagedBean(name="holidayBean")
@SessionScoped
public class HolidayBean implements Serializable {

	private static final long serialVersionUID = -5036075639517377354L;

	@EJB
	private EventControllerInterface eventController;
	
	@EJB
	private ShopControllerInterface shopController;
	
	@EJB
	private UserControllerInterface userController;
	
	@Inject
	private FacesContext facesContext;

    @ManagedProperty(value="#{eventBean}")
    private EventBean eventBean;

	private Event selectedHoliday;
	
	public HolidayBean(){
		// System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}
	
	public EventBean getEventBean() {
		return eventBean;
	}

	public void setEventBean(EventBean eventBean) {
		this.eventBean = eventBean;
	}

	public Event getSelectedHoliday() {
		if(selectedHoliday == null){
			selectedHoliday = eventController.factoryNewEvent();
			selectedHoliday.setEventType(eventController.getEventTypeByShortName("U"));
			selectedHoliday.setShop(shopController.factoryExternalDummyShop());
			selectedHoliday.setAllDay(true);
			selectedHoliday.setWorkingTime(true);
			selectedHoliday.setEnabled(true);
		}
			
		return selectedHoliday;
	}

	public void setSelectedHoliday(Event selectedHoliday) {
		this.selectedHoliday = selectedHoliday;
	}
	
	public void saveSelectedHoliday(){
		
		if(selectedHoliday.getEffectiveDate() == null || selectedHoliday.getExpiryDate() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhaftes Datum", null));
			return;
		}
		
		Calendar c = Calendar.getInstance();
		
		if(selectedHoliday.isAllDay()){
			c.setTime(selectedHoliday.getExpiryDate());
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			selectedHoliday.setExpiryDate(c.getTime());
		}
		
		try {
			eventBean.setSelectedEventTypeShortName(null);
			selectedHoliday = eventBean.saveEvent(selectedHoliday);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler beim speichern!", null));
		}
		
		if(selectedHoliday != null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Urlaub erfolgreich gespeichert!", null));
			selectedHoliday = null;
		}
		
	}


	public List<? extends Event> getHolidayList(User user) {
		 List<? extends Event> list = null;
		 
		 EventType type = eventController.getEventTypeByShortName("U");
		 
		 list = eventController.getEventsByType(null, null, user, type);
		 
		return list;
	}
	
	
	public float getVacationDays(Event event){
		return userController.calculateVacationDuration(event);
	}
	
	public void deleteHoliday(){
		if(selectedHoliday == null)
			return;
		if(selectedHoliday.getId() == 0)
			return;
		
		eventController.deleteEvent(selectedHoliday);

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Urlaub erfolgreich gelöscht!", null));
		selectedHoliday = null;
	}
	
	public void clearHoliday(){
		selectedHoliday = null;
	}
}
