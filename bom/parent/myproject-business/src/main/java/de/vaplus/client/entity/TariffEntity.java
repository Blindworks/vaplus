package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.vaplus.api.entity.Tariff;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Tariff")
public class TariffEntity extends BaseProductEntity implements Tariff {
	
	private static final long serialVersionUID = -6485681816118632426L;

	@Column(name="minimumTermOfContract", nullable = false)
	private int minimumTermOfContract;

	@Column(name="extensionOfTheTerm", nullable = false)
	private boolean extensionOfTheTerm;

	public TariffEntity() {
		super();
	}

	@Override
	public int getMinimumTermOfContract() {
		return minimumTermOfContract;
	}

	@Override
	public void setMinimumTermOfContract(int minimumTermOfContract) {
		this.minimumTermOfContract = minimumTermOfContract;
	}

	@Override
	public boolean isExtensionOfTheTerm() {
		return extensionOfTheTerm;
	}

	@Override
	public void setExtensionOfTheTerm(boolean extensionOfTheTerm) {
		this.extensionOfTheTerm = extensionOfTheTerm;
	}

	@Override
	public boolean askForOrderNumber() {
		return false;
	}

}
