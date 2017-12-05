package de.vaplus.api.entity;

import de.vaplus.api.entity.embeddable.Address;

public interface Stock extends StatusBase{

	String getName();

	void setName(String name);

	Address getAddress();

	void setAddress(Address address);


}
