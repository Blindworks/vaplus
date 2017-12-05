package de.vaplus.api.controller.stub;

import java.util.Date;

import de.vaplus.api.entity.StatusBase;

public class StatusBaseStub extends BaseStub{

	private static final long serialVersionUID = -694894954602288006L;

	private Date effectiveDate;

	private Date expiryDate;
	
	private boolean enabled;
	
	public StatusBaseStub(){}
	
	public StatusBaseStub(StatusBase obj){
		super(obj);
		effectiveDate = obj.getEffectiveDate();
		expiryDate = obj.getExpiryDate();
		enabled = obj.isEnabled();
	}

//	@Override
	public Date getEffectiveDate() {
		return effectiveDate;
	}

//	@Override
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

//	@Override
	public Date getExpiryDate() {
		return expiryDate;
	}

//	@Override
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

//	@Override
	public boolean isEnabled() {
		return enabled;
	}

//	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

//	@Override
	public void setStatusBaseEntityValues(StatusBase statusBase) {
		return;
	}

//	@Override
	public boolean isEnabledAndInTime() {
		return true;
	}

//	@Override
	public long getDaysTillExpiration() {
		return 0;
	}
}
