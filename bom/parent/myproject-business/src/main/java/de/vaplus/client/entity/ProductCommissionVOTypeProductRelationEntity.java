package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ProductCommissionVOTypeProductRelation")
public class ProductCommissionVOTypeProductRelationEntity extends StatusBaseEntity implements ProductCommissionVOTypeProductRelation {
	
	private static final long serialVersionUID = -7860424303275976653L;

	@ManyToOne
    @JoinColumn(name="ProductCommissionVOType_id", nullable = false)
    private ProductCommissionVOTypeEntity productCommissionVOType;

	@ManyToOne
    @JoinColumn(name="BaseProduct_id", nullable = false)
    private BaseProductEntity product;

	@Embedded
	private CommissionableEmbeddable commission;

	@Column(name="subsidyBudgetary", precision = 10, scale = 2, nullable = false)
	private BigDecimal subsidyBudgetary;

	@ManyToMany(fetch = FetchType.EAGER)
    private List<ProductOptionEntity> productOptionList;


	public ProductCommissionVOTypeProductRelationEntity() {
		super();
		subsidyBudgetary = new BigDecimal(0);
	}


	@Override
	public BaseProduct getProduct() {
		return product;
	}

	@Override
	public void setProduct(BaseProduct product) {
		this.product = (BaseProductEntity) product;
		
		// clear options
		productOptionList = null;
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
	public ProductCommissionVOType getProductCommissionVOType() {
		return productCommissionVOType;
	}

	@Override
	public void setProductCommissionVOType(
			ProductCommissionVOType productCommissionVOType) {
		this.productCommissionVOType = (ProductCommissionVOTypeEntity) productCommissionVOType;
	}

	@Override
	public List<? extends ProductOption> getProductOptionList() {
		if(productOptionList == null)
			productOptionList = new ArrayList<ProductOptionEntity>();
		return productOptionList;
	}

	@Override
	public void setProductOptionList(List<? extends ProductOption> productOptionList) {
		this.productOptionList = (List<ProductOptionEntity>) productOptionList;
	}

	@Override
	public BigDecimal getSubsidyBudgetary() {
		return subsidyBudgetary;
	}

	@Override
	public void setSubsidyBudgetary(BigDecimal subsidyBudgetary) {
		this.subsidyBudgetary = subsidyBudgetary;
	}

}
