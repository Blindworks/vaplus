package de.vaplus.api.entity;

public interface UserPermission extends Permission{

	User getUser();

	void setUser(User user);


}
