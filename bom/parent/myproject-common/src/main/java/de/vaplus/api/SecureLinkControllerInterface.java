package de.vaplus.api;

import java.io.Serializable;
import java.util.Date;

import de.vaplus.api.entity.SecureLink;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.api.entity.User;

public interface SecureLinkControllerInterface extends Serializable {

	public static final int DEFAULT_SECURELINK_LIFETIME = 7;

	public static final String OPERATION_CHANGE_PASSWORD = "changePwd";
	
	SecureUserLink factoryNewSecureUserLink(User user, String operation,
			Date expiryDate);

	SecureLink getSecureLinkByCode(String code);

	void expireSecureLink(SecureLink secureLink);

}
