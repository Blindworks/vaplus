package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import de.vaplus.api.entity.Permission;


/**
 * Entity implementation class for Entity: User
 *
 */
@MappedSuperclass
public abstract class PermissionEntity implements Permission {
	
	private static final long serialVersionUID = 4421225374441684919L;

	@Id
	@Column(name="resource")
	protected String resource;
	
	@Id
	@Column(name="command")
	protected String command;
	
	@Column(name="permission")
	private int permission;
	
	public PermissionEntity(){
		super();
	}
	
	public PermissionEntity(String resource, String command, int permission){
		super();
		setResource(resource);
		setCommand(command);
		setPermission(permission);
	}

	@Override
	public String getResource() {
		return resource;
	}

	@Override
	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public int getPermission() {
		return permission;
	}

	@Override
	public void setPermission(int permission) {
		this.permission = permission;
	}


}
