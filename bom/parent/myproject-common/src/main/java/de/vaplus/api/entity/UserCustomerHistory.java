package de.vaplus.api.entity;

import java.io.Serializable;
import java.util.Date;

public interface UserCustomerHistory extends Serializable{

	Customer getCustomer();

	Date getLastOpened();

	void refreshLastOpened();

}
