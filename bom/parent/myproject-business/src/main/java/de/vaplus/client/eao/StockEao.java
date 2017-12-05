package de.vaplus.client.eao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.Stock;
import de.vaplus.api.entity.StockMovement;
import de.vaplus.api.entity.StockPickslip;
import de.vaplus.api.entity.Supplier;
import de.vaplus.client.entity.StockEntity;
import de.vaplus.client.entity.StockMovementEntity;
import de.vaplus.client.entity.StockPickslipEntity;
import de.vaplus.client.entity.StockPickslipEntity_;
import de.vaplus.client.querymapping.SingleProductStockLevelValue;
import de.vaplus.client.querymapping.SingleProductStockLevelValueInterface;


@Stateless
public class StockEao implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 2066749648262682869L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;


    public StockEntity saveStock(StockEntity stock){
    	return em.merge(stock);
    }
    
	public Stock getStock(long id) {
		return em.find(StockEntity.class, id);
	}


	public List<StockEntity> getStockList(){
		return getStockList(false);
	}

	public List<StockEntity> getStockList(boolean showDisabled){
		
		List<StockEntity> list = new ArrayList<StockEntity>();
		
		try{
	
			if(! showDisabled){
				
				list = em.createNamedQuery("Stock.listEnabled",StockEntity.class)
			            .setParameter("enabled", true)
			            .getResultList();
			}else{
				list = em.createNamedQuery("Stock.listAll",StockEntity.class)
			            .getResultList();
			}
		}
		catch(NoResultException e){
		}
		
		return list;
	}


	public Stock refreshStock(Stock stock) {
		Stock s = em.find(StockEntity.class, stock.getId());
		em.refresh(s);
		return s;
	}

	public StockPickslip refreshPickslip(StockPickslip pickslip) {
		StockPickslip p = em.find(StockPickslipEntity.class, pickslip.getId());
		em.refresh(p);
		return p;
	}

	public StockPickslip saveStockPickslip(StockPickslip stockPickslip) {
		stockPickslip = em.merge(stockPickslip);
		em.flush();
		// refresh do get per trigger generated number values
		StockPickslip sps = em.find(StockPickslipEntity.class, stockPickslip.getId());
		
		em.refresh(sps);
		return sps;
	}

	public List<? extends StockPickslip> getStockPickslipList(Stock stock, Supplier supplier) {

		List<? extends StockPickslip> list = new ArrayList<StockPickslipEntity>();
		
		TypedQuery<StockPickslipEntity> query;
		
		try{
			

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<StockPickslipEntity> cQuery = cb.createQuery(StockPickslipEntity.class);
			Root<StockPickslipEntity> root = cQuery.from(StockPickslipEntity.class);
			cQuery.select(root);

			List<Predicate> criteria = new ArrayList<Predicate>();

			if(stock != null){
				criteria.add(
						cb.equal(root.get(StockPickslipEntity_.stock), stock)
				);
			}

			if(supplier != null){
				criteria.add(
					cb.equal(root.get(StockPickslipEntity_.supplier), supplier)
				);
			}


			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));

			cQuery.orderBy(cb.desc(root.get(StockPickslipEntity_.creationDate)));

			list = em.createQuery(cQuery).getResultList();

		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends StockMovement> getStockMovementList(Product product) {
		
		List<StockMovementEntity> list = new ArrayList<StockMovementEntity>();
		
		if(product == null)
			return list;
		
		try{
	
			list = em.createNamedQuery("StockMovement.listByProduct",StockMovementEntity.class)
			            .setParameter("product", product)
			            .getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public BigDecimal getStockLevel(Stock stock, Product product) {
		
		BigDecimal count = new BigDecimal(0);
		
		if(product == null)
			return count;
		
		if(stock == null)
			return count;
		
		try{
			
			BigDecimal in = em.createQuery("SELECT SUM(sm.quantity) FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.dst_stock and sm.stockPickslip.stock = :stock", BigDecimal.class)
	
		            .setParameter("product", product)
		            .setParameter("stock", stock)
			        .getSingleResult();
			
//			System.out.println("Stock Level IN: "+in);
			
			if(in != null)
				count = count.add(in);
			
			BigDecimal out = em.createQuery("SELECT SUM(sm.quantity) FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.src_stock and sm.stockPickslip.stock = :stock", BigDecimal.class)
	
		            .setParameter("product", product)
		            .setParameter("stock", stock)
			        .getSingleResult();

//			System.out.println("Stock Level OUT: "+out);

			if(out != null)
				count = count.subtract(out);
		}
		catch(NoResultException e){
		}
		
		return count;
	}


	public BigDecimal getStockLevel(Stock stock, Product product, String serial) {
		BigDecimal count = new BigDecimal(0);
		
		if(product == null)
			return count;
		
		if(stock == null)
			return count;
		
		try{
			
			BigDecimal in = em.createQuery("SELECT SUM(sm.quantity) FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.dst_stock and sm.stockPickslip.stock = :stock AND sm.serial = :serial", BigDecimal.class)
	
		            .setParameter("product", product)
		            .setParameter("stock", stock)
		            .setParameter("serial", serial)
			        .getSingleResult();
			
//			System.out.println("Stock Level IN: "+in);
			
			if(in != null)
				count = count.add(in);
			
			BigDecimal out = em.createQuery("SELECT SUM(sm.quantity) FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.src_stock and sm.stockPickslip.stock = :stock AND sm.serial = :serial", BigDecimal.class)
	
		            .setParameter("product", product)
		            .setParameter("stock", stock)
		            .setParameter("serial", serial)
			        .getSingleResult();

//			System.out.println("Stock Level OUT: "+out);

			if(out != null)
				count = count.subtract(out);
		}
		catch(NoResultException e){
		}
		
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public List<? extends SingleProductStockLevelValueInterface> getStockLevelForSingleProduct(Product product){
		
		List<SingleProductStockLevelValue> list = new LinkedList<SingleProductStockLevelValue>();

		try{
			
			Query q = em.createNativeQuery("Select m.id, j1.stock_id, j1.serial, m.purchasePrice, s.name "+
			"FROM `StockMovement` m "+
			"JOIN( "+
			    "SELECT m1.id as m_id ,p1.id as p_id,m1.product_id,m1.serial,p1.stock_id, p1.src_stock_id, p1.dst_stock_id, count(m1.id) as in_count "+
						"FROM `StockMovement` m1, `StockPickslip` p1 "+
						"WHERE "+
			            "m1.product_id = ?1 "+
						"AND m1.stockPickslip_id = p1.id  "+
			            "AND p1.dst_stock_id = p1.stock_id  "+
			            "GROUP BY m1.serial,p1.stock_id "+
			    ") j1 "+
			    "ON j1.m_id = m.id "+
			"LEFT OUTER JOIN( "+
			    "SELECT m1.id as m_id ,p1.id as p_id,m1.product_id,m1.serial,p1.stock_id, p1.src_stock_id, p1.dst_stock_id, count(m1.id) as out_count "+
						"FROM `StockMovement` m1, `StockPickslip` p1 "+
						"WHERE  "+
			            "m1.product_id = ?2 "+
						"AND m1.stockPickslip_id = p1.id "+ 
			            "AND p1.src_stock_id = p1.stock_id  "+
			            "GROUP BY m1.serial,p1.stock_id "+
			    ") j2 "+
			    "ON j1.stock_id = j2.stock_id AND j1.serial = j2.serial "+
			    "LEFT OUTER JOIN `Stock` s ON j1.stock_id = s.id "+
			    "WHERE (j1.in_count - (CASE WHEN j2.out_count IS NULL THEN 0 ELSE j2.out_count END)) != 0 "+
			    "ORDER BY s.name", "SingleProductStockLevelValueMapping");

//			q.setParameter("product_id", product.getId());
			q.setParameter(1, product.getId()); 
			q.setParameter(2, product.getId()); 
//			q.setParameter("product_id", product.getId());
			
			list = q.getResultList();
			    
		}
		catch(NoResultException e){
		} 
		
		return list;
	}


	public BigDecimal getAveragePurchasePrice(Product product) {
		
		BigDecimal price = new BigDecimal(0);
		
		if(product == null)
			return price;
		
		try{

			BigDecimal sum = new BigDecimal(0);
			BigDecimal quantity = new BigDecimal(0);
			
			Object[] resultsIN = (Object[]) em.createQuery("SELECT sum(sm.purchasePrice * sm.quantity) , sum(sm.quantity) FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.dst_stock")
		            .setParameter("product", product)
			        .getSingleResult();

			if(resultsIN[0] != null)
				sum = sum.add((BigDecimal) resultsIN[0]);
			
			if(resultsIN[1] != null)
				quantity = quantity.add((BigDecimal) resultsIN[1]);
			

//			System.out.println("sum IN: "+(BigDecimal) resultsIN[0]);
//			System.out.println("quantty IN: "+(BigDecimal) resultsIN[1]);
			
			Object[] resultsOUT = (Object[]) em.createQuery("SELECT sum(sm.purchasePrice * sm.quantity) , sum(sm.quantity) FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.src_stock")
		            .setParameter("product", product)
			        .getSingleResult();

			if(resultsOUT[0] != null)
				sum = sum.subtract((BigDecimal) resultsOUT[0]);
			
			if(resultsOUT[1] != null)
				quantity = quantity.subtract((BigDecimal) resultsOUT[1]);

			if(quantity.compareTo(new BigDecimal(0)) != 0)
				price = sum.divide(quantity, 4, RoundingMode.HALF_UP);

//			System.out.println("sum OUT: "+(BigDecimal) resultsOUT[0]);
//			System.out.println("quantty OUT: "+(BigDecimal) resultsOUT[1]);
//
//			System.out.println("sum: "+sum);
//			System.out.println("quantty: "+quantty);
//
//			System.out.println("price: "+price);
			
		}
		catch(NoResultException e){
//			e.printStackTrace();
		}
		
		return price;
	}


	public BigDecimal getMinPurchasePrice(Product product) {
		
		BigDecimal purchasePricein = new BigDecimal(0);
		
		if(product == null)
			return purchasePricein;
		
		try{
			
			purchasePricein = em.createQuery("SELECT sm.purchasePrice FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.dst_stock ORDER BY sm.purchasePrice ASC", BigDecimal.class)
		            .setParameter("product", product)
		            .setMaxResults(1)
			        .getSingleResult();

		}
		catch(NoResultException e){
//			e.printStackTrace();
		}
		
		return purchasePricein;
	}

	public BigDecimal getMaxPurchasePrice(Product product) {
		
		BigDecimal purchasePricein = new BigDecimal(0);
		
		if(product == null)
			return purchasePricein;
		
		try{
			
			purchasePricein = em.createQuery("SELECT sm.purchasePrice FROM StockMovementEntity sm WHERE sm.product = :product AND sm.stockPickslip.stock = sm.stockPickslip.dst_stock ORDER BY sm.purchasePrice DESC", BigDecimal.class)
		            .setParameter("product", product)
		            .setMaxResults(1)
			        .getSingleResult();

		}
		catch(NoResultException e){
//			e.printStackTrace();
		}
		
		return purchasePricein;
	}

	public List<? extends Product> getStockProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	public StockPickslip getStockPickslip(Long id) {
		return em.find(StockPickslipEntity.class, id);
	}

	public StockPickslip getStockPickslipByNumber(String number) {
		try{
			return em.createNamedQuery("StockPickslip.getByNumber",StockPickslipEntity.class)
			            .setParameter("number", number)
			            .setMaxResults(1)
			            .getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return null;
	}

	public List<? extends StockPickslip> getOpenStockPickslipExports() {
		
		List<? extends StockPickslip> list = new LinkedList<StockPickslip>();
		
		try{
			
			list = em.createQuery("SELECT p FROM StockPickslipEntity p WHERE p.stock = p.src_stock AND p.dst_stock IS NOT NULL AND p.completelyReImported = false", StockPickslipEntity.class)
					.getResultList();

		}
		catch(NoResultException e){
//			e.printStackTrace();
		}
		
		return list;
	}
	
	/*
	 * 
	 
	 
	 SELECT SUM(quantity) FROM `StockMovement` sm, `StockPickSlip` sps WHERE product_id = 357 AND sm.stockPickslip_id = sps.id and sps.stock_id = sps.dst_stock_id and sps.stock_id = 10
	 
	  
	 * 
	 */
}
