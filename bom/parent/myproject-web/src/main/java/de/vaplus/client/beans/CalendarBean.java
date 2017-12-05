package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;

@ManagedBean(name = "calendarBean")
@SessionScoped
public class CalendarBean implements Serializable {


	@EJB
	private EventControllerInterface eventController;
	
    @ManagedProperty(value="#{eventBean}")
    private EventBean eventBean;

    @ManagedProperty(value="#{permissionBean}")
    private PermissionBean permissionBean;
    
	private Event selectedEvent;
	
	private boolean calendarEditable;
	
	private String selectedEventUuid;

	private Date calendarRangeStart;
	private Date calendarRangeEnd;
	
	private ShopGroup selectedShopGroup;


	@Inject
	private FacesContext facesContext;
	
	public CalendarBean() {
		// System.out.println("### INIT "+this.getClass().getSimpleName()+" ###");
	}


	public boolean isCalendarEditable() {
		return calendarEditable;
	}

	public void toggleCalendarEditable() {
		calendarEditable = calendarEditable ? false : true;
	}


	public Event getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(Event selectedEvent) {
		this.selectedEvent = selectedEvent;
	}

	public EventBean getEventBean() {
		return eventBean;
	}


	public void setEventBean(EventBean eventBean) {
		this.eventBean = eventBean;
	}


	public String getSelectedEventUuid() {
		return selectedEventUuid;
	}

	public void setSelectedEventUuid(String selectedEventUuid) {
		this.selectedEventUuid = selectedEventUuid;
	}

	
	public PermissionBean getPermissionBean() {
		return permissionBean;
	}


	public void setPermissionBean(PermissionBean permissionBean) {
		this.permissionBean = permissionBean;
	}


	public void loadSelectedEvent(){
		selectedEvent = eventController.getEventByUUID(selectedEventUuid);
	}
	
	public void deleteSelectedEvent(){
		eventController.deleteEvent(selectedEvent);
	}
	

	public Date getCalendarRangeStart() {
		if(calendarRangeStart == null)
			calendarRangeStart = new Date();
		return calendarRangeStart;
	}


	public void setCalendarRangeStart(Date calendarRangeStart) {
		this.calendarRangeStart = calendarRangeStart;
	}


	public Date getCalendarRangeEnd() {
		if(calendarRangeEnd == null)
			calendarRangeEnd = new Date();
		return calendarRangeEnd;
	}


	public void setCalendarRangeEnd(Date calendarRangeEnd) {
		this.calendarRangeEnd = calendarRangeEnd;
	}
	
	public boolean isSelectedEventEditable(){
		if(! this.isCalendarEditable())
			return false;
		
		if(! permissionBean.hasPermission("calendar","edit"))
			return false;
		
		if(eventBean.getSelectedEvent() == null)
			return false;
		
		if(eventBean.getSelectedEvent().getEventType() == null)
			return true;
		
		if(eventBean.getSelectedEvent().getEventType().getShortName().equalsIgnoreCase("U"))
			return false;
		
		if(eventBean.getSelectedEvent().getEventType().getShortName().equalsIgnoreCase("EZ"))
			return false;
					
		return true;
	}
	
	public String getUserCalendarFeed(User user){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String url;
		
		if(user == null)
			return "";
		
		if ((request.getServerPort() == 80) || (request.getServerPort() == 441))
			url = request.getScheme() + "://" + request.getServerName()
	                + request.getContextPath();
	    else
	    	url = request.getScheme() + "://" + request.getServerName() + ":"
	                + request.getServerPort() + request.getContextPath();
		
		url += "/sockets/ical/user/"+ user.getUuid()+"/calendar.ics";
		
		return url;
		
	}


	public ShopGroup getSelectedShopGroup() {
		return selectedShopGroup;
	}


	public void setSelectedShopGroup(ShopGroup selectedShopGroup) {
		this.selectedShopGroup = selectedShopGroup;
	}

	public List<? extends Shop> getGroupedShopList(){
		if(this.getSelectedShopGroup() == null)
			return null;
		return this.getSelectedShopGroup().getShopList();
	}
}
