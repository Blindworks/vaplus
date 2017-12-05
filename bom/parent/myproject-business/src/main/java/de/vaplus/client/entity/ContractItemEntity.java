package de.vaplus.client.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ContractItem")
@Inheritance(strategy = InheritanceType.JOINED)
public class ContractItemEntity extends StatusBaseEntity implements ContractItem, Comparable<ContractItemEntity>{
	
	private static final long serialVersionUID = 286705185824519790L;

	@Column(name="name", nullable = false)
	private String productName;

	@ManyToOne
    @JoinColumn(name="baseProduct_id", nullable = false)
    private BaseProductEntity baseProduct;

	@ManyToOne
    @JoinColumn(name="vendor_id", nullable = false)
    private VendorEntity vendor;

	@ManyToOne
    @JoinColumn(name="productCategory_id", nullable = false)
    private ProductCategoryEntity productCategory;
	
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="points",column=@Column(name="productPoints", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="commission",column=@Column(name="productCommission", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="price",column=@Column(name="productPrice", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="vat",column=@Column(name="productVat", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="tax",column=@Column(name="productTax", precision = 5, scale = 2, nullable = false))
	  })
	private CommissionableEmbeddable productCommission;
	
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="points",column=@Column(name="voPoints", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="commission",column=@Column(name="voCommission", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="price",column=@Column(name="voPrice", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="vat",column=@Column(name="voVat", precision = 10, scale = 4, nullable = false)),
	    @AttributeOverride(name="tax",column=@Column(name="voTax", precision = 5, scale = 2, nullable = false))
	  })
	private CommissionableEmbeddable voCommission;
	

	public ContractItemEntity() {
		super();
	}
	
	public ContractItemEntity(BaseProductEntity baseProduct) {
		super();
		
		if(baseProduct != null){
			setBaseProduct(baseProduct);
			setProductName(baseProduct.getName());
			setProductCommission((CommissionableEmbeddable) baseProduct.getCommission());
			setVendor((VendorEntity) baseProduct.getVendor());
			setProductCategory((ProductCategoryEntity) baseProduct.getProductCategory());
			super.setEffectiveDate(baseProduct.getEffectiveDate());
			super.setExpiryDate(baseProduct.getExpiryDate());
		}
	}
	
	@Override
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Override
	public Commissionable getProductCommission() {
		if(productCommission == null)
			productCommission = new CommissionableEmbeddable();
		return productCommission;
	}
	
	private void setProductCommission(Commissionable productCommission) {
		if(productCommission == null)
			productCommission = new CommissionableEmbeddable();
		this.productCommission = (CommissionableEmbeddable) productCommission;
	}

	@Override
	public BaseProduct getBaseProduct() {
		return baseProduct;
	}

	private void setBaseProduct(BaseProductEntity baseProduct) {
		this.baseProduct = baseProduct;
	}

	@Override
	public Commissionable getVoCommission() {
		if(voCommission == null)
			voCommission = new CommissionableEmbeddable();
		return voCommission;
	}

	@Override
	public void setVoCommission(Commissionable voCommission) {
		if(voCommission == null)
			voCommission = new CommissionableEmbeddable();
		this.voCommission = (CommissionableEmbeddable) voCommission;
	}

	@Override
	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(VendorEntity vendor) {
		this.vendor = vendor;
	}

	@Override
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	private void setProductCategory(ProductCategoryEntity productCategory) {
		this.productCategory = productCategory;
	}

	@Override
    public int compareTo(ContractItemEntity other) {

		if(!(baseProduct instanceof ProductOption) && !(other.getBaseProduct() instanceof ProductOption) )
			return 0;
		if(baseProduct instanceof ProductOption && !(other.getBaseProduct() instanceof ProductOption) )
			return 1;
		if(!(baseProduct instanceof ProductOption) && other.getBaseProduct() instanceof ProductOption )
			return -1;
		
        if (((ProductOption)this.getBaseProduct()).getWeight() < ((ProductOption)((ContractItemEntity)other).getBaseProduct()).getWeight()){
            return -1;
        }else{
            return 1;
        }
    }
   
}
