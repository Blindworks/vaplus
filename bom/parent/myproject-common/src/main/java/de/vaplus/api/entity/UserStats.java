package de.vaplus.api.entity;

import java.io.Serializable;
import java.util.Date;

public interface UserStats extends Serializable{

	int getYear();

	void setYear(int year);

	int getMonth();

	void setMonth(int month);

	User getUser();

	void setOvertime(int overtime);

	int getOvertime();

	public Date getDate();

	float getOvertimeHours();

	void setOvertimeHours(float overtime);

}
