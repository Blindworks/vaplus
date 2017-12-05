package de.vaplus.api;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.Supplier;
import de.vaplus.client.querymapping.SingleProductStockLevelValueInterface;

public interface StockControllerInterface extends Serializable{

	Stock getStock(long id);

	List<? extends Stock> getStockList();

	List<? extends Stock> getStockList(boolean showDisabled);

	Stock saveStock(Stock stock);

	Stock factoryNewStock();

	Stock refreshStock(Stock stock);

	StockMovement factoryNewStockMovement();

	StockPickslip factoryNewStockPickslip();

	StockPickslip saveStockPickslip(StockPickslip stockPickslip) throws Exception;

	List<? extends StockPickslip> getStockPickslipList(Stock stock, Supplier supplier);

	List<? extends StockMovement> getStockMovementList(Product product);

	BigDecimal getStockLevel(Stock stock, Product product);

	BigDecimal getAveragePurchasePrice(Product product);

	void updateProductStockCache();

	BigDecimal getMinPurchasePrice(Product product);

	BigDecimal getMaxPurchasePrice(Product product);

	boolean isProductOnStock(Stock stock, Product product, String serial);

	StockPickslip getStockPickslip(Long id);

	StockPickslip getStockPickslipByNumber(String numer);

	void updateDocImage(InputStream is, String contentType, long size);

	DBFile getDocImage();

	List<? extends SingleProductStockLevelValueInterface> getStockLevelForSingleProduct(Product product);

	StockPickslip refreshPickslip(StockPickslip exportPickslip);

	List<? extends StockPickslip> getOpenStockPickslipExports();

}
