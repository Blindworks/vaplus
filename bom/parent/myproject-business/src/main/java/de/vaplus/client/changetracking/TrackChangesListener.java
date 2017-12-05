package de.vaplus.client.changetracking;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.apache.commons.lang.Validate;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.internal.sessions.DirectToFieldChangeRecord;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;

import de.vaplus.client.entity.BaseEntity;

public class TrackChangesListener extends DescriptorEventAdapter {
	

//	@Override
//	public void postMerge(DescriptorEvent event) {
//        System.out.println("postMerge");
//        BaseEntity entity;
//        
//        if(event.getObject() instanceof BaseEntity){
//	        System.out.println("Object instanceof BaseEntity");
//        	
//        	entity = (BaseEntity) event.getObject();
//        	ObjectChangeSet changeSet = event.getChangeSet();
//        	
//        	
//        	
//        	if (changeSet != null) {
//    	        System.out.println("ObjectChangeSet not null");
//
//	    		entity.setChangeRecordList(null);
//	    		
//    	    	List<org.eclipse.persistence.sessions.changesets.ChangeRecord> changes = changeSet.getChanges();
//    	    	Iterator<ChangeRecord> i = changes.iterator();
//    	    	ChangeRecord cr;
//    	    	DirectToFieldChangeRecord dfcr;
//    	    	while(i.hasNext()){
//    	    		cr = i.next();
//
//    	    		if(cr.getAttribute().equalsIgnoreCase("updateDate"))
//    	    			continue;
//    	    		
//    	    		
//    	    		if(cr instanceof DirectToFieldChangeRecord){
//    	    			entity.getChangeRecordList().add((DirectToFieldChangeRecord) cr);
//    	    		}
//    	    		
//    		        System.out.println("ObjectChange: "+cr.getAttribute()+" "+cr.getOldValue());
//    	    	}
//    	    	
//
//            	entity.getEao().postMerge(entity);
//    	    }
//        	
//        }
//	}

}
