package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.entity.EventType;

@ManagedBean(name = "eventTypeBean")
@SessionScoped
public class EventTypeBean implements Serializable {


	@EJB
	private EventControllerInterface eventController;
	
	@Inject
	private FacesContext facesContext;
	
	private boolean eventTypeListEditable;
	
	private EventType selectedEventType;
	
	public EventTypeBean() {
//		 System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}

	public List<? extends EventType> getEventTypeList(){
		return eventController.getEventTypeList();
	}

	public boolean isEventTypeListEditable() {
		return eventTypeListEditable;
	}

	public void toggleEventTypeListEditable() {
		eventTypeListEditable = eventTypeListEditable ? false : true;
	}

	public EventType getSelectedEventType() {
		if(selectedEventType == null)
			selectedEventType = eventController.factoryNewEventType();
		return selectedEventType;
	}

	public void setSelectedEventType(EventType selectedEventType) {
		this.selectedEventType = selectedEventType;
	}
	
	public String saveEventType(){
		selectedEventType = eventController.saveEventType(selectedEventType);
		return "lists";
	}
}
