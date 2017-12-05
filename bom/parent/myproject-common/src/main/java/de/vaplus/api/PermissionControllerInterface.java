package de.vaplus.api;

import java.io.Serializable;

import de.vaplus.api.entity.User;
import de.vaplus.api.entity.UserGroup;

public interface PermissionControllerInterface extends Serializable {

	boolean hasOwnPermission(User user, String resource, String command);

	boolean hasOwnPermission(UserGroup userGroup, String resource, String command);
	
	boolean hasPermission(User user, String resource, String command);

	void grantPermission(User user, String resource, String command, int permission);

	void revokePermission(User user, String resource, String command);

	void grantPermission(UserGroup userGroup, String resource, String command, int permission);

	void revokePermission(UserGroup userGroup, String resource, String command);

	User getUserByAccountTypeId(String attributedTypeId);


}
