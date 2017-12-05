package de.vaplus.api.entity;

import java.util.List;

public interface ProductCategory extends StatusBase{

	String getName();

	void setName(String name);

	String getColor();

	void setColor(String color);

	boolean isShowInOverview();

	void setShowInOverview(boolean showInOverview);

	ProductCategory getParentCategory();

	void setParentCategory(ProductCategory parentCategory);

	List<? extends ProductCategory> getChildCategoryList();

	void setChildCategoryList(List<? extends ProductCategory> childCategoryList);
}
