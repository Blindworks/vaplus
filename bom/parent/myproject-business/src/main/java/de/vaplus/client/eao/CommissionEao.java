package de.vaplus.client.eao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.AquiredRevenueLevel;
import de.vaplus.api.entity.ContractedRevenueLevel;
import de.vaplus.api.entity.RevenueLevel;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.BaseContractEntity_;
import de.vaplus.client.entity.CommissionActivityEntity;
import de.vaplus.client.entity.CommissionActivityEntity_;
import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.InvoiceEntity_;
import de.vaplus.client.entity.OrderEntity;
import de.vaplus.client.entity.OrderEntity_;
import de.vaplus.client.entity.commission.AquiredRevenueLevelEntity;
import de.vaplus.client.entity.commission.AquiredRevenueLevelEntity_;
import de.vaplus.client.entity.commission.ContractedRevenueLevelEntity;
import de.vaplus.client.entity.commission.ContractedRevenueLevelEntity_;
import de.vaplus.client.entity.commission.RevenueLevelEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable_;

@Stateless
public class CommissionEao implements Serializable{
	
	
	private static final long serialVersionUID = -4203712069678588358L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;


	public Commissionable getUserCommissionSum(User user, Date from, Date to) {

		return getCommissionSum(user, from, to);
	}
	
	public Commissionable getShopCommissionSum(Shop shop, Date from, Date to) {
		
		return getCommissionSum(shop, from, to);
	}
	
	public Commissionable getVOCommissionSum(VO vo, Date from, Date to) {
		
		return getCommissionSum(vo, from, to);
	}
	
	private Commissionable getCommissionSum(Object obj, Date from, Date to) {
		Commissionable commissionableSum = new CommissionableEmbeddable();

//		System.out.println("getCommissionSum for "+obj+" from: "+from+" to:"+to);
		

		Commissionable contractCommissionable = new CommissionableEmbeddable();
		// Contracts
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery< Object[]> cQuery = cb.createQuery( Object[].class);
			Root<BaseContractEntity> root = cQuery.from(BaseContractEntity.class);

			Expression<BigDecimal> sumPoints = cb.sum(root.get(BaseContractEntity_.finalCommission).get(CommissionableEmbeddable_.points));
			Expression<BigDecimal> sumCommission = cb.sum(root.get(BaseContractEntity_.finalCommission).get(CommissionableEmbeddable_.commission));
			Expression<BigDecimal> sumPrice = cb.sum(root.get(BaseContractEntity_.finalCommission).get(CommissionableEmbeddable_.price));
			
			cQuery.multiselect(sumPoints, sumCommission, sumPrice);

			
			Predicate entityPredicate = null;
			
			if(obj instanceof User){
				entityPredicate = cb.equal(root.get(BaseContractEntity_.user), obj);
			}else if(obj instanceof Shop){
				entityPredicate = cb.equal(root.get(BaseContractEntity_.shop), obj);
			}else if(obj instanceof VO){
				entityPredicate = cb.equal(root.get(BaseContractEntity_.vo), obj);
			}	
			
			cQuery.where(
					cb.and(
							entityPredicate,
							cb.greaterThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), from),
							cb.lessThanOrEqualTo(root.get(BaseContractEntity_.effectiveDate), to)
					)
			);
			
			Object[] valueArray = em.createQuery( cQuery ).getSingleResult();

			contractCommissionable.setPoints((BigDecimal) valueArray[0]);
			contractCommissionable.setCommission((BigDecimal) valueArray[1]);
			contractCommissionable.setPrice((BigDecimal) valueArray[2]);


			if(contractCommissionable.getPoints() == null)
				contractCommissionable.setPoints(new BigDecimal(0));
			if(contractCommissionable.getPrice() == null)
				contractCommissionable.setPrice(new BigDecimal(0));
			if(contractCommissionable.getCommission() == null)
				contractCommissionable.setCommission(new BigDecimal(0));
		}
		catch(NoResultException e){
		}
		
//		System.out.println("contractCommissionable: "+contractCommissionable);
		
		commissionableSum = commissionableSum.addCommissionable(contractCommissionable);
		
		
		/* ORDER COMMISSION   DEPRECATED*/ 

		Commissionable orderCommissionable = new CommissionableEmbeddable();

		// Order Only Shop and User Points
		if(!(obj instanceof VO)){
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery< Object[]> cQuery = cb.createQuery( Object[].class);
			Root<OrderEntity> root = cQuery.from(OrderEntity.class);

			Expression<BigDecimal> sumPoints = cb.sum(root.get(OrderEntity_.commission).get(CommissionableEmbeddable_.points));
			Expression<BigDecimal> sumCommission = cb.sum(root.get(OrderEntity_.commission).get(CommissionableEmbeddable_.commission));
			Expression<BigDecimal> sumPrice = cb.sum(root.get(OrderEntity_.commission).get(CommissionableEmbeddable_.price));
			
			cQuery.multiselect(sumPoints, sumCommission, sumPrice);

			
			Predicate entityPredicate = null;
			
			if(obj instanceof User){
				entityPredicate = cb.equal(root.get(OrderEntity_.user), obj);
			}else if(obj instanceof Shop){
				entityPredicate = cb.equal(root.get(OrderEntity_.shop), obj);
			}	
			
			cQuery.where(
					cb.and(
							entityPredicate,
							cb.greaterThanOrEqualTo(root.get(OrderEntity_.effectiveDate), from),
							cb.lessThanOrEqualTo(root.get(OrderEntity_.effectiveDate), to)
					)
			);
			
			Object[] valueArray = em.createQuery( cQuery ).getSingleResult();

			orderCommissionable.setPoints((BigDecimal) valueArray[0]);
			orderCommissionable.setCommission((BigDecimal) valueArray[1]);
			orderCommissionable.setPrice((BigDecimal) valueArray[2]);


			if(orderCommissionable.getPoints() == null)
				orderCommissionable.setPoints(new BigDecimal(0));
			if(orderCommissionable.getPrice() == null)
				orderCommissionable.setPrice(new BigDecimal(0));
			if(orderCommissionable.getCommission() == null)
				orderCommissionable.setCommission(new BigDecimal(0));
		}
		catch(NoResultException e){
		}
		}

//		System.out.println("orderCommissionable: "+orderCommissionable);

		commissionableSum = commissionableSum.addCommissionable(orderCommissionable);
		
		
		
		/* INVOICE COMMISSION */

		Commissionable invoiceCommissionable = new CommissionableEmbeddable();

		// Order Only Shop and User Points
		if(!(obj instanceof VO)){
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery< Object[]> cQuery = cb.createQuery( Object[].class);
			Root<InvoiceEntity> root = cQuery.from(InvoiceEntity.class);

			Expression<BigDecimal> sumPoints = cb.sum(root.get(InvoiceEntity_.commission).get(CommissionableEmbeddable_.points));
			Expression<BigDecimal> sumCommission = cb.sum(root.get(InvoiceEntity_.commission).get(CommissionableEmbeddable_.commission));
			Expression<BigDecimal> sumPrice = cb.sum(root.get(InvoiceEntity_.commission).get(CommissionableEmbeddable_.price));
			
			cQuery.multiselect(sumPoints, sumCommission, sumPrice);

			
			Predicate entityPredicate = null;
			
			if(obj instanceof User){
				entityPredicate = cb.equal(root.get(InvoiceEntity_.user), obj);
			}else if(obj instanceof Shop){
				entityPredicate = cb.equal(root.get(InvoiceEntity_.shop), obj);
			}	
			
			cQuery.where(
					cb.and(
							entityPredicate,
							cb.greaterThanOrEqualTo(root.get(InvoiceEntity_.effectiveDate), from),
							cb.lessThanOrEqualTo(root.get(InvoiceEntity_.effectiveDate), to)
					)
			);
			
			Object[] valueArray = em.createQuery( cQuery ).getSingleResult();
			
//			System.out.println("Calculate commission for invoices | PT:"+valueArray[0]+" Comm:"+valueArray[1]+" Price:"+valueArray[2]);
			

			invoiceCommissionable.setPoints((BigDecimal) valueArray[0]);
			invoiceCommissionable.setCommission((BigDecimal) valueArray[1]);
			invoiceCommissionable.setPrice((BigDecimal) valueArray[2]);


			if(invoiceCommissionable.getPoints() == null)
				invoiceCommissionable.setPoints(new BigDecimal(0));
			if(invoiceCommissionable.getPrice() == null)
				invoiceCommissionable.setPrice(new BigDecimal(0));
			if(invoiceCommissionable.getCommission() == null)
				invoiceCommissionable.setCommission(new BigDecimal(0));
		}
		catch(NoResultException e){
		}
		}

//		System.out.println("orderCommissionable: "+orderCommissionable);

		commissionableSum = commissionableSum.addCommissionable(invoiceCommissionable);
		
		
		

		// CommissionActivity Only Shop and User Points

		Commissionable commissionActivityCommissionable = new CommissionableEmbeddable();
		if(!(obj instanceof VO)){
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery< Object[]> cQuery = cb.createQuery( Object[].class);
			Root<CommissionActivityEntity> root = cQuery.from(CommissionActivityEntity.class);

			Expression<BigDecimal> sumPoints = cb.sum(root.get(CommissionActivityEntity_.commission).get(CommissionableEmbeddable_.points));
			Expression<BigDecimal> sumCommission = cb.sum(root.get(CommissionActivityEntity_.commission).get(CommissionableEmbeddable_.commission));
			Expression<BigDecimal> sumPrice = cb.sum(root.get(CommissionActivityEntity_.commission).get(CommissionableEmbeddable_.price));
			
			cQuery.multiselect(sumPoints, sumCommission, sumPrice);

			
			Predicate entityPredicate = null;
			
			if(obj instanceof User){
				entityPredicate = cb.equal(root.get(CommissionActivityEntity_.user), obj);
			}else if(obj instanceof Shop){
				entityPredicate = cb.equal(root.get(CommissionActivityEntity_.shop), obj);
			}	
			
			cQuery.where(
					cb.and(
							entityPredicate,
							cb.greaterThanOrEqualTo(root.get(CommissionActivityEntity_.effectiveDate), from),
							cb.lessThanOrEqualTo(root.get(CommissionActivityEntity_.effectiveDate), to)
					)
			);
			
			Object[] valueArray = em.createQuery( cQuery ).getSingleResult();

			commissionActivityCommissionable.setPoints((BigDecimal) valueArray[0]);
			commissionActivityCommissionable.setCommission((BigDecimal) valueArray[1]);
			orderCommissionable.setPrice((BigDecimal) valueArray[2]);


			if(commissionActivityCommissionable.getPoints() == null)
				commissionActivityCommissionable.setPoints(new BigDecimal(0));
			if(commissionActivityCommissionable.getPrice() == null)
				commissionActivityCommissionable.setPrice(new BigDecimal(0));
			if(commissionActivityCommissionable.getCommission() == null)
				commissionActivityCommissionable.setCommission(new BigDecimal(0));
		}
		catch(NoResultException e){
		}
		}

//		System.out.println("commissionActivityCommissionable: "+commissionActivityCommissionable);

		commissionableSum = commissionableSum.addCommissionable(commissionActivityCommissionable);
			

//		System.out.println("commissionableSum: "+commissionableSum);
//		System.out.println("getCommissionSum END for "+obj+" from: "+from+" to:"+to);
		
		return commissionableSum;
	}

	public List<? extends RevenueLevel> getRevenueLevelList() {
		
		List<? extends RevenueLevel> list = new ArrayList<RevenueLevel>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<RevenueLevelEntity> cQuery = cb.createQuery(RevenueLevelEntity.class);
			Root<RevenueLevelEntity> root = cQuery.from(RevenueLevelEntity.class);
			cQuery.select(root);
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public RevenueLevelEntity saveRevenueLevel(RevenueLevelEntity selectedRevenueLevel) {
		return em.merge(selectedRevenueLevel);
	}

	public List<? extends AquiredRevenueLevel> getAquiredRevenueLevelList(Vendor vendor) {
		List<? extends AquiredRevenueLevel> list = new ArrayList<AquiredRevenueLevel>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AquiredRevenueLevelEntity> cQuery = cb.createQuery(AquiredRevenueLevelEntity.class);
			Root<AquiredRevenueLevelEntity> root = cQuery.from(AquiredRevenueLevelEntity.class);
			cQuery.select(root);
			
			cQuery.where(
					cb.equal(root.get(AquiredRevenueLevelEntity_.vendor), vendor)
			);
			
			cQuery.orderBy(cb.asc(root.get(AquiredRevenueLevelEntity_.scaleFrom)));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends ContractedRevenueLevel> getContractedRevenueLevelList(Vendor vendor) {
		List<? extends ContractedRevenueLevel> list = new ArrayList<ContractedRevenueLevel>();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ContractedRevenueLevelEntity> cQuery = cb.createQuery(ContractedRevenueLevelEntity.class);
			Root<ContractedRevenueLevelEntity> root = cQuery.from(ContractedRevenueLevelEntity.class);
			cQuery.select(root);
			
			cQuery.where(
					cb.equal(root.get(ContractedRevenueLevelEntity_.vendor), vendor)
			);
			
			cQuery.orderBy(cb.asc(root.get(ContractedRevenueLevelEntity_.scaleFrom)));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public void deleteRevenueLevel(RevenueLevelEntity revenueLevel) {
		revenueLevel = em.find(RevenueLevelEntity.class, revenueLevel.getId());
		em.refresh(revenueLevel);
		em.remove(revenueLevel);
	}

	public AquiredRevenueLevel getAquiredRevenueLevel(BigDecimal revenue, Vendor vendor) {
		AquiredRevenueLevel revenueLevel = new AquiredRevenueLevelEntity();
		
		try{
			
			revenueLevel = (AquiredRevenueLevelEntity) em.createNamedQuery("RevenueLevel.listAquiredRevenueLevelEntityInRange")
		            .setParameter("gteScaleFrom", revenue)
		            .setParameter("lteScaleTo", revenue)
		            .setParameter("vendorId", vendor.getId())
		            .getSingleResult();
			
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<AquiredRevenueLevelEntity> cQuery = cb.createQuery(AquiredRevenueLevelEntity.class);
//			Root<AquiredRevenueLevelEntity> root = cQuery.from(AquiredRevenueLevelEntity.class);
//			cQuery.select(root);
//
//
//			cQuery.where(
//					cb.and(
//					        cb.lessThanOrEqualTo(
//					                root.get(AquiredRevenueLevelEntity_.scaleFrom), revenue
//					        ),
//					        cb.greaterThanOrEqualTo(
//					        		root.get(AquiredRevenueLevelEntity_.scaleTo), revenue
//					        ),
//					        cb.equal(
//					        		root.get(AquiredRevenueLevelEntity_.vendor), vendor
//					        )
//				    )
//			);
//			
//			revenueLevel = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return revenueLevel;
	}

	public ContractedRevenueLevel getContractedRevenueLevel(BigDecimal grossRevenue, Vendor vendor) {
		ContractedRevenueLevel revenueLevel = new ContractedRevenueLevelEntity();
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ContractedRevenueLevelEntity> cQuery = cb.createQuery(ContractedRevenueLevelEntity.class);
			Root<ContractedRevenueLevelEntity> root = cQuery.from(ContractedRevenueLevelEntity.class);
			cQuery.select(root);


			cQuery.where(
					cb.and(
					        cb.lessThanOrEqualTo(
					                root.get(ContractedRevenueLevelEntity_.scaleFrom), grossRevenue
					        ),
					        cb.greaterThanOrEqualTo(
					        		root.get(ContractedRevenueLevelEntity_.scaleTo), grossRevenue
					        ),
					        cb.equal(
					        		root.get(ContractedRevenueLevelEntity_.vendor), vendor
					        )
				    )
			);

			
			revenueLevel = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return revenueLevel;
	}

}
