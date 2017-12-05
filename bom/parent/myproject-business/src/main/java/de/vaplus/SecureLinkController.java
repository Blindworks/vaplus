package de.vaplus;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.SecureLinkControllerInterface;
import de.vaplus.api.entity.SecureLink;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.User;
import de.vaplus.client.entity.SecureLinkEntity;
import de.vaplus.client.entity.SecureUserLinkEntity;
import de.vaplus.client.entity.UserEntity;

@Stateless
public class SecureLinkController implements SecureLinkControllerInterface {


	private static final String LETTERS = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
	private static final int CODE_LENGTH = 100;
	
	
	private static final Random RANDOM = new SecureRandom();

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;
    
    @Override
    public void expireSecureLink(SecureLink secureLink){
    	
    	if(secureLink == null){
    		System.err.println("NO SECURE LINK TO EXPIRE");
    		return;
    	}
    		
    	
    	SecureLinkEntity secureLinkEntity = em.find(SecureLinkEntity.class, ((SecureLinkEntity)secureLink).getId());
    	
    	secureLinkEntity.setEnabled(false);
    	em.merge(secureLinkEntity);
    }
    
    @Override
    public SecureLink getSecureLinkByCode(String code){
    	
    	SecureLink secureLink = null;
		try{
	
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<SecureLinkEntity> cQuery = cb.createQuery(SecureLinkEntity.class);
			Root<SecureLinkEntity> root = cQuery.from(SecureLinkEntity.class);
			cQuery.select(root);

			List<Predicate> criteria = new ArrayList<Predicate>();
			
			// Code
			criteria.add(cb.equal(root.get("code"), code));
			
			// Enabled Flag
			criteria.add(cb.equal(root.get("enabled"), true));
			
			// Expired Date
			criteria.add(cb.or( 
					cb.greaterThan(root.<Date>get("expiryDate"), Calendar.getInstance().getTime()),
					cb.isNull(root.<Date>get("expiryDate"))
			));
			
			cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			secureLink = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
			
		}
		catch(NoResultException e){
//			System.out.println("No SecureLink found for Code");
		}
		
		// check 
		
		
		return secureLink;
    }
    

    @Override
    public SecureUserLink factoryNewSecureUserLink(User user, String operation, Date expiryDate){
    	SecureUserLinkEntity link = new SecureUserLinkEntity();
    	prepareNewSecureLink(link, operation, expiryDate);
    	link.setUserEntity((UserEntity) user);
    	link.setEnabled(true);
    	
    	em.merge(link);
    	
    	return link;
    }

    private void prepareNewSecureLink(SecureLinkEntity link, String operation, Date expiryDate){
    	
        String code = "";
        
        for (int i=0; i<CODE_LENGTH; i++)
        {
            int index = (int)(RANDOM.nextDouble()*LETTERS.length());
            code += LETTERS.substring(index, index+1);
        }
        
    	link.setExpiryDate(expiryDate);
    	link.setOperation(operation);
    	link.setCode(code);
    	
    	// generate Key
    }
}
