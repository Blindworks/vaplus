package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.CommissionActivity;
import de.vaplus.api.entity.ContractStatus;
import de.vaplus.api.entity.ContractStatusChange;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.entity.BaseContractCancellationEntity;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.BaseContractEntity_;
import de.vaplus.client.entity.BaseProductEntity_;
import de.vaplus.client.entity.CommissionActivityEntity;
import de.vaplus.client.entity.CommissionActivityEntity_;
import de.vaplus.client.entity.ContractItemEntity;
import de.vaplus.client.entity.ContractItemEntity_;
import de.vaplus.client.entity.ContractStatusEntity;
import de.vaplus.client.entity.ExternalCustomerEntity;
import de.vaplus.client.entity.ExternalCustomerEntity_;
import de.vaplus.client.entity.PhoneContractEntity;
import de.vaplus.client.entity.PhoneContractEntity_;

@Stateless
public class ContractEao implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 8670264870437906676L;


	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public BaseContract saveContract(BaseContract contract){
		return em.merge(contract);
	}


	public List<? extends BaseContract> getCustomerContractList(Customer customer) {
		List<? extends BaseContract> list = new ArrayList<BaseContract>();

		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BaseContractEntity> cQuery = cb.createQuery(BaseContractEntity.class);
			Root<BaseContractEntity> root = cQuery.from(BaseContractEntity.class);
			cQuery.select(root);

			cQuery.where(cb.equal(root.get(BaseContractEntity_.customer), customer));

			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}

		return list;
	}


	public List<? extends BaseContract> getContractList(Shop shop, VO vo, User user, Customer customer, Date from, Date to, boolean onlyActive){

			List<? extends BaseContract> list = new ArrayList<BaseContract>();

			try{

				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BaseContractEntity> cQuery = cb.createQuery(BaseContractEntity.class);
				Root<BaseContractEntity> root = cQuery.from(BaseContractEntity.class);
				cQuery.select(root);

				List<Predicate> criteria = new ArrayList<Predicate>();

				if(customer != null)
					criteria.add(cb.equal(root.get(BaseContractEntity_.customer), customer));

				if(user != null)
					criteria.add(cb.equal(root.get(BaseContractEntity_.user), user));

				if(shop != null)
					criteria.add(cb.equal(root.get(BaseContractEntity_.shop), shop));

				if(vo != null)
					criteria.add(cb.equal(root.get(BaseContractEntity_.vo), vo));

				if(from != null)
					criteria.add(cb.greaterThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), from));

				if(to != null)
					criteria.add(cb.lessThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), to));

				if(onlyActive){
					criteria.add(
							cb.equal(root.get(BaseContractEntity_.enabled), true)
						);
					criteria.add(
							cb.greaterThan(root.get(BaseContractEntity_.expiryDate), new Date())
						);
				}

				if(criteria.size() > 0)
					cQuery.where(cb.and(criteria.toArray(new Predicate[0])));

				cQuery.orderBy(cb.desc(root.get(BaseContractEntity_.effectiveDate)));

				list = em.createQuery(cQuery).getResultList();
			}
			catch(NoResultException e){
			}


			return list;
	}


	public List<? extends CommissionActivity> getCommissionActivityList(Shop shop, User user, Customer customer, Date from, Date to, boolean onlyActive){

		List<? extends CommissionActivity> list = new ArrayList<CommissionActivity>();


		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CommissionActivityEntity> cQuery = cb.createQuery(CommissionActivityEntity.class);
			Root<CommissionActivityEntity> root = cQuery.from(CommissionActivityEntity.class);
			cQuery.select(root);

			List<Predicate> criteria = new ArrayList<Predicate>();

			if(customer != null)
				criteria.add(cb.equal(root.get(CommissionActivityEntity_.customer), customer));

			if(user != null)
				criteria.add(cb.equal(root.get(CommissionActivityEntity_.user), user));

			if(shop != null)
				criteria.add(cb.equal(root.get(CommissionActivityEntity_.shop), shop));


			if(from != null)
				criteria.add(cb.greaterThanOrEqualTo(root.get(CommissionActivityEntity_.effectiveDate), from));

			if(to != null)
				criteria.add(cb.lessThanOrEqualTo(root.get(CommissionActivityEntity_.effectiveDate), to));

			if(onlyActive){
				criteria.add(
						cb.equal(root.get(CommissionActivityEntity_.enabled), true)
					);
				criteria.add(
						cb.greaterThan(root.get(CommissionActivityEntity_.expiryDate), new Date())
					);
			}

			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));

			cQuery.orderBy(cb.desc(root.get(CommissionActivityEntity_.effectiveDate)));

			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}


		return list;
}




	public List<? extends BaseContract> getContractListByExpiryDate(Date expiryDateFrom, Date expiryDateTo, boolean onlyWithExtensionOfTheTermPermission){
		List<? extends BaseContract> list = new ArrayList<BaseContract>();
		try{
			String extensionOfTheTermFilter = "";

			TypedQuery<Long> query = em.createQuery("SELECT a.id FROM ActivityEntity a WHERE a.expiryDate >= :from AND a.expiryDate <= :to AND  TYPE(a) IN :classes ORDER BY a.expiryDate ASC", Long.class)
				.setParameter("from",  expiryDateFrom)
				.setParameter("to",  expiryDateTo)
				.setParameter("classes",  Arrays.asList(PhoneContractEntity.class));
			
			List<Long> idList = query.getResultList();
			
			if(onlyWithExtensionOfTheTermPermission)
				extensionOfTheTermFilter = " AND b.extensionOfTheTermPermission = 1 ";
			
			TypedQuery<PhoneContractEntity> query2 = em.createQuery("SELECT b FROM BaseContractEntity b WHERE b.id IN :ids "+extensionOfTheTermFilter, PhoneContractEntity.class)
				.setParameter("ids",  idList);
			
			list = query2.getResultList();
			
			
		}
		catch(NoResultException e){
		}

		return list;
	}



	public long countContractListByExpiryDate(Date expiryDate, Shop shop, User user, ContractStatus status, BaseProduct tariff, boolean onlyWithExtensionOfTheTermPermission) {
		try{
			
			String shopFilter = "";
			String userFilter = "";
			String statusFilter = "";
			String tariffFilter = "";
			String extensionOfTheTermFilter = "";
			
			if(shop != null){
				shopFilter = " AND a.customer.shop = :shop ";
			}
			
			if(user != null){
				userFilter = " AND a.customer.accountManager = :user ";
			}
			
			if(status != null){
				statusFilter = " AND a.status = :status ";
			}
			
			if(tariff != null){
				tariffFilter = " AND a.cachedTariff.baseProduct = :tariff ";
			}

			
			if(onlyWithExtensionOfTheTermPermission && false)
				extensionOfTheTermFilter = " AND a.extensionOfTheTermPermission = 1 ";
			
			
			String queryString = "SELECT Count (a.id) FROM BaseContractEntity a WHERE a.expiryDate <= :to "+shopFilter+userFilter+statusFilter+tariffFilter+" AND TYPE(a) IN :classes "+extensionOfTheTermFilter+" ";

			TypedQuery<Long> query = em.createQuery(queryString, Long.class)
				.setParameter("to",  expiryDate)
				.setParameter("classes",  Arrays.asList(PhoneContractEntity.class));

//			System.out.println(queryString);
//			System.out.println("shop: "+shop);
//			System.out.println("user: "+user);
//			System.out.println("status: "+status);
//			System.out.println("tariff: "+tariff);
			
			if(shop != null){
				query.setParameter("shop",  shop);
			}
			
			if(user != null){
				query.setParameter("user",  user);
			}
			
			if(status != null){
				query.setParameter("status",  status);
			}
			
			if(tariff != null){
				query.setParameter("tariff",  tariff);
			}
			
			
			long count = query.setMaxResults(1).getSingleResult();
			
			return count;
			
		}
		catch(NoResultException e){
		}

		return 0;
	}

	public List<? extends BaseContract> getContractListByExpiryDate(Date expiryDate, Shop shop, User user,
			ContractStatus status, BaseProduct tariff, int limit, int offset, boolean onlyWithExtensionOfTheTermPermission, String sortField, String sortOrder) {
		List<? extends BaseContract> list = new ArrayList<BaseContract>();
		try{

			String orderQuery = null;
			
			if(sortField != null && sortField.length() > 0 && sortOrder != null && sortOrder.length() > 0){


				if(sortField.equalsIgnoreCase("id")){
					orderQuery = "ORDER BY pc.id";
				}
				
				else if(sortField.equalsIgnoreCase("customer")){
					orderQuery = "ORDER BY pc.customer.lastname";
				}
				
				else if(sortField.equalsIgnoreCase("shop")){
					orderQuery = "ORDER BY pc.shop.name";
				}
				
				else if(sortField.equalsIgnoreCase("user")){
					orderQuery = "ORDER BY pc.user.firstname";
				}
				
				else if(sortField.equalsIgnoreCase("callingNumber")){
					orderQuery = "ORDER BY pc.callingNumber";
				}
				
				else if(sortField.equalsIgnoreCase("tariff")){
					orderQuery = "ORDER BY pc.cachedTariff.productName";
				}
				
				else if(sortField.equalsIgnoreCase("expiryDate")){
					orderQuery = "ORDER BY pc.expiryDate";
				}
				
				else if(sortField.equalsIgnoreCase("status")){
					orderQuery = "ORDER BY pc.status";
				}
				
				if(orderQuery != null){
					if(sortOrder.equalsIgnoreCase("desc"))
						orderQuery += " DESC ";
					else
						orderQuery += " ASC ";
					
				}
			}
			
			if(orderQuery == null)
				orderQuery = "";
			
			String shopFilter = "";
			String userFilter = "";
			String statusFilter = "";
			String tariffFilter = "";
			String extensionOfTheTermFilter = "";
			
			if(shop != null){
				shopFilter = " AND a.customer.shop = :shop ";
			}
			
			if(user != null){
				userFilter = " AND a.customer.accountManager = :user ";
			}
			
			if(status != null){
				statusFilter = " AND a.status = :status ";
			}
			
			if(tariff != null){
				tariffFilter = " AND a.cachedTariff.baseProduct = :tariff ";
			}
			
			if(onlyWithExtensionOfTheTermPermission && false)
				extensionOfTheTermFilter = " AND a.extensionOfTheTermPermission = 1 ";
			
			//String queryString = "SELECT a.id FROM BaseContractEntity a WHERE a.expiryDate <= :to "+shopFilter+userFilter+statusFilter+tariffFilter+" AND TYPE(a) IN :classes  "+extensionOfTheTermFilter+" "+orderQuery;
			String queryString = "SELECT a.id FROM PhoneContractEntity pc, BaseContractEntity a WHERE a.id = pc.id AND a.expiryDate <= :to "+shopFilter+userFilter+statusFilter+tariffFilter+"  "+extensionOfTheTermFilter+" "+orderQuery;

//			System.out.println("queryString: "+queryString);
			
			TypedQuery<Long> query = em.createQuery(queryString, Long.class)
				.setParameter("to",  expiryDate);
				//setParameter("classes",  Arrays.asList(PhoneContractEntity.class));

//			System.out.println("expiryDate: "+expiryDate);

			if(shop != null){
				query.setParameter("shop",  shop);
			}
			
			if(user != null){
				query.setParameter("user",  user);
			}
			
			if(status != null){
				query.setParameter("status",  status);
			}
			
			if(tariff != null){
				query.setParameter("tariff",  tariff);
			}
			
			if(limit > 0){
				query.setMaxResults(limit);
				query.setFirstResult(offset);
			}

			
			List<Long> idList = query.getResultList();
			
			if(idList.isEmpty())
				return list;
			
			
			
			TypedQuery<PhoneContractEntity> query2 = em.createQuery("SELECT pc FROM PhoneContractEntity pc WHERE pc.id IN :ids "+orderQuery, PhoneContractEntity.class)
				.setParameter("ids",  idList);
			
			
			list = query2.getResultList();
			
			
		}
		catch(NoResultException e){
		}

		return list;
	}


	public long getContractCountByExpiryDate(Date expiryDateFrom, Date expiryDateTo) {
		

		try{

			TypedQuery<Long> query = em.createQuery("SELECT Count(a) FROM ActivityEntity a WHERE a.expiryDate >= :from AND a.expiryDate <= :to AND  TYPE(a) IN :classes", Long.class)

				.setParameter("from",  expiryDateFrom)
				.setParameter("to",  expiryDateTo)
				.setParameter("classes",  Arrays.asList(PhoneContractEntity.class));
			
			return query.getSingleResult();
		}
		catch(NoResultException e){
		}


		return 0L;
	}

	public List<? extends BaseContract> getContractList(Vendor vendor, Date from, Date to, boolean onlyActive){

			List<? extends BaseContract> list = new ArrayList<BaseContract>();

			try{

				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BaseContractEntity> cQuery = cb.createQuery(BaseContractEntity.class);
				Root<BaseContractEntity> root = cQuery.from(BaseContractEntity.class);

				Join <BaseContractEntity,ContractItemEntity> joinContractItem = root.join( BaseContractEntity_.cachedTariff);


				cQuery.select(root);

				List<Predicate> criteria = new ArrayList<Predicate>();

				if(vendor != null)
					criteria.add(cb.equal(joinContractItem.get(ContractItemEntity_.vendor), vendor));

				if(from != null)
					criteria.add(cb.greaterThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), from));

				if(to != null)
					criteria.add(cb.lessThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), to));

				if(onlyActive){
					criteria.add(
							cb.equal(root.get(BaseContractEntity_.enabled), true)
						);
					criteria.add(
							cb.greaterThan(root.get(BaseContractEntity_.expiryDate), new Date())
						);
				}

				if(criteria.size() > 0)
					cQuery.where(cb.and(criteria.toArray(new Predicate[0])));

				cQuery.orderBy(cb.desc(root.get(BaseContractEntity_.effectiveDate)));

				list = em.createQuery(cQuery).getResultList();
			}
			catch(NoResultException e){
			}


			return list;
	}

	public int getFirstContractYear() {

	    Calendar cal = Calendar.getInstance();

		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Date> cQuery = cb.createQuery(Date.class);
			Root<BaseContractEntity> root = cQuery.from(BaseContractEntity.class);
			cQuery.select(root.get(BaseContractEntity_.effectiveDate));

			cQuery.orderBy(cb.asc(root.get(BaseContractEntity_.effectiveDate)));

			cQuery.where(cb.isNotNull(root.get(BaseContractEntity_.effectiveDate)));

			Date firstDate = em.createQuery(cQuery).setMaxResults(1).getSingleResult();

		    cal.setTime(firstDate);
			return cal.get(Calendar.YEAR);
		}
		catch(NoResultException e){
			cal.setTime(new Date());
		}

		return cal.get(Calendar.YEAR);

	}


	public List<? extends BaseContractEntity> findContract(String query) {
		List<PhoneContractEntity> list = new ArrayList<PhoneContractEntity>();

		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PhoneContractEntity> cQuery = cb.createQuery(PhoneContractEntity.class);
			Root<PhoneContractEntity> root = cQuery.from(PhoneContractEntity.class);

			Join <PhoneContractEntity,ExternalCustomerEntity> joinExternalCustomer = root.join( PhoneContractEntity_.externalCustomer, JoinType.LEFT);



//			EntityType<BaseContractEntity> type = em.getMetamodel().entity(BaseContractEntity.class);
			cQuery.select(root);

			List<Predicate> criteria = new ArrayList<Predicate>();

			if (query.matches("-?\\d+(\\.\\d+)?")) {
			    // numeric


				try{
					long numericQuery = Long.valueOf(query);

					criteria.add(cb.equal( root.get( PhoneContractEntity_.id) , numericQuery ));
				}catch(NumberFormatException e){
					
				}


			}

			criteria.add(cb.like(
		            cb.lower(
			                root.get( PhoneContractEntity_.callingNumber)
			            ), "%" + query.toLowerCase() + "%"
			        ));

			criteria.add(cb.like(
		            cb.lower(
		            		joinExternalCustomer.get( ExternalCustomerEntity_.customerId)
		            ), "%" + query.toLowerCase() + "%"
		        ));


			cQuery.where(
					cb.and(
						cb.or(
								criteria.toArray(new Predicate[0])
					    )
					    ,
						cb.equal(root.get(BaseProductEntity_.deleted), false)
					)
				);




			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}

		return list;
	}


	public List<? extends BaseContractEntity> findContract(String externalCustomerId, String callingNumber, Date from, Date to) {
		List<PhoneContractEntity> list = new ArrayList<PhoneContractEntity>();

		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PhoneContractEntity> cQuery = cb.createQuery(PhoneContractEntity.class);
			Root<PhoneContractEntity> root = cQuery.from(PhoneContractEntity.class);

			Join <PhoneContractEntity,ExternalCustomerEntity> joinExternalCustomer = root.join( PhoneContractEntity_.externalCustomer);

//			EntityType<BaseContractEntity> type = em.getMetamodel().entity(BaseContractEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
						cb.or(
							cb.equal(
									root.get( PhoneContractEntity_.callingNumber)
						            , callingNumber
						    ),
							cb.equal(
									root.get( PhoneContractEntity_.callingNumber)
						            , "0" + callingNumber
						    )
					    ),

						cb.equal(
					            joinExternalCustomer.get( ExternalCustomerEntity_.customerId)
					            , externalCustomerId
					    ),

					    cb.greaterThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), from),

						cb.lessThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), to)

				    )
				);

			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}

		return list;
	}

	public BaseContractCancellationEntity saveBaseContractCancellation(
			BaseContractCancellationEntity baseContractCancellation) {
		return em.merge(baseContractCancellation);
	}


	public BaseContract refresh(BaseContract contract) {
		BaseContractEntity c = em.find(BaseContractEntity.class, contract.getId());
		em.refresh(c);
		return c;
	}


	public void detachBaseContract(BaseContractEntity clone) {
		em.detach(clone);
	}


	public List<? extends ContractStatus> getContractStatusList() {
		List<ContractStatusEntity> list = new ArrayList<ContractStatusEntity>();

		try{
	
			list = em.createNamedQuery("ContractStatus.getStatusList", ContractStatusEntity.class).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}


	public ContractStatus getContractStatusById(long id) {
		return em.find(ContractStatusEntity.class, id);
	}


	public ContractStatus saveContractStatus(ContractStatus contractStatus) {
		return em.merge(contractStatus);
	}


	public ContractStatusChange saveContractStatusChange(ContractStatusChange contractStatusChange) {
		return em.merge(contractStatusChange);
	}



}
