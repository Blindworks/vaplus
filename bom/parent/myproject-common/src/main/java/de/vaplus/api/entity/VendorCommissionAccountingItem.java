package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.Date;

public interface VendorCommissionAccountingItem extends Base{

	public BigDecimal getCommissionWorth();
	
	public String getTitle();

	VendorCommissionAccountingFile getFile();

	void setFile(VendorCommissionAccountingFile file);

	AccountingAssignment getAccountingAssignment();

	void setAccountingAssignment(AccountingAssignment accountingAssignment);

	Date getDate();

	boolean isCancelation();

}
