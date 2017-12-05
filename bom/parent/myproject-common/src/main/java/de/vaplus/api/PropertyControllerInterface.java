package de.vaplus.api;

import java.io.Serializable;
import java.math.BigDecimal;

import de.vaplus.api.entity.User;

public interface PropertyControllerInterface extends Serializable {

	String getLicenseName();

	double getDoubleProperty(String name, double defaultValue);

	BigDecimal getDecimalProperty(String name, BigDecimal defaultValue);

	long getLongProperty(String name, long defaultValue);

	String getStringProperty(String name, String defaultValue);



	void updateDoubleProperty(String name, double value);

	void updateDecimalProperty(String name, BigDecimal value);

	void updateLongProperty(String name, long value);

	void updateStringProperty(String name, String value);

	long getLongUserProperty(User user, String name, long defaultValue);

	String getStringUserProperty(User user, String name, String defaultValue);

	BigDecimal getDecimalUserProperty(User user, String name,
			BigDecimal defaultValue);

	double getDoubleUserProperty(User user, String name, double defaultValue);

	void updateDoubleUserProperty(User user, String name, double value);

	void updateDecimalUserProperty(User user, String name, BigDecimal value);

	void updateLongUserProperty(User user, String name, long value);

	void updateStringUserProperty(User user, String name, String value);

	boolean getBooleanUserProperty(User user, String name, boolean defaultValue);

	void updateBooleanUserProperty(User user, String name, boolean value);

	String getAppVerson();

	String getDBVersion();

	String getBaseFilePath();

	long getStorageLimit();

}
