package de.vaplus.client.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserPermission;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@IdClass(UserPermissionEntityPK.class)
@Table(name="UserPermission")
public class UserPermissionEntity extends PermissionEntity implements UserPermission{

	private static final long serialVersionUID = -8973017877497738168L;
	
	@Id
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

	public UserPermissionEntity(){
		super();
	}

	public UserPermissionEntity(User user, String resource, String command, int permission){
		super(resource, command, permission);
		setUser(user);
	}
	
	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = (UserEntity) user;
	}

}
