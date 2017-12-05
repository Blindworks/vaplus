package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.TaskControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.Event;
import de.vaplus.api.entity.EventType;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.TaskActivity;
import de.vaplus.api.entity.User;

@ManagedBean(name="taskBean")
@SessionScoped
public class TaskBean implements Serializable {

	private static final long serialVersionUID = -1591009098945267530L;

	@EJB
	private TaskControllerInterface taskController;
	
	@Inject
	private FacesContext facesContext;

    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

    @ManagedProperty(value="#{shopBean}")
    private ShopBean shopBean;
	
	private TaskActivity selectedTask;
	
	public List<? extends TaskActivity> getTaskList() {
		return taskController.getTaskList();
	}
	
	public List<? extends TaskActivity> getOwnTaskList() {
		return taskController.getOwnTaskList(userBean.getActiveUser(), shopBean.getActiveShop());
	}
	
	public List<? extends TaskActivity> getCreatedTaskList() {
		return taskController.getCreatedTaskList(userBean.getActiveUser());
	}
	
	public List<? extends TaskActivity> getOwnOpenedTaskList() {
		return taskController.getOpenedTaskList(userBean.getActiveUser(), shopBean.getActiveShop());
	}
	
	public int countOwnOpenedTaskList() {
		return taskController.countOpenedTaskList(userBean.getActiveUser(), shopBean.getActiveShop());
	}

	public TaskActivity getSelectedTask() {
		if(selectedTask == null){
			selectedTask = taskController.factoryTaskActivity(userBean.getActiveUser());
		}
		return selectedTask;
	}

	public void setSelectedTask(TaskActivity selectedTask) {
		this.selectedTask = selectedTask;
	}

	public String saveTask() {
		
		if(!selectedTask.isToEveryone() && selectedTask.getUserReceipientList().size() == 0 && selectedTask.getUserGroupReceipientList().size() == 0 && selectedTask.getShopReceipientList().size() == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Es muss mind. 1 Empfänger angegeben werden.", null));
			return "";
		}
		this.selectedTask = taskController.saveTaskActivity(getSelectedTask());
		return "taskList";
	}

	public String completeTask() {
		getSelectedTask().setCompletedByUser(userBean.getActiveUser());
		getSelectedTask().setCompletedDate(new Date());
		saveTask();
		return "taskList";
	}

	public String openTask() {
		getSelectedTask().setCompletedByUser(null);
		getSelectedTask().setCompletedDate(null);
		saveTask();
		return "taskList";
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public ShopBean getShopBean() {
		return shopBean;
	}

	public void setShopBean(ShopBean shopBean) {
		this.shopBean = shopBean;
	}

	public boolean existsOverdueTasks() {
		List<? extends TaskActivity> list = getOwnOpenedTaskList();
		Iterator<? extends TaskActivity> i = list.iterator();
		while(i.hasNext()){
			if(i.next().isOverdue())
				return true;
		}
		
		return false;
	}
}
