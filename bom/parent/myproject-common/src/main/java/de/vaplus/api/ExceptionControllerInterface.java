package de.vaplus.api;

import java.io.Serializable;

public interface ExceptionControllerInterface extends Serializable{

	void logException(Throwable t, String info);

	void logException(Throwable t);

}
