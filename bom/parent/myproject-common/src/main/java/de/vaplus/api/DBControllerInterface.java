package de.vaplus.api;

import java.io.Serializable;

public interface DBControllerInterface extends Serializable {

	void checkForDBUpdates();

	boolean isDBUpToDate();

//	void resetTrigger() throws Exception;

//	String getDBVersion();


}
