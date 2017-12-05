package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.Country;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentAccountTransaction;
import de.vaplus.api.entity.PaymentStatus;
import de.vaplus.api.entity.Vendor;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="PaymentStatus")
public class PaymentStatusEntity extends ActivityEntity implements PaymentStatus {

	private static final long serialVersionUID = 3855704594761563734L;

	@ManyToOne
    @JoinColumn(name="payment_id", nullable = false)
    private PaymentEntity payment;
	
	@Column(name="open", nullable = false)
	private boolean open;
	
	@Column(name="cumulativeSum", nullable = false)
	private BigDecimal cumulativeSum;
	
	public PaymentStatusEntity() {
		super();
	}

	@Override
	public Payment getPayment() {
		return payment;
	}

	@Override
	public void setPayment(Payment payment) {
		this.payment = (PaymentEntity) payment;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	@Override
	public BigDecimal getCumulativeSum() {
		return cumulativeSum;
	}

	@Override
	public void setCumulativeSum(BigDecimal cumulativeSum) {
		this.cumulativeSum = cumulativeSum;
	}

}
