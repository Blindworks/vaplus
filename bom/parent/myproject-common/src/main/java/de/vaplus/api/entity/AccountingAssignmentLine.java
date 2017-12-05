package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

public interface AccountingAssignmentLine {

	String[] getLeftLines();

//	String[] getRightLines();

	BigDecimal getLeftWorth();

//	BigDecimal getRightWorth();

	List<VendorCommissionAccountingItem> getCommissionAccountingList();

}
