package de.vaplus.api.entity;

public interface ManualCommission extends CommissionActivity{

	User getCreator();

	void setCreator(User creator);

}
