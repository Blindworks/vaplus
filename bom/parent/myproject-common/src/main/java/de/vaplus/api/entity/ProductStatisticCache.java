package de.vaplus.api.entity;

public interface ProductStatisticCache extends Base{

	int getYear();

	void setYear(int year);

	int getMonth();

	void setMonth(int month);

	int getPieces();

	void setPieces(int pieces);

	User getUser();

	void setUser(User user);

	Shop getShop();

	VO getVo();

	void setVo(VO vo);

	void setShop(Shop shop);

	ProductStatistic getProductStatistic();

	void setProductStatistic(ProductStatistic productStatistic);

	void addPieces(int pieces);

	void clearPieces();

}
