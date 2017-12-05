package de.vaplus.client.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ProductCommissionVOType")
public class ProductCommissionVOTypeEntity extends VOTypeEntity implements ProductCommissionVOType {
	
	private static final long serialVersionUID = -7552104769627052371L;
	
	@OneToMany(mappedBy="productCommissionVOType", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductCommissionVOTypeProductRelationEntity> productCommissionList;

	public ProductCommissionVOTypeEntity() {
		super();
	}

	@Override
	public List<? extends ProductCommissionVOTypeProductRelation> getProductCommissionList() {
		if(productCommissionList == null)
			productCommissionList = new ArrayList<ProductCommissionVOTypeProductRelationEntity>();
		
		return productCommissionList;
	}

	@Override
	public void setProductCommissionList(
			List<? extends ProductCommissionVOTypeProductRelation> productCommissionList) {
		this.productCommissionList = (List<ProductCommissionVOTypeProductRelationEntity>) productCommissionList;
	}

}
