package de.vaplus.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public interface AchievementUserGoalAttainment extends Serializable{

	Achievement getAchievement();

	void setAchievement(Achievement achievement);

	User getUser();

	void setUser(User user);


	BigDecimal getPieceGoalAttainment();

	void setPieceGoalAttainment(BigDecimal pieceGoalAttainment);

	BigDecimal getSumGoalAttainment();

	void setSumGoalAttainment(BigDecimal sumGoalAttainment);

	BigDecimal getAquiredRevenueGoalAttainment();

	void setAquiredRevenueGoalAttainment(BigDecimal aquiredRevenueGoalAttainment);

	BigDecimal getContractedRevenueGoalAttainment();

	void setContractedRevenueGoalAttainment(
			BigDecimal contractedRevenueGoalAttainment);

	void clearGoalAttainment();

	void addPieceGoalAttainment(BigDecimal quantity);

	void addAquiredRevenueGoalAttainment(BigDecimal quantity);

	void addContractedRevenueGoalAttainment(BigDecimal quantity);

	void addSumGoalAttainment(BigDecimal quantity);
}
