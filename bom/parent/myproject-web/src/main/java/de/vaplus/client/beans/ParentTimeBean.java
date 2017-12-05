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

@ManagedBean(name="parentTimeBean")
@SessionScoped
public class ParentTimeBean implements Serializable {

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

	private Event selectedParentTime;
	
	public ParentTimeBean(){
		// System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}
	
	public EventBean getEventBean() {
		return eventBean;
	}

	public void setEventBean(EventBean eventBean) {
		this.eventBean = eventBean;
	}

	public Event getSelectedParentTime() {
		if(selectedParentTime == null){
			selectedParentTime = eventController.factoryNewEvent();
			selectedParentTime.setEventType(eventController.getEventTypeByShortName("EZ"));
			selectedParentTime.setShop(shopController.factoryExternalDummyShop());
			selectedParentTime.setAllDay(true);
			selectedParentTime.setWorkingTime(true);
		}
			
		return selectedParentTime;
	}

	public void setSelectedParentTime(Event selectedParentTime) {
		this.selectedParentTime = selectedParentTime;
	}
	
	public void saveSelectedParentTime(){
		
		if(selectedParentTime.getEffectiveDate() == null || selectedParentTime.getExpiryDate() == null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhaftes Datum", null));
			return;
		}
		
		Calendar c = Calendar.getInstance();
		
		if(selectedParentTime.isAllDay()){
			c.setTime(selectedParentTime.getExpiryDate());
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			selectedParentTime.setExpiryDate(c.getTime());
		}
		
		try {
			eventBean.setSelectedEventTypeShortName(null);
			selectedParentTime = eventBean.saveEvent(selectedParentTime);
		} catch (Exception e) {
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler beim speichern!", null));
		}
		
		if(selectedParentTime != null){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Elternzeit erfolgreich gespeichert!", null));
			selectedParentTime = null;
		}
		
	}
	

	public List<? extends Event> getParentalTimeList(User user) {
		 List<? extends Event> list = null;
		 
		 EventType type = eventController.getEventTypeByShortName("EZ");
		 
		 list = eventController.getEventsByType(null, null, user, type);
		 
		return list;
	}
	
	public float getVacationDays(Event event){
		return userController.calculateVacationDuration(event);
	}
	
	public void deleteParentTime(){
		if(selectedParentTime == null)
			return;
		if(selectedParentTime.getId() == 0)
			return;
		
		eventController.deleteEvent(selectedParentTime);

		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Elternzeit erfolgreich gelöscht!", null));
		selectedParentTime = null;
	}
	
	public void clearParentTime(){
		selectedParentTime = null;
	}
}
