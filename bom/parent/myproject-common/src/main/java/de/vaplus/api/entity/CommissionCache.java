package de.vaplus.api.entity;

import java.math.BigDecimal;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface CommissionCache extends Base{

	int getYear();

	void setYear(int year);

	int getMonth();

	void setMonth(int month);

	BigDecimal getPointGoal();

	void setPointGoal(BigDecimal pointGoal);

	BigDecimal getGoalAttainment();

	Commissionable getCommission();

	void setCommission(Commissionable commission);

	int getExtensionOfTheTermSum();

	void setExtensionOfTheTermSum(int extensionOfTheTermSum);

	int getNewContractSum();

	void setNewContractSum(int newContractSum);


}
