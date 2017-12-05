package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="paginationBean")
@SessionScoped
public class PaginationBean implements Serializable {

	private static final long serialVersionUID = 8863155565837796496L;

	static class Pagination {
		public int offset = 0;
		public int limit = 10;
		public long maxEntries = 0;
		public int page = 1;
    }
	
	private Map<String,Pagination> pagitaionList;
	
	public PaginationBean(){
		pagitaionList = new HashMap<String,Pagination>();
	}
	
	private Pagination getPagination(String key){
		if(! pagitaionList.containsKey(key))
			pagitaionList.put(key, new Pagination());
		
		return pagitaionList.get(key);
	}
	
	public int getPage(String key){
		return  getPagination(key).page;
	}
	
	public void setPage(String key, int page){
		getPagination(key).page = page;
		getPagination(key).offset = (int) ((page - 1) * getPagination(key).limit);
	}
	
	public int getOffset(String key){
		return  getPagination(key).offset;
	}
	
	public int getLimit(String key){
		return  getPagination(key).limit;
	}
	
	public void setLimit(String key, int limit){
		getPagination(key).limit = limit;
		getPagination(key).offset = 0;
		getPagination(key).page = 1;
	}
	
	public long getMaxEntries(String key){
		return  getPagination(key).maxEntries;
	}
	
	public void setMaxEntries(String key, long maxEntries){
		getPagination(key).maxEntries = maxEntries;
	}
	
	public int getPageCount(String key){
		
		return (int) Math.ceil((double) getPagination(key).maxEntries / (double) getPagination(key).limit);
	}
	
	public void nextPage(String key){
		setPage(key,getPage(key) + 1);

		if(getPage(key) > getPageCount(key))
			setPage(key,getPageCount(key));
	}
	
	public void previousPage(String key){
		setPage(key,getPage(key) - 1);
		if(getPage(key) < 1)
			setPage(key,1);
	}
	
	public int getPrevPrevPageNumber(String key){
		int p = getPage(key) - 2;
		if(p > 0)
			return p;
		return 0;
	}
	
	public int getPrevPageNumber(String key){
		int p = getPage(key) - 1;
		if(p > 0)
			return p;
		return 0;
	}

	public int getNextPageNumber(String key){
		int p = getPage(key) + 1;
		if(p <= getPageCount(key))
			return p;
		return 0;
	}

	public int getNextNextPageNumber(String key){
		int p = getPage(key) + 2;
		if(p <= getPageCount(key))
			return p;
		return 0;
	}
}
