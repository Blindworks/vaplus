package de.vaplus;

import java.io.InputStream;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.ActivityControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.entity.Activity;
import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.client.eao.ActivityEao;
import de.vaplus.client.entity.ActivityEntity;
import de.vaplus.client.entity.CustomerEntity;
import de.vaplus.client.entity.FileActivityEntity;
import de.vaplus.client.entity.FileEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;


@Stateless
public class ActivityController implements ActivityControllerInterface {

	private static final long serialVersionUID = 4315674787975062364L;
	
	@EJB
	private FileControllerInterface fileController;
	
	@Inject
    private ActivityEao activityEao;

	@Override
	public List<? extends Activity> getCustomerTimeline(Customer owner, int limit, int offset){
		return activityEao.getActivities(owner, limit, offset);
	}

	@Override
	public List<? extends Activity> getUserTimeline(User owner, int limit, int offset){
		return activityEao.getActivities(owner, limit, offset);
	}

	@Override
	public List<? extends Activity> getShopTimeline(Shop owner, int limit, int offset){
		return activityEao.getActivities(owner, limit, offset);
	}

	@Override
	public void createFileActivity(User creator, ActivityOwner fileOwner, Base fileRelation,
			InputStream is, String fileName, String mimeType,
			long size) {
		
		FileActivityEntity fileActivity = new FileActivityEntity();
		
		fileActivity.setCreator((UserEntity)creator);
		
		if(fileOwner instanceof User){
			fileActivity.setUser((UserEntity)fileOwner);
		}else if(fileOwner instanceof Customer){
			fileActivity.setCustomer((CustomerEntity)fileOwner);
		}else if(fileOwner instanceof Shop){
			fileActivity.setShop((ShopEntity)fileOwner);
		}
		
		if(fileRelation != null){
			fileActivity.setRelationClass(fileRelation.getClass().getSimpleName());
			fileActivity.setRelation(fileRelation.getId());
		}
		
		File file = fileController.saveFileSystemFile(is, fileName, mimeType, size);
		fileActivity.setFile((FileEntity) file);

		fileActivity.setEnabled(true);
		fileActivity.setInvisible(false);
		
		fileActivity = activityEao.saveActivity(fileActivity);
		
	}

	@Override
	public FileActivity saveFileActivity(FileActivity fileActivity) {
		return activityEao.saveActivity((ActivityEntity) fileActivity);
	}
	
}
