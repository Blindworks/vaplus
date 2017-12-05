package de.vaplus.client.entity.commission.vendor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.VendorCommissionAccountingFile;
import de.vaplus.api.entity.VendorCommissionAccountingItem;
import de.vaplus.client.entity.BaseEntity;

@Entity
@Table(name="VendorCommissionAccountingItem")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VendorCommissionAccountingItemEntity extends BaseEntity implements VendorCommissionAccountingItem{

	private static final long serialVersionUID = -1897944270306659466L;
	
	@ManyToOne
    @JoinColumn(name="file_id", nullable = false)
    private VendorCommissionAccountingFileEntity file;

	@ManyToOne
    @JoinColumn(name="accountingAssignment_id", nullable = false)
    private AccountingAssignmentEntity accountingAssignment;

	@Override
	public abstract Date getDate();

	@Override
	public abstract boolean isCancelation();
	
	public VendorCommissionAccountingItemEntity(){
		super();
	}
	
	public VendorCommissionAccountingItemEntity(String csvLine){
		super();
	}
	
	@Override
	public VendorCommissionAccountingFile getFile() {
		return file;
	}

	@Override
	public void setFile(VendorCommissionAccountingFile file) {
		this.file = (VendorCommissionAccountingFileEntity) file;
	}


	@Override
	public AccountingAssignment getAccountingAssignment() {
		return accountingAssignment;
	}

	@Override
	public void setAccountingAssignment(
			AccountingAssignment accountingAssignment) {
		this.accountingAssignment = (AccountingAssignmentEntity) accountingAssignment;
	}
	

}
