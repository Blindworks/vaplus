package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.TaskActivity;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserGroup;
import de.vaplus.client.entity.TaskActivityEntity;


@Stateless
public class TaskEao implements Serializable {

	private static final long serialVersionUID = -6375173529173621595L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public List<? extends TaskActivity> getTaskList(){
		
		List<? extends TaskActivity> list = new ArrayList<TaskActivityEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TaskActivityEntity> cQuery = cb.createQuery(TaskActivityEntity.class);
			Root<TaskActivityEntity> root = cQuery.from(TaskActivityEntity.class);
			cQuery.select(root);
			
			list = em.createNamedQuery("TaskActivity.getAll", TaskActivityEntity.class).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public TaskActivity getTaskById(long id) {
		return em.find(TaskActivityEntity.class, id);
	}

	public TaskActivity saveTaskActivity(TaskActivity taskActivity) {
		return em.merge(taskActivity);
	}

	public List<? extends TaskActivity> getTaskList(User activeUser, UserGroup userGroup, Shop activeShop) {

		List<? extends TaskActivity> list = new ArrayList<TaskActivityEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TaskActivityEntity> cQuery = cb.createQuery(TaskActivityEntity.class);
			Root<TaskActivityEntity> root = cQuery.from(TaskActivityEntity.class);
			cQuery.select(root);
			
			list = em.createNamedQuery("TaskActivity.getTaskList", TaskActivityEntity.class)
					.setParameter("user", activeUser)
					.setParameter("userGroup", userGroup)
					.setParameter("shop", activeShop)
					.getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends TaskActivity> getOpenedTaskList(User activeUser, UserGroup userGroup, Shop activeShop) {

		List<? extends TaskActivity> list = new ArrayList<TaskActivityEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TaskActivityEntity> cQuery = cb.createQuery(TaskActivityEntity.class);
			Root<TaskActivityEntity> root = cQuery.from(TaskActivityEntity.class);
			cQuery.select(root);
			
			list = em.createNamedQuery("TaskActivity.getOpenedTaskList", TaskActivityEntity.class)
					.setParameter("user", activeUser)
					.setParameter("userGroup", userGroup)
					.setParameter("shop", activeShop)
					.getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public int countOpenedTaskList(User activeUser, UserGroup userGroup, Shop activeShop) {
		
		List<? extends TaskActivity> list = getOpenedTaskList(activeUser, userGroup, activeShop);
		
		if(list == null)
			return 0;
		
		return list.size();
	}

	public List<? extends TaskActivity> getcreatedTaskList(User user) {

		List<? extends TaskActivity> list = new ArrayList<TaskActivityEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TaskActivityEntity> cQuery = cb.createQuery(TaskActivityEntity.class);
			Root<TaskActivityEntity> root = cQuery.from(TaskActivityEntity.class);
			cQuery.select(root);
			
			list = em.createNamedQuery("TaskActivity.getTaskListByCreator", TaskActivityEntity.class)
					.setParameter("user", user)
					.getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}
}
