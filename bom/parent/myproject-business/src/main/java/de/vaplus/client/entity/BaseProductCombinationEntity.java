package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
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
@Table(name="BaseProductCombination")
@NamedQueries({
    @NamedQuery(
        name = "BaseProductCombination.getList",
        query = "SELECT p FROM BaseProductCombinationEntity p WHERE p.deleted = false ORDER BY p.product.name ASC"
        ,
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    	)
})
public class BaseProductCombinationEntity extends BaseEntity implements BaseProductCombination {
	
	private static final long serialVersionUID = 962609577023155915L;

	@ManyToOne
    @JoinColumn(name="BaseProduct_id", nullable = false)
    private BaseProductEntity product;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="BaseProductCombination_ProductOption",
	joinColumns={@JoinColumn(name="baseProductCombinationID", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="productOptionID", referencedColumnName="id")})
	private List<ProductOptionEntity> productOptionList;

	@Override
	public BaseProduct getProduct() {
		return product;
	}

	@Override
	public void setProduct(BaseProduct product) {
		this.product = (BaseProductEntity) product;
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
	public String toString(){
		String s = "";
		String so = "";
		
		if(product != null)
			s += product.getName();
		
		s +=" (";
		
		Iterator<? extends ProductOption> i = getProductOptionList().iterator();
		ProductOption po;
		while(i.hasNext()){
			po = i.next();
			
			if(so.length() > 0)
				so += " | ";
			
			so += po.getName();
		}
		s += so;
		
		s +=")";
		
		return s;
	}
}
