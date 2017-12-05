package de.vaplus.client.entity;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.Note;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.TaskActivity;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserGroup;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="TaskActivity")
@NamedQueries({
    @NamedQuery(
            name = "TaskActivity.getAll",
                    query = "SELECT t FROM TaskActivityEntity t ORDER BY t.targetDate ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "TaskActivity.getTaskList",
                    query = "SELECT t FROM TaskActivityEntity t LEFT JOIN t.userReceipientList uR LEFT JOIN t.userGroupReceipientList uGR LEFT JOIN t.shopReceipientList sR WHERE uR = :user or uGR = :userGroup or sR = :shop or t.toEveryone = true ORDER BY t.targetDate ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "TaskActivity.getTaskListByCreator",
                    query = "SELECT t FROM TaskActivityEntity t WHERE t.user = :user ORDER BY t.targetDate ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        ),
    @NamedQuery(
            name = "TaskActivity.getOpenedTaskList",
                    query = "SELECT t FROM TaskActivityEntity t LEFT JOIN t.userReceipientList uR LEFT JOIN t.userGroupReceipientList uGR LEFT JOIN t.shopReceipientList sR WHERE (uR = :user or uGR = :userGroup or sR = :shop or t.toEveryone = true) and t.completedDate IS NULL ORDER BY t.targetDate ASC",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
        )
})
public class TaskActivityEntity extends ActivityEntity implements TaskActivity {


	private static final long serialVersionUID = 1418706915052911149L;

	@Column(name="toEveryone", nullable = false)
    private boolean toEveryone;

	@Column(name="title", nullable = false)
    private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="targetDate", nullable = true)
	private Date targetDate;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="TaskActivity_UserGroupReceipientList",
	joinColumns={@JoinColumn(name="taskActivity_id", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="userGroup_id", referencedColumnName="id")})
    private List<UserGroupEntity> userGroupReceipientList;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="TaskActivity_ShopReceipientList",
	joinColumns={@JoinColumn(name="taskActivity_id", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="shop_id", referencedColumnName="id")})
    private List<ShopEntity> shopReceipientList;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="TaskActivity_UserReceipientList",
	joinColumns={@JoinColumn(name="taskActivity_id", referencedColumnName="id")},
	inverseJoinColumns={@JoinColumn(name="user_id", referencedColumnName="id")})
    private List<UserEntity> userReceipientList;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="completedDate", nullable = true)
	private Date completedDate;

	@ManyToOne
    @JoinColumn(name="completedByUser_id", nullable = false)
    private UserEntity completedByUser;
	

	public TaskActivityEntity() {
		super();
	}

	@Override
	public boolean isToEveryone() {
		return toEveryone;
	}

	@Override
	public void setToEveryone(boolean toEveryone) {
		this.toEveryone = toEveryone;
	}

	@Override
	public List<? extends UserGroup> getUserGroupReceipientList() {
		if(userGroupReceipientList == null)
			userGroupReceipientList = new LinkedList<UserGroupEntity>();
		return userGroupReceipientList;
	}

	@Override
	public void setUserGroupReceipientList(List<? extends UserGroup> userGroupReceipientList) {
		this.userGroupReceipientList = (List<UserGroupEntity>) userGroupReceipientList;
	}

	@Override
	public List<? extends Shop> getShopReceipientList() {
		if(shopReceipientList == null)
			shopReceipientList = new LinkedList<ShopEntity>();
		return shopReceipientList;
	}

	@Override
	public void setShopReceipientList(List<? extends Shop> shopReceipientList) {
		this.shopReceipientList = (List<ShopEntity>) shopReceipientList;
	}

	@Override
	public List<? extends User> getUserReceipientList() {
		if(userReceipientList == null)
			userReceipientList = new LinkedList<UserEntity>();
		return userReceipientList;
	}

	@Override
	public void setUserReceipientList(List<? extends User> userReceipientList) {
		this.userReceipientList = (List<UserEntity>) userReceipientList;
	}

	@Override
	public Date getCompletedDate() {
		return completedDate;
	}

	@Override
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	@Override
	public User getCompletedByUser() {
		return completedByUser;
	}

	@Override
	public void setCompletedByUser(User completedByUser) {
		this.completedByUser = (UserEntity) completedByUser;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Date getTargetDate() {
		return targetDate;
	}

	@Override
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	@Transient
	@Override
	public boolean isCompleted(){
		if(this.getCompletedDate() != null && this.getCompletedByUser() != null)
			return true;
		
		return false;
	}
	
	@Transient
	@Override
	public boolean isOverdue(){
		if(isCompleted() == false && getTargetDate() != null && getTargetDate().compareTo(new Date()) < 0)
			return true;
		
		return false;
	}

	@Transient
	@Override
	public String getReceipients(){
		String receipients = "";
		
		if(this.isToEveryone())
			receipients += "Jeder";
		
		Iterator<? extends User> i1 = getUserReceipientList().iterator();
		User u;
		while(i1.hasNext()){
			u = i1.next();
			if(u == null)
				continue;
			
			if(receipients.length() > 0)
				receipients += ", ";
			
			receipients += u.getName();
		}
		
		Iterator<? extends UserGroup> i2 = getUserGroupReceipientList().iterator();
		UserGroup ug;
		while(i2.hasNext()){
			ug = i2.next();
			if(ug == null)
				continue;
			
			if(receipients.length() > 0)
				receipients += ", ";
			
			receipients += ug.getName();
		}
		
		Iterator<? extends Shop> i3 = getShopReceipientList().iterator();
		Shop s;
		while(i3.hasNext()){
			s= i3.next();
			if(s == null)
				continue;
				
			if(receipients.length() > 0)
				receipients += ", ";
			receipients += s.getName();
		}
		
		return receipients;
	}
	
}
