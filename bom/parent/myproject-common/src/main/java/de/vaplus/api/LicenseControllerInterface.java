package de.vaplus.api;

import java.io.Serializable;

public interface LicenseControllerInterface extends Serializable{

	void checkLicense(String serverName);

}
