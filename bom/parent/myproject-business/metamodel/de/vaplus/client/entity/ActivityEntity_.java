package de.vaplus.client.entity;

import de.vaplus.client.entity.CustomerEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(ActivityEntity.class)
public abstract class ActivityEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<ActivityEntity, String> note;
    public static volatile SingularAttribute<ActivityEntity, ShopEntity> shop;
    public static volatile SingularAttribute<ActivityEntity, Boolean> hideInTimeline;
    public static volatile SingularAttribute<ActivityEntity, UserEntity> user;
    public static volatile SingularAttribute<ActivityEntity, CustomerEntity> customer;

}