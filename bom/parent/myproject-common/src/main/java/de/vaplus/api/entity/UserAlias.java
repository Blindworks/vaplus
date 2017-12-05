package de.vaplus.api.entity;

import java.io.Serializable;

public interface UserAlias extends Serializable{

	User getUser();

	void setUser(User user);

	String getAlias();

}
