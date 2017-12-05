package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.JobTitle;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="JobTitle")
@NamedQueries({
    @NamedQuery(
        name = "JobTitle.getList",
        query = "SELECT j FROM JobTitleEntity j WHERE j.deleted = false ORDER BY j.name ASC"
        ,
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    	)
})
public class JobTitleEntity extends BaseEntity implements JobTitle {

	private static final long serialVersionUID = -7472809210474740296L;
	
	@Column(name="name", nullable = false)
	private String name;

	public JobTitleEntity() {
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

	@Override
	public String toString(){
		return this.getName();
	}
   
}
