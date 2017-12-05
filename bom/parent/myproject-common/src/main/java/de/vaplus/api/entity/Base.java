package de.vaplus.api.entity;

import java.io.Serializable;
import java.util.Date;

public interface Base extends Serializable{

	long getId();

	Date getCreationDate();

	Date getUpdateDate();

	boolean isDeleted();

	void setDeleted();

}
