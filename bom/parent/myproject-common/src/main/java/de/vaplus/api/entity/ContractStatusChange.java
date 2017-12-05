package de.vaplus.api.entity;

public interface ContractStatusChange extends Activity{

	String getOldStatus();

	void setOldStatus(String oldStatus);

	String getNewStatus();

	void setNewStatus(String newStatus);

	BaseContract getContract();

	void setContract(BaseContract contract);

}
