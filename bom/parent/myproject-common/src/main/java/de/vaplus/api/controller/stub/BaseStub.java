package de.vaplus.api.controller.stub;

import java.util.Date;

import de.vaplus.api.entity.Base;

public class BaseStub implements Base {

	private static final long serialVersionUID = 7679412941076365501L;

	private long id;

	private Date creationDate;

	private Date updateDate;
	
	public BaseStub(){}
	
	public BaseStub(Base obj){
		id = obj.getId();
		creationDate = obj.getCreationDate();
		updateDate = obj.getUpdateDate();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public boolean isDeleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDeleted() {
		// TODO Auto-generated method stub
		
	}

}
