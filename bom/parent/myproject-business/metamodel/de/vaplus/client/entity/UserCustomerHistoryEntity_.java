package de.vaplus.client.entity;

import de.vaplus.client.entity.CustomerEntity;
import de.vaplus.client.entity.UserEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T17:31:03")
@StaticMetamodel(UserCustomerHistoryEntity.class)
public class UserCustomerHistoryEntity_ { 

    public static volatile SingularAttribute<UserCustomerHistoryEntity, Date> lastOpened;
    public static volatile SingularAttribute<UserCustomerHistoryEntity, UserEntity> user;
    public static volatile SingularAttribute<UserCustomerHistoryEntity, CustomerEntity> customer;

}