package de.vaplus.api.entity;

public interface Activity extends StatusBase{

	Customer getCustomer();

	void setCustomer(Customer customer);

	User getUser();

	void setUser(User user);

	Shop getShop();

	void setShop(Shop shop);

	boolean isHideInTimeline();

	void setHideInTimeline(boolean hideInTimeline);

	String getNote();

	void setNote(String note);

}
