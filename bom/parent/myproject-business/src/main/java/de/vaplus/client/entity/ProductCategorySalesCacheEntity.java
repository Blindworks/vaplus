package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;

@Entity
@Table(name="ProductCategorySalesCache")
public class ProductCategorySalesCacheEntity extends BaseEntity implements ProductCategorySalesCache{

	private static final long serialVersionUID = 5807413374486955915L;

	@Column(name="year", nullable = false)
	private int year;

	@Column(name="month", nullable = false)
	private int month;

	@Column(name="pieces", nullable = false)
	private BigDecimal pieces;

	@ManyToOne
    @JoinColumn(name="productCategory_id", nullable = false)
    private ProductCategoryEntity productCategory;

	@ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserEntity user;

	@ManyToOne
    @JoinColumn(name="shop_id", nullable = false)
    private ShopEntity shop;

	@ManyToOne
    @JoinColumn(name="vo_id", nullable = false)
    private VOEntity vo;
	
	public ProductCategorySalesCacheEntity(){
		this.setPieces(new BigDecimal(0));
	}
	
	public ProductCategorySalesCacheEntity(ProductCategory category, int year, int month){
		this.setYear(year);
		this.setMonth(month);
		this.setProductCategory(category);
		this.setPieces(new BigDecimal(0));
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
	public BigDecimal getPieces() {
		if(pieces == null)
			pieces = new BigDecimal(0);
		return pieces;
	}

	@Override
	public void setPieces(BigDecimal pieces) {
		this.pieces = pieces;
	}

	@Override
	public ProductCategoryEntity getProductCategory() {
		return productCategory;
	}

	@Override
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = (ProductCategoryEntity) productCategory;
	}

	@Override
	public UserEntity getUser() {
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
	
}
