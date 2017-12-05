package de.vaplus.client.entity;

import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.vaplus.api.entity.ProductVOCommission;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;

/**
 * Entity implementation class for Entity: Shop
 *
 */
//@Entity
//@Table(name="ProductVOCommission")
public class ProductVOCommissionEntity extends BaseProductEntity implements ProductVOCommission {
	
	private static final long serialVersionUID = -7693105667288848660L;

	@Embedded
	private CommissionableEmbeddable commission;

	@ManyToOne
    @JoinColumn(name="baseProduct_id")
    private BaseProductEntity product;

	@ManyToOne
    @JoinColumn(name="vendor_id")
    private VendorEntity vendor;
	

	public ProductVOCommissionEntity() {
		super();
	}

}
