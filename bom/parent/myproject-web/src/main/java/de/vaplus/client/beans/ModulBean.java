package de.vaplus.client.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.ModulControllerInterface;


@ManagedBean(name="modulBean")
@SessionScoped
public class ModulBean implements Serializable{
	
	private static final long serialVersionUID = -1836400320897162195L;

	@EJB
	private ModulControllerInterface modulController;

	public boolean isTaskModulLicensed() {
		return true;
	}

	public boolean isVvlModulLicensed() {
		return modulController.isVvlModulLicensed();
	}

	public boolean isMessageModulLicensed() {
		return modulController.isMessageModulLicensed();
	}

	public boolean isCalendarModulLicensed() {
		return modulController.isCalendarModulLicensed();
	}

	public boolean isDevelopmentView() {
		return modulController.isDevelopmentView();
	}
	
}
