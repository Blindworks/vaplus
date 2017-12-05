package de.vaplus.api.entity;

import java.util.Date;
import java.util.List;

public interface TaskActivity  extends Activity {

	boolean isToEveryone();

	void setToEveryone(boolean toEveryone);

	Date getCompletedDate();

	void setCompletedDate(Date completedDate);

	User getCompletedByUser();

	void setCompletedByUser(User completedByUser);

	String getTitle();

	void setTitle(String title);

	List<? extends UserGroup> getUserGroupReceipientList();

	void setUserGroupReceipientList(List<? extends UserGroup> userGroupReceipientList);

	List<? extends Shop> getShopReceipientList();

	void setShopReceipientList(List<? extends Shop> shopReceipientList);

	List<? extends User> getUserReceipientList();

	void setUserReceipientList(List<? extends User> userReceipientList);

	Date getTargetDate();

	void setTargetDate(Date targetDate);

	boolean isCompleted();

	boolean isOverdue();

	String getReceipients();

}
