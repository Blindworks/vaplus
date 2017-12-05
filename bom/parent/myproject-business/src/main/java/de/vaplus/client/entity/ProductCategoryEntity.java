package de.vaplus.client.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.ProductCategory;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="ProductCategory")
public class ProductCategoryEntity extends StatusBaseEntity implements ProductCategory {

	private static final long serialVersionUID = -4290052364407469091L;
	
	@Column(name="name", nullable = false)
	private String name;

	@Column(name="color", nullable = true, length=30)
	private String color;

	@Column(name="showInOverview")
	private boolean showInOverview;

    @ManyToOne 
    @JoinColumn(name="parentCategory_id", nullable = true)
    private ProductCategoryEntity parentCategory;

	@OneToMany(mappedBy="parentCategory", fetch = FetchType.LAZY)
	private List<ProductCategoryEntity> childCategoryList;

	public ProductCategoryEntity() {
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
	public String getColor() {
		return color;
	}

	@Override
	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public boolean isShowInOverview() {
		return showInOverview;
	}

	@Override
	public void setShowInOverview(boolean showInOverview) {
		this.showInOverview = showInOverview;
	}

	@Override
	public ProductCategory getParentCategory() {
		return parentCategory;
	}

	@Override
	public void setParentCategory(ProductCategory parentCategory) {
		this.parentCategory = (ProductCategoryEntity) parentCategory;
	}

	@Override
	public List<? extends ProductCategory> getChildCategoryList() {
		if(childCategoryList == null)
			childCategoryList = new LinkedList<ProductCategoryEntity>();
		return childCategoryList;
	}

	@Override
	public void setChildCategoryList(List<? extends ProductCategory> childCategoryList) {
		this.childCategoryList = (List<ProductCategoryEntity>) childCategoryList;
	}
   
}
