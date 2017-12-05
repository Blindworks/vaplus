package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.ProductCommissionVOTypeProductRelation;
import de.vaplus.api.entity.RevenueLevelVOType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.entity.ProductCommissionVOTypeEntity;
import de.vaplus.client.entity.RevenueLevelVOTypeEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.VOEntity;
import de.vaplus.client.entity.VOEntity_;
import de.vaplus.client.entity.VOTypeEntity;


@Stateless
public class VOEao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466161576414849348L;

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;
    
    public VOEntity saveVO(VOEntity vo){
    	return em.merge(vo);
    }    
    
    public VOTypeEntity saveVOType(VOTypeEntity voType){
    	return em.merge(voType);
    }  
    
    public void refreshVOType(VOTypeEntity voType){
    	voType = em.find(VOTypeEntity.class, voType.getId());
    	em.refresh(voType);
    }

	public VO getVO(long id) {
		return em.find(VOEntity.class, id);
	}

	public List<VOEntity> getVOList(Vendor vendor){
		
		List<VOEntity> list = new ArrayList<VOEntity>();
		
		try{
	

			if(vendor != null){
			list = em.createNamedQuery("VO.getListByVendor")
		            .setParameter("vendor", vendor).getResultList();
			}else{
				list = em.createNamedQuery("VO.getList").getResultList();
			}
			
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<VOEntity> cQuery = cb.createQuery(VOEntity.class);
//			Root<VOEntity> root = cQuery.from(VOEntity.class);
//			cQuery.select(root);
//
//			if(vendor != null)
//				cQuery.where(
//						cb.equal(root.get(VOEntity_.vendor), vendor)
//				);
//			
//			list = em.createQuery(cQuery).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<VOTypeEntity> getVOTypeList(Vendor vendor){
		
		
		List<VOTypeEntity> list = new ArrayList<VOTypeEntity>();

		if(vendor == null)
			return list;
		
		try{

			list = em.createNamedQuery("VOType.findByVendor")
		            .setParameter("vendor", vendor).getResultList();

	
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<VOTypeEntity> cQuery = cb.createQuery(VOTypeEntity.class);
//			Root<VOTypeEntity> root = cQuery.from(VOTypeEntity.class);
//			cQuery.select(root);
//			
//			cQuery.where(
//					cb.equal(root.get(VOTypeEntity_.vendor), vendor)
//			);
//			
//			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public VOType getVOType(long id) {
		return em.find(VOTypeEntity.class, id);
	}

	public List<? extends RevenueLevelVOType> getVOTypeRevenueLevelList(Vendor vendor) {
		
		List<RevenueLevelVOTypeEntity> list = new ArrayList<RevenueLevelVOTypeEntity>();
		
		try{
			list = em.createNamedQuery("VOType.getRevenueLevelVOTypeEntity")
		            .setParameter("vendor", vendor)
		            .getResultList();
			
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<RevenueLevelVOTypeEntity> cQuery = cb.createQuery(RevenueLevelVOTypeEntity.class);
//			Root<RevenueLevelVOTypeEntity> root = cQuery.from(RevenueLevelVOTypeEntity.class);
//			cQuery.select(root);
//
//			cQuery.where(
//					cb.equal(root.get(RevenueLevelVOTypeEntity_.vendor), vendor)
//			);
//			
//			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends ProductCommissionVOType> getVOTypeProductCommissionList() {
		
		List<ProductCommissionVOTypeEntity> list = new ArrayList<ProductCommissionVOTypeEntity>();
		
		try{
			
			list = em.createNamedQuery("VOType.getProductCommissionVOTypeEntity").getResultList();
//	
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<ProductCommissionVOTypeEntity> cQuery = cb.createQuery(ProductCommissionVOTypeEntity.class);
//			Root<ProductCommissionVOTypeEntity> root = cQuery.from(ProductCommissionVOTypeEntity.class);
//			cQuery.select(root);
//			
//			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends VO> getVOList(Shop shop, boolean includePermitted) {
		
		List<VOEntity> list = new ArrayList<VOEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VOEntity> cQuery = cb.createQuery(VOEntity.class);
			Root<VOEntity> root = cQuery.from(VOEntity.class);
			
			if(includePermitted){

				ListJoin<VOEntity, ShopEntity> join = root.join(VOEntity_.permittedShopShopList,JoinType.LEFT);

				cQuery.where(
						cb.and(
						cb.or(
								cb.equal(root.get(VOEntity_.shop), shop),
								join.in(shop)
							),
						cb.equal(root.get(VOEntity_.deleted), false)
						)
				);
				
			}else{

				cQuery.where(
					cb.and(
						cb.equal(root.get(VOEntity_.shop), shop),
						cb.equal(root.get(VOEntity_.deleted), false)
					)
				);
			}
			
			
			
			cQuery.select(root).distinct(true);
			list = em.createQuery(cQuery).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends VO> getVOList() {
		
		List<VOEntity> list = new ArrayList<VOEntity>();
		
		try{
			
			list = em.createNamedQuery("VO.getList",VOEntity.class)
		            .getResultList();
//	
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<VOEntity> cQuery = cb.createQuery(VOEntity.class);
//			Root<VOEntity> root = cQuery.from(VOEntity.class);
//			cQuery.select(root);
//			
//			cQuery.orderBy(cb.asc(root.get(VOEntity_.shop).get(ShopEntity_.name)));
//			
//			list = em.createQuery(cQuery).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public ProductCommissionVOTypeProductRelation saveProductCommissionVOTypeProductRelation(
			ProductCommissionVOTypeProductRelation relation) {
		return em.merge(relation);
	}

	public VOEntity getByNumber(String voNumber) {
		
		if(voNumber == null || voNumber.length() == 0)
			return null;
	
		try{
			
			return em.createNamedQuery("VO.getByNumber",VOEntity.class)
		            .setParameter("number", voNumber)
		            .getSingleResult();
	
		}
		catch(NoResultException e){
		}
		
		return null;
	}
}
