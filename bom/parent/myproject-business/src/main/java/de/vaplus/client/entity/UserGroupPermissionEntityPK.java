package de.vaplus.client.entity;

import java.io.Serializable;

public class UserGroupPermissionEntityPK implements Serializable {

	private static final long serialVersionUID = 1634798145683624253L;
	
	private long userGroup;
	private String resource;
	private String command;
	
	public long getUserGroup() {
		return userGroup;
	}
	
	public void setUserGroup(long userGroup) {
		this.userGroup = userGroup;
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
        return (int) userGroup + resource.hashCode() + command.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof UserGroupPermissionEntityPK)) return false;
        UserGroupPermissionEntityPK pk = (UserGroupPermissionEntityPK) obj;
        return pk.userGroup == userGroup && pk.resource.equalsIgnoreCase(resource) && pk.command.equalsIgnoreCase(command);
    }
}
