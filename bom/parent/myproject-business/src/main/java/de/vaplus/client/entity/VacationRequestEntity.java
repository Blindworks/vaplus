package de.vaplus.client.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VacationRequest;


@Entity
@Table(name="VacationRequest")
public class VacationRequestEntity extends BaseEntity implements VacationRequest {

	private static final long serialVersionUID = 4766370569885168358L;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
	private UserEntity user;
	
	@ManyToOne
    @JoinColumn(name="manager_id", nullable = false)
	private UserEntity manager;

	@Column(name="status")
	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="statusChangedDate", nullable = false)
	private Date statusChangedDate;
	
	@Lob 
	@Column(name="creatorNote", nullable = true)
	private String creatorNote;
	
	@Lob 
	@Column(name="managerNote", nullable = true)
	private String managerNote;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="vacationFrom", nullable = false)
	private Date vacationFrom;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="vacationTo", nullable = false)
	private Date vacationTo;
	
	@ManyToOne
    @JoinColumn(name="event_id", nullable = false)
	private EventEntity event;
	
	@Transient
	private boolean startHalfDay;
	
	@Transient
	private boolean endHalfDay;

	public VacationRequestEntity() {
		super();
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = (UserEntity) user;
	}

	@Override
	public User getManager() {
		return manager;
	}

	@Override
	public void setManager(User manager) {
		this.manager = (UserEntity) manager;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
		setStatusChangedDate(new Date());
	}

	@Override
	public Date getStatusChangedDate() {
		return statusChangedDate;
	}

	public void setStatusChangedDate(Date statusChangedDate) {
		this.statusChangedDate = statusChangedDate;
	}

	@Override
	public Date getVacationFrom() {
		return vacationFrom;
	}

	@Override
	public void setVacationFrom(Date vacationFrom) {
		this.vacationFrom = vacationFrom;
	}

	@Override
	public Date getVacationTo() {
		return vacationTo;
	}

	@Override
	public void setVacationTo(Date vacationTo) {
		this.vacationTo = vacationTo;
	}

	@Override
	public Event getEvent() {
		return event;
	}

	@Override
	public void setEvent(Event event) {
		this.event = (EventEntity) event;
	}

	@Override
	public String getCreatorNote() {
		return creatorNote;
	}

	@Override
	public void setCreatorNote(String creatorNote) {
		this.creatorNote = creatorNote;
	}

	@Override
	public String getManagerNote() {
		return managerNote;
	}

	@Override
	public void setManagerNote(String managerNote) {
		this.managerNote = managerNote;
	}

	@Override
	public boolean isStartHalfDay() {
		return startHalfDay;
	}

	@Override
	public void setStartHalfDay(boolean startHalfDay) {
		this.startHalfDay = startHalfDay;
	}

	@Override
	public boolean isEndHalfDay() {
		return endHalfDay;
	}

	@Override
	public void setEndHalfDay(boolean endHalfDay) {
		this.endHalfDay = endHalfDay;
	}

}
