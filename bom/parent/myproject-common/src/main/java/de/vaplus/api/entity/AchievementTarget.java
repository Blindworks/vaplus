package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface AchievementTarget extends Base{

	BigDecimal getTarget();

	void setTarget(BigDecimal target);

	BigDecimal getPayout();

	void setPayout(BigDecimal payout);

	String getPayoutText();

	void setPayoutText(String payoutText);

	BigDecimal getCommission();

	void setCommission(BigDecimal commission);

	boolean isTargetReached(BigDecimal attainment);

}
