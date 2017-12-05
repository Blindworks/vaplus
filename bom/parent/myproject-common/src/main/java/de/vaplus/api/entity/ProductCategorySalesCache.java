package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface ProductCategorySalesCache extends Base{

	int getYear();

	void setYear(int year);

	int getMonth();

	void setMonth(int month);

	BigDecimal getPieces();

	void setPieces(BigDecimal pieces);

	void setProductCategory(ProductCategory productCategory);

	ProductCategory getProductCategory();

	User getUser();

	void setUser(User user);

	Shop getShop();

	void setShop(de.vaplus.api.entity.Shop shop);

	VO getVo();

	void setVo(VO vo);

}
