package de.vaplus.api.entity;

public interface Event extends Activity{

	String getUuid();

	String getTitle();

	void setTitle(String title);

	boolean isAllDay();

	void setAllDay(boolean allDay);

	EventType getEventType();

	void setEventType(EventType eventType);

	boolean isWorkingTime();

	void setWorkingTime(boolean workingTime);

	int getPauseLength();

	void setPauseLength(int pauseLength);

	long getDuration();

	long getWorkingDuration();

	boolean isSingleDay();

	long getDurationInDays();

}
