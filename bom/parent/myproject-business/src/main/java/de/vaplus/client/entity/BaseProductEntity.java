package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.api.entity.TaxRate;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import de.vaplus.helper.TaxHelper;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="BaseProduct")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(
        name = "BaseProduct.findActive",
        query = "SELECT p FROM BaseProductEntity p WHERE ((((p.enabled = true) AND ((p.effectiveDate <= :effectiveDate) OR (p.effectiveDate IS NULL))) AND ((p.expiryDate >= :expiryDate) OR (p.effectiveDate IS NULL))) AND (p.deleted = false))  ORDER BY p.name ASC"
        ,
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    	),
    @NamedQuery(
            name = "Product.findActive",
            query = "SELECT p FROM ProductEntity p WHERE ((((p.enabled = true) AND ((p.effectiveDate <= :effectiveDate) OR (p.effectiveDate IS NULL))) AND ((p.expiryDate >= :expiryDate) OR (p.effectiveDate IS NULL))) AND (p.deleted = false))  ORDER BY p.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "ProductOption.findActive",
            query = "SELECT p FROM ProductOptionEntity p WHERE ((((p.enabled = true) AND ((p.effectiveDate <= :effectiveDate) OR (p.effectiveDate IS NULL))) AND ((p.expiryDate >= :expiryDate) OR (p.effectiveDate IS NULL))) AND (p.deleted = false))  ORDER BY p.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "Product.getForeignRetailProductList",
            query = "SELECT p FROM ProductEntity p WHERE ((((p.enabled = true) AND ((p.effectiveDate <= :effectiveDate) OR (p.effectiveDate IS NULL))) AND ((p.expiryDate >= :expiryDate) OR (p.effectiveDate IS NULL))) AND (p.deleted = false)) AND p.bookableAsForeignWare = true ORDER BY p.name ASC"
            ,
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
        name = "BaseProduct.getFilteredList",
        query = "SELECT p FROM BaseProductEntity p WHERE p.deleted = false AND (p.enabled = :enabled_1 OR p.enabled = :enabled_2) AND (true = :disableCatFilter OR p.productCategory = :category) AND (true = :disableNameFilter OR p.name LIKE :name) AND (true = :disableTypeFilter OR TYPE(p) = :class)  ORDER BY p.name ASC"
//        ,
//        hints = {
//                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
//            }
    	),
})
public abstract class BaseProductEntity extends StatusBaseEntity implements BaseProduct, SearchResult  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2451218366401896089L;
	
	@Column(name="name", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vendor_id", nullable = false)
    private VendorEntity vendor;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="taxRate_id", nullable = false)
    private TaxRateEntity taxRate;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productCategory_id", nullable = false)
    private ProductCategoryEntity productCategory;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="ProductVOTypePermission",
		joinColumns={@JoinColumn(name="baseProductID", referencedColumnName="id")},
		inverseJoinColumns={@JoinColumn(name="voTypeID", referencedColumnName="id")})
	private List<VOTypeEntity> voTypePermissionList;
	
	@Embedded
	private CommissionableEmbeddable commission;

	public BaseProductEntity() {
		super();
	}
	
	@Override
	@Transient
	public void setBaseProductValues(BaseProduct product){
		this.setStatusBaseEntityValues(product);
		
		this.setCommission(product.getCommission());
		this.setVoTypePermissionList(product.getVoTypePermissionList());
		this.setProductCategory(product.getProductCategory());
		this.setVendor(product.getVendor());
		this.setName(product.getName());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Vendor getVendor() {
		return vendor;
	}

	@Override
	public void setVendor(Vendor vendor) {
		this.vendor = (VendorEntity) vendor;
	}

	@Override
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	@Override
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = (ProductCategoryEntity) productCategory;
	}

	@Override
	public Commissionable getCommission() {
		if(commission == null)
			commission = new CommissionableEmbeddable();
		
		if(getTaxRate() != null)
			commission.setTax(getTaxRate().getTax());
		
		return commission;
	}

	@Override
	public void setCommission(Commissionable commission) {
		this.commission = (CommissionableEmbeddable) commission;
		
		if(getTaxRate() != null)
			commission.setTax(getTaxRate().getTax());
	}

	@Override
	public List<? extends VOType> getVoTypePermissionList() {
		return voTypePermissionList;
	}

	@Override
	public void setVoTypePermissionList(List<? extends VOType> voTypePermissionList) {
		this.voTypePermissionList = (List<VOTypeEntity>) voTypePermissionList;
	}

	@Override
	public TaxRate getTaxRate() {
		return taxRate;
	}

	@Override
	public void setTaxRate(TaxRate taxRate) {
		this.taxRate = (TaxRateEntity) taxRate;
		
		if(taxRate != null)
			getCommission().setTax(taxRate.getTax());
	}
	
	@Override
	public String toString(){
		return this.getName();
	}

	public void setPercentagePrice(boolean percentagePrice) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	@Override
	public BigDecimal getVat() {
		return TaxHelper.getVatFromNetPrice(getCommission().getPrice(), getCommission().getTax());
	}

   
}
