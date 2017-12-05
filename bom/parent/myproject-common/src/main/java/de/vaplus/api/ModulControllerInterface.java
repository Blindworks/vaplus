package de.vaplus.api;

import java.io.Serializable;

public interface ModulControllerInterface extends Serializable{

	boolean isTaskModulLicensed();

	boolean isVvlModulLicensed();

	boolean isMessageModulLicensed();

	boolean isCalendarModulLicensed();

	boolean isDevelopmentView();

	boolean isFileModulLicensed();


}
