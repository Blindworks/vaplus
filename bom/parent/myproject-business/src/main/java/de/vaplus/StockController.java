package de.vaplus;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.TaxRateControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.ProductStockCache;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.Supplier;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.eao.NumberRangeEao;
import de.vaplus.client.eao.ProductEao;
import de.vaplus.client.eao.StockEao;
import de.vaplus.client.entity.BaseProductCombinationEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.CellPhoneTariffEntity;
import de.vaplus.client.entity.LandlineTariffEntity;
import de.vaplus.client.entity.PrePaidTariffEntity;
import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.ProductEntity;
import de.vaplus.client.entity.ProductOptionEntity;
import de.vaplus.client.entity.StockEntity;
import de.vaplus.client.entity.StockMovementEntity;
import de.vaplus.client.entity.StockPickslipEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.VOTypeEntity;
import de.vaplus.client.entity.VendorEntity;
import de.vaplus.client.querymapping.SingleProductStockLevelValueInterface;


@Stateless
public class StockController implements StockControllerInterface {

	private static final long serialVersionUID = -1409525157192414774L;

	@EJB
	private ProductControllerInterface productController;
	
	@EJB
	private PropertyControllerInterface propertyController;
	
	@EJB
	private FileControllerInterface fileController;
	
	@EJB
	private ShopControllerInterface shopController;

	@Inject
    private StockEao eao;

	@Inject
    private NumberRangeEao numberRangeEao;


	@Override
	public Stock getStock(long id) {
		return eao.getStock(id);
	}

	@Override
	public List<? extends Stock> getStockList() {
		return eao.getStockList();
	}

	@Override
	public List<? extends Stock> getStockList(boolean showDisabled) {
		return eao.getStockList(showDisabled);
	}

	@Override
	public Stock saveStock(Stock stock) {
    	
    	if(! (stock instanceof StockEntity))
    		return stock;
    	
		stock =  eao.saveStock((StockEntity) stock);

		return stock;
    	
	}

	@Override
	public Stock factoryNewStock() {
		StockEntity stock = new StockEntity();
		return stock;
	}

	@Override
	public StockMovement factoryNewStockMovement() {
		StockMovementEntity stockMovement = new StockMovementEntity();
		return stockMovement;
	}

	@Override
	public StockPickslip factoryNewStockPickslip() {
		StockPickslipEntity stockPickslip = new StockPickslipEntity();
		return stockPickslip;
	}

	@Override
	public Stock refreshStock(Stock stock) {
		if(stock == null || stock.getId() == 0)
			return stock;
		return eao.refreshStock(stock);
	}

	@Override
	public StockPickslip refreshPickslip(StockPickslip pickslip) {
		if(pickslip == null || pickslip.getId() == 0)
			return pickslip;
		return eao.refreshPickslip(pickslip);
	}
	
	@Override
	public StockPickslip saveStockPickslip(StockPickslip stockPickslip) throws Exception {
		
		setNextPickslipNumber(stockPickslip);
		
		StockPickslip sps = eao.saveStockPickslip(stockPickslip);
		
		ProductEntity p;
		Iterator<? extends StockMovement> i = sps.getStockMovementList().iterator();
		StockMovement sm;
		while(i.hasNext()){
			sm = i.next();
			
			updateProductCache(sps.getStock(), (ProductEntity) sm.getProduct());
		}
		
		return sps;
	}
	
	private void setNextPickslipNumber(StockPickslip pickslip) throws Exception{
		if(pickslip.getNumber() != null)
			return;
		
		Shop shop = shopController.refreshShop(pickslip.getShop());
		
		long nextNumber = numberRangeEao.getNextPickslipNumber(shop);

		String pickslipNumber = "LI-"+shop.getId()+"-"+nextNumber;
		
		StockPickslip sp = getStockPickslipByNumber(pickslipNumber);
		if(sp != null){
			throw new Exception("Lieferscheinnummer "+pickslipNumber+" bereits vorhanden. Bitte prüfen Sie Ihre Einstellungen.");
		}
		
//		shopController.saveShop(shop);
		
		pickslip.setNumber(pickslipNumber);
	}

	@Override
	public List<? extends StockPickslip> getStockPickslipList(Stock stock, Supplier supplier) {
		return eao.getStockPickslipList(stock, supplier);
	}

	@Override
	public List<? extends StockMovement> getStockMovementList(Product product) {
		return eao.getStockMovementList(product);
	}

	@Override
	public BigDecimal getStockLevel(Stock stock, Product product) {
		return eao.getStockLevel(stock, product);
	}

	@Override
	public BigDecimal getAveragePurchasePrice(Product product) {
		return eao.getAveragePurchasePrice(product);
	}

	@Override
	public BigDecimal getMinPurchasePrice(Product product) {
		return eao.getMinPurchasePrice(product);
	}

	@Override
	public BigDecimal getMaxPurchasePrice(Product product) {
		return eao.getMaxPurchasePrice(product);
	}
	
	@Override
	@Schedule( minute="0", hour="2", persistent=false)
	public void updateProductStockCache(){
		Iterator<? extends Product> i = productController.getRetailProductList().iterator();
		ProductEntity p;
		Iterator<? extends Stock> is;
		Stock s;
		while(i.hasNext()){
			p = (ProductEntity) i.next();
			is = this.getStockList(true).iterator();
			while(is.hasNext()){
				s = is.next();
				
				updateProductCache(s,p);
			}
		}
	}
	
	public void updateProductCache(Stock s, ProductEntity p){
		BigDecimal qty,avergePurchasePrice,minPurchasePrice,maxPurchasePrice;
		
		qty = getStockLevel(s,p);
		avergePurchasePrice = getAveragePurchasePrice(p);
		minPurchasePrice = getMinPurchasePrice(p);
		maxPurchasePrice = getMaxPurchasePrice(p);
		
		// set new stock Quantity
		p.getProductStockCache(s).setQuantity(qty);
		
		// set new cached prices
		p.setAvergePurchasePrice(avergePurchasePrice);
		p.setMinPurchasePrice(minPurchasePrice);
		p.setMaxPurchasePrice(maxPurchasePrice);
		
		// set overall stock quantity
		Iterator<? extends ProductStockCache> i = p.getProductStockCacheList().iterator();
		qty = new BigDecimal(0);
		while(i.hasNext()){
			qty = qty.add(i.next().getQuantity());
			p.setStockQuantity(qty);
		}
		productController.saveProduct(p);
		
		
	}

	@Override
	public boolean isProductOnStock(Stock stock, Product product, String serial) {
		return eao.getStockLevel(stock, product, serial).compareTo(new BigDecimal(1)) ==  0;
	}

	@Override
	public StockPickslip getStockPickslip(Long id) {
		return eao.getStockPickslip(id);
	}

	@Override
	public StockPickslip getStockPickslipByNumber(String number) {
		return eao.getStockPickslipByNumber(number);
	}


	/*
	 * Returns filtered StockMovement
	 * Only Stock Serials are Returned and in which Stock
	 */
	@Override
	public List<? extends SingleProductStockLevelValueInterface> getStockLevelForSingleProduct(Product product){
		return eao.getStockLevelForSingleProduct(product);
	}
	


	@Override
	public void updateDocImage(InputStream is, String contentType, long size) {
		
		long docImageId = propertyController.getLongProperty("DOC_BG_IMAGE_ID", 0);

		DBFile docImage = null;

		if(docImageId > 0)
			docImage = fileController.getDBFileEntity(docImageId);
	
		docImage = fileController.updateDBFile(docImage, is, contentType, size);
		fileController.flushEao();
		
		System.out.println(docImage.getId());
		
		propertyController.updateLongProperty("DOC_BG_IMAGE_ID", docImage.getId());
		
	}

	@Override
	public DBFile getDocImage() {
		
		long docImageId = propertyController.getLongProperty("DOC_BG_IMAGE_ID", 0);
		
		DBFile docImage = null;

		if(docImageId > 0)
			docImage = fileController.getDBFileEntity(docImageId);
		
		return docImage;
	}

	@Override
	public List<? extends StockPickslip> getOpenStockPickslipExports() {
		return eao.getOpenStockPickslipExports();
	}
}
