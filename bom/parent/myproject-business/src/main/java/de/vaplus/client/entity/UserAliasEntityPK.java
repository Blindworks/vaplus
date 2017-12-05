package de.vaplus.client.entity;

import java.io.Serializable;

public class UserAliasEntityPK implements Serializable {

	private static final long serialVersionUID = 1634798145683624253L;
	
	private long user;
	private String alias;
	
	public long getUser() {
		return user;
	}
	
	public void setUser(long user) {
		this.user = user;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
    public int hashCode() {
        return (int) user + alias.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof UserAliasEntityPK)) return false;
        UserAliasEntityPK pk = (UserAliasEntityPK) obj;
        return pk.user == user && pk.alias.equalsIgnoreCase(alias);
    }
}
