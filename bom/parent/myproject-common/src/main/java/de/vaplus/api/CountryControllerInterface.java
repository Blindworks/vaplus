package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.Country;

public interface CountryControllerInterface extends Serializable {

	List<? extends Country> getCountryList();

	Country getCountryById(long id);

}
