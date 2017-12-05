package org.picketlink.idm.jpa.model.sample.simple;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T09:10:31.830+0100")
@StaticMetamodel(PermissionTypeEntity.class)
public class PermissionTypeEntity_ {
	public static volatile SingularAttribute<PermissionTypeEntity, Long> id;
	public static volatile SingularAttribute<PermissionTypeEntity, String> assignee;
	public static volatile SingularAttribute<PermissionTypeEntity, String> resourceClass;
	public static volatile SingularAttribute<PermissionTypeEntity, String> resourceIdentifier;
	public static volatile SingularAttribute<PermissionTypeEntity, String> operation;
}
