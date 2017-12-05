package de.vaplus.client.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserCustomerHistory;
import de.vaplus.api.entity.UserGroup;
import de.vaplus.api.entity.UserGroupPermission;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@IdClass(UserCustomerHistoryEntityPK.class)
@Table(name="UserCustomerHistory")
public class UserCustomerHistoryEntity implements UserCustomerHistory{

	private static final long serialVersionUID = -7096615129273282540L;

	@Id
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;
	
	@Id
	@ManyToOne
    @JoinColumn(name="customer_id", nullable = false)
    private CustomerEntity customer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lastOpened", nullable = true)
	private Date lastOpened;


	public UserCustomerHistoryEntity(){
	}

	public UserCustomerHistoryEntity(User user, Customer customer){
		setUser((UserEntity) user);
		setCustomer((CustomerEntity) customer);
		setLastOpened(new Date());
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}
	
	@Override
	public Date getLastOpened() {
		return lastOpened;
	}

	public void setLastOpened(Date lastOpened) {
		this.lastOpened = lastOpened;
	}

	@Override
	public void refreshLastOpened(){
		setLastOpened(new Date());
	}
}
