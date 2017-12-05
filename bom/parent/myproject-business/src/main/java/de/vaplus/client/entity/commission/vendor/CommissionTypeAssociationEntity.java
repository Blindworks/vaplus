package de.vaplus.client.entity.commission.vendor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.entity.BaseEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ProductOptionEntity;
import de.vaplus.client.entity.VendorEntity;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="CommissionTypeAssociation", uniqueConstraints = @UniqueConstraint(columnNames = {"venor_id","commissionText","commissionSubText"}))
@NamedQueries({
    @NamedQuery(
            name = "CommissionTypeAssociation.findByText",
            query = "SELECT c FROM CommissionTypeAssociationEntity c Where c.commissionText = :commissionText and  c.commissionSubText = :commissionSubText",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    ),
    @NamedQuery(
            name = "CommissionTypeAssociation.getList",
            query = "SELECT c FROM CommissionTypeAssociationEntity c Where c.vendor = :vendor ORDER BY c.commissionText, c.commissionSubText ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
    )
})
public class CommissionTypeAssociationEntity extends BaseEntity implements CommissionTypeAssociation {
	
	private static final long serialVersionUID = 4255479843530967908L;

	@ManyToOne
    @JoinColumn(name="venor_id", nullable = false)
    private VendorEntity vendor;

	@Column(name="commissionText", nullable=false)
	private String commissionText;

	@Column(name="commissionSubText", nullable=false)
	private String commissionSubText;
//	
//	@Column(name="chargeback", nullable=false)
//	boolean chargeback;
	
	@Column(name="extensionOfTheTerm", nullable=false)
	boolean extensionOfTheTerm;
	
	@ManyToOne
    @JoinColumn(name="baseProduct_id", nullable = false)
    private BaseProductEntity product;

	@ManyToMany(fetch = FetchType.EAGER)
    private List<ProductOptionEntity> productOptionList;


	@Override
	public Vendor getVendor() {
		return vendor;
	}

	@Override
	public void setVendor(Vendor vendor) {
		this.vendor = (VendorEntity) vendor;
	}

	@Override
	public String getCommissionText() {
		return commissionText;
	}

	public void setCommissionText(String commissionText) {
		this.commissionText = commissionText;
	}

	@Override
	public String getCommissionSubText() {
		return commissionSubText;
	}

	public void setCommissionSubText(String commissionSubText) {
		this.commissionSubText = commissionSubText;
	}

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
//
//	@Override
//	public boolean isChargeback() {
//		return chargeback;
//	}
//
//	@Override
//	public void setChargeback(boolean chargeback) {
//		this.chargeback = chargeback;
//	}

	@Override
	public boolean isExtensionOfTheTerm() {
		return extensionOfTheTerm;
	}

	@Override
	public void setExtensionOfTheTerm(boolean extensionOfTheTerm) {
		this.extensionOfTheTerm = extensionOfTheTerm;
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof CommissionTypeAssociationEntity))
	    	return false;
		CommissionTypeAssociationEntity otherCTA = (CommissionTypeAssociationEntity) other;
		if(this.getId() > 0 && otherCTA.getId() > 0)
			return this.getId() == otherCTA.getId();
		else if (this.getId() > 0 || otherCTA.getId() > 0)
			return false;
		else
			return this.getCommissionText().equalsIgnoreCase(otherCTA.getCommissionText()) && this.getCommissionSubText().equalsIgnoreCase(otherCTA.getCommissionSubText());
	}
	
	@Override
	public boolean hasProduct(BaseProduct p){
		if(getProduct() == null)
			return false;
		if(getProduct().equals(p))
			return true;
		return false;
	}
	
	@Override
	public boolean hasProductOption(ProductOption po){
		if(getProductOptionList() == null)
			return false;
		Iterator<? extends ProductOption> i = getProductOptionList().iterator();
		ProductOption productOption;
		while(i.hasNext()){
			productOption = i.next();

			if(productOption.equals(po))
				return true;
		}

		return false;
	}

}
