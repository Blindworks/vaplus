package de.vaplus.client.entity;

import java.io.Serializable;

public class PaymentShopAccountPK implements Serializable {

	private static final long serialVersionUID = 3925069618224957664L;
	
	private long shop;
	private long payment;
	
	public long getShop() {
		return shop;
	}
	
	public void setShop(long shop) {
		this.shop = shop;
	}
	
	public long getPayment() {
		return payment;
	}
	
	public void setpayment(long payment) {
		this.payment = payment;
	}
	
    public int hashCode() {
        return (int) shop + (int) payment;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof PaymentShopAccountPK)) return false;
        PaymentShopAccountPK pk = (PaymentShopAccountPK) obj;
        return pk.shop == shop && pk.payment == payment;
    }
}
