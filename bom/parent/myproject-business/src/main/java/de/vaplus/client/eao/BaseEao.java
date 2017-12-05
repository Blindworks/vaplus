package de.vaplus.client.eao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;


@Stateless
public class BaseEao implements Serializable {

	private static final long serialVersionUID = 3109004286787960117L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;
    
    protected <T> T merge(T e){
    	
//    	
//    	if(e instanceof BaseEntity){
//    		BaseEntity be = (BaseEntity) e;
//    		T old = (T) em.find(e.getClass(), be.getId());
////    		em.refresh(old);
//    		
//    		try {
//    			BeanInfo info = Introspector.getBeanInfo(e.getClass(), Object.class);
//
//    	        PropertyDescriptor[] props = info.getPropertyDescriptors();
//    	        for (PropertyDescriptor pd : props) {
//    	            String name = pd.getName();
//    	            Method getter = pd.getReadMethod();
//    	            Class<?> type = pd.getPropertyType();
//    	     
//    	            Object value = getter.invoke(e);
//    	            
////    	            if()
//    	            	
//
//					System.out.println("OLD VALUE: "+name+" "+getter.invoke(old));
//	            	System.out.println("NEW VALUE: "+name+" "+getter.invoke(e));
//    	            
//    	     
////    	            System.out.println(name + " = " + value + "; type = " + type);
//    	        }
//    		} catch (Exception e1) {
//    			// TODO Auto-generated catch block
//    			e1.printStackTrace();
//    		}
//    		
//
//
//    	}
//    	
//    	T entity = em.merge(e);
//    	System.out.println("EAO merge Entity");
//    	
//    	if(e instanceof BaseEntity){
//    		BaseEntity be = (BaseEntity) e;
//    		
//    		be.setEao(this);
//    		
//    		Iterator<DirectToFieldChangeRecord> i = be.getChangeRecordList().iterator();
//    		DirectToFieldChangeRecord r;
//    		while(i.hasNext()){
//    			r = i.next();
//    			System.out.println("EAO A find ObjectChange: "+r.getAttribute()+" "+r.getOldValue()+" -> "+r.getNewValue());
//    		}
//    	}
//    	
//    	if(entity instanceof BaseEntity){
//    		BaseEntity be = (BaseEntity) entity;
//    		
//    		Iterator<DirectToFieldChangeRecord> i = be.getChangeRecordList().iterator();
//    		DirectToFieldChangeRecord r;
//    		while(i.hasNext()){
//    			r = i.next();
//    			System.out.println("EAO B find ObjectChange: "+r.getAttribute()+" "+r.getOldValue()+" -> "+r.getNewValue());
//    		}
//    	}
//    	
//    	return entity;
    	
    	return em.merge(e);
    }
    
//    public final void postMerge(BaseEntity entity){
//    	System.out.println("EAO postMerge Entity "+entity);
//    	
//		BaseEntity be = (BaseEntity) entity;
//    		
//		Iterator<DirectToFieldChangeRecord> i = be.getChangeRecordList().iterator();
//		DirectToFieldChangeRecord r;
//		while(i.hasNext()){
//			r = i.next();
//			System.out.println("EAO B find ObjectChange: "+r.getAttribute()+" "+r.getOldValue()+" -> "+r.getNewValue());
//		}
//    }
    
    protected CriteriaBuilder getCriteriaBuilder(){
    	return em.getCriteriaBuilder();
    }
    
    protected Query createNamedQuery(String query){
    	return em.createNamedQuery(query);
    }
    
    protected <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1){
    	return em.createNamedQuery(arg0, arg1);
    }
    
    protected Query createQuery(String arg0){
    	return em.createQuery(arg0);
    }
    
    protected <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0){
    	return em.createQuery(arg0);
    }
    
    protected <T> T find(Class<T> arg0, Object arg1){
    	return em.find(arg0, arg1);
    }
    
    protected EntityManagerFactory getEntityManagerFactory(){
    	return em.getEntityManagerFactory();
    }
    
    protected void remove(Object arg0){
    	em.remove(arg0);
    }
    
    protected Metamodel getMetamodel(){
    	return em.getMetamodel();
    }
    
    protected void refresh(Object arg0){
    	em.refresh(arg0);
    }
}
