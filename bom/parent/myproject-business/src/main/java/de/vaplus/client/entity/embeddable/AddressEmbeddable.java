package de.vaplus.client.entity.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.vaplus.api.entity.embeddable.Address;

@Embeddable
public class AddressEmbeddable implements Address{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7835326685477755037L;

	@Column(name="street")
	private String street;

	@Column(name="streetNumber")
	private String streetNumber;

	@Column(name="addressline")
	private String addressline;

	@Column(name="zip")
	private String zip;

	@Column(name="city")
	private String city;

	@Column(name="country")
	private String country;

	@Override
	public String getStreet() {
		return street;
	}

	@Override
	public void setStreet(String street) {
		if(street != null)
			this.street = street.trim();
		else
			this.street = null;
	}

	@Override
	public String getStreetNumber() {
		return streetNumber;
	}

	@Override
	public void setStreetNumber(String streetNumber) {
		if(streetNumber != null)
			this.streetNumber = streetNumber.trim();
		else
			this.streetNumber = null;
	}

	@Override
	public String getZip() {
		return zip;
	}

	@Override
	public void setZip(String zip) {
		if(zip != null)
			this.zip = zip.trim();
		else
			this.zip = null;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		if(city != null)
			this.city = city.trim();
		else
			this.city = null;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public void setCountry(String country) {
		if(country != null)
			this.country = country.trim().toUpperCase();
		else
			this.country = null;
	}

	@Override
	public String getAddressline() {
		return addressline;
	}

	@Override
	public void setAddressline(String addressline) {
		if(addressline != null)
			this.addressline = addressline.trim();
		else
			this.addressline = null;
	}
	
}
