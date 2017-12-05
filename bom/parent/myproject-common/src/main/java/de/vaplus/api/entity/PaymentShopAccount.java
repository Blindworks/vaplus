package de.vaplus.api.entity;

import java.io.Serializable;

public interface PaymentShopAccount extends Serializable {

	Shop getShop();

	void setShop(Shop shop);

	Payment getPayment();

	void setPayment(Payment payment);

	PaymentAccount getAccount();

	void setAccount(PaymentAccount account);

	PaymentAccount getSettlingAccount();

	void setSettlingAccount(PaymentAccount settlingAccount);

}
