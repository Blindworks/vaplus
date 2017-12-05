package de.vaplus.client.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.ContractRetailItem;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ExternalCustomer")
public class ExternalCustomerEntity extends BaseEntity implements ExternalCustomer {

	private static final long serialVersionUID = -8262057971281967418L;
	
	@Column(name="extCustomerId", nullable = false)
	private String customerId;
	
	@Column(name="password", nullable = false)
	private String password;

	@ManyToOne
    @JoinColumn(name="customer_id", nullable = false)
    private CustomerEntity customer;

	@OneToMany(mappedBy="externalCustomer", fetch = FetchType.EAGER)
	private List<BaseContractEntity> contractList;

	public ExternalCustomerEntity() {
		super();
	}

	@Override
	public Customer getCustomer() {
		return customer;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = (CustomerEntity) customer;
	}

	@Override
	public String getCustomerId() {
		return customerId;
	}

	@Override
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public List<? extends BaseContract> getContractList() {
		if(contractList == null)
			contractList = new LinkedList<BaseContractEntity>();
		return contractList;
	}

	@Override
	public void setContractList(List<? extends BaseContract> contractList) {
		this.contractList = (List<BaseContractEntity>) contractList;
	}
}
