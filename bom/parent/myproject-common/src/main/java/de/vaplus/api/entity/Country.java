package de.vaplus.api.entity;

public interface Country extends Base{

	String getShortName();

	void setShortName(String shortName);

	String getLongName();

	void setLongName(String longName);

	String getNationality();

	void setNationality(String nationality);

	String getCountry();

	void setCountry(String country);

	String getIso31662();

	void setIso31662(String iso31662);

	String getIso31663();

	void setIso31663(String iso31663);

	String getBevCode();

	void setBevCode(String bevCode);

	boolean isDefaultSelection();

	void setDefaultSelection(boolean defaultSelection);

	String getName();

	void setName(String name);

}
