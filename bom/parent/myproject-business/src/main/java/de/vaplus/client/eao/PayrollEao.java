package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserPayroll;
import de.vaplus.client.entity.UserPayrollEntity;

@Stateless
public class PayrollEao implements Serializable {

	private static final long serialVersionUID = -5667593533331928151L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public List<? extends UserPayroll> getUserPayrollList(int month, int year) {
		
		List<? extends UserPayroll> list = em.createNamedQuery("UserPayroll.findByDate", UserPayrollEntity.class)
		            .setParameter("year", year)
		            .setParameter("month", month)
		            .getResultList();
		
		
		return list;
	}

	public UserPayroll getUserPayroll(User user, int month, int year) {
		UserPayroll payroll = null;
		
		try{
			payroll = em.createNamedQuery("UserPayroll.findByDateAndUser", UserPayrollEntity.class)
		            .setParameter("year", year)
		            .setParameter("month", month)
		            .setParameter("user", user)
		            .setMaxResults(1)
		            .getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return payroll;
	}

	public UserPayroll saveUserPayroll(UserPayroll userPayroll) {
		return em.merge(userPayroll);
	}
}
