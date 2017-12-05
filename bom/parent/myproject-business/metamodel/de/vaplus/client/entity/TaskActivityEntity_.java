package de.vaplus.client.entity;

import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.UserGroupEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(TaskActivityEntity.class)
public class TaskActivityEntity_ extends ActivityEntity_ {

    public static volatile ListAttribute<TaskActivityEntity, UserEntity> userReceipientList;
    public static volatile SingularAttribute<TaskActivityEntity, Date> targetDate;
    public static volatile ListAttribute<TaskActivityEntity, UserGroupEntity> userGroupReceipientList;
    public static volatile SingularAttribute<TaskActivityEntity, UserEntity> completedByUser;
    public static volatile SingularAttribute<TaskActivityEntity, Boolean> toEveryone;
    public static volatile SingularAttribute<TaskActivityEntity, String> title;
    public static volatile ListAttribute<TaskActivityEntity, ShopEntity> shopReceipientList;
    public static volatile SingularAttribute<TaskActivityEntity, Date> completedDate;

}