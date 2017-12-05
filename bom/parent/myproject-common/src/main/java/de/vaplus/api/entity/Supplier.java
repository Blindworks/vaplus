package de.vaplus.api.entity;

import de.vaplus.api.entity.embeddable.Address;

public interface Supplier extends StatusBase{

	String getName();

	void setName(String name);

	Address getAddress();

	void setAddress(Address address);
}
