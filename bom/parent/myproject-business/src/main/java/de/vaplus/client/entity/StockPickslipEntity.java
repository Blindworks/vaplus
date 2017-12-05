package de.vaplus.client.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.CommissionController;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductCategorySalesCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.State;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.Supplier;
import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="StockPickslip")
@NamedQueries({
	@NamedQuery(
	        name = "StockPickslip.listAll",
	        query = "SELECT s FROM StockPickslipEntity s",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    ),@NamedQuery(
            name = "StockPickslip.listByStock",
            query = "SELECT s FROM StockPickslipEntity s WHERE s.stock = :stock AND s.supplier = :supplier",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	),@NamedQuery(
            name = "StockPickslip.getByNumber",
            query = "SELECT s FROM StockPickslipEntity s WHERE s.number = :number",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	)
})
public class StockPickslipEntity extends ActivityEntity implements StockPickslip {

	private static final long serialVersionUID = -2206204007394461974L;

    @Column(name="number", nullable = false)
    private String number;

	@ManyToOne
    @JoinColumn(name="stock_id", nullable = false)
    private StockEntity stock;

	@ManyToOne
    @JoinColumn(name="src_stock_id", nullable = false)
    private StockEntity src_stock;
	
	@ManyToOne
    @JoinColumn(name="dst_stock_id", nullable = false)
    private StockEntity dst_stock;

	@ManyToOne
    @JoinColumn(name="supplier_id", nullable = false)
    private SupplierEntity supplier;
	
	@ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    private OrderEntity order;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="stockPickslip")
	private List<StockMovementEntity> stockMovementList;
	
	@ManyToOne
    @JoinColumn(name="pickslipReference_id", nullable = false)
    private StockPickslipEntity pickslipReference;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="pickslipReference")
	private List<StockPickslipEntity> pickslipReimportList;
	
    @Column(name="completelyReImported", nullable = false)
    private boolean completelyReImported;
	
	public StockPickslipEntity() {
		super();
	}

	
	@PrePersist
	public void initRelations(){
		if(getStockMovementList().size() > 0){
			Iterator<StockMovementEntity> i = (Iterator<StockMovementEntity>) getStockMovementList().iterator();
			while(i.hasNext()){
				i.next().setStockPickslip(this);
			}
		}
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public void setStock(Stock stock) {
		this.stock = (StockEntity) stock;
	}

	@Override
	public Stock getSrc_stock() {
		return src_stock;
	}

	@Override
	public void setSrc_stock(Stock src_stock) {
		this.src_stock = (StockEntity) src_stock;
	}

	@Override
	public Stock getDst_stock() {
		return dst_stock;
	}

	@Override
	public void setDst_stock(Stock dst_stock) {
		this.dst_stock = (StockEntity) dst_stock;
	}

	@Override
	public Supplier getSupplier() {
		return supplier;
	}

	@Override
	public void setSupplier(Supplier supplier) {
		this.supplier = (SupplierEntity) supplier;
	}

	@Override
	public Order getOrder() {
		return order;
	}

	@Override
	public void setOrder(Order order) {
		this.order = (OrderEntity) order;
	}


	@Override
	public List<? extends StockMovement> getStockMovementList() {
		if(stockMovementList == null)
			stockMovementList = new LinkedList<StockMovementEntity>();
		return stockMovementList;
	}

	@Override
	public void setStockMovementList(List<? extends StockMovement> stockMovementList) {
		this.stockMovementList = (List<StockMovementEntity>) stockMovementList;
	}


	@Transient
	@Override
	public BigDecimal getMovementCount() {
		BigDecimal count = new BigDecimal(0);
		
		Iterator<? extends StockMovement> i = getStockMovementList().iterator();
		StockMovement sm;
		while(i.hasNext()){
			sm = i.next();
			count = count.add(sm.getQuantity());
		}
		
		return count;
	}

	@Transient
	@Override
	public BigDecimal getPurchaseSum() {
		BigDecimal sum = new BigDecimal(0);
		
		Iterator<? extends StockMovement> i = getStockMovementList().iterator();
		StockMovement sm;
		while(i.hasNext()){
			sm = i.next();
			sum = sum.add(sm.getQuantity().multiply(sm.getPurchasePrice()));
		}
		
		return sum;
	}

	@Override
	public StockPickslip getPickslipReference() {
		return pickslipReference;
	}

	@Override
	public void setPickslipReference(StockPickslip pickslipReference) {
		this.pickslipReference = (StockPickslipEntity) pickslipReference;
	}

	@Override
	public List<? extends StockPickslip> getPickslipReimportList() {
		if(pickslipReimportList == null)
			pickslipReimportList = new LinkedList<StockPickslipEntity>();
		return pickslipReimportList;
	}

	@Override
	public void setPickslipReimportList(List<? extends StockPickslip> pickslipReimportList) {
		this.pickslipReimportList = (List<StockPickslipEntity>) pickslipReimportList;
	}

	@Override
	public boolean isCompletelyReImported() {
		return completelyReImported;
	}

	@Override
	public void setCompletelyReImported(boolean completelyReImported) {
		this.completelyReImported = completelyReImported;
	}


}
