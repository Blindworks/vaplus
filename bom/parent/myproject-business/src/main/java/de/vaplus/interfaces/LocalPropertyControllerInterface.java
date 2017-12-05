package de.vaplus.interfaces;

public interface LocalPropertyControllerInterface {

	String getOldLicenseName();
	
	String getAppVerson();

	String getDBVersion();

	void setLicenseName(String licenseName);

	void setBaseFilePath(String baseFilePath);

	void setStorageLimit(long storageLimit);
}
