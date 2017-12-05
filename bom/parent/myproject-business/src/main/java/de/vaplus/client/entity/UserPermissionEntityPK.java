package de.vaplus.client.entity;

import java.io.Serializable;

public class UserPermissionEntityPK implements Serializable {

	private static final long serialVersionUID = 1634798145683624253L;
	
	private long user;
	private String resource;
	private String command;
	
	public long getUser() {
		return user;
	}
	
	public void setUser(long user) {
		this.user = user;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
    public int hashCode() {
        return (int) user + resource.hashCode() + command.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof UserPermissionEntityPK)) return false;
        if (obj == null) return false;
        UserPermissionEntityPK pk = (UserPermissionEntityPK) obj;
        return pk.user == user && pk.resource.equalsIgnoreCase(resource) && pk.command.equalsIgnoreCase(command);
    }
}
