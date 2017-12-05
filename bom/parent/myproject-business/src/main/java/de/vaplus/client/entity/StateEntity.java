package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.State;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="State")
@NamedQueries({
    @NamedQuery(
        name = "State.getStates",
        query = "SELECT s FROM StateEntity s Order by s.name ASC",
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    )
})
public class StateEntity extends BaseEntity implements State {
	
	private static final long serialVersionUID = -1851368775300420138L;
	
	@Column(name="name")
	private String name;

	public StateEntity() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
