package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.interfaces.RankableByCommission;
import de.vaplus.comparator.GoalAttainmentRankingComparator;

@ManagedBean(name="sortingBean")
@SessionScoped
public class SortingBean implements Serializable {
	
	private static final long serialVersionUID = 5324436649718733301L;

	public List<? extends RankableByCommission> orderRankableList(List<? extends RankableByCommission> list){
		
		Collections.sort(list, new GoalAttainmentRankingComparator());
		
		return list;
	}
}
