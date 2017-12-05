package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseContractCancellation;
import de.vaplus.api.entity.CommissionActivity;
import de.vaplus.api.entity.ContractItem;
import de.vaplus.api.entity.ContractRetailItem;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;

@Entity
@Table(name="BaseContractCancellation")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseContractCancellationEntity extends CommissionActivityEntity implements BaseContractCancellation {

	private static final long serialVersionUID = 8034156569251020216L;
	
	@OneToOne
    @JoinColumn(name="contract_id", nullable = false)
    private BaseContractEntity contract;

	public BaseContractCancellationEntity() {
		super();
	}

	@Override
	public BaseContract getContract() {
		return contract;
	}

	@Override
	public void setContract(BaseContract contract) {
		this.contract = (BaseContractEntity) contract;
	}

}
