package de.vaplus.api.entity;

public interface Tariff extends BaseProduct{

	String getName();

	void setName(String name);

	int getMinimumTermOfContract();

	void setMinimumTermOfContract(int minimumTermOfContract);

	boolean isExtensionOfTheTerm();

	void setExtensionOfTheTerm(boolean extensionOfTheTerm);

	boolean askForOrderNumber();
}
