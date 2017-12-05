package de.vaplus.api;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

public interface ActivityControllerInterface extends Serializable{

	List<? extends Activity> getCustomerTimeline(Customer owner, int limit,
			int offset);

	List<? extends Activity> getUserTimeline(User owner, int limit, int offset);

	List<? extends Activity> getShopTimeline(Shop owner, int limit, int offset);

	void createFileActivity(User creator, ActivityOwner fileOwner, Base fileRelation,
			InputStream inputStream, String fileName,
			String contentType, long size);

	FileActivity saveFileActivity(FileActivity fileActivity);



}
