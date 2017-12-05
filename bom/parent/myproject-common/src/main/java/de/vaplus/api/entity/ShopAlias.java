package de.vaplus.api.entity;

import java.io.Serializable;

public interface ShopAlias extends Serializable{

	Shop getShop();

	void setShop(Shop shop);

	String getAlias();

}
