package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.ContractStatus;
import de.vaplus.api.entity.ContractStatusChange;
import de.vaplus.api.entity.Note;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ContractStatusChange")
public class ContractStatusChangeEntity extends ActivityEntity implements ContractStatusChange {

	private static final long serialVersionUID = 8004560643331449138L;

    @Column(name="old_status", nullable = false)
    private String oldStatus;

    @Column(name="new_status", nullable = false)
    private String newStatus;

	@ManyToOne
    @JoinColumn(name="contract_id", nullable = false)
    private BaseContractEntity contract;

	public ContractStatusChangeEntity() {
		super();
	}

	@Override
	public String getOldStatus() {
		return oldStatus;
	}

	@Override
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	@Override
	public String getNewStatus() {
		return newStatus;
	}

	@Override
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
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
