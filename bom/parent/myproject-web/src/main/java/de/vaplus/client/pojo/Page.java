package de.vaplus.client.pojo;

import java.util.LinkedList;
import java.util.List;

public class Page {

	private String viewId;
	private String viewIdAlias;
	private String icon;
	private String title;
	private int pageNum;
	private boolean active;
	

	private List<Page> subPages;
	
	public Page(String title, String viewId, String icon){
		setViewId(viewId);
		setIcon(icon);
		setTitle(title);
	}
	
	public Page(String title, String viewId, String icon, String viewIdAlias){
		setViewId(viewId);
		setIcon(icon);
		setTitle(title);
		setViewIdAlias(viewIdAlias);
	}
	
	public Page(String title, String viewId, boolean active){
		setTitle(title);
		setViewId(viewId);
		setActive(active);
	}
	
	public Page(String title, int pageNum, String icon, boolean active){
		setTitle(title);
		setPageNum(pageNum);
		setIcon(icon);
		setActive(active);
	}
	
	public String getPageId(){
		return "page_" + getTitle().toLowerCase().replaceAll("[^0-9a-z]", "");
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	
	public boolean isIconSet(){
		return this.icon != null;
	}
	
	public boolean isSubPageSet(){
		return subPages != null;
	}

	public List<Page> getSubPages() {
		return subPages;
	}

	public void clearSubPages() {
		subPages = null;
	}

	public void addSubPage(Page page) {
		if(!isSubPageSet())
			subPages = new LinkedList<Page>();
		subPages.add(page);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int isPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getViewIdAlias() {
		return viewIdAlias;
	}

	public void setViewIdAlias(String viewIdAlias) {
		this.viewIdAlias = viewIdAlias;
	}
	
}
