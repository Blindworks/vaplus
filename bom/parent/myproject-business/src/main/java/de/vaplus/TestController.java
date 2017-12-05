package de.vaplus;



import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.AccountingControllerInterface;
import de.vaplus.api.AchievementControllerInterface;
import de.vaplus.api.CommissionAccountingControllerInterface;
import de.vaplus.api.CommissionControllerInterface;
import de.vaplus.api.ContractControllerInterface;
import de.vaplus.api.CrosscanControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.EventControllerInterface;
import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.SalesControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.StockControllerInterface;
import de.vaplus.api.TestControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.client.eao.AccountingEao;
import de.vaplus.client.eao.NumberRangeEao;


@Stateless
//@ManagedBean(name="testBeanImpl")
//@Local(TestBean.class)
public class TestController implements TestControllerInterface {

	@EJB
	private UserControllerInterface userController;

	@EJB
	private CustomerControllerInterface customerController;

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private VOControllerInterface voController;

	@EJB
	private CommissionControllerInterface commissionController;

	@EJB
	private ContractControllerInterface contractController;

	@EJB
	private EventControllerInterface eventController;

	@EJB
	private CommissionAccountingControllerInterface commissionAccountingController;

	@EJB
	private AccountingControllerInterface accountingController;

	@EJB
	private AchievementControllerInterface achievementController;

	@EJB
	private ExceptionControllerInterface exceptionController;

	@EJB
	private SalesControllerInterface salesController;

	@EJB
	private CrosscanControllerInterface crosscanController;

	@EJB
	private StockControllerInterface stockController;

	@Inject
    private AccountingEao accountingEao;

	@Inject
    private NumberRangeEao numberRangeEao;


	public TestController(){
	}

	@Override
	public void test(){
		
		commissionController.updateAllComissionHistory();
		
		/*
		
//		Shop shop = shopController.getShop(1);
		
		AbstractSessionLog.getLog().log(SessionLog.INFO, DatabaseLogin.getVersion());

		System.out.println("START "+ DatabaseLogin.getVersion());
		
//		String query = "SELECT NEW de.vaplus.client.entity.AchievementEntity(a.name, a.effectiveDate, a.expiryDate)  FROM AchievementEntity a WHERE a.deleted = false ORDER BY a.weight, a.name ASC";
		String query = "SELECT a FROM AchievementEntity a WHERE a.deleted = false AND a.id = 13 ORDER BY a.weight, a.name ASC";
//		AND a.id = 22 
		List<AchievementEntity> olist = em.createQuery(query, AchievementEntity.class).getResultList();

		System.out.println("GOT LIST "+ olist.size());
		
		Iterator<AchievementEntity> i = olist.iterator();
		
		AchievementEntity a;
		while(i.hasNext()){
			a = i.next();
			
			em.merge(a);
			
			System.out.println("---");
			System.out.println("ID: "+ a.getId() );
			System.out.println("NAME: "+ a.getName() );

//			System.out.println("userGoalAttainmentList: "+ a.getUserGoalAttainmentList().size());
			
//			System.out.println("User: "+ a.getPayoutUserList().size());
//			System.out.println("getPieceTargetList: "+ a.getPieceTargetList().size());
//			System.out.println("getContractedRevenueTargetList: "+ a.getContractedRevenueTargetList().size());
//			System.out.println("effectiveDate: "+ a.getEffectiveDate() );
//			System.out.println("expiryDate: "+  a.getExpiryDate() );
//			System.out.println("---");
			
			
		}
		
//		List<? extends Achievement> list = em.createQuery(query, AchievementEntity.class)
//				.
//				        .getResultList();

//		Iterator<? extends Achievement> i = list.iterator();
//		
//		while(i.hasNext()){
//			System.out.println("A: "+i.next().getName());
//		}
		
		System.out.println("END");
		
		*/
		
	}

}
