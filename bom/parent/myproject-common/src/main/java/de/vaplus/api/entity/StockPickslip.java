package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.List;

public interface StockPickslip extends Activity{

	Stock getSrc_stock();

	void setSrc_stock(Stock src_stock);

	Stock getDst_stock();

	void setDst_stock(Stock dst_stock);

	Supplier getSupplier();

	void setSupplier(Supplier supplier);

	List<? extends StockMovement> getStockMovementList();

	void setStockMovementList(List<? extends StockMovement> stockMovementList);

	BigDecimal getMovementCount();

	BigDecimal getPurchaseSum();

	Stock getStock();

	void setStock(Stock stock);

	StockPickslip getPickslipReference();

	void setPickslipReference(StockPickslip pickslipReference);

	String getNumber();

	void setNumber(String number);

	Order getOrder();

	void setOrder(Order order);

	List<? extends StockPickslip> getPickslipReimportList();

	void setPickslipReimportList(List<? extends StockPickslip> pickslipReimportList);

	boolean isCompletelyReImported();

	void setCompletelyReImported(boolean completelyReImported);
}
