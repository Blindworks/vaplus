package de.vaplus;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.vaplus.api.DBControllerInterface;
import de.vaplus.api.ModulControllerInterface;
import de.vaplus.api.PropertyControllerInterface;


@Stateless
public class ModulController implements ModulControllerInterface {

	private static final long serialVersionUID = -8340271804357236113L;

	@EJB
    private PropertyControllerInterface propertyController;
	
	@Resource(name = "developmentView")
    private boolean developmentView;
	
	@Resource(name = "taskModulLicensed")
    private boolean taskModulLicensed;
	
	@Resource(name = "vvlModulLicensed")
    private boolean vvlModulLicensed;
	
	@Resource(name = "messageModulLicensed")
    private boolean messageModulLicensed;
	
	@Resource(name = "calendarModulLicensed")
    private boolean calendarModulLicensed;

	@Override
	public boolean isDevelopmentView() {
		return developmentView;
	}

	@Override
	public boolean isTaskModulLicensed() {
		return taskModulLicensed;
	}

	@Override
	public boolean isVvlModulLicensed() {
		return true;
//		return vvlModulLicensed;
	}

	@Override
	public boolean isMessageModulLicensed() {
		return messageModulLicensed;
	}

	@Override
	public boolean isCalendarModulLicensed() {
		return calendarModulLicensed;
	}

	@Override
	public boolean isFileModulLicensed() {
		
		if(propertyController.getStorageLimit() == 0)
			return false;
		
		if(propertyController.getBaseFilePath() == null || propertyController.getBaseFilePath().length() == 0)
			return false;
		
		else
			return true;
	}

}
