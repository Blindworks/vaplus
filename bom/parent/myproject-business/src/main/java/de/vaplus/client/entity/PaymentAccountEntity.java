package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="PaymentAccount")
public class PaymentAccountEntity extends BaseEntity implements PaymentAccount {

	private static final long serialVersionUID = 972826266111430796L;

	@Column(name="name", nullable = false)
	private String name;


	public PaymentAccountEntity() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

   
}
