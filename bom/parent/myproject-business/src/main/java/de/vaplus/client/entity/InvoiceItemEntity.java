package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.InvoiceItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import de.vaplus.helper.TaxHelper;

@Entity
@Table(name="InvoiceItem")
public class InvoiceItemEntity extends BaseEntity implements InvoiceItem  {
	
	private static final long serialVersionUID = -1882817446856572660L;

	@Column(name="title", nullable = false)
	private String title;

	@Column(name="subTitle", nullable = false)
	private String subTitle;

	@Column(name="quantity")
	private BigDecimal quantity;
	
	@Column(name="serial", nullable = true)
	private String serial;

	@Column(name="blockedItem")
	private boolean blockedItem;

	@Column(name="manualItem")
	private boolean manualItem;

	@ManyToOne
    @JoinColumn(name="product_id", nullable = true)
    private ProductEntity product;

	@ManyToOne
    @JoinColumn(name="invoice_id", nullable = false)
    private InvoiceEntity invoice;

	@Embedded
	private CommissionableEmbeddable commission;

    @Column(name="unitPurchasePrice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPurchasePrice;

    @Column(name="unitPrice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

	public InvoiceItemEntity() {
		super();
		commission = new CommissionableEmbeddable();
	}
	
	public InvoiceItemEntity(Invoice invoice, Product product, String serial, BigDecimal quantity) {
		super();
		commission = new CommissionableEmbeddable();
		
		this.setInvoice(invoice);
		this.setProduct(product);
		this.setSerial(serial);
		this.setQuantity(quantity);
		
		this.setTitle(product.getName());
		this.setUnitPrice(product.getCommission().getPrice());
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getSubTitle() {
		return subTitle;
	}

	@Override
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	@Override
	public BigDecimal getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getSerial() {
		return serial;
	}

	@Override
	public void setSerial(String serial) {
		this.serial = serial;
	}

	@Override
	public Product getProduct() {
		return product;
	}

	@Override
	public void setProduct(Product product) {
		this.product = (ProductEntity) product;
	}

	@Override
	public Invoice getInvoice() {
		return invoice;
	}

	@Override
	public void setInvoice(Invoice invoice) {
		this.invoice = (InvoiceEntity) invoice;
	}

	@Override
	public boolean isBlockedItem() {
		return blockedItem;
	}

	@Override
	public void setBlockedItem(boolean blockedItem) {
		this.blockedItem = blockedItem;
	}

	@Override
	public boolean isManualItem() {
		return manualItem;
	}

	@Override
	public void setManualItem(boolean manualItem) {
		this.manualItem = manualItem;
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
	public BigDecimal getUnitPurchasePrice() {
		if(unitPurchasePrice == null)
			unitPurchasePrice = new BigDecimal(0);
		return unitPurchasePrice;
	}

	@Override
	public void setUnitPurchasePrice(BigDecimal unitPurchasePrice) {
		this.unitPurchasePrice = unitPurchasePrice;
	}

	@Override
	public BigDecimal getUnitPrice() {
		if(unitPrice == null)
			unitPrice = new BigDecimal(0);
		return unitPrice;
	}

	@Override
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Transient
	@Override
	public BigDecimal getUnitGrossPrice() {
		return TaxHelper.addTax(getUnitPrice(), commission.getTax());
	}

	@Transient
	@Override
	public void setUnitGrossPrice(BigDecimal unitGrossPrice) {
		setUnitPrice(TaxHelper.removeTax(unitGrossPrice, commission.getTax()));
	}

}
