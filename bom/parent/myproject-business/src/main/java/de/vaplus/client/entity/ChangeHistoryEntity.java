package de.vaplus.client.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.ChangeHistory;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

/**
 * Entity implementation class for Entity: SecureLink
 *
 */
@Entity
@Table(name="ChangeHistory")
public class ChangeHistoryEntity implements ChangeHistory {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    @JoinColumn(name="baseClass", nullable = false)
    private String baseClass;
	
    @JoinColumn(name="base_id", nullable = false)
    private Long baseId;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

	@ManyToOne
    @JoinColumn(name="shop_id", nullable = false)
    private ShopEntity shop;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="changeDate", nullable = false)
	private Date changeDate;
	
	public ChangeHistoryEntity() {
		super();
	}
	
	public ChangeHistoryEntity(User user, Shop shop) {
		super();
		this.user = (UserEntity) user;
		this.shop = (ShopEntity) shop;
		this.changeDate = new Date();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public Date getChangeDate() {
		return changeDate;
	}

}
