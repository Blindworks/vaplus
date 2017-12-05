package de.vaplus.client.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserStats;


@Entity
@Table(name="UserStats")
@IdClass(UserStatsEntityPK.class)
public class UserStatsEntity implements UserStats{

	private static final long serialVersionUID = 5172228882876281784L;

	@Id
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
	private UserEntity user;

	@Id
	@Column(name="year", nullable = false)
	private int year;

	@Id
	@Column(name="month", nullable = false)
	private int month;
	
	
	@Column(name="overtime", nullable = false)
	private int overtime;
	
	
	public UserStatsEntity(){
		
	}
	
	public UserStatsEntity(User user, int year, int month){
		setUser(user);
		setYear(year);
		setMonth(month);
	}

	@Override
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = (UserEntity) user;
	}

	@Override
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Override
	public int getOvertime() {
		return overtime;
	}

	@Override
	public void setOvertime(int overtime) {
		this.overtime = overtime;
	}

	@Override
	public float getOvertimeHours() {
		return overtime / 60;
	}

	@Override
	public void setOvertimeHours(float overtime) {
		this.overtime = (int) (overtime * 60);
	}

	@Override
	public Date getDate() {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}


}