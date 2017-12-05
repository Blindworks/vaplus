package de.vaplus;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.PayrollControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.CommissionCache;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserPayroll;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.eao.PayrollEao;
import de.vaplus.client.entity.UserPayrollEntity;

@Stateless
public class PayrollController implements PayrollControllerInterface{

	private static final long serialVersionUID = -1211445707025902101L;


	@EJB
	private UserControllerInterface userController;
	
	@Inject
    private PayrollEao eao;
	
	@Override
	public List<? extends UserPayroll> getUserPayrollList(int month, int year) {
		return eao.getUserPayrollList(month, year);
	}
	
	@Override
	public UserPayroll getUserPayroll(User user, int month, int year) {
		return eao.getUserPayroll(user, month, year);
	}
	
	@Override
	public UserPayroll factoryNewUserPayroll(User user, int month, int year) {
		return new UserPayrollEntity(user, month, year);
	}

	@Override
	public void generateUserPayrolls(int month, int year) {
		List<? extends User> userList = userController.getUserList();
		Iterator<? extends User> i = userList.iterator();
		User user;
		UserPayroll userPayroll;
		
		while(i.hasNext()){
			user = i.next();
			
			userPayroll = getUserPayroll(user,month,year);
			if(userPayroll == null)
				userPayroll = factoryNewUserPayroll(user,month,year);
			
			userPayroll = updateUserPayroll(userPayroll);
		}
		
	}
	
	private UserPayroll updateUserPayroll(UserPayroll userPayroll){
		
		userPayroll.updateUserValues();
		
		CommissionCache commissionCache = userPayroll.getUser().getCommissionCache(userPayroll.getYear(), userPayroll.getMonth());
		userPayroll.setPoints(commissionCache.getCommission().getPoints());
		userPayroll.setYieldedCommission(commissionCache.getCommission().getCommission());
		
		System.out.println("check points goal:"+userPayroll.getPoints()+" / " +userPayroll.getMinimalBonusPointGoal());
		
		if(userPayroll.getPoints().compareTo(userPayroll.getMinimalBonusPointGoal()) < 0){
			System.out.println("points under goal");
			// points under goal
			userPayroll.setCommission(new BigDecimal(0));
			
		}else{
			System.out.println("point goal reached");
			// point goal reached
			userPayroll.setCommission(userPayroll.getBonusSalary().divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP).multiply(userPayroll.getGoalAttainment()));
		}
		
		return eao.saveUserPayroll(userPayroll);
	}
	
	@Override
	public UserPayroll saveUserPayroll(UserPayroll userPayroll){
		return eao.saveUserPayroll(userPayroll);
	}
}
