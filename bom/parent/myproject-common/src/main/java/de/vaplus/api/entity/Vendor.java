package de.vaplus.api.entity;

public interface Vendor extends StatusBase{

	String getName();

	void setName(String name);

	boolean isDefaultSelection();

	void setDefaultSelection(boolean defaultSelection);
}
