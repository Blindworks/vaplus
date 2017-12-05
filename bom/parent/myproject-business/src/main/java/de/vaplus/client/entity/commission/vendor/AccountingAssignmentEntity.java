package de.vaplus.client.entity.commission.vendor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.AccountingAssignmentLine;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.LandlineTariff;
import de.vaplus.api.entity.PhoneContract;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.VendorCommissionAccountingItem;
import de.vaplus.api.entity.VodafoneCommissionAccountingItem;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.BaseEntity;
import de.vaplus.client.entity.VendorEntity;

@Entity
@Table(name="AccountingAssignment")

public class AccountingAssignmentEntity extends BaseEntity implements AccountingAssignment{
	
	private static final long serialVersionUID = 2111004683109637874L;
	
	public static final int NEW = 0;
	public static final int PROOF = 1;
	public static final int ACCEPTED = 2;
	public static final int SUSPENDED = 3;

	@OneToOne(mappedBy = "accountingAssignment", cascade = {CascadeType.DETACH,  CascadeType.MERGE})
	private BaseContractEntity baseContract;

	@OneToOne(mappedBy = "cancelationAccountingAssignment", cascade = {CascadeType.DETACH,  CascadeType.MERGE})
	private BaseContractEntity cancelationBaseContract;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "accountingAssignment")
	private List<VendorCommissionAccountingItemEntity> accountingItemList; 

	@Column(name="status", nullable=false)
	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date", nullable=false)
	private Date date;

	@Column(name="commissionMonth", nullable=false)
	private int commissionMonth;

	@Column(name="commissionYear", nullable=false)
	private int commissionYear;

	@ManyToOne
    @JoinColumn(name="vendor_id", nullable = false)
    private VendorEntity vendor;

	@Column(name="correctUser", nullable=false)
	private boolean correctUser;
	
	@Column(name="correctDate", nullable=false)
	private boolean correctDate;

	@Column(name="correctElements", nullable=false)
	private boolean correctElements;

	@Column(name="correctCustomer", nullable=false)
	private boolean correctCustomer;

	@Column(name="correctCommission", nullable=false)
	private boolean correctCommission;

	@Column(name="correctVO", nullable=false)
	private boolean correctVO;

	@Column(name="info", nullable=false)
	private String info = "";
	
	@Transient
	private List<AccountingAssignmentLineEntity> elementLines;



	public AccountingAssignmentEntity(){
		this.status = NEW;
	}

	public AccountingAssignmentEntity(Vendor vendor, int commissionYear, int commissionMonth){
		this.status = NEW;
		this.setVendor((VendorEntity) vendor);
		this.commissionMonth = commissionMonth;
		this.commissionYear = commissionYear;
	}

	@Override
	public int compareTo(AccountingAssignment other) {
		
		long thisTime = this.getSortingTime();
		long otherTime = other.getSortingTime();

		if(thisTime == otherTime)
			return 0;
		if(thisTime > otherTime)
			return 1;
		if(thisTime < otherTime)
			return -1;
		
		return 0;
	}
	
	@Override
	public boolean isCancelation(){
		if(getBaseContract() != null){
			if(getBaseContract().isCanceled())
				return true;
		}
		
		Iterator<? extends VendorCommissionAccountingItem> i = this.getAccountingItemList().iterator();
		VendorCommissionAccountingItem vendorCommissionAccountingItem;
		while(i.hasNext()){
			vendorCommissionAccountingItem = i.next();
			
			if(vendorCommissionAccountingItem.isCancelation())
				return true;
		}
		
		return false;
	}
	
	@Override
	public BaseContract getBaseContract() {
		return baseContract;
	}
	
	@Override
	public BaseContract getContract() {
		if(this.isCancelation())
			return this.getCancelationBaseContract();
		else
			return this.getBaseContract();
	}

	@Override
	public void setBaseContract(BaseContract baseContract) {
		
		baseContract.setAccountingAssignment(this);
		this.baseContract = (BaseContractEntity) baseContract;
		
		Calendar c = Calendar.getInstance();
		c.setTime(this.baseContract.getEffectiveDate());
		
		this.commissionMonth = c.get(Calendar.MONTH);
		this.commissionYear = c.get(Calendar.YEAR);
		
		if(this.getDate() == null)
			this.setDate(this.baseContract.getEffectiveDate());
		
		if(this.getVendor() == null)
			this.setVendor((VendorEntity) this.baseContract.getCachedTariff().getVendor());
	}

	@Override
	public BaseContract getCancelationBaseContract() {
		return cancelationBaseContract;
	}

	@Override
	public void setCancelationBaseContract(BaseContract baseContract) {

		baseContract.setCancelationAccountingAssignment(this);
		this.cancelationBaseContract = (BaseContractEntity) baseContract;
		
//		Calendar c = Calendar.getInstance();
//		c.setTime(this.baseContract.getEffectiveDate());
		
//		this.commissionMonth = c.get(Calendar.MONTH);
//		this.commissionYear = c.get(Calendar.YEAR);
//		
//		if(this.getDate() == null)
//			this.setDate(this.baseContract.getEffectiveDate());
		
		if(this.getVendor() == null)
			this.setVendor((VendorEntity) this.baseContract.getCachedTariff().getVendor());
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}
		
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public boolean isCorrectElements() {
		return correctElements;
	}

	public void setCorrectElements(boolean correctElements) {
		this.correctElements = correctElements;
	}

	@Override
	public boolean isCorrectCustomer() {
		return correctCustomer;
	}

	public void setCorrectCustomer(boolean correctCustomer) {
		this.correctCustomer = correctCustomer;
	}

	@Override
	public boolean isCorrectUser() {
		return correctUser;
	}

	public void setCorrectUser(boolean correctUser) {
		this.correctUser = correctUser;
	}

	@Override
	public boolean isCorrectDate() {
		return correctDate;
	}

	public void setCorrectDate(boolean correctDate) {
		this.correctDate = correctDate;
	}

	@Override
	public boolean isCorrectVO() {
		return correctVO;
	}

	public void setCorrectVO(boolean correctVO) {
		this.correctVO = correctVO;
	}

	@Override
	public boolean isCorrectCommission() {
		return correctCommission;
	}

	public void setCorrectCommission(boolean correctCommission) {
		this.correctCommission = correctCommission;
	}
	
	@Override
	public boolean isCorrect(){
		return isCorrectCommission() && isCorrectDate() && isCorrectElements() && isCorrectUser() && isCorrectCustomer() && isCorrectVO(); 
	}

	@Override
	public long getSortingTime(){
		
		Date d = getDate();
		
		return d != null ? d.getTime() : 0;
	}

//	@Override
//	public List<PhoneContract> getpContractList() {
//		if(pContractList == null)
//			pContractList = new LinkedList<PhoneContract>();
//		return pContractList;
//	}
//
//	@Override
//	public void setpContractList(List<PhoneContract> pContractList) {
//		this.pContractList = pContractList;
//	}

	@Override
	public List<? extends VendorCommissionAccountingItem> getAccountingItemList() {
		if(accountingItemList == null)
			accountingItemList = new LinkedList<VendorCommissionAccountingItemEntity>();
		return accountingItemList;
	}

	@Override
	public void setAccountingItemList(List<? extends VendorCommissionAccountingItem> accountingItemList) {
		this.accountingItemList = (List<VendorCommissionAccountingItemEntity>) accountingItemList;
	}
	
		
	@Override
	public void addAccountingItem(VendorCommissionAccountingItem accountingItem) {
		accountingItem.setAccountingAssignment(this);
		((List<VendorCommissionAccountingItem>) this.getAccountingItemList()).add(accountingItem);
		VodafoneCommissionAccountingItem vItem;

		if(this.getDate() == null){
			if(accountingItem instanceof VodafoneCommissionAccountingItem){
				vItem = (VodafoneCommissionAccountingItem) accountingItem;
				this.setDate(accountingItem.getDate());
			}
		}
	}
	
	@Override
	public BigDecimal getSum(){
		BigDecimal sum = new BigDecimal(0);
		
		if(baseContract != null && baseContract.getFinalCommission() != null)
			sum = sum.subtract(baseContract.getFinalCommission().getCommission());
		
		
		Iterator<? extends VendorCommissionAccountingItem> i2 = getAccountingItemList().iterator();
		VendorCommissionAccountingItem accountingItem;
		while(i2.hasNext()){
			accountingItem = i2.next();
			
			BigDecimal itemWorth = new BigDecimal(0);

			if(accountingItem instanceof VodafoneCommissionAccountingItem){
				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) accountingItem;
				
				itemWorth = vItem.getCommissionWorth();
				
			}

			sum = sum.add(itemWorth);
		}
		
		return sum;
	}
	
	public boolean isCorrectCommission_OLD(){
		return getSum().compareTo(new BigDecimal(0)) == 0;
	}
	
	@Override
	public String getInfo() {
		return info;
	}
	
	@Override
	public String getHtmlInfo() {
		return info.replace("\n", "<br/>");
	}

	@Override
	public void clearInfo() {
		setInfo("");
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void addInfoLine(String info) {
		if(this.info != null && this.info.length() > 0)
			this.info += "\n";
		this.info += info;
	}

//	@Override
//	public Date getDate(){
//		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
//		
//		if(baseContract != null){
//			return baseContract.getEffectiveDate();
//		}else if(getAccountingItemList() != null && getAccountingItemList().size() > 0){
//			
//			if(getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
//				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) getAccountingItemList().get(0);
//				
//				try {
//					if(vItem.getDatumAktivierung().trim().length() > 0){
//
//						Date d = formatter.parse(vItem.getDatumAktivierung());
//						
//						return d;
//					}
//					
//				} catch (ParseException e) {
//				}
//			}
//			
//		}
//		
//		return null;
//	}
	
	@Override
	public String getCustomerName(){
		if(baseContract != null){
			return baseContract.getCustomer().getName();
		}else if(getAccountingItemList().size() > 0){
			
			if(getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) getAccountingItemList().get(0);
				
				return vItem.getKundenname();
			}
			
		}
		
		return null;
	}
	
	@Override
	public long getContractId(){
		if(baseContract != null){
			return baseContract.getId();
		}
		return 0;
	}
	
	@Override
	public String getCustomerId(){
		if(baseContract != null){
			return baseContract.getExternalCustomer().getCustomerId();
		}else if(getAccountingItemList().size() > 0){
			
			if(getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) getAccountingItemList().get(0);
				
				return vItem.getKundennummer();
			}
			
		}
		
		return null;
	}
	
	@Override
	public String getUserName(){
		if(baseContract != null){
			return baseContract.getUser().getName();
		}else if(getAccountingItemList().size() > 0){
			
			if(getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) getAccountingItemList().get(0);
				
				return vItem.getVerkaeufer();
			}
			
		}
		return null;
	}
	
	@Override
	public String getVo(){
		if(baseContract != null){
			return baseContract.getVo().getNumber();
		}else if(getAccountingItemList().size() > 0){
			
			if(getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) getAccountingItemList().get(0);
				
				return vItem.getVoIDAbrechnung();
			}
			
		}
		return null;
	}
	
	public VendorEntity getVendor() {
		return vendor;
	}

	public void setVendor(VendorEntity vendor) {
		this.vendor = vendor;
	}

	public int getCommissionMonth() {
		return commissionMonth;
	}

	@Override
	public void setCommissionMonth(int commissionMonth) {
		this.commissionMonth = commissionMonth;
	}

	public int getCommissionYear() {
		return commissionYear;
	}

	@Override
	public void setCommissionYear(int commissionYear) {
		this.commissionYear = commissionYear;
	}

	@Override
	public String getCallingNumber(){
		if(baseContract != null){
			
			if(baseContract.getTariff() instanceof LandlineTariff){
				return baseContract.getExternalCustomer().getCustomerId();
			}
			if(baseContract instanceof PhoneContract){
				return ((PhoneContract)baseContract).getCallingNumber();
			}
			return null;
			
		}else if(getAccountingItemList().size() > 0){
			
			if(getAccountingItemList().get(0) instanceof VodafoneCommissionAccountingItem){
				VodafoneCommissionAccountingItem vItem = (VodafoneCommissionAccountingItem) getAccountingItemList().get(0);
				
				return vItem.getRufnummerBarcodeWebOrderID();
			}
			
		}
		
		return null;
	}

	@Override
	public List<? extends AccountingAssignmentLine> getElementLines() {
		AccountingAssignmentLineEntity line;
		Iterator<? extends ContractItem> i;
		ContractItem ci;
//		if(elementLines == null){
			
			
			line = new AccountingAssignmentLineEntity();
			if(this.getContract() != null){
				elementLines = new LinkedList<AccountingAssignmentLineEntity>();
				line.addLeftLine(this.getContract().getCachedTariff().getProductName());
				line.setLeftWorth(this.getContract().getFinalCommission().getCommission());
				elementLines.add(line);
				
				
				
				i = this.getContract().getCachedTariffOptionList().iterator();
				while(i.hasNext()){
					ci = i.next();

					elementLines = new LinkedList<AccountingAssignmentLineEntity>();
					line.addLeftLine(ci.getProductName());
					elementLines.add(line);
					
				}
				
				if(this.getContract().isExtensionOfTerm()){

					elementLines = new LinkedList<AccountingAssignmentLineEntity>();
					line.addLeftLine("Vertrags VL");
					elementLines.add(line);
				}
				
			}
			
			Iterator<? extends VendorCommissionAccountingItem> i2 = this.getAccountingItemList().iterator();
			VendorCommissionAccountingItem vItem;
			VodafoneCommissionAccountingItem vodafoneItem;
			while(i2.hasNext()){
				vItem = i2.next();
				
				if(vItem instanceof VodafoneCommissionAccountingItem){
					vodafoneItem = (VodafoneCommissionAccountingItem) vItem;

					elementLines = new LinkedList<AccountingAssignmentLineEntity>();
					line.getCommissionAccountingList().add(vItem);
					elementLines.add(line);
				}
			}
			
			
			
//		}
		return elementLines;
	}

	public void setElementLines(List<AccountingAssignmentLineEntity> elementLines) {
		this.elementLines = elementLines;
	}

}
