package de.vaplus.api.interfaces;

import java.io.Serializable;

public interface AuthResult extends Serializable{

	String getLicenseName();

	String getAuthHash();

	String getBaseFilePath();

	long getStorageLimit();

	boolean isAck();

}
