package org.picketlink.idm.jpa.model.sample.simple;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T09:10:31.773+0100")
@StaticMetamodel(GroupTypeEntity.class)
public class GroupTypeEntity_ extends IdentityTypeEntity_ {
	public static volatile SingularAttribute<GroupTypeEntity, String> name;
	public static volatile SingularAttribute<GroupTypeEntity, String> path;
	public static volatile SingularAttribute<GroupTypeEntity, GroupTypeEntity> parent;
}
