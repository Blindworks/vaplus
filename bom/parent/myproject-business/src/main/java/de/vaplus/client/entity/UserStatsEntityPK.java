package de.vaplus.client.entity;

import java.io.Serializable;

public class UserStatsEntityPK implements Serializable {

	private static final long serialVersionUID = 1634798145683624253L;
	
	private long user;
	private int year;
	private int month;
	
	public long getUser() {
		return user;
	}
	
	public void setUser(long user) {
		this.user = user;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
    public int hashCode() {
        return (int) user + year + month;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof UserStatsEntityPK)) return false;
        UserStatsEntityPK pk = (UserStatsEntityPK) obj;
        return pk.user == user && pk.year == year && pk.month == month;
    }
}
