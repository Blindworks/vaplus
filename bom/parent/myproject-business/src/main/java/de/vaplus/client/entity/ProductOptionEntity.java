package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.ProductOption;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ProductOption")
public class ProductOptionEntity extends BaseProductEntity implements ProductOption {
	
	private static final long serialVersionUID = 7201099924088227409L;
	
	@Column(name = "revenueCommissionRelevant", nullable = false)
	private boolean revenueCommissionRelevant;

	@Column(name="generatedRevenue", precision = 10, scale = 4, nullable = false)
	private BigDecimal generatedRevenue;

	@Column(name = "percentagePrice", nullable = false)
	private boolean percentagePrice;
	
	@Column(name = "weigth", nullable = false)
	private int weight;
	
	@Column(name = "forceExtensionOfTheTerm", nullable = false)
	private boolean forceExtensionOfTheTerm;

	@ManyToOne
    @JoinColumn(name="targetProductCategory_id")
    private ProductCategoryEntity targetProductCategory;
	
	public ProductOptionEntity() {
		super();
	}

	@Override
	public boolean isRevenueCommissionRelevant() {
		return revenueCommissionRelevant;
	}

	@Override
	public void setRevenueCommissionRelevant(boolean revenueCommissionRelevant) {
		this.revenueCommissionRelevant = revenueCommissionRelevant;
	}

	@Override
	public boolean isPercentagePrice() {
		return percentagePrice;
	}

	@Override
	public void setPercentagePrice(boolean percentagePrice) {
		this.percentagePrice = percentagePrice;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public boolean isForceExtensionOfTheTerm() {
		return forceExtensionOfTheTerm;
	}

	@Override
	public void setForceExtensionOfTheTerm(boolean forceExtensionOfTheTerm) {
		this.forceExtensionOfTheTerm = forceExtensionOfTheTerm;
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

}
