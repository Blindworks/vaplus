package de.vaplus.client.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import de.vaplus.api.entity.CellPhoneTariff;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="CellPhoneTariff")
public class CellPhoneTariffEntity extends TariffEntity implements CellPhoneTariff {

	public CellPhoneTariffEntity() {
		super();
	}

	@Override
	public boolean askForOrderNumber() {
		return false;
	}

}
