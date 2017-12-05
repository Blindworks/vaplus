package de.vaplus.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.ProductStatistic;
import de.vaplus.api.entity.ProductStatisticCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;

@Entity
@Table(name="ProductStatisticCache")
public class ProductStatisticCacheEntity extends BaseEntity implements ProductStatisticCache{

	private static final long serialVersionUID = -8744750083751563929L;

	@Column(name="year", nullable = false)
	private int year;

	@Column(name="month", nullable = false)
	private int month;

	@Column(name="pieces", nullable = false)
	private int pieces;

	@ManyToOne
    @JoinColumn(name="productStatistic_id", nullable = false)
    private ProductStatisticEntity productStatistic;

	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

	@ManyToOne
    @JoinColumn(name="shop_id", nullable = false)
    private ShopEntity shop;

	@ManyToOne
    @JoinColumn(name="vo_id", nullable = false)
    private VOEntity vo;
	
	public ProductStatisticCacheEntity(){
		
	}
	
	public ProductStatisticCacheEntity(ProductStatistic productStatistic, int year, int month){
		this.setYear(year);
		this.setMonth(month);
		this.setProductStatistic(productStatistic);
	}

	@Override
	public int getYear() {
		return year;
	}

	@Override
	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public int getMonth() {
		return month;
	}

	@Override
	public void setMonth(int month) {
		this.month = month;
	}

	@Override
	public int getPieces() {
		return pieces;
	}

	@Override
	public void setPieces(int pieces) {
		this.pieces = pieces;
	}

	@Override
	public ProductStatistic getProductStatistic() {
		return productStatistic;
	}

	@Override
	public void setProductStatistic(ProductStatistic productStatistic) {
		this.productStatistic = (ProductStatisticEntity) productStatistic;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = (UserEntity) user;
	}

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public void setShop(Shop shop) {
		this.shop = (ShopEntity) shop;
	}

	@Override
	public VO getVo() {
		return vo;
	}

	@Override
	public void setVo(VO vo) {
		this.vo = (VOEntity) vo;
	}
	
	@Override
	public void addPieces(int pieces){
		setPieces(getPieces() + pieces);
	}
	
	@Override
	public void clearPieces(){
		setPieces(0);
	}
	
}
