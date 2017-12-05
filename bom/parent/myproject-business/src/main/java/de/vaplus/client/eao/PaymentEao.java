package de.vaplus.client.eao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentAccountTransaction;
import de.vaplus.api.entity.PaymentShopAccount;
import de.vaplus.api.entity.PaymentStatus;
import de.vaplus.api.entity.Shop;
import de.vaplus.client.entity.PaymentAccountEntity;
import de.vaplus.client.entity.PaymentAccountTransactionEntity;
import de.vaplus.client.entity.PaymentEntity;
import de.vaplus.client.entity.PaymentShopAccountEntity;
import de.vaplus.client.entity.PaymentStatusEntity;
import de.vaplus.client.entity.PaymentStatusEntity_;


@Stateless
public class PaymentEao implements Serializable {

	private static final long serialVersionUID = -6375173529173621595L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public List<PaymentEntity> getPaymentList(boolean showDisabled){
		
		List<PaymentEntity> list = new ArrayList<PaymentEntity>();
		
		try{
	
			if(showDisabled)
				list = em.createNamedQuery("Payment.getAllPayments", PaymentEntity.class).getResultList();
			else
				list = em.createNamedQuery("Payment.getActivePayments", PaymentEntity.class).getResultList();
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public Payment getPaymentById(long id) {
		return em.find(PaymentEntity.class, id);
	}

	public Payment savePayment(Payment payment) {
		return em.merge(payment);
	}

	public PaymentShopAccount getPaymentShopAccount(Payment p, Shop s) {
		PaymentShopAccount account = null;
		
		
		try{
			account = em.createNamedQuery("PaymentShopAccount.get", PaymentShopAccountEntity.class)
					.setParameter("payment", p)
					.setParameter("shop", s)
					.setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return account;
	}

	public PaymentShopAccountEntity savePaymentShopAccount(PaymentShopAccountEntity paymentShopAccount) {
		return em.merge(paymentShopAccount);
	}

	public PaymentAccount getPaymentAccountById(long id) {
		return em.find(PaymentAccountEntity.class, id);
	}

	public PaymentAccount savePaymentAccount(PaymentAccount paymentAccount) {
		return em.merge(paymentAccount);
	}

	public void flush() {
		em.flush();
	}

	public PaymentAccountTransaction savePaymentAccountTransaction(PaymentAccountTransaction transaction) {
		return em.merge(transaction);
	}

	public BigDecimal getAccountBalance(PaymentAccount paymentAccount) {
		BigDecimal balance = new BigDecimal(0);
		
		balance = balance.add(getAccountIncoming(paymentAccount));
		
		balance = balance.subtract(getAccountOutgoing(paymentAccount));
		
		return balance;
	}

	public BigDecimal getAccountIncoming(PaymentAccount paymentAccount) {
		BigDecimal sum = new BigDecimal(0);
		
		try{
			sum = em.createNamedQuery("PaymentAccountTransaction.getAccountIncoming", BigDecimal.class)
					.setParameter("account", paymentAccount)
					.setMaxResults(1)
					.getSingleResult();
		}
		catch(NoResultException e){
		}
		
		if(sum == null)
			return new BigDecimal(0);
		
		return sum;
	}

	public BigDecimal getAccountOutgoing(PaymentAccount paymentAccount) {
		BigDecimal sum = new BigDecimal(0);
		
//		System.out.println("getAccountOutgoing for "+paymentAccount.getId());
		
		try{
//			balance = em.createNamedQuery("PaymentAccountTransaction.getAccountOutgoing", BigDecimal.class)
//					.setParameter("account", paymentAccount)
//					.getSingleResult();
			
			
			sum = em.createQuery("SELECT SUM(t.amount) FROM PaymentAccountTransactionEntity t WHERE t.sourceAccount = :account", BigDecimal.class)
					.setParameter("account", paymentAccount)
			        .getSingleResult();
		}
		catch(NoResultException e){
			e.printStackTrace();
		}

//		System.out.println("getAccountOutgoing sum: "+sum);
		
		if(sum == null)
			return new BigDecimal(0);
		
		return sum;
	}

	public List<? extends PaymentAccountTransaction> getAccountTransactions(PaymentAccount paymentAccount) {
		List<? extends PaymentAccountTransaction> list = new LinkedList<PaymentAccountTransactionEntity>();
		
		try{
			list = em.createNamedQuery("PaymentAccountTransaction.getTransactions", PaymentAccountTransactionEntity.class)
					.setParameter("account", paymentAccount)
					.setParameter("account", paymentAccount)
					.getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends PaymentStatus> getPaymentStatusList(Payment payment, Shop shop) {
		List<? extends PaymentStatus> list = new LinkedList<PaymentStatusEntity>();

		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PaymentStatusEntity> cQuery = cb.createQuery(PaymentStatusEntity.class);
			Root<PaymentStatusEntity> root = cQuery.from(PaymentStatusEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
							cb.equal(root.get(PaymentStatusEntity_.payment), payment),
							cb.equal(root.get(PaymentStatusEntity_.shop), shop)
					)
			);
			
			cQuery.orderBy(cb.desc(root.get(PaymentStatusEntity_.id)));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<? extends PaymentStatus> getPaymentStatusList(Payment payment, Shop shop, int month, int year) {
		List<? extends PaymentStatus> list = new LinkedList<PaymentStatusEntity>();
		
		Date dateStart;
		Date dateEnd;
		
		Calendar c = Calendar.getInstance();
		
		c.set(year, month, 1, 0, 0, 0);

		dateStart = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.SECOND, -1);
		
		dateEnd = c.getTime();
		
		System.out.println("getPaymentStatusList from "+dateStart+" to "+dateEnd);
		
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PaymentStatusEntity> cQuery = cb.createQuery(PaymentStatusEntity.class);
			Root<PaymentStatusEntity> root = cQuery.from(PaymentStatusEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
							cb.equal(root.get(PaymentStatusEntity_.payment), payment),
							cb.equal(root.get(PaymentStatusEntity_.shop), shop),
							cb.greaterThanOrEqualTo(root.get(PaymentStatusEntity_.effectiveDate), dateStart),
							cb.lessThanOrEqualTo(root.get(PaymentStatusEntity_.effectiveDate), dateEnd)
					)
			);
			
			cQuery.orderBy(cb.desc(root.get(PaymentStatusEntity_.id)));
			
			list = em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public BigDecimal getCumulativeSumForToday(Payment payment, Shop shop) {
		// TODO Auto-generated method stub
		return null;
	}

	public PaymentStatus savePaymentStatus(PaymentStatus paymentStatus) {
		return em.merge(paymentStatus);
	}
}
