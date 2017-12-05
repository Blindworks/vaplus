package de.vaplus.api.entity;

import java.io.Serializable;

public interface Permission extends Serializable{

	public static int ALLOW = 1;
	public static int DENY = -1;
	public static int INHERIT = 0;
	String getResource();
	void setResource(String resource);
	String getCommand();
	void setCommand(String command);
	int getPermission();
	void setPermission(int permission);

}
