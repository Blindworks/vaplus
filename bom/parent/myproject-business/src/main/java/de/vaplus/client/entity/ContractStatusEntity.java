package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.ContractStatus;
import de.vaplus.api.entity.EventType;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ContractStatus")
@NamedQueries({
    @NamedQuery(
        name = "ContractStatus.getStatusList",
        query = "SELECT s FROM ContractStatusEntity s Order by s.name ASC",
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    )
})
public class ContractStatusEntity extends BaseEntity implements ContractStatus {
	
	private static final long serialVersionUID = 8636707876149900449L;

	@Column(name="name")
	private String name;

	@Column(name="editable")
	private boolean editable;

	@Column(name="showInExtensionOfTheTermList")
	private boolean showInExtensionOfTheTermList;

	public ContractStatusEntity() {
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
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public boolean isShowInExtensionOfTheTermList() {
		return showInExtensionOfTheTermList;
	}

	@Override
	public void setShowInExtensionOfTheTermList(boolean showInExtensionOfTheTermList) {
		this.showInExtensionOfTheTermList = showInExtensionOfTheTermList;
	}

	
}