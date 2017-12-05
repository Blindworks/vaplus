package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.Country;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.Vendor;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Payment")
@NamedQueries({
    @NamedQuery(
            name = "Payment.getActivePayments",
            query = "SELECT p FROM PaymentEntity p WHERE p.enabled = true AND p.deleted = false Order by p.name ASC ",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "Payment.getAllPayments",
            query = "SELECT p FROM PaymentEntity p WHERE p.deleted = false Order by p.name ASC ",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    )
})
public class PaymentEntity extends StatusBaseEntity implements Payment {
	
	private static final long serialVersionUID = -6711907778670950340L;

	@Column(name="name", nullable = false)
	private String name;

	@Column(name="description", nullable = false)
	private String description;

	// if is checked, id,name and account are not changeable
	@Column(name="systemPayment", nullable = false)
	private boolean systemPayment;

	// if is checked, payment ist adhoc booked
	@Column(name="direct", nullable = false)
	private boolean direct;

	// termOfPayment in days
	@Column(name="termOfPayment", nullable = false)
	private int termOfPayment;

	@Lob 
	@Column(name="invoiceText")
	private String invoiceText;
	
	public PaymentEntity() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean isSystemPayment() {
		return systemPayment;
	}

	public void setSystemPayment(boolean systemPayment) {
		this.systemPayment = systemPayment;
	}

	@Override
	public boolean isDirect() {
		return direct;
	}

	@Override
	public void setDirect(boolean direct) {
		this.direct = direct;
	}

	@Override
	public String getInvoiceText() {
		return invoiceText;
	}

	@Override
	public void setInvoiceText(String invoiceText) {
		this.invoiceText = invoiceText;
	}

	@Override
	public int getTermOfPayment() {
		return termOfPayment;
	}

	@Override
	public void setTermOfPayment(int termOfPayment) {
		this.termOfPayment = termOfPayment;
	}


   
}
