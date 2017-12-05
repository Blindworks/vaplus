package de.vaplus.client.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.User;

@Entity
@Table(name="SecureUserLink")
public class SecureUserLinkEntity extends SecureLinkEntity implements SecureUserLink{


    /**
	 * 
	 */
	private static final long serialVersionUID = -6443835225544830172L;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity userEntity;

	public SecureUserLinkEntity(){
		super();
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	@Override
	@Transient
	public User getUser() {
		return (User) userEntity;
	}
}
