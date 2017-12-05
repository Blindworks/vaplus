package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.EmploymentForm;
import de.vaplus.api.entity.JobTitle;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="EmploymentForm")
@NamedQueries({
    @NamedQuery(
        name = "EmploymentForm.getList",
        query = "SELECT e FROM EmploymentFormEntity e WHERE e.deleted = false ORDER BY e.name ASC"
        ,
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    	)
})
public class EmploymentFormEntity extends BaseEntity implements EmploymentForm {
	
	private static final long serialVersionUID = 506158317773705041L;
	
	@Column(name="name", nullable = false)
	private String name;

	public EmploymentFormEntity() {
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
