package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.InvoiceItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.TaxRate;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;

@Entity
@Table(name="Invoice")
@NamedQueries({
    @NamedQuery(
        name = "Invoice.getInvoiceByNumber",
        query = "SELECT i FROM InvoiceEntity i WHERE i.number = :number",
        hints = {
                @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
            }
    )
})
public class InvoiceEntity extends ActivityEntity implements Invoice {

	private static final long serialVersionUID = -3133904099022841461L;

	@Column(name="number", nullable = false)
    private String number;

//	@Column(name="drafterLine")
//	private String drafterLine;

	@Column(name="recipientLine1")
	private String recipientLine1;

	@Column(name="recipientLine2")
	private String recipientLine2;

	@Column(name="recipientLine3")
	private String recipientLine3;

	@Column(name="recipientLine4")
	private String recipientLine4;

	@Column(name="recipientLine5")
	private String recipientLine5;

	@Column(name="introduction")
	private String introduction;
	
	@Column(name="paymentInformation")
	private String paymentInformation;
	
	@Column(name="closing")
	private String closing;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dueDate", nullable = true)
	private Date dueDate;
	
	@Embedded
	private CommissionableEmbeddable commission;
	
	@OneToMany(mappedBy="invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InvoiceItemEntity> invoiceItemList;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pickslip_id")
    private StockPickslipEntity stockPickslip;

	@ManyToOne
    @JoinColumn(name="customerAccount_id", nullable = true)
    private PaymentAccountEntity customerAccount;

	@ManyToOne
    @JoinColumn(name="payment_id", nullable = true)
    private PaymentEntity payment;

	@ManyToOne
    @JoinColumn(name="invoiceFile_id", nullable = false)
	private FileEntity invoiceFile;

	@OneToOne(mappedBy="canceledinvoice", fetch = FetchType.EAGER)
    private InvoiceEntity cancelationInvoice;

	@OneToOne
	@JoinColumn(name="canceledinvoice_id", nullable = true)
    private InvoiceEntity canceledinvoice;

    @OneToOne(mappedBy="subsidyInvoice") 
    private BaseContractEntity subsidyContract;
    
    // keine automatische Subvention
    @Transient
    private boolean noSubsidy;
	

	public InvoiceEntity() {
		super();
		commission = new CommissionableEmbeddable();
	}
	
	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@PrePersist
	public void initRelations(){
		if(getInvoiceItemList().size() > 0){
			Iterator<InvoiceItemEntity> i = (Iterator<InvoiceItemEntity>) getInvoiceItemList().iterator();
			while(i.hasNext()){
				i.next().setInvoice(this);
			}
		}
	}

	@Override
	public String getIntroduction() {
		return introduction;
	}

	@Override
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Override
	public String getClosing() {
		return closing;
	}

	@Override
	public void setClosing(String closing) {
		this.closing = closing;
	}

	@Override
	public Commissionable getCommission() {
		if(commission == null)
			commission = new CommissionableEmbeddable();
		return commission;
	}

	@Override
	public void setCommission(Commissionable commission) {
		this.commission = (CommissionableEmbeddable) commission;
	}

	@Override
	public List<? extends InvoiceItem> getInvoiceItemList() {
		if(invoiceItemList == null)
			invoiceItemList = new LinkedList<InvoiceItemEntity>();
		return (List<? extends InvoiceItem>) invoiceItemList;
	}

	@Override
	public void setInvoiceItemList(List<? extends InvoiceItem> invoiceItemList) {
		this.invoiceItemList = (List<InvoiceItemEntity>) invoiceItemList;
	}

	@Override
	public String getRecipientLine1() {
		return recipientLine1;
	}

	@Override
	public void setRecipientLine1(String recipientLine1) {
		this.recipientLine1 = recipientLine1;
	}

	@Override
	public String getRecipientLine2() {
		return recipientLine2;
	}

	@Override
	public void setRecipientLine2(String recipientLine2) {
		this.recipientLine2 = recipientLine2;
	}

	@Override
	public String getRecipientLine3() {
		return recipientLine3;
	}

	@Override
	public void setRecipientLine3(String recipientLine3) {
		this.recipientLine3 = recipientLine3;
	}

	@Override
	public String getRecipientLine4() {
		return recipientLine4;
	}

	@Override
	public void setRecipientLine4(String recipientLine4) {
		this.recipientLine4 = recipientLine4;
	}

	@Override
	public String getRecipientLine5() {
		return recipientLine5;
	}

	@Override
	public void setRecipientLine5(String recipientLine5) {
		this.recipientLine5 = recipientLine5;
	}

//	@Override
//	public String getDrafterLine() {
//		return drafterLine;
//	}
//
//	@Override
//	public void setDrafterLine(String drafterLine) {
//		this.drafterLine = drafterLine;
//	}

	@Override
	public StockPickslip getStockPickslip() {
		return stockPickslip;
	}

	@Override
	public void setStockPickslip(StockPickslip stockPickslip) {
		this.stockPickslip = (StockPickslipEntity) stockPickslip;
	}

	@Override
	public PaymentAccount getCustomerAccount() {
		return customerAccount;
	}

	@Override
	public void setCustomerAccount(PaymentAccount customerAccount) {
		this.customerAccount = (PaymentAccountEntity) customerAccount;
	}

	@Override
	public Payment getPayment() {
		return payment;
	}

	@Override
	public void setPayment(Payment payment) {
		this.payment = (PaymentEntity) payment;
	}

	@Override
	public String getPaymentInformation() {
		return paymentInformation;
	}

	@Override
	public void setPaymentInformation(String paymentInformation) {
		this.paymentInformation = paymentInformation;
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}

	@Override
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public File getInvoiceFile() {
		return invoiceFile;
	}

	@Override
	public void setInvoiceFile(File invoiceFile) {
		this.invoiceFile = (FileEntity) invoiceFile;
	}

    @Override
    @Transient
    public Map<BigDecimal, BigDecimal> getVatList(){
    	
    	Map<BigDecimal,BigDecimal> vatList = new HashMap<BigDecimal,BigDecimal>();
    	
    	BigDecimal tax;
    	
		Iterator<? extends InvoiceItem> i = getInvoiceItemList().iterator();
		InvoiceItemEntity invoiceItem = null;
		while(i.hasNext()){
			invoiceItem = (InvoiceItemEntity) i.next();
			
			tax = invoiceItem.getCommission().getTax();
			
			if(! vatList.containsKey(tax))
				vatList.put(tax, new BigDecimal(0));
			
			vatList.put(tax, vatList.get(tax).add(invoiceItem.getCommission().getVat()));
		}
		
		return vatList;
    }

    @Override
	public Invoice getCancelationInvoice() {
		return cancelationInvoice;
	}

    @Override
	public void setCancelationInvoice(Invoice cancelationInvoice) {
		this.cancelationInvoice = (InvoiceEntity) cancelationInvoice;
	}

    @Override
	public Invoice getCanceledinvoice() {
		return canceledinvoice;
	}

    @Override
	public void setCanceledinvoice(Invoice canceledinvoice) {
		this.canceledinvoice = (InvoiceEntity) canceledinvoice;
	}
	
    @Override
    public boolean isCanceled(){
    	if(this.cancelationInvoice != null)
    		return true;
    	
    	return false;
    }
	
    @Override
    public boolean isCancelation(){
    	if(this.canceledinvoice != null)
    		return true;
    	
    	return false;
    }

    @Override
	public BaseContract getSubsidyContract() {
		return subsidyContract;
	}

    @Override
	public void setSubsidyContract(BaseContract subsidyContract) {
		this.subsidyContract = (BaseContractEntity) subsidyContract;
	}

    @Override
	public boolean isNoSubsidy() {
		return noSubsidy;
	}

    @Override
	public void setNoSubsidy(boolean noSubsidy) {
		this.noSubsidy = noSubsidy;
	}
	
}
