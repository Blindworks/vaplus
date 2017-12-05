package de.vaplus.client.entity;

import java.io.Serializable;

public class ShopAliasEntityPK implements Serializable {

	private static final long serialVersionUID = 1634798145683624253L;
	
	private long shop;
	private String alias;
	
	public long getShop() {
		return shop;
	}
	
	public void setShop(long shop) {
		this.shop = shop;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
    public int hashCode() {
        return (int) shop + alias.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShopAliasEntityPK)) return false;
        ShopAliasEntityPK pk = (ShopAliasEntityPK) obj;
        return pk.shop == shop && pk.alias.equalsIgnoreCase(alias);
    }
}
