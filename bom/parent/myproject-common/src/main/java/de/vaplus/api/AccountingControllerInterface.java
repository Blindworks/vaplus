package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.ManualCommission;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.VendorCommissionAccountingFile;
import de.vaplus.api.entity.VendorCommissionAccountingItem;


public interface AccountingControllerInterface extends Serializable{

	List<? extends VendorCommissionAccountingFile> getCommissionAccountingFileList(int year, int month);

	CommissionTypeAssociation getCommissionTypeAssociationByText(String commissiontText, String commissiontSubText);

	CommissionTypeAssociation saveCommissionTypeAssociation(
			CommissionTypeAssociation unknownCommissionTypeAssociation);

	void checkAccountingAssignment(AccountingAssignment accountingAssignment);

	CommissionTypeAssociation getCommissionTypeAssociationByCommissionAccountingItem(
			VendorCommissionAccountingItem accountingItem);

	CommissionTypeAssociation factoryNewCommissionTypeAssociation(
			String commissionText, String commissionSubText);

	void updateAccountingAssignment(Vendor vendor, int year, int month);

	AccountingAssignment factoryNewAccountingAssignment(BaseContract contract);

	List<? extends AccountingAssignment> getAccountingAssignmentList(Vendor vendor, int year, int month);

	List<String> getUnknownUserAliasList(Vendor vendor, int year, int month);

	List<CommissionTypeAssociation> getUnknownCommissionTypeAssociationList(Vendor vendor, int year, int month);

	List<? extends CommissionTypeAssociation> getCommissionTypeAssociationList(
			Vendor vendor);

	void deleteCommissionTypeAssociation(
			CommissionTypeAssociation commissionTypeAssociation);

	ManualCommission factoryNewManualCommission(User activeUser);

	ManualCommission saveManualCommission(ManualCommission selectedManualCommission);

	List<? extends ManualCommission> getManualCommissionList();

}
