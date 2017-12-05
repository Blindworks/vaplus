package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.vaplus.api.entity.Achievement;
import de.vaplus.client.entity.AchievementEntity;
import de.vaplus.client.entity.AchievementTargetEntity;

@Stateless
@Dependent
public class AchievementEao implements Serializable {

	private static final long serialVersionUID = -1494929240535058960L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public AchievementEntity saveAchievement(AchievementEntity achievement) {
		return em.merge(achievement);
	}

	public List<? extends Achievement> getAchievementList(Date date) {
		List<? extends Achievement> list = em.createNamedQuery("Achievement.findActive", AchievementEntity.class)
	            .setParameter("effectiveDate", date)
	            .setParameter("expiryDate", date)
		            .getResultList();
		return list;
	}

	public List<? extends Achievement> getActiveAchievementList(Date ltEffectiveDate, Date gtExpiryDate) {
		List<? extends Achievement> list = em.createNamedQuery("Achievement.findActive", AchievementEntity.class)
	            .setParameter("effectiveDate", ltEffectiveDate)
	            .setParameter("expiryDate", gtExpiryDate)
		            .getResultList();
		return list;
	}

	public List<? extends Achievement> getOpenAchievementList() {
		List<? extends Achievement> list = em.createNamedQuery("Achievement.findOpen", AchievementEntity.class)
	            .setParameter("date", new Date())
		        .getResultList();
		return list;
	}

	public List<? extends Achievement> getClosedAchievementList() {
		List<? extends Achievement> list = em.createNamedQuery("Achievement.findClosed", AchievementEntity.class)
	            .setParameter("date", new Date())
		        .getResultList();
		return list;
	}

	public List<? extends Achievement> getAchievementList() {
		
		System.out.println("getAchievementList START");
		List<? extends Achievement> list = em.createNamedQuery("Achievement.getAll", AchievementEntity.class)
		            .getResultList();
		System.out.println("getAchievementList END");
		return list;
	}

	public Achievement getActiveAchievement(long id) {
		return em.find(AchievementEntity.class, id);
	}

	public void detachAchievement(AchievementEntity achievement) {
		em.detach(achievement);
	}

	public void detachAchievementTarget(AchievementTargetEntity achievementTarget) {
		em.detach(achievementTarget);
		
	}

	public List<? extends Achievement> getAchievementList(Date from, Date to) {
		List<? extends Achievement> list = em.createNamedQuery("Achievement.findFinishedBetween", AchievementEntity.class)
	            .setParameter("from", from)
	            .setParameter("to", to)
		        .getResultList();
		return list;
	}
}
