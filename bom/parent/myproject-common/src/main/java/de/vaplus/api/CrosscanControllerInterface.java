package de.vaplus.api;

import java.io.Serializable;

import de.vaplus.api.entity.Shop;

public interface CrosscanControllerInterface extends Serializable {

	void updateCrosscanData();

	long getCrosscanDataForMonth(Shop shop, int year, int month);

}
