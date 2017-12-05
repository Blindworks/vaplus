package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentShopAccount;
import de.vaplus.api.entity.ProductStockCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserStats;


@Entity
@Table(name="PaymentShopAccount")
@NamedQueries({
    @NamedQuery(
            name = "PaymentShopAccount.get",
            query = "SELECT a FROM PaymentShopAccountEntity a Where a.shop = :shop AND a.payment = :payment",
            hints = {
                    @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
                }
    ),
})
@IdClass(PaymentShopAccountPK.class)
public class PaymentShopAccountEntity implements PaymentShopAccount{

	private static final long serialVersionUID = 9142545955584424314L;

	@Id
	@ManyToOne
    @JoinColumn(name="shop_id", nullable = false)
	private ShopEntity shop;

	@Id
	@ManyToOne
    @JoinColumn(name="payment_id", nullable = false)
	private PaymentEntity payment;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_id", nullable = true)
    private PaymentAccountEntity account;

	// Account to balance the user account
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="settlingAccount_id", nullable = true)
    private PaymentAccountEntity settlingAccount;
	
	public PaymentShopAccountEntity(){
		
	}

	@Override
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = (ShopEntity) shop;
	}

	@Override
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = (PaymentEntity) payment;
	}

	@Override
	public PaymentAccount getAccount() {
		return account;
	}

	public void setAccount(PaymentAccount account) {
		this.account = (PaymentAccountEntity) account;
	}

	@Override
	public PaymentAccount getSettlingAccount() {
		return settlingAccount;
	}

	public void setSettlingAccount(PaymentAccount settlingAccount) {
		this.settlingAccount = (PaymentAccountEntity) settlingAccount;
	}

}