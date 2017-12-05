package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopAlias;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.client.entity.ShopAliasEntity;
import de.vaplus.client.entity.ShopAliasEntityPK;
import de.vaplus.client.entity.ShopAliasEntity_;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.ShopGroupEntity;


@Stateless
public class ShopEao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466161576414849348L;

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;


    public ShopEntity saveShop(ShopEntity shop){
    	
    	
    	shop = em.merge(shop);
    	
    	em.flush();
    	
    	return shop;
    	
    }
    
    public ShopGroupEntity saveShopGroup(ShopGroupEntity shopGroup){
    	return em.merge(shopGroup);
    }

	public Shop getShop(long id) {
		return em.find(ShopEntity.class, id);
	}

	public ShopEntity getShopByUUID(String uuid){
		
		ShopEntity shop = null;
		
		try{
			

			shop = (ShopEntity) em.createNamedQuery("Shop.findByUUID")
		            .setParameter("uuid", uuid)
		            .setParameter("enabled", true)
		            .getSingleResult();

//	
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<ShopEntity> cQuery = cb.createQuery(ShopEntity.class);
//			Root<ShopEntity> root = cQuery.from(ShopEntity.class);
//			EntityType<ShopEntity> type = em.getMetamodel().entity(ShopEntity.class);
//			cQuery.select(root);
//			
//			cQuery.where(
//					cb.and(
//				        cb.equal(
//				                root.get(
//				                    type.getDeclaredSingularAttribute("uuid", String.class)
//				                ),
//				                uuid
//				        ),
//				        cb.equal(
//				                root.get(
//				                		ShopEntity_.enabled
//				                ),
//				                true
//				        ),
//				        cb.equal(
//				                root.get(
//				                		ShopEntity_.deleted
//				                ),
//				                false
//				        )
//				     )
//				);
//			
//			shop = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return shop;
	}


	public List<ShopEntity> getShopList(){
		return getShopList(false);
	}

	public List<ShopEntity> getShopList(boolean showDisabled){
		
		List<ShopEntity> list = new ArrayList<ShopEntity>();
		
		try{
	
			if(! showDisabled){
				
				list = em.createNamedQuery("Shop.listEnabled")
			            .setParameter("enabled", true)
			            .getResultList();
			}else{
				list = em.createNamedQuery("Shop.listAll")
			            .getResultList();
			}
		}
		catch(NoResultException e){
		}
		
		return list;
	}


	public List<ShopGroupEntity> getShopGroupList(){
		return getShopGroupList(false);
	}

	public List<ShopGroupEntity> getShopGroupList(boolean showDisabled){
		
		List<ShopGroupEntity> list = new ArrayList<ShopGroupEntity>();
		
		try{
	
			if(! showDisabled){
				
				list = em.createNamedQuery("ShopGroup.listEnabled")
			            .setParameter("enabled", true)
			            .getResultList();
			}else{
				list = em.createNamedQuery("ShopGroup.listAll")
			            .getResultList();
			}
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public Long getShopCount() {
		
		return (long) getShopList(false).size();

//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Long> cQuery = cb.createQuery(Long.class);
//		Root<ShopEntity> root = cQuery.from(ShopEntity.class);
//		cQuery.select(cb.count(root));
//		
//		cQuery.where(
//				cb.and(
//			        cb.equal(
//			                root.get(
//			                		ShopEntity_.enabled
//			                ),
//			                true
//			        ),
//			        cb.equal(
//			                root.get(
//			                		ShopEntity_.deleted
//			                ),
//			                false
//			        )
//			     )
//			);
//		
//		return em.createQuery(cQuery).getSingleResult();
	}


	public Shop getShopByAlias(String alias) {
		
		ShopEntity shop = null;
		
		try{
			
			shop = (ShopEntity) em.createNamedQuery("Shop.findByAlias")
		            .setParameter("alias", alias)
		            .setMaxResults(1)
		            .getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return shop;
	}

	public ShopAlias saveShopAlias(ShopAlias shopAlias) {
    	return em.merge(shopAlias);
	}

	public ShopGroup getShopGroup(long id) {
		return em.find(ShopGroupEntity.class, id);
	}

	public Shop refreshShop(Shop shop) {
		Shop s = em.find(ShopEntity.class, shop.getId());
		em.refresh(s);
		return s;
	}


	public List<? extends ShopAlias> getShopAliasList(){
		
		List<? extends ShopAlias> list = new ArrayList<ShopAlias>();
		
		try{
	
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ShopAliasEntity> cQuery = cb.createQuery(ShopAliasEntity.class);
			Root<ShopAliasEntity> root = cQuery.from(ShopAliasEntity.class);
			cQuery.select(root);
			
			cQuery.orderBy(cb.asc(root.get(ShopAliasEntity_.shop)));
			
			return em.createQuery(cQuery).getResultList();

			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public void deleteShopAlias(ShopAlias shopAlias) {
		ShopAliasEntityPK pk = new ShopAliasEntityPK();
		
		pk.setAlias(shopAlias.getAlias());
		pk.setShop(shopAlias.getShop().getId());
		
		ShopAliasEntity a = em.find(ShopAliasEntity.class, pk);
		
		em.remove(a);
	}
}
