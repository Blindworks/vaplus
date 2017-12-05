package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.CommissionController;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopDocNumberRanges;
import de.vaplus.api.entity.ShopGroup;

@Entity
@Table(name="ShopDocNumberRanges")
@NamedQueries({
	@NamedQuery(
	        name = "ShopDocNumberRanges.get",
	        query = "SELECT s FROM ShopDocNumberRangesEntity s WHERE s.shop = :shop"
    )
})
public class ShopDocNumberRangesEntity implements ShopDocNumberRanges  {

	private static final long serialVersionUID = 2116842967731454207L;

	@Id
	@OneToOne
    @JoinColumn(name="shop_id", nullable = true)
    private ShopEntity shop;

	@Column(name="invoiceNumber")
	private long invoiceNumber;

	@Column(name="orderNumber")
	private long orderNumber;

	@Column(name="pickslipNumber")
	private long pickslipNumber;

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public void setShop(Shop shop) {
		this.shop = (ShopEntity) shop;
	}

	@Override
	public long getInvoiceNumber() {
		return invoiceNumber;
	}

	@Override
	public void setInvoiceNumber(long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	@Override
	public long getOrderNumber() {
		return orderNumber;
	}

	@Override
	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public long getPickslipNumber() {
		return pickslipNumber;
	}

	@Override
	public void setPickslipNumber(long pickslipNumber) {
		this.pickslipNumber = pickslipNumber;
	}

}
