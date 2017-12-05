package de.vaplus.client.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.vaplus.api.SearchControllerInterface;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.client.pojo.Icon;
import de.vaplus.client.pojo.Page;

@ManagedBean(name="searchBean")
@SessionScoped
public class SearchBean implements Serializable {
	
	private static final long serialVersionUID = -8377125983084844337L;
	
	public static final int RESULTS_PER_PAGE = 5;

	@EJB
	private SearchControllerInterface seachController;
	
	private String query;
	
	private int firstPagedResult = 0;
	
	private List<? extends SearchResult> results;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getInitQuery() {
		return null;
	}

	public void setInitQuery(String query) {
		this.query = query;
	}
	
	public String search(){
		setFirstPagedResult(0);
		
		if(query.trim().length() > 0)
			setResults(seachController.getResults(query));
		else
			setResults(new ArrayList<SearchResult>());
		
		return "/searchResult";
	}

	public List<? extends SearchResult> getResultList() {
		return results;
	}

	public List<? extends SearchResult> getResultSubList() {
		if(results == null)
			return null;
		
		int fromIndex = firstPagedResult >= 0 ? firstPagedResult : 0;
		int toIndex = fromIndex + RESULTS_PER_PAGE < results.size() ? fromIndex + RESULTS_PER_PAGE : results.size() -1;
		List<? extends SearchResult> subList = results.subList(fromIndex, toIndex +1);
		
		return subList;
	}

	public void setResults(List<? extends SearchResult> results) {
		this.results = results;
	}

	public int getFirstPagedResult() {
		return firstPagedResult;
	}

	public void setFirstPagedResult(int firstPagedResult) {
		this.firstPagedResult = firstPagedResult;
	}
	
	public List<Page> getResultPages(){
		List<Page> pages = new ArrayList<Page>();

		pages.add(new Page(null,0,Icon.CHEVRON_LEFT, false));
		
		pages.add(new Page("1",0,null, true));
		pages.add(new Page("2",5,null, false));
		pages.add(new Page("3",10,null, false));
		pages.add(new Page("4",15,null, false));
		
		pages.add(new Page(null,30,Icon.CHEVRON_RIGHT, false));
		
		return pages;
	}
}
