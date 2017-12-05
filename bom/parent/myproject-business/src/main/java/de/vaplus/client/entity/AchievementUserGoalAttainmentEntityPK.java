package de.vaplus.client.entity;

import java.io.Serializable;

public class AchievementUserGoalAttainmentEntityPK implements Serializable {

	private static final long serialVersionUID = 1007427922492447939L;
	
	private long achievement;
	private long user;
	
	public long getAchievement() {
		return achievement;
	}

	public void setAchievement(long achievement) {
		this.achievement = achievement;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}
	
    public int hashCode() {
        return (int) achievement + (int) user;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof AchievementUserGoalAttainmentEntityPK)) return false;
        AchievementUserGoalAttainmentEntityPK pk = (AchievementUserGoalAttainmentEntityPK) obj;
        return pk.user == user && pk.achievement == achievement ;
    }
}
