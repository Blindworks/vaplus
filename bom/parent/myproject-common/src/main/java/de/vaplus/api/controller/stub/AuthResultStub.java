package de.vaplus.api.controller.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import de.vaplus.api.interfaces.AuthResult;

@XmlAccessorType(XmlAccessType.FIELD)
public class AuthResultStub{

	@XmlAttribute
	private boolean ack = false;
	
	@XmlAttribute
	private String licenseName;

	@XmlAttribute
	private String authHash;

	@XmlAttribute
	private String baseFilePath;

	@XmlAttribute
	private long storageLimit;
	
	public AuthResultStub(){}
	
	public AuthResultStub(AuthResult obj){
		ack = obj.isAck();
		licenseName = obj.getLicenseName();
		authHash = obj.getAuthHash();
		baseFilePath = obj.getBaseFilePath();
		storageLimit = obj.getStorageLimit();
	}

//	@Override
	public String getLicenseName() {
		return licenseName;
	}

//	@Override
	public String getAuthHash() {
		return authHash;
	}

//	@Override
	public String getBaseFilePath() {
		return baseFilePath;
	}

//	@Override
	public long getStorageLimit() {
		return storageLimit;
	}

//	@Override
	public boolean isAck() {
		return ack;
	}
}
