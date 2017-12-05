package de.vaplus.client.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

/**
 * Entity implementation class for Entity: SecureLink
 *
 */
@Entity
@Table(name="Activity")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ActivityEntity extends StatusBaseEntity implements Activity {

	private static final long serialVersionUID = 9189527544067109060L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable = false)
    private CustomerEntity customer;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id", nullable = false)
    private ShopEntity shop;
	
	@Column(name="hideInTimeline", nullable = false)
	private boolean hideInTimeline;

	@Lob 
	@Column(name="note", nullable = true)
	private String note;
	
	public ActivityEntity() {
		super();
	}

	@Override
	public Customer getCustomer() {
		return customer;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = (CustomerEntity) customer;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = (UserEntity) user;
	}

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public void setShop(Shop shop) {
		this.shop = (ShopEntity) shop;
	}

	@Override
	public boolean isHideInTimeline() {
		return hideInTimeline;
	}

	@Override
	public void setHideInTimeline(boolean hideInTimeline) {
		this.hideInTimeline = hideInTimeline;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}

   
}
