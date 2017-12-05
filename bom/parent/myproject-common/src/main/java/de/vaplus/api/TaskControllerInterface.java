package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.TaskActivity;
import de.vaplus.api.entity.User;

public interface TaskControllerInterface extends Serializable{

	List<? extends TaskActivity> getTaskList();

	TaskActivity getTaskById(long id);

	TaskActivity saveTaskActivity(TaskActivity taskActivity);

	TaskActivity factoryTaskActivity(User activeUser);

	List<? extends TaskActivity> getOwnTaskList(User activeUser, Shop activeShop);

	List<? extends TaskActivity> getOpenedTaskList(User activeUser, Shop activeShop);

	int countOpenedTaskList(User activeUser, Shop activeShop);

	List<? extends TaskActivity> getCreatedTaskList(User activeUser);

}
