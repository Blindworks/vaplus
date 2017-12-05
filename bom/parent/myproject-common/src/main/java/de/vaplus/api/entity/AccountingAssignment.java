package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AccountingAssignment extends Base, Comparable<AccountingAssignment> {

	public int compareTo(AccountingAssignment other);
	
	public long getSortingTime();

	public boolean isCorrectElements();
	
	public boolean isCorrectCustomer();

	public boolean isCorrectUser();

	public boolean isCorrectDate();

	public boolean isCorrectCommission();

	public boolean isCorrect();

//	public List<PhoneContract> getpContractList();

//	public void setpContractList(List<PhoneContract> pContractList);

	public List<? extends VendorCommissionAccountingItem> getAccountingItemList();

	public void setAccountingItemList(List<? extends VendorCommissionAccountingItem> accountingItemList);
	
	public BigDecimal getSum();

	public Date getDate();
	
	public String getCustomerName();
	
	public String getCustomerId();
	
	public String getUserName();
	
	public String getVo();
	
	public String getCallingNumber();
	
	public String getInfo();

	List<? extends AccountingAssignmentLine> getElementLines();

	String getHtmlInfo();

	BaseContract getBaseContract();

	void setBaseContract(BaseContract baseContract);

	int getStatus();

	void setStatus(int status);

	void addAccountingItem(VendorCommissionAccountingItem accountingItem);

	long getContractId();

	void clearInfo();

	void setCommissionMonth(int commissionMonth);

	void setCommissionYear(int commissionYear);

	boolean isCorrectVO();

	boolean isCancelation();

	BaseContract getCancelationBaseContract();

	void setCancelationBaseContract(BaseContract baseContract);

	BaseContract getContract();

}
