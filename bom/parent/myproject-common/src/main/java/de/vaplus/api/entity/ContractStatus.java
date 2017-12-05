package de.vaplus.api.entity;

public interface ContractStatus extends Base{

	boolean isEditable();

	void setEditable(boolean editable);

	String getName();

	void setName(String name);

	boolean isShowInExtensionOfTheTermList();

	void setShowInExtensionOfTheTermList(boolean showInExtensionOfTheTermList);

}
