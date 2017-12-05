package de.vaplus.client.eao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopDocNumberRanges;
import de.vaplus.client.entity.ShopDocNumberRangesEntity;
import de.vaplus.client.entity.ShopDocNumberRangesEntity_;

@Stateless
public class NumberRangeEao implements Serializable{
	
	private static final long serialVersionUID = 8120891097087116368L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;


	public long getNextInvoiceNumber(Shop shop) {
		
		checkShopDocNumberRanges(shop);
		
		ShopDocNumberRangesEntity shopDocNumberRanges = null;
		
		try{
			
			Query updateQuery = em.createNativeQuery("UPDATE ShopDocNumberRanges SET invoiceNumber=invoiceNumber+1 WHERE shop_id=?").setParameter(1, shop.getId());
			updateQuery.executeUpdate();
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ShopDocNumberRangesEntity> cQuery = cb.createQuery(ShopDocNumberRangesEntity.class);
			Root<ShopDocNumberRangesEntity> root = cQuery.from(ShopDocNumberRangesEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get(ShopDocNumberRangesEntity_.shop), shop));
			
			shopDocNumberRanges = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
			
			em.refresh(shopDocNumberRanges);

		}
		catch(NoResultException e){
		}
		
		System.out.println("GET InvoiceNumber "+shopDocNumberRanges.getInvoiceNumber());
		
		return shopDocNumberRanges.getInvoiceNumber();
	}


	public long getNextPickslipNumber(Shop shop) {
		
		checkShopDocNumberRanges(shop);
		
		ShopDocNumberRangesEntity shopDocNumberRanges = null;
		
		try{
			
			Query updateQuery = em.createNativeQuery("UPDATE ShopDocNumberRanges SET pickslipNumber=pickslipNumber+1 WHERE shop_id=?").setParameter(1, shop.getId());
			updateQuery.executeUpdate();
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ShopDocNumberRangesEntity> cQuery = cb.createQuery(ShopDocNumberRangesEntity.class);
			Root<ShopDocNumberRangesEntity> root = cQuery.from(ShopDocNumberRangesEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get(ShopDocNumberRangesEntity_.shop), shop));
			
			shopDocNumberRanges = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
			
			em.refresh(shopDocNumberRanges);

		}
		catch(NoResultException e){
		}
		
		System.out.println("GET PickslipNumber "+shopDocNumberRanges.getPickslipNumber());
		
		return shopDocNumberRanges.getPickslipNumber();
	}


	public long getNextOrderNumber(Shop shop) {
		
		checkShopDocNumberRanges(shop);
		
		ShopDocNumberRangesEntity shopDocNumberRanges = null;
		
		try{
			
			Query updateQuery = em.createNativeQuery("UPDATE ShopDocNumberRanges SET orderNumber=orderNumber+1 WHERE shop_id=?").setParameter(1, shop.getId());
			updateQuery.executeUpdate();
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ShopDocNumberRangesEntity> cQuery = cb.createQuery(ShopDocNumberRangesEntity.class);
			Root<ShopDocNumberRangesEntity> root = cQuery.from(ShopDocNumberRangesEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get(ShopDocNumberRangesEntity_.shop), shop));
			
			shopDocNumberRanges = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
			
			em.refresh(shopDocNumberRanges);

		}
		catch(NoResultException e){
		}
		
		System.out.println("GET OrderNumber "+shopDocNumberRanges.getOrderNumber());
		
		return shopDocNumberRanges.getOrderNumber();
	}
	
	public ShopDocNumberRanges getShopDocNumberRanges(Shop shop) {
		
		if(shop == null || shop.getId() == 0)
			return null;
		
		checkShopDocNumberRanges(shop);
		
		ShopDocNumberRangesEntity shopDocNumberRanges = null;
		
		try{
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ShopDocNumberRangesEntity> cQuery = cb.createQuery(ShopDocNumberRangesEntity.class);
			Root<ShopDocNumberRangesEntity> root = cQuery.from(ShopDocNumberRangesEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get(ShopDocNumberRangesEntity_.shop), shop));
			
			shopDocNumberRanges = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
			
			em.refresh(shopDocNumberRanges);

		}
		catch(NoResultException e){
		}
		
		return shopDocNumberRanges;
	}
	
	public ShopDocNumberRanges updateShopDocNumberRanges(ShopDocNumberRanges shopDocNumberRanges) {
		
		if(shopDocNumberRanges == null)
			return null;
		
		shopDocNumberRanges = em.merge(shopDocNumberRanges);
		
		return shopDocNumberRanges;
	}
	
	public void checkShopDocNumberRanges(Shop shop) {
		
		ShopDocNumberRanges shopDocNumberRanges = null;
			
		try{
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ShopDocNumberRangesEntity> cQuery = cb.createQuery(ShopDocNumberRangesEntity.class);
			Root<ShopDocNumberRangesEntity> root = cQuery.from(ShopDocNumberRangesEntity.class);
			cQuery.select(root);
			
			cQuery.where(cb.equal(root.get(ShopDocNumberRangesEntity_.shop), shop));
			
			shopDocNumberRanges = em.createQuery(cQuery).setMaxResults(1).getSingleResult();

		}
		catch(NoResultException e){
		}
		
		if(shopDocNumberRanges == null){
			shopDocNumberRanges = new ShopDocNumberRangesEntity();
			shopDocNumberRanges.setShop(shop);
			shopDocNumberRanges.setInvoiceNumber(100000);
			shopDocNumberRanges.setOrderNumber(200000);
			shopDocNumberRanges.setPickslipNumber(300000);
			
			if(shop != null && shop.getId() != 0)
				em.merge(shopDocNumberRanges);
		}
	}
	
}
