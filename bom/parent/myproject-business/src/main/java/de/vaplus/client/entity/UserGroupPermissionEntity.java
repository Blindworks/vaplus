package de.vaplus.client.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.UserGroup;
import de.vaplus.api.entity.UserGroupPermission;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@IdClass(UserGroupPermissionEntityPK.class)
@Table(name="UserGroupPermission")
public class UserGroupPermissionEntity extends PermissionEntity implements UserGroupPermission{

	private static final long serialVersionUID = -8973017877497738168L;
	
	@Id
	@ManyToOne
    @JoinColumn(name="userGroup_id", nullable = false)
    private UserGroupEntity userGroup;


	public UserGroupPermissionEntity(){
		super();
	}

	public UserGroupPermissionEntity(UserGroup userGroup, String resource, String command, int permission){
		super(resource, command, permission);
		setUserGroup(userGroup);
	}

	@Override
	public UserGroup getUserGroup() {
		return userGroup;
	}

	@Override
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = (UserGroupEntity) userGroup;
	}

}
