package de.vaplus.api.entity;

import java.io.Serializable;
import java.util.Date;

public interface ChangeHistory extends Serializable{

	long getId();

	User getUser();

	Shop getShop();

	Date getChangeDate();

}
