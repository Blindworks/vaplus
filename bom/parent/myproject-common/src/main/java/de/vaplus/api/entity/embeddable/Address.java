package de.vaplus.api.entity.embeddable;

import java.io.Serializable;

public interface Address extends Serializable{

	void setCountry(String country);

	String getCountry();

	void setCity(String city);

	String getCity();

	void setZip(String zip);

	String getZip();

	String getStreet();

	void setStreet(String street);

	String getStreetNumber();

	void setStreetNumber(String streetNumber);

	String getAddressline();

	void setAddressline(String addressline);

}
