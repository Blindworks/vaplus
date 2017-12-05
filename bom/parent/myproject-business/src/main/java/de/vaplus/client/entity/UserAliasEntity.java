package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserAlias;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@IdClass(UserAliasEntityPK.class)
@Table(name="UserAlias")
public class UserAliasEntity implements UserAlias {

	private static final long serialVersionUID = 9140149186273897271L;
	
	@Id
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;
	
	@Id
    @Column(name="alias", nullable = false)
    private String alias;

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = (UserEntity) user;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
