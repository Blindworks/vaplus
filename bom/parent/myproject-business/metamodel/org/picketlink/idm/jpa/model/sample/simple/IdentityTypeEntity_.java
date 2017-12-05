package org.picketlink.idm.jpa.model.sample.simple;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T09:10:31.787+0100")
@StaticMetamodel(IdentityTypeEntity.class)
public class IdentityTypeEntity_ extends AttributedTypeEntity_ {
	public static volatile SingularAttribute<IdentityTypeEntity, String> typeName;
	public static volatile SingularAttribute<IdentityTypeEntity, Date> createdDate;
	public static volatile SingularAttribute<IdentityTypeEntity, Date> expirationDate;
	public static volatile SingularAttribute<IdentityTypeEntity, Boolean> enabled;
	public static volatile SingularAttribute<IdentityTypeEntity, PartitionTypeEntity> partition;
}
