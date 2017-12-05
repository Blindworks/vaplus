package de.vaplus.api.entity;

import java.util.Date;

public interface VacationRequest extends Base{

	public static int VACATION_REQUEST_STATUS_NEW = 0;
	public static int VACATION_REQUEST_STATUS_ACKNOWLEDGED = 1;
	public static int VACATION_REQUEST_STATUS_DENIED = 2;
	public static int VACATION_REQUEST_STATUS_WITHDRAWN = 3;
	User getUser();
	void setUser(User user);
	User getManager();
	void setManager(User manager);
	int getStatus();
	void setStatus(int status);
	Date getStatusChangedDate();
	Date getVacationFrom();
	void setVacationFrom(Date vacationFrom);
	Date getVacationTo();
	void setVacationTo(Date vacationTo);
	Event getEvent();
	void setEvent(Event event);
	String getCreatorNote();
	void setCreatorNote(String creatorNote);
	String getManagerNote();
	void setManagerNote(String managerNote);
	boolean isStartHalfDay();
	void setStartHalfDay(boolean startHalfDay);
	boolean isEndHalfDay();
	void setEndHalfDay(boolean endHalfDay);
	
}
