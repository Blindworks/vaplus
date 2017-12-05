package de.vaplus.api.entity;

public interface EventType extends Base{

	String getUuid();

	String getTitle();

	void setTitle(String title);

	String getShortName();

	void setShortName(String shortName);

	boolean isMultiUserEvent();

	void setMultiUserEvent(boolean multiUserEvent);

	boolean isEditable();

	void setEditable(boolean editable);

	boolean isAllDay();

	void setAllDay(boolean allDay);

	int getMarginTime();

	void setMarginTime(int marginTime);

	String getPreferedEventStartTime();

	void setPreferedEventStartTime(String preferedEventStartTime);

	String getPreferedEventEndTime();

	void setPreferedEventEndTime(String preferedEventEndTime);

	boolean isExternalEvent();

	void setExternalEvent(boolean externalEvent);

	boolean isWorkingTime();

	void setWorkingTime(boolean workingTime);

}
