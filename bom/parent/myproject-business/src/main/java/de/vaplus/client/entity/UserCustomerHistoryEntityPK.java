package de.vaplus.client.entity;

import java.io.Serializable;

public class UserCustomerHistoryEntityPK implements Serializable {

	private static final long serialVersionUID = -8211271691829163750L;
	
	private long user;
	private long customer;
	
	public long getUser() {
		return user;
	}
	
	public void setUser(long user) {
		this.user = user;
	}
	
	public long getCustomer() {
		return customer;
	}
	
	public void setCustomer(long customer) {
		this.customer = customer;
	}
	
    public int hashCode() {
        return (int) user + (int) customer;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof UserCustomerHistoryEntityPK)) return false;
        UserCustomerHistoryEntityPK pk = (UserCustomerHistoryEntityPK) obj;
        return pk.user == user && pk.customer == customer;
    }
}
