package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import de.vaplus.api.entity.CrosscanData;
import de.vaplus.api.entity.Shop;
import de.vaplus.client.entity.CrosscanDataEntity;


@Stateless
public class CrosscanDataEao implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 2066749648262682869L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;


    public CrosscanDataEntity saveCrosscanData(CrosscanDataEntity crosscanData){
    	return em.merge(crosscanData);
    }
    
	public CrosscanData getCrosscanData(long id) {
		return em.find(CrosscanDataEntity.class, id);
	}

	public boolean checkIfDataExists(Shop shop, int time){
		
		try{
	
			long count = em.createNamedQuery("CrosscanData.checkIfDataExists",Long.class)
		            .setParameter("shop", shop)
		            .setParameter("time", time)
		            .setMaxResults(1)
		            .getSingleResult();
			
			return count > 0;
				
			
		}
		catch(NoResultException e){
		}
		
		return false;
		
	}

	public int getLatestDataTime(Shop shop){
		
		int time = 0;
		
		try{
	
			time = em.createNamedQuery("CrosscanData.getLatestDataTime",Integer.class)
			            .setParameter("shop", shop)
			            .setMaxResults(1)
			            .getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return time;
	}

	public List<CrosscanDataEntity> getCrosscanDataList(){
		return getCrosscanDataList(false);
	}

	public List<CrosscanDataEntity> getCrosscanDataList(boolean showDisabled){
		
		List<CrosscanDataEntity> list = new ArrayList<CrosscanDataEntity>();
		
		try{
	
			if(! showDisabled){
				
				list = em.createNamedQuery("CrosscanData.listEnabled",CrosscanDataEntity.class)
			            .setParameter("enabled", true)
			            .getResultList();
			}else{
				list = em.createNamedQuery("CrosscanData.listAll",CrosscanDataEntity.class)
			            .getResultList();
			}
		}
		catch(NoResultException e){
		}
		
		return list;
	}


	public CrosscanData refreshCrosscanData(CrosscanData crosscanData) {
		CrosscanData s = em.find(CrosscanDataEntity.class, crosscanData.getId());
		em.refresh(s);
		return s;
	}

	public long getAmountSum(Shop shop, int startTime, int endTime) {
		long sum = 0;
		
		if(shop == null)
			return 0;
		
		try{
			List<Long> resultList = em.createNamedQuery("CrosscanData.amountSum",Long.class)
						            .setParameter("shop", shop)
						            .setParameter("startTime", startTime)
						            .setParameter("endTime", endTime)
						            .getResultList();
			
			if(resultList.size() > 0 && resultList.get(0) != null){
				sum = resultList.get(0);
			}
		}
		catch(NoResultException e){
			System.out.println("NoResultException for "+shop+" "+startTime+" "+endTime+" "+e.getMessage());
		}
		
		return sum;
	}

}
