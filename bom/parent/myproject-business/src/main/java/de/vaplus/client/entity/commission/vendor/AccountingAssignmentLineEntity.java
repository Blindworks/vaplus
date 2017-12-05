package de.vaplus.client.entity.commission.vendor;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import de.vaplus.api.entity.AccountingAssignmentLine;
import de.vaplus.api.entity.VendorCommissionAccountingItem;

public class AccountingAssignmentLineEntity implements AccountingAssignmentLine {
	
	private static String DIVIDER = "###";

	private String left = "";
	
	private List<VendorCommissionAccountingItem> commissionAccountingList;
	
//	private String right = "";
	
	private BigDecimal leftWorth;
	
	private BigDecimal rightWorth;
	
	public void setLeft(String left) {
		this.left = left;
	}

	public String getLeft() {
		return left;
	}
	
	public void addLeftLine(String leftLine){
		if(left != null && left.length() > 0)
			left += AccountingAssignmentLineEntity.DIVIDER;
		
		left += leftLine;
	}
	
//	public void setRight(String right) {
//		this.right = right;
//	}
//	
//	public String getRight() {
//		return right;
//	}
//	
//	public void addRightLine(String rightLine){
//		if(right != null && right.length() > 0)
//			right += AccountingAssignmentLineEntity.DIVIDER;
//		
//		right += rightLine;
//	}

	
	@Override
	public String[] getLeftLines() {
		if(left == null || left.length() == 0)
			return null;
		
		return left.split(AccountingAssignmentLineEntity.DIVIDER);
	}
	
//	@Override
//	public String[] getRightLines() {
//		if(right == null || right.length() == 0)
//			return null;
//		
//		return right.split(AccountingAssignmentLineEntity.DIVIDER);
//	}

	@Override
	public BigDecimal getLeftWorth() {
		return leftWorth;
	}

	public void setLeftWorth(BigDecimal leftWorth) {
		this.leftWorth = leftWorth;
	}

//	@Override
//	public BigDecimal getRightWorth() {
//		return rightWorth;
//	}

	public void setRightWorth(BigDecimal rightWorth) {
		this.rightWorth = rightWorth;
	}

	@Override
	public List<VendorCommissionAccountingItem> getCommissionAccountingList() {
		if(commissionAccountingList == null)
			commissionAccountingList = new LinkedList<VendorCommissionAccountingItem>();
		return commissionAccountingList;
	}

	public void setCommissionAccountingList(
			List<VendorCommissionAccountingItem> commissionAccountingList) {
		this.commissionAccountingList = commissionAccountingList;
	}
}
