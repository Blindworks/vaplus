package de.vaplus.client.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="FEPluginBean")
@SessionScoped
public class FEPluginBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3617333883476727083L;

	private boolean chosen;
	private boolean morris;
	private boolean flot;
	private boolean datepicker;
	private boolean dataTables;
	private boolean dropzone;
	private boolean fullCalendar;
	private boolean raphael;
	private boolean metisMenu;
	private boolean slimScroll;
	private boolean jasny;
	private boolean iCheck;
	private boolean peity;
	private boolean chartJs;
	private boolean validation;
	private boolean colorpicker;
	
	public FEPluginBean(){
		metisMenu = true;
		slimScroll = true;
	}
	
	public void includeChosen(){
		chosen = true;
	}
	
	public void includeMorris(){
		morris = true;
	}
	
	public void includeFlot(){
		flot = true;
	}
	
	public void includeDatepicker(){
		datepicker = true;
	}
	
	public void includeDataTables(){
		dataTables = true;
	}
	
	public void includeDropzone(){
		dropzone = true;
	}
	
	public void includeFullCalendar(){
		fullCalendar = true;
	}
	
	public void includeRaphael(){
		raphael = true;
	}
	
	public void includeMetisMenu(){
		metisMenu = true;
	}
	
	public void includeSlimScroll(){
		slimScroll = true;
	}
	
	public void includeJasny(){
		jasny = true;
	}
	
	public void includeICheck(){
		iCheck = true;
	}
	
	public void includePeity(){
		peity = true;
	}
	
	public void includeChartJs(){
		chartJs = true;
	}
	
	public void includeValidation(){
		validation = true;
	}
	
	public void includeColorpicker(){
		colorpicker = true;
	}
	
	
	
	
	public boolean isChosen() {
		return chosen;
	}

	public boolean isMorris() {
		return morris;
	}

	public boolean isFlot() {
		return flot;
	}

	public boolean isDatepicker() {
		return datepicker;
	}

	public boolean isDataTables() {
		return dataTables;
	}

	public boolean isDropzone() {
		return dropzone;
	}

	public boolean isFullCalendar() {
		return fullCalendar;
	}

	public boolean isRaphael() {
		return raphael;
	}

	public boolean isMetisMenu() {
		return metisMenu;
	}

	public boolean isSlimScroll() {
		return slimScroll;
	}

	public boolean isJasny() {
		return jasny;
	}

	public boolean isiCheck() {
		return iCheck;
	}

	public boolean isPeity() {
		return peity;
	}

	public boolean isChartJs() {
		return chartJs;
	}

	public boolean isValidation() {
		return validation;
	}

	public boolean isColorpicker() {
		return colorpicker;
	}
	
}