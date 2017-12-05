package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@IdClass(ShopAliasEntityPK.class)
@Table(name="ShopAlias")
public class ShopAliasEntity implements ShopAlias {

	private static final long serialVersionUID = 9140149186273897271L;
	
	@Id
	@ManyToOne
    @JoinColumn(name="shop_id", nullable = false)
    private ShopEntity shop;
	
	@Id
    @Column(name="alias", nullable = false)
    private String alias;

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public void setShop(Shop shop) {
		this.shop = (ShopEntity) shop;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
