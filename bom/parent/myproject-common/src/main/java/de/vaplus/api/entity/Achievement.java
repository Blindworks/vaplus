package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

public interface Achievement extends StatusBase{

	boolean isInvisible();

	void setInvisible(boolean invisible);

	List<? extends ProductCategory> getSelectedProductCategoryList();

	void setSelectedProductCategoryList(
			List<? extends ProductCategory> selectedProductCategoryList);

	List<? extends BaseProduct> getSelectedProductList();

	void setSelectedProductList(List<? extends BaseProduct> selectedProductList);

	List<? extends Shop> getSourceShopList();

	void setSourceShopList(List<? extends Shop> sourceShopList);

	List<? extends User> getSourceUserList();

	List<? extends VO> getSourceVOList();

	void setSourceVOList(List<? extends VO> sourceVOList);

	void setSourceUserList(List<? extends User> sourceUserList);

	List<? extends ShopGroup> getSourceShopGroupList();

	void setSourceShopGroupList(List<? extends ShopGroup> sourceShopGroupList);

	boolean isPayoutToSource();

	void setPayoutToSource(boolean payoutToSource);

	List<? extends User> getPayoutUserList();

	void setPayoutUserList(List<? extends User> payoutUserList);

	AchievementTarget getTotalPieceTarget();

	void setTotalPieceTarget(AchievementTarget totalPieceTarget);

	AchievementTarget getTotalSumTarget();

	void setTotalSumTarget(AchievementTarget totalSumTarget);

	AchievementTarget getTotalAquiredRevenueTarget();

	void setTotalAquiredRevenueTarget(
			AchievementTarget totalAquiredRevenueTarget);

	AchievementTarget getTotalContractedRevenueTarget();

	void setTotalContractedRevenueTarget(
			AchievementTarget totalContractedRevenueTarget);

	List<? extends AchievementTarget> getPieceTargetList();

	void setPieceTargetList(List<? extends AchievementTarget> pieceTargetList);

	List<? extends AchievementTarget> getSumTargetList();

	void setSumTargetList(List<? extends AchievementTarget> sumTargetList);

	List<? extends AchievementTarget> getAquiredRevenueTargetList();

	void setAquiredRevenueTargetList(
			List<? extends AchievementTarget> aquiredRevenueTargetList);

	List<? extends AchievementTarget> getContractedRevenueTargetList();

	void setContractedRevenueTargetList(
			List<? extends AchievementTarget> contractedRevenueTargetList);

	String getName();

	void setName(String name);

	List<? extends BaseProduct> getSelectedProductFilterList();

	void setSelectedProductFilterList(
			List<? extends BaseProduct> selectedProductFilterList);

	boolean isNewContract();

	void setNewContract(boolean newContract);

	boolean isExtensionOfTheTerm();

	void setExtensionOfTheTerm(boolean extensionOfTheTerm);

	boolean isDebidCreditChange();

	void setDebidCreditChange(boolean debidCreditChange);

	BigDecimal getMaxAquiredRevenueTarget();

	BigDecimal getMaxPieceTarget();

	BigDecimal getMaxSumTarget();

	BigDecimal getMaxContractedRevenueTarget();

	void clearGoalAttainment();

	void addPieceGoalAttainment(User user, BigDecimal quantity);

	void addAquiredRevenueGoalAttainment(User user, BigDecimal quantity);

	void addContractedRevenueGoalAttainment(User user, BigDecimal quantity);

	void addSumGoalAttainment(User user, BigDecimal quantity);

	BigDecimal getPieceGoalAttainment();

	void setPieceGoalAttainment(BigDecimal pieceGoalAttainment);

	BigDecimal getSumGoalAttainment();

	void setSumGoalAttainment(BigDecimal sumGoalAttainment);

	BigDecimal getAquiredRevenueGoalAttainment();

	void setAquiredRevenueGoalAttainment(BigDecimal aquiredRevenueGoalAttainment);

	BigDecimal getContractedRevenueGoalAttainment();

	void setContractedRevenueGoalAttainment(
			BigDecimal contractedRevenueGoalAttainment);

	List<? extends AchievementUserGoalAttainment> getUserGoalAttainmentList();

	AchievementUserGoalAttainment getUserGoalAttainment(User user);

	void setUserGoalAttainmentList(
			List<? extends AchievementUserGoalAttainment> userGoalAttainmentList);

	List<? extends BaseProductCombination> getSelectedProductCombinationList();

	void setSelectedProductCombinationList(
			List<? extends BaseProductCombination> selectedProductCombinationList);

	List<? extends BaseProductCombination> getSelectedProductCombinationFilterList();

	void setSelectedProductCombinationFilterList(
			List<? extends BaseProductCombination> selectedProductCombinationFilterList);

	boolean isTotalPieceTargetReached();

	boolean isTotalSumTargetReached();

	boolean isTotalAquiredRevenueTargetReached();

	boolean isTotalContractedRevenueTargetReached();

	boolean isPieceTargetReached();

	boolean isSumTargetReached();

	boolean isAquiredRevenueTargetReached();

	boolean isContractedRevenueTargetReached();

	boolean isTargetReached();

	boolean isExpired();

	List<User> getAcquisitionSourceUserList();

	AchievementTarget getReachedPieceTarget();

	AchievementTarget getReachedSumTarget();

	AchievementTarget getReachedAquiredRevenueTarget();

	AchievementTarget getReachedContractedRevenueTarget();

	BigDecimal getAchievedCommission();

	BigDecimal getPayout(User user);

	String getPayoutText(User user);

	int getWeight();

	void setWeight(int weight);

	boolean isShowOnCompanyDashboard();

	void setShowOnCompanyDashboard(boolean showOnCompanyDashboard);

}
