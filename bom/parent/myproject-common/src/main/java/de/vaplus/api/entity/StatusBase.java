package de.vaplus.api.entity;

import java.util.Date;

public interface StatusBase extends Base{

	Date getEffectiveDate();

	void setEffectiveDate(Date effectiveDate);

	Date getExpiryDate();

	void setExpiryDate(Date expiryDate);

	boolean isEnabled();

	void setEnabled(boolean enabled);

	void setStatusBaseEntityValues(StatusBase statusBase);

	boolean isEnabledAndInTime();

	long getDaysTillExpiration();

}
