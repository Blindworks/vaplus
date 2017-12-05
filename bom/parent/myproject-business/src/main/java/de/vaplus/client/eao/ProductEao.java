package de.vaplus.client.eao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.BaseProductCombination;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Supplier;
import de.vaplus.api.entity.Tariff;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.entity.BaseProductCombinationEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.BaseProductEntity_;
import de.vaplus.client.entity.CellPhoneTariffEntity;
import de.vaplus.client.entity.LandlineTariffEntity;
import de.vaplus.client.entity.PrePaidTariffEntity;
import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.ProductCategoryEntity_;
import de.vaplus.client.entity.ProductEntity;
import de.vaplus.client.entity.ProductEntity_;
import de.vaplus.client.entity.ProductOptionEntity;
import de.vaplus.client.entity.ProductOptionEntity_;
import de.vaplus.client.entity.SupplierEntity;
import de.vaplus.client.entity.TariffEntity;
import de.vaplus.client.entity.TariffEntity_;
import de.vaplus.client.entity.VOTypeEntity;
import de.vaplus.client.entity.VendorEntity;
import de.vaplus.client.entity.VendorEntity_;


@Stateless
public class ProductEao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6115020818895720470L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public VendorEntity getVendorById(Long id) {
		VendorEntity vendor = em.find(VendorEntity.class, id);
		
		if(vendor.isDeleted())
			return null;
		
		return vendor;
	}
	
	public List<VendorEntity> getVendorList() {
		List<VendorEntity> list = new ArrayList<VendorEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VendorEntity> cQuery = cb.createQuery(VendorEntity.class);
			Root<VendorEntity> root = cQuery.from(VendorEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.equal(root.get(VendorEntity_.deleted), false)
			);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
//			System.out.println("No Vendors found.");
		}
		
		return list;
	}

	public VendorEntity saveVendor(VendorEntity vendor) {
    	return em.merge(vendor);
	}

	public VendorEntity factoryNewVendor() {
		return new VendorEntity();
	}

	public ProductCategoryEntity saveProductCategory(ProductCategoryEntity productCategory) {
    	return em.merge(productCategory);
	}

	public ProductCategoryEntity factoryNewProductCategory() {
		return new ProductCategoryEntity();
	}

	public ProductCategoryEntity getProductCategoryById(Long id) {
		ProductCategoryEntity cat = em.find(ProductCategoryEntity.class, id);
		
		if(cat.isDeleted())
			return null;
		
		return cat;
	}

	public List<ProductCategoryEntity> getProductCategoryList() {
		List<ProductCategoryEntity> list = new ArrayList<ProductCategoryEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductCategoryEntity> cQuery = cb.createQuery(ProductCategoryEntity.class);
			Root<ProductCategoryEntity> root = cQuery.from(ProductCategoryEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
							cb.equal(root.get(ProductCategoryEntity_.deleted), false),
							cb.isNull(root.get(ProductCategoryEntity_.parentCategory))
					)
			);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
//			System.out.println("No ProductCategories found.");
		}
		
		return list;
	}

	public List<ProductCategoryEntity> getOverviewProductCategoryList() {
		List<ProductCategoryEntity> list = new ArrayList<ProductCategoryEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductCategoryEntity> cQuery = cb.createQuery(ProductCategoryEntity.class);
			Root<ProductCategoryEntity> root = cQuery.from(ProductCategoryEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
							cb.equal(root.get(ProductCategoryEntity_.deleted), false),
							cb.equal(root.get(ProductCategoryEntity_.showInOverview), true)
					)
			);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
//			System.out.println("No ProductCategories found.");
		}
		
		return list;
	}

	
	
	public List<? extends BaseProductEntity> getBaseProductList() {
		
		// to hit cache, cut the minutes of
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		List<? extends BaseProductEntity> list = em.createNamedQuery("BaseProduct.findActive", BaseProductEntity.class)
		            .setParameter("effectiveDate", c.getTime())
		            .setParameter("expiryDate", c.getTime())
		            .getResultList();
		
		
		return list;
	}

	public List<? extends ProductEntity> getRetailProductList() {
		
		// to hit cache, cut the minutes of
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		List<? extends ProductEntity> list = em.createNamedQuery("Product.findActive", ProductEntity.class)
		            .setParameter("effectiveDate", c.getTime())
		            .setParameter("expiryDate", c.getTime())
		            .getResultList();
		
		
		return list;
	}

	public List<? extends ProductOptionEntity> getProductOptionList() {
		
		// to hit cache, cut the minutes of
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		List<? extends ProductOptionEntity> list = em.createNamedQuery("ProductOption.findActive", ProductOptionEntity.class)
		            .setParameter("effectiveDate", c.getTime())
		            .setParameter("expiryDate", c.getTime())
		            .getResultList();
		
		
		return list;
	}
	
	

	public List<? extends Product> getForeignRetailProductList() {
		
		// to hit cache, cut the minutes of
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		List<? extends Product> list = em.createNamedQuery("Product.getForeignRetailProductList", ProductEntity.class)
		            .setParameter("effectiveDate", c.getTime())
		            .setParameter("expiryDate", c.getTime())
		            .getResultList();
		
		
		return list;
	}

	public List<? extends BaseProduct> getFilteredProductList(Class<? extends BaseProductEntity> productClass, String name, ProductCategory category, int status) {

		boolean enabled_1 = true,enabled_2 = false,disableCatFilter = false,disableNameFilter = false,disableTypeFilter = false;
		
		
		if(status == -1){
			// only disabled product
			enabled_1 = false;
			enabled_2 = false;
		}else if(status == 0){
			// all products
			enabled_1 = true;
			enabled_2 = false;
		}else if(status == 1){
			// only enabled product
			enabled_1 = true;
			enabled_2 = true;
		}
		
		if(category == null){
			disableCatFilter = true;
		}
		
		if(name == null){
			disableNameFilter = true;
		}else{
			name = "%"+name+"%";
		}
		
		if(productClass == null){
			disableTypeFilter = true;
		}
		
		List<BaseProductEntity> list = em.createNamedQuery("BaseProduct.getFilteredList", BaseProductEntity.class)
		            .setParameter("enabled_1", enabled_1)
		            .setParameter("enabled_2", enabled_2)
		            .setParameter("category", category)
		            .setParameter("disableCatFilter", disableCatFilter)
		            .setParameter("name", name)
		            .setParameter("disableNameFilter", disableNameFilter)
		            .setParameter("class", productClass)
		            .setParameter("disableTypeFilter", disableTypeFilter)
		            .getResultList();
		
		return list;
	}

//	public <T extends BaseProductEntity> List<T> getBaseProductList(Class<T> productClass, String orderBy, String sorting, boolean showDisabled, Set<EaoFilter<?>> filterSet) {
//		List<T> list = new ArrayList<T>();
//		
//		
//		
//		
////		System.out.println("getBaseProductList START "+productClass+(new Date()).toString());
//		
////		Thread.dumpStack();
//		
//		
//		// to hit cache, cut the minutes of
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//		
//		if(productClass == BaseProductEntity.class){
//			
//			list = em.createNamedQuery("BaseProduct.findActive")
//		            .setParameter("enabled", true)
//		            .setParameter("deleted", false)
//		            .setParameter("effectiveDate", c.getTime())
//		            .setParameter("expiryDate", c.getTime())
//		            .getResultList();
//			
//			
//		}else{
//		
//
//			
//		
//			try{
//		
//				CriteriaBuilder cb = em.getCriteriaBuilder();
//				CriteriaQuery<T> cQuery = cb.createQuery(productClass);
//				EntityType<T> type = em.getMetamodel().entity(productClass);
//				Root<T> root = cQuery.from(productClass);
//				cQuery.select(root);
//				
//				List<Predicate> predicates = new ArrayList<Predicate>();
//				if(showDisabled == false){
//	
//					// enabled
//					predicates.add( cb.equal(root.get(BaseProductEntity_.enabled), true) );
//					
//					// Now after effectiveDate
//					predicates.add( 
//							cb.or(
//									cb.lessThanOrEqualTo(root.get(BaseProductEntity_.effectiveDate), new Date()) ,
//									cb.isNull(root.get(BaseProductEntity_.effectiveDate))
//							)
//					);
//					
//					// Now before expiryDate
//					predicates.add( 
//							cb.or(
//									cb.greaterThanOrEqualTo(root.get(BaseProductEntity_.expiryDate), new Date()) ,
//									cb.isNull(root.get(BaseProductEntity_.effectiveDate))
//							)
//					);
//					
//				}
//				
//				if(filterSet != null){
//					Iterator<EaoFilter<?>> filterIterator = filterSet.iterator();
//					EaoFilter<?> filter;
//					while(filterIterator.hasNext()){
//						filter = filterIterator.next();
//	
//	
//						if(filter.name.equalsIgnoreCase("enabled")){
//							predicates.add(cb.equal(root.get(BaseProductEntity_.enabled),filter.expression));
//						}
//						
//						if(filter.name.equalsIgnoreCase("category")){
//							predicates.add(cb.equal(root.get(BaseProductEntity_.productCategory),filter.expression));
//						}
//						
//						if(filter.name.equalsIgnoreCase("name")){
//							if(filter.likeFilter){
//								predicates.add(cb.like(root.get(BaseProductEntity_.name),"%"+filter.expression+"%"));
//							}else{
//								predicates.add(cb.equal(root.get(BaseProductEntity_.name),filter.expression));
//							}
//						}
//	//					
//	//					
//	//					
//	//					if(filter.likeFilter){
//	//						predicates.add(cb.like(root.get(BaseProductEntity_.name), "%"+filter.expression+"%"));
//	//					}else{
//	//						predicates.add(cb.equal(root.get(type.getDeclaredSingularAttribute(filter.name, filter.expression.getClass())),filter.expression));
//	//					}
//					}
//				}
//				
//				predicates.add(cb.equal(root.get(BaseProductEntity_.deleted), false));
//	
//				cQuery.where(cb.and(predicates.toArray(new Predicate[] {})));
//				
//				
//				if(orderBy != null && sorting != null){
//					Path<Object> orderByPath = null;
//					Order order;
//					
//					// ORDER BY
//					if(orderBy.startsWith("commission")){
//						orderBy = orderBy.replaceAll("commission.", "");
//						orderByPath = root.get("commission").get(orderBy);
//					}
//					else{
//						orderByPath = root.get(orderBy);
//					}
//					
//					if(sorting.equalsIgnoreCase("asc"))
//						order = cb.asc(orderByPath);
//					else
//						order = cb.desc(orderByPath);
//					
//					cQuery.orderBy(order);
//				}
//			
//				list = em.createQuery(cQuery).getResultList();
//			}
//			catch(NoResultException e){
//	//			System.out.println("No BaseProducts found.");
//			}
//		}
//
////		System.out.println("getBaseProductList END "+(new Date()).toString());
//		
//		return list;
//	}

	public List<BaseProductEntity> findProduct(String query){
		
		
		List<BaseProductEntity> list = new ArrayList<BaseProductEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BaseProductEntity> cQuery = cb.createQuery(BaseProductEntity.class);
			Root<BaseProductEntity> root = cQuery.from(BaseProductEntity.class);
			EntityType<BaseProductEntity> type = em.getMetamodel().entity(BaseProductEntity.class);
			cQuery.select(root);
			
			cQuery.where(
					cb.and(
				        cb.like(
				            cb.lower(
				                root.get(
				                    type.getDeclaredSingularAttribute("name", String.class)
				                )
				            ), "%" + query.toLowerCase() + "%"
				        ),
						cb.equal(root.get(BaseProductEntity_.deleted), false)
					)
				);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public BaseProductEntity saveProduct(BaseProductEntity product) {
    	return em.merge(product);
	}

	public Product factoryNewRetailProduct() {
		ProductEntity product = new ProductEntity();
		product.setAvergePurchasePrice(new BigDecimal(0));
		product.setMinPurchasePrice(new BigDecimal(0));
		product.setMaxPurchasePrice(new BigDecimal(0));
		product.setStockQuantity(new BigDecimal(0));
		product.setStockControlled(true);
		return product;
	}

	public BaseProductEntity factoryNewTariffProduct() {
		return new TariffEntity();
	}

	public BaseProduct factoryNewCellPhoneTariffProduct() {
		return new CellPhoneTariffEntity();
	}

	public BaseProduct factoryNewLandlineTariffProduct() {
		return new LandlineTariffEntity();
	}

	public BaseProduct factoryNewPrePaidTariffProduct() {
		return new PrePaidTariffEntity();
	}

	public BaseProduct getProductById(Long id) {
		BaseProductEntity product = em.find(BaseProductEntity.class, id);
		
		if(product.isDeleted())
			return null;
		
		return product;
	}


	public BaseProduct getProductByName(String name) {

		BaseProduct product = null;
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BaseProductEntity> cQuery = cb.createQuery(BaseProductEntity.class);
			Root<BaseProductEntity> root = cQuery.from(BaseProductEntity.class);
			cQuery.select(root);
			
			cQuery.where(
				cb.and(
					cb.equal(root.<String>get("name"), name),
					cb.equal(root.get(BaseProductEntity_.deleted), false)
				)
			);
			
			product = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return product;
	}

	public ProductOption factoryNewProductOption() {
		return new ProductOptionEntity();
	}

	public List<? extends Tariff> getTariffList() {
		List<? extends Tariff> list = new ArrayList<TariffEntity>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TariffEntity> cQuery = cb.createQuery(TariffEntity.class);
			Root<TariffEntity> root = cQuery.from(TariffEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.equal(root.get(BaseProductEntity_.deleted), false)
			);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

//	public List<? extends ProductOption> getProductOptionList() {
//		List<? extends ProductOption> list = new ArrayList<ProductOptionEntity>();
//		
//		try{
//	
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<ProductOptionEntity> cQuery = cb.createQuery(ProductOptionEntity.class);
//			Root<ProductOptionEntity> root = cQuery.from(ProductOptionEntity.class);
//			cQuery.select(root);
//
//			cQuery.where(
//					cb.equal(root.get(BaseProductEntity_.deleted), false)
//			);
//			
//			list = em.createQuery(cQuery).getResultList();
//		}
//		catch(NoResultException e){
//		}
//		
//		return list;
//	}

	public Vendor getVendorByName(String name, boolean createIfNotExists) {

		Vendor vendor = null;
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VendorEntity> cQuery = cb.createQuery(VendorEntity.class);
			Root<VendorEntity> root = cQuery.from(VendorEntity.class);
			cQuery.select(root);

			cQuery.where(
				cb.and(	
					cb.equal(root.get(VendorEntity_.name), name),
					cb.equal(root.get(VendorEntity_.deleted), false)
				)
			);
			
			vendor = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
			if(createIfNotExists){
				vendor = new VendorEntity();
				vendor.setName(name);
				
				vendor = em.merge(vendor);
			}
		}
		
		return vendor;
	}

	public ProductCategory getProductCategoryByName(String name, boolean createIfNotExists) {

		ProductCategory cat = null;
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductCategoryEntity> cQuery = cb.createQuery(ProductCategoryEntity.class);
			Root<ProductCategoryEntity> root = cQuery.from(ProductCategoryEntity.class);
			cQuery.select(root);

			cQuery.where(
				cb.and(
					cb.equal(root.get(ProductCategoryEntity_.name), name),
					cb.equal(root.get(ProductCategoryEntity_.deleted), false)
				)
			);
			
			cat = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
			if(createIfNotExists){
				cat = new ProductCategoryEntity();
				cat.setName(name);
				
				cat = em.merge(cat);
			}
		}
		
		return cat;
	}

	public void refreshVendor(Vendor vendor) {
		vendor = em.find(VendorEntity.class, vendor.getId());
    	em.refresh(vendor);
	}

	public Vendor getDefaultVendor() {
		Vendor vendor = null;
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VendorEntity> cQuery = cb.createQuery(VendorEntity.class);
			Root<VendorEntity> root = cQuery.from(VendorEntity.class);
			cQuery.select(root);
			
			cQuery.where(
				cb.and(
					cb.equal(root.get(VendorEntity_.defaultSelection), true),
					cb.equal(root.get(VendorEntity_.deleted), false)
				)
			);
			
			
			vendor = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return vendor;
	}

	public List<? extends Tariff> getTariffList(VO vo) {
		if(vo == null)
			return getTariffListByVOType(null);
		
		return getTariffListByVOType(vo.getVoType());
	}

	public List<? extends Tariff> getTariffListByVOType(VOType voType) {
		List<? extends Tariff> list = new ArrayList<TariffEntity>();
		
		try{
			
			List<Predicate> criteria = new ArrayList<Predicate>();
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TariffEntity> cQuery = cb.createQuery(TariffEntity.class);
			Root<TariffEntity> root = cQuery.from(TariffEntity.class);

//			Predicate joinPredicate = null;
			if(voType != null){
				ListJoin<TariffEntity, VOTypeEntity> join = root.join(TariffEntity_.voTypePermissionList);
//				joinPredicate = join.in(voType);
				criteria.add(join.in(voType));
			}
			
			criteria.add(cb.equal(root.get(TariffEntity_.deleted), false));
			
			criteria.add(cb.equal(root.get(TariffEntity_.enabled), true));
			
			criteria.add(cb.or(
					cb.lessThanOrEqualTo(root.get(TariffEntity_.effectiveDate), new Date()) ,
					cb.isNull(root.get(TariffEntity_.effectiveDate))
			));
			
			criteria.add(cb.or(
					cb.greaterThanOrEqualTo(root.get(TariffEntity_.expiryDate), new Date()) ,
					cb.isNull(root.get(TariffEntity_.effectiveDate))
			));

			cQuery.where(
				cb.and(
					criteria.toArray(new Predicate[0])
				)
			);
			
			cQuery.select(root);

			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends ProductOption> getProductOptionList(VO vo) {
		return getProductOptionList(vo.getVoType());
	}

	public List<? extends ProductOption> getProductOptionList(VOType voType) {
		List<? extends ProductOption> list = new ArrayList<ProductOptionEntity>();
		
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductOptionEntity> cQuery = cb.createQuery(ProductOptionEntity.class);
			Root<ProductOptionEntity> root = cQuery.from(ProductOptionEntity.class);

			ListJoin<ProductOptionEntity, VOTypeEntity> join = root.join(ProductOptionEntity_.voTypePermissionList);
			
			cQuery.where(
				cb.and(
					join.in(voType),
					cb.equal(root.get(ProductOptionEntity_.deleted), false)
				)
			);
			
			cQuery.select(root);

			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public Product getProductByEAN(String ean) {

		Product product = null;
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductEntity> cQuery = cb.createQuery(ProductEntity.class);
			Root<ProductEntity> root = cQuery.from(ProductEntity.class);
			cQuery.select(root);
			
			cQuery.where(
				cb.and(
					cb.equal(root.get(ProductEntity_.ean), ean),
					cb.equal(root.get(BaseProductEntity_.deleted), false)
				)
			);
			
			product = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return product;
	}

	public void detachProduct(BaseProduct product) {
		em.detach(product);
	}

	public BaseProductCombination saveProductCombination(
			BaseProductCombination productCombination) {
    	return em.merge(productCombination);
	}

	public List<? extends BaseProductCombination> getProductCombinationList() {
		
		return em.createNamedQuery("BaseProductCombination.getList", BaseProductCombinationEntity.class)
	            .getResultList();
	}

	public BaseProductCombination getProductCombination(long id) {
		return em.find(BaseProductCombinationEntity.class, id);
	}

	public Supplier getSupplierById(Long id) {
		SupplierEntity supplier = em.find(SupplierEntity.class, id);
		
		if(supplier == null)
			return null;
		
		if(supplier.isDeleted())
			return null;
		
		return supplier;
	}

	public List<? extends Supplier> getSupplierList(boolean showDisabled) {
		
		if(showDisabled){
			return em.createNamedQuery("Supplier.listAll", SupplierEntity.class)
		            .getResultList();
		}else{
			return em.createNamedQuery("Supplier.listEnabled", SupplierEntity.class)
		            .getResultList();
		}
	}

	public Supplier saveSupplier(SupplierEntity supplier) {
    	return em.merge(supplier);
	}

	public Supplier factoryNewSupplier() {
		return new SupplierEntity();
	}

	public Supplier refreshSupplier(Supplier supplier) {
    	Supplier s = em.find(SupplierEntity.class, supplier.getId());
		em.refresh(s);
		return s;
	}


    

}
