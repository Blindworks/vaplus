package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseContractCancellation;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.ContractRetailItem;
import de.vaplus.api.entity.ContractStatus;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;

/**
 * Entity implementation class for Entity: SecureLink
 *
 */
@Entity
@Table(name="BaseContract")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseContractEntity extends ActivityEntity implements BaseContract, SearchResult {

	private static final long serialVersionUID = -3452737833800748319L;

	public static final int COMMISSION_STATUS_NEW = 0;
	public static final int COMMISSION_STATUS_PROOFED = 1;
	public static final int COMMISSION_STATUS_ACCEPTED = 2;
	public static final int COMMISSION_STATUS_SUSPENDED = 3;
	
	@Column(name="info")
	private String info;

	@Column(name="extensionOfTheTermPermission", nullable = false)
	private boolean extensionOfTheTermPermission;

	@Column(name="leadTime", nullable = false)
	private int leadTime;
	
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="points",column=@Column(name="revenueStepPoints", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="commission",column=@Column(name="revenueStepCommission", precision = 12, scale = 4, nullable = false)),
	    @AttributeOverride(name="price",column=@Column(name="revenueStepPrice", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="vat",column=@Column(name="revenueStepVat", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="tax",column=@Column(name="revenueStepTax", precision = 5, scale = 2, nullable = false))
	  })
	private CommissionableEmbeddable revenueStepCommission;
	
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="points",column=@Column(name="finalPoints", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="commission",column=@Column(name="finalCommission", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="price",column=@Column(name="finalPrice", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="vat",column=@Column(name="finalVat", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="tax",column=@Column(name="finalTax", precision = 5, scale = 2, nullable = false))
	  })
	private CommissionableEmbeddable finalCommission;
	
	@Column(name="generatedRevenue", precision = 10, scale = 4, nullable = false)
	private BigDecimal generatedRevenue;


	@ManyToOne
    @JoinColumn(name="tariff_id", nullable = false)
    private ContractItemEntity cachedTariff;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="externalCustomer_id", nullable = true)
    private ExternalCustomerEntity externalCustomer;

	@Column(name="orderNumber", nullable = true)
	private String orderNumber;

	@ManyToMany(fetch = FetchType.LAZY)
    private List<ContractItemEntity> cachedTariffOptionList;

	@ManyToOne
    @JoinColumn(name="vo_id", nullable = false)
    private VOEntity vo;
	
	@OneToMany(mappedBy="baseContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContractRetailItemEntity> contractRetailItemList;

	@ManyToOne
    @JoinColumn(name="extendedContract_id")
    private BaseContractEntity extendedContract;
	
    @Column(name="extensionOfTheTerm", nullable = false)
    private boolean extensionOfTerm;

	@Column(name="debidCreditChange", nullable = false)
	private boolean debidCreditChange;
	
    @Column(name="subsidyBudgetary", nullable = false)
    private BigDecimal subsidyBudgetary;

	@OneToOne
    @JoinColumn(name="subsidyOrder_id", nullable = true)
    private OrderEntity subsidyOrder;

	@OneToOne
    @JoinColumn(name="subsidyInvoice_id", nullable = true)
    private InvoiceEntity subsidyInvoice;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="accountingAssignment_id", referencedColumnName = "ID", nullable = true)
    private AccountingAssignmentEntity accountingAssignment;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="cancelationAccountingAssignment_id", referencedColumnName = "ID", nullable = true)
    private AccountingAssignmentEntity cancelationAccountingAssignment;
	
	@OneToOne(mappedBy = "contract", fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private BaseContractCancellationEntity contractCancellation;

	@ManyToOne
    @JoinColumn(name="status_id", nullable = false)
    private ContractStatusEntity status;

	public BaseContractEntity() {
		super();
		subsidyBudgetary = new BigDecimal(0);
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public VO getVo() {
		return vo;
	}

	@Override
	public void setVo(VO vo) {
		this.vo = (VOEntity) vo;
		this.setCachedTariff(null);
		this.setCachedTariffOptionList(null);
	}

	@Override
	public List<? extends ContractRetailItem> getContractRetailItemList() {
		if(contractRetailItemList == null)
			contractRetailItemList = new ArrayList<ContractRetailItemEntity>();
		return contractRetailItemList;
	}

	@Override
	public void setContractRetailItemList(
			List<? extends ContractRetailItem> contractRetailItemList) {
		this.contractRetailItemList = (List<ContractRetailItemEntity>) contractRetailItemList;
	}

	@Override
	public BaseContract getExtendedContract() {
		return extendedContract;
	}

	@Override
	public void setExtendedContract(BaseContract extendedContract) {
		this.extendedContract = (BaseContractEntity) extendedContract;
	}

	@Override
	public Commissionable getRevenueStepCommission() {
		if(revenueStepCommission == null)
			revenueStepCommission = new CommissionableEmbeddable();
		return revenueStepCommission;
	}

	@Override
	public void setRevenueStepCommission(
			Commissionable revenueStepCommission) {
		this.revenueStepCommission = (CommissionableEmbeddable) revenueStepCommission;
	}

	@Override
	public Commissionable getFinalCommission() {
		if(finalCommission == null)
			finalCommission = new CommissionableEmbeddable();
		return finalCommission;
	}

	@Override
	public void setFinalCommission(Commissionable finalCommission) {
		this.finalCommission = (CommissionableEmbeddable) finalCommission;
	}

	@Override
	public BigDecimal getGeneratedRevenue() {
		if(generatedRevenue == null)
			generatedRevenue = new BigDecimal(0);
		return generatedRevenue;
	}

	@Override
	public void setGeneratedRevenue(BigDecimal generatedRevenue) {
		this.generatedRevenue = generatedRevenue;
	}

	@Override
	public ContractItem getCachedTariff() {
		return cachedTariff;
	}

	@Override
	public void setCachedTariff(ContractItem cachedTariff) {
		this.cachedTariff = (ContractItemEntity) cachedTariff;
	}

	@Override
	public List<? extends ContractItem> getCachedTariffOptionList() {
		if(cachedTariffOptionList == null)
			cachedTariffOptionList = new ArrayList<ContractItemEntity>();
		return cachedTariffOptionList;
	}

	@Override
	public void setCachedTariffOptionList(List<? extends ContractItem> cachedTariffOptionList) {
		this.cachedTariffOptionList = (List<ContractItemEntity>) cachedTariffOptionList;
	}

	
	@Override
	public int getLeadTime() {
		return leadTime;
	}

	@Override
	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}

	@Override
	public ExternalCustomer getExternalCustomer() {
		if(externalCustomer == null)
			newExternalCustomer();
		return externalCustomer;
	}
	
	@Override
	public void newExternalCustomer(){
		externalCustomer = new ExternalCustomerEntity();
	}

	@Override
	public void setExternalCustomer(ExternalCustomer externalCustomer) {
		this.externalCustomer = (ExternalCustomerEntity) externalCustomer;
	}

	@Override
	public String getOrderNumber() {
		return orderNumber;
	}

	@Override
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public void setEffectiveDate(Date effectiveDate){
		super.setEffectiveDate(effectiveDate);
	}
	
	@Override
	public boolean isExtensionOfTheTermPermission() {
		return extensionOfTheTermPermission;
	}

	@Override
	public void setExtensionOfTheTermPermission(boolean extensionOfTheTermPermission) {
		this.extensionOfTheTermPermission = extensionOfTheTermPermission;
	}

	@Override
	public boolean isExtensionOfTerm() {
		return extensionOfTerm;
	}

	@Override
	public void setExtensionOfTerm(boolean extensionOfTerm) {
		this.extensionOfTerm = extensionOfTerm;
	}

	@Transient
	@Override
	public void setTariff(Tariff tariff) {
		
		if(tariff == null)
			return;
		
		this.cachedTariff = new ContractItemEntity((BaseProductEntity) tariff);
		this.setExtensionOfTheTermPermission(tariff.isExtensionOfTheTerm());
	}

	@Transient
	@Override
	public Tariff getTariff() {
		if(this.getCachedTariff() == null)
			return null;
		return (Tariff) this.getCachedTariff().getBaseProduct();
	}

	@Override
	public void setTariffOptionList(List<? extends ProductOption> tariffOptionList) {
		if(tariffOptionList == null)
			return;

		Iterator<? extends ProductOption> i = tariffOptionList.iterator();
		List<ContractItemEntity> list = new ArrayList<ContractItemEntity>();
		while(i.hasNext()){
			list.add( new ContractItemEntity((BaseProductEntity) i.next()));
		}
		
		this.setCachedTariffOptionList(list);
	}

	@Override
	public List<? extends ProductOption> getTariffOptionList() {
		Iterator<? extends ContractItem> i = this.getCachedTariffOptionList().iterator();
		List<ProductOptionEntity> list = new ArrayList<ProductOptionEntity>();
		while(i.hasNext()){
			list.add( (ProductOptionEntity) i.next().getBaseProduct());
		}
		
		return list;
	}

	@Override
	public BigDecimal getSubsidyBudgetary() {
		if(subsidyBudgetary == null)
			subsidyBudgetary = new BigDecimal(0);
		return subsidyBudgetary;
	}

	@Override
	public void setSubsidyBudgetary(BigDecimal subsidyBudgetary) {
		this.subsidyBudgetary = subsidyBudgetary;
	}

	@Override
	public Order getSubsidyOrder() {
		return subsidyOrder;
	}

	@Override
	public void setSubsidyOrder(Order subsidyOrder) {
		this.subsidyOrder = (OrderEntity) subsidyOrder;
		this.subsidyOrder.setSubsidyContract(this);
	}
	
	@Override
	public Invoice getSubsidyInvoice() {
		return subsidyInvoice;
	}

	@Override
	public void setSubsidyInvoice(Invoice subsidyInvoice) {
		this.subsidyInvoice = (InvoiceEntity) subsidyInvoice;
	}

	@Override
	public void setShop(Shop shop){
		super.setShop(shop);
		if(subsidyOrder != null)
			subsidyOrder.setShop(shop);
	}
	
	@Override
	public void setUser(User user){
		super.setUser(user);
		if(subsidyOrder != null)
			subsidyOrder.setUser(user);
	}
	
	@Override
	public void setCustomer(Customer customer){
		super.setCustomer(customer);
		if(subsidyOrder != null)
			subsidyOrder.setCustomer(customer);
	}

	@Override
	public AccountingAssignment getCancelationAccountingAssignment() {
		return cancelationAccountingAssignment;
	}

	@Override
	public void setCancelationAccountingAssignment(
			AccountingAssignment cancelationAccountingAssignment) {
		this.cancelationAccountingAssignment = (AccountingAssignmentEntity) cancelationAccountingAssignment;
	}

	@Override
	public AccountingAssignment getAccountingAssignment() {
		return accountingAssignment;
	}

	@Override
	public void setAccountingAssignment(
			AccountingAssignment accountingAssignment) {
		this.accountingAssignment = (AccountingAssignmentEntity) accountingAssignment;
	}

	@Override
	public boolean isDebidCreditChange() {
		return debidCreditChange;
	}

	@Override
	public void setDebidCreditChange(boolean debidCreditChange) {
		this.debidCreditChange = debidCreditChange;
	}

	@Override
	public BaseContractCancellation getContractCancellation() {
		return contractCancellation;
	}

	@Override
	public void setContractCancellation( BaseContractCancellation contractCancellation) {
		this.contractCancellation = (BaseContractCancellationEntity) contractCancellation;
	}
	
	@Transient
	@Override
	public boolean isCanceled(){
		if(this.getContractCancellation() != null)
			return true;
		else
			return false;
	}

	@Override
	public ContractStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(ContractStatus status) {
		this.status = (ContractStatusEntity) status;
	}

}
