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
import de.vaplus.api.entity.Vendor;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="Country")
@NamedQueries({
    @NamedQuery(
        name = "Country.getCountries",
        query = "SELECT c FROM CountryEntity c Order by c.name ASC",
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    )
})
public class CountryEntity extends BaseEntity implements Country {

	private static final long serialVersionUID = -1993754846553592826L;

	@Column(name="name", nullable = false)
	private String name;

	@Column(name="shortName", nullable = false)
	private String shortName;
	
	@Column(name="longName", nullable = false)
	private String longName;
	
	@Column(name="nationality", nullable = false)
	private String nationality;
	
	@Column(name="country", nullable = false)
	private String country;
	
	@Column(name="iso31662", nullable = false)
	private String iso31662;
	
	@Column(name="iso31663", nullable = false)
	private String iso31663;
	
	@Column(name="bevCode", nullable = false)
	private String bevCode;
	
	@Column(name="defaultSelection", nullable = false)
	private boolean defaultSelection;

	public CountryEntity() {
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
	public String getShortName() {
		return shortName;
	}

	@Override
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String getLongName() {
		return longName;
	}

	@Override
	public void setLongName(String longName) {
		this.longName = longName;
	}

	@Override
	public String getNationality() {
		return nationality;
	}

	@Override
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getIso31662() {
		return iso31662;
	}

	@Override
	public void setIso31662(String iso31662) {
		this.iso31662 = iso31662;
	}

	@Override
	public String getIso31663() {
		return iso31663;
	}

	@Override
	public void setIso31663(String iso31663) {
		this.iso31663 = iso31663;
	}

	@Override
	public String getBevCode() {
		return bevCode;
	}

	@Override
	public void setBevCode(String bevCode) {
		this.bevCode = bevCode;
	}

	@Override
	public boolean isDefaultSelection() {
		return defaultSelection;
	}

	@Override
	public void setDefaultSelection(boolean defaultSelection) {
		this.defaultSelection = defaultSelection;
	}

   
}
