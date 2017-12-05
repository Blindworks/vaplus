package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.ContractRetailItem;
import de.vaplus.api.entity.Product;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ContractRetailItem")
@Inheritance(strategy = InheritanceType.JOINED)
public class ContractRetailItemEntity extends BaseEntity implements ContractRetailItem  {
	
	private static final long serialVersionUID = 2384517378647409822L;

	@Column(name="serial")
	private String serial;

	@ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private ProductEntity product;

	@ManyToOne
    @JoinColumn(name="contract_id", nullable = false)
    private BaseContractEntity baseContract;
	
	public ContractRetailItemEntity() {
		super();
	}

	public ContractRetailItemEntity(BaseContract contract, Product product, String serial) {
		super();
		setProduct(product);
		setBaseContract(contract);
		setSerial(serial);
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
	public BaseContract getBaseContract() {
		return baseContract;
	}

	@Override
	public void setBaseContract(BaseContract baseContract) {
		this.baseContract = (BaseContractEntity) baseContract;
	}

   
}
