package de.vaplus.pojo;


public class EaoFilter<T> {
	public String name;
	public T expression;
	public boolean likeFilter;
	
//	public SingularAttribute<? super T,Y> attribute;
	
	public EaoFilter(String name, T expression, boolean likeFilter){
		this.name = name;
		this.expression = expression;
		this.likeFilter = likeFilter;
//		this.attribute = attribute;
	}
}
