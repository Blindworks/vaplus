package de.vaplus.client.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import de.vaplus.api.entity.LandlineTariff;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="LandlineTariff")
public class LandlineTariffEntity extends TariffEntity implements LandlineTariff {

	public LandlineTariffEntity() {
		super();
	}

	@Override
	public boolean askForOrderNumber() {
		return true;
	}

}
