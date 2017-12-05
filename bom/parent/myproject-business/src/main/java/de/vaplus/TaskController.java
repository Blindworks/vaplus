package de.vaplus;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StateControllerInterface;
import de.vaplus.api.TaskControllerInterface;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.TaskActivity;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.eao.ShopEao;
import de.vaplus.client.eao.StateEao;
import de.vaplus.client.eao.TaskEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.ShopAliasEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.ShopGroupEntity;
import de.vaplus.client.entity.TaskActivityEntity;
import de.vaplus.client.entity.VOEntity;


@Stateless
public class TaskController implements TaskControllerInterface {
	
	private static final long serialVersionUID = -6829064751540540307L;
	
	@Inject
    private TaskEao taskEao;

	@Override
	public List<? extends TaskActivity> getTaskList(){
		return taskEao.getTaskList();
	}

	@Override
	public TaskActivity getTaskById(long id) {
		return taskEao.getTaskById(id);
	}

	private TaskActivity factoryTaskActivity() {
		TaskActivityEntity taskActivity = new TaskActivityEntity();
		return taskActivity;
	}

	@Override
	public TaskActivity saveTaskActivity(TaskActivity taskActivity) {
		return taskEao.saveTaskActivity(taskActivity);
	}

	@Override
	public TaskActivity factoryTaskActivity(User activeUser) {
		TaskActivityEntity taskActivity = (TaskActivityEntity) factoryTaskActivity();
		((List<User>)taskActivity.getUserReceipientList()).add(activeUser);
		taskActivity.setUser(activeUser);
		return taskActivity;
	}

	@Override
	public List<? extends TaskActivity> getCreatedTaskList(User activeUser) {
		return taskEao.getcreatedTaskList(activeUser);
	}

	@Override
	public List<? extends TaskActivity> getOwnTaskList(User activeUser, Shop activeShop) {
		if(activeUser == null || taskEao == null)
			return new ArrayList<TaskActivity>();
		
		return taskEao.getTaskList(activeUser, activeUser.getUserGroup(), activeShop);
	}

	@Override
	public List<? extends TaskActivity> getOpenedTaskList(User activeUser, Shop activeShop) {
		if(activeUser == null || taskEao == null)
			return new ArrayList<TaskActivity>();
		
		return taskEao.getOpenedTaskList(activeUser, activeUser.getUserGroup(), activeShop);
	}

	@Override
	public int countOpenedTaskList(User activeUser, Shop activeShop) {
		if(activeUser == null || taskEao == null)
			return 0;
		
		return taskEao.countOpenedTaskList(activeUser, activeUser.getUserGroup(), activeShop);
	}
}
