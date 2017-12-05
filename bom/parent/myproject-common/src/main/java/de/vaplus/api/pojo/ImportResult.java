package de.vaplus.api.pojo;

import java.util.Date;

public class ImportResult {
	private int processedDataSets = 0;
	private int importetCustomer = 0;
	private int importetContracts = 0;
	private Date startDate;
	private Date endDate;
	
	
	public int getProcessedDataSets() {
		return processedDataSets;
	}
	public void setProcessedDataSets(int processedDataSets) {
		this.processedDataSets = processedDataSets;
	}
	public int getImportetCustomer() {
		return importetCustomer;
	}
	public void setImportetCustomer(int importetCustomer) {
		this.importetCustomer = importetCustomer;
	}
	public int getImportetContracts() {
		return importetContracts;
	}
	public void setImportetContracts(int importetContracts) {
		this.importetContracts = importetContracts;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void start(){
		this.startDate = new Date();
	}
	
	public void end(){
		this.endDate = new Date();
	}
}
