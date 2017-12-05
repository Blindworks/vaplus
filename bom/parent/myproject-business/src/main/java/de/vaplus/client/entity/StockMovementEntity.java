package de.vaplus.client.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;

/**
 * Entity implementation class for Entity: Shop
 *
 */
@Entity
@Table(name="StockMovement")
@NamedQueries({
	@NamedQuery(
	        name = "StockMovement.listAll",
	        query = "SELECT s FROM StockMovementEntity s",
	        hints = {
	                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
	            }
    ),@NamedQuery(
            name = "StockMovement.listByStock",
            query = "SELECT m FROM StockMovementEntity m JOIN m.stockPickslip p WHERE p.stock = :stock",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	),@NamedQuery(
            name = "StockMovement.listByProduct",
            query = "SELECT m FROM StockMovementEntity m WHERE m.product = :product",
            hints = {
                    @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
                }
	)
})

//        classes = @ConstructorResult(
//                targetClass = SingleProductStockLevelValue.class,
//                columns = {
//                    @ColumnResult(name = "stock_id", column="stock_id", type = Long.class),
//                    @ColumnResult(name = "serial", column="serial"),
//                    @ColumnResult(name = "stockName", column="stockName")
////                    @FieldResult(name="id", column="order_id"),
////                    @FieldResult(name="quantity", column="order_quantity"),
////                    @FieldResult(name="item", column="order_item")})
//                    }
//                )
//)
public class StockMovementEntity extends BaseEntity implements StockMovement {

	private static final long serialVersionUID = -2206204007394461974L;

	@ManyToOne
    @JoinColumn(name="stockPickslip_id", nullable = false)
    private StockPickslipEntity stockPickslip;

	@ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private ProductEntity product;

	@Column(name="quantity", precision = 10, scale = 2, nullable = false)
	private BigDecimal quantity;

	@Column(name="serial", nullable = false)
	private String serial;

	@Column(name="purchasePrice", precision = 10, scale = 2, nullable = false)
	private BigDecimal purchasePrice;
	
	public StockMovementEntity() {
		super();
	}

	@Override
	public StockPickslip getStockPickslip() {
		return stockPickslip;
	}

	@Override
	public void setStockPickslip(StockPickslip stockPickslip) {
		this.stockPickslip = (StockPickslipEntity) stockPickslip;
	}

	@Override
	public Product getProduct() {
		return product;
	}

	@Override
	public void setProduct(Product product) {
		this.product = (ProductEntity) product;
	}

	@Override
	public BigDecimal getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getSerial() {
		return serial;
	}

	@Override
	public void setSerial(String serial) {
		this.serial = serial;
	}

	@Override
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	@Override
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}


}
