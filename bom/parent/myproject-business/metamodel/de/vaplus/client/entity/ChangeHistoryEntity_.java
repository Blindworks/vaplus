package de.vaplus.client.entity;

import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(ChangeHistoryEntity.class)
public class ChangeHistoryEntity_ { 

    public static volatile SingularAttribute<ChangeHistoryEntity, ShopEntity> shop;
    public static volatile SingularAttribute<ChangeHistoryEntity, Date> changeDate;
    public static volatile SingularAttribute<ChangeHistoryEntity, String> baseClass;
    public static volatile SingularAttribute<ChangeHistoryEntity, Long> id;
    public static volatile SingularAttribute<ChangeHistoryEntity, Long> baseId;
    public static volatile SingularAttribute<ChangeHistoryEntity, UserEntity> user;

}