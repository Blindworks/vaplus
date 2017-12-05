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
import de.vaplus.api.entity.Vendor;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="PaymentAccountTransaction")
@NamedQueries({
    @NamedQuery(
            name = "PaymentAccountTransaction.getAccountIncoming",
            query = "SELECT SUM(t.amount) FROM PaymentAccountTransactionEntity t WHERE t.destinationAccount = :account"
    ),
    @NamedQuery(
            name = "PaymentAccountTransaction.getAccountOutgoing",
            query = "SELECT SUM(t.amount) FROM PaymentAccountTransactionEntity t WHERE t.sourceAccount = :account"
    ),
    @NamedQuery(
            name = "PaymentAccountTransaction.getTransactions",
            query = "SELECT t FROM PaymentAccountTransactionEntity t WHERE t.sourceAccount = :account or t.destinationAccount = :account",
            hints = {
            		@QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
})
public class PaymentAccountTransactionEntity extends BaseEntity implements PaymentAccountTransaction {

	private static final long serialVersionUID = 2555268177216918788L;
	
	@Column(name="amount", nullable = false)
	private BigDecimal amount;
	
	@Column(name="note", nullable = false)
	private String note;
	
	@ManyToOne
    @JoinColumn(name="sourceAccount_id", nullable = false)
    private PaymentAccountEntity sourceAccount;
	
	@ManyToOne
    @JoinColumn(name="destinationAccount_id", nullable = false)
    private PaymentAccountEntity destinationAccount;
	
	@ManyToOne
    @JoinColumn(name="invoiceReference_id", nullable = false)
    private InvoiceEntity invoiceReference;
	

	public PaymentAccountTransactionEntity() {
		super();
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public PaymentAccount getSourceAccount() {
		return sourceAccount;
	}

	@Override
	public void setSourceAccount(PaymentAccount sourceAccount) {
		this.sourceAccount = (PaymentAccountEntity) sourceAccount;
	}

	@Override
	public PaymentAccount getDestinationAccount() {
		return destinationAccount;
	}

	@Override
	public void setDestinationAccount(PaymentAccount destinationAccount) {
		this.destinationAccount = (PaymentAccountEntity) destinationAccount;
	}

	@Override
	public Invoice getInvoiceReference() {
		return invoiceReference;
	}

	@Override
	public void setInvoiceReference(Invoice invoiceReference) {
		this.invoiceReference = (InvoiceEntity) invoiceReference;
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
