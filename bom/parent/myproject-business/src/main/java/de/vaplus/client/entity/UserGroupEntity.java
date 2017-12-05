package de.vaplus.client.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.UserGroup;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="UserGroup")
@NamedQueries({
    @NamedQuery(
        name = "UserGroup.getAll",
        query = "SELECT g FROM UserGroupEntity g WHERE g.deleted = false ORDER BY g.name",
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
        }
    )
})
public class UserGroupEntity extends StatusBaseEntity implements UserGroup {

	private static final long serialVersionUID = 4327413636914767211L;
	
	@Column(name="name", nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "userGroup")
    private List<UserGroupPermissionEntity> permissionList;

	public UserGroupEntity() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<UserGroupPermissionEntity> getPermissionList() {
		if(permissionList == null)
			permissionList = new ArrayList<UserGroupPermissionEntity>();
		return permissionList;
	}

	public void setPermissionList(List<UserGroupPermissionEntity> permissionList) {
		this.permissionList = permissionList;
	}

	@Override
	public String toString(){
		return this.getName();
	}
   
}
