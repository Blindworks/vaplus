package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.vaplus.api.entity.EventType;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="EventType", uniqueConstraints = @UniqueConstraint(columnNames = {"uuid", "title", "shortName"}))
public class EventTypeEntity extends BaseEntity implements EventType {
	
	private static final long serialVersionUID = 5059715796568277948L;

	@Column(name="uuid", nullable = false)
	private String uuid;
	
	@Column(name="title")
	private String title;
	
	@Column(name="shortName")
	private String shortName;

	@Column(name="multiUserEvent")
	private boolean multiUserEvent;

	@Column(name="editable")
	private boolean editable;

	@Column(name="allDay")
	private boolean allDay;

	@Column(name="marginTime")
	private int marginTime;

	@Column(name="preferedEventStartTime", nullable = true, length=5)
	private String preferedEventStartTime;

	@Column(name="preferedEventEndTime", nullable = true, length=5)
	private String preferedEventEndTime;

	@Column(name="externalEvent")
	private boolean externalEvent;

	@Column(name="workingTime", nullable = false)
	private boolean workingTime;

	
	public EventTypeEntity() {
		super();
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getShortName() {
		return shortName;
	}

	@Override
	public void setShortName(String shortName) {
		this.shortName = shortName.toUpperCase();
	}

	@Override
	public boolean isMultiUserEvent() {
		return multiUserEvent;
	}

	@Override
	public void setMultiUserEvent(boolean multiUserEvent) {
		this.multiUserEvent = multiUserEvent;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean isAllDay() {
		return allDay;
	}

	@Override
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	@Override
	public int getMarginTime() {
		return marginTime;
	}

	@Override
	public void setMarginTime(int marginTime) {
		this.marginTime = marginTime;
	}

	@Override
	public String getPreferedEventStartTime() {
		return preferedEventStartTime;
	}

	@Override
	public void setPreferedEventStartTime(String preferedEventStartTime) {
		this.preferedEventStartTime = preferedEventStartTime;
	}

	@Override
	public String getPreferedEventEndTime() {
		return preferedEventEndTime;
	}

	@Override
	public void setPreferedEventEndTime(String preferedEventEndTime) {
		this.preferedEventEndTime = preferedEventEndTime;
	}

	@Override
	public boolean isExternalEvent() {
		return externalEvent;
	}

	@Override
	public void setExternalEvent(boolean externalEvent) {
		this.externalEvent = externalEvent;
	}

	@Override
	public boolean isWorkingTime() {
		return workingTime;
	}

	@Override
	public void setWorkingTime(boolean workingTime) {
		this.workingTime = workingTime;
	}

	
}