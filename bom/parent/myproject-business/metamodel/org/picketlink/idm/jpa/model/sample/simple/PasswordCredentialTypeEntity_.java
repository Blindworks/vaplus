package org.picketlink.idm.jpa.model.sample.simple;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T09:10:31.814+0100")
@StaticMetamodel(PasswordCredentialTypeEntity.class)
public class PasswordCredentialTypeEntity_ {
	public static volatile SingularAttribute<PasswordCredentialTypeEntity, String> passwordEncodedHash;
	public static volatile SingularAttribute<PasswordCredentialTypeEntity, String> passwordSalt;
}
