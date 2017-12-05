package de.vaplus.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;

public interface AchievementControllerInterface extends Serializable{

	Achievement factoryNewAchievement();

	Achievement saveAchievement(Achievement achievement);

	AchievementTarget factoryNewAchievementTarget();

	AchievementTarget factoryNewAchievementTarget(BigDecimal target,
			BigDecimal payout, String payoutText, BigDecimal commission);

	List<? extends Achievement> getAchievementList();

	List<? extends Achievement> getActiveAchievementList();

	Achievement getActiveAchievement(long id);

	void calculateAchievementGoalAttainment(Achievement achievement);

	boolean isAchivementSource(Achievement achievement, User user, Shop shop,
			VO vo, List<? extends VO> voList);

	Achievement cloneAchievement(Achievement achievement);

	List<? extends Achievement> getClosedAchievementList();

	List<? extends Achievement> getOpenAchievementList();

	AchievementTarget cloneAchievementTarget(AchievementTarget achievementTarget);

	List<? extends Achievement> getAchievementList(int month, int year);

	void calculateAllActiveAchievementGoalAttainments();

	List<? extends Achievement> getActiveAchievementListForCompanyDashboard();

}
