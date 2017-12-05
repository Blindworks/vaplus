package de.vaplus.comparator;

import java.util.Comparator;

import de.vaplus.api.interfaces.RankableByCommission;

public class GoalAttainmentRankingComparator implements Comparator<RankableByCommission>{

	@Override
	public int compare(RankableByCommission o1, RankableByCommission o2) {
		return o2.getCurrentCommissionCache().getGoalAttainment().compareTo(o1.getCurrentCommissionCache().getGoalAttainment());
	}
}
