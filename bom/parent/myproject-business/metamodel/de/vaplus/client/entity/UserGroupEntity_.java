package de.vaplus.client.entity;

import de.vaplus.client.entity.UserGroupPermissionEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T16:54:18")
@StaticMetamodel(UserGroupEntity.class)
public class UserGroupEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<UserGroupEntity, String> name;
    public static volatile ListAttribute<UserGroupEntity, UserGroupPermissionEntity> permissionList;

}