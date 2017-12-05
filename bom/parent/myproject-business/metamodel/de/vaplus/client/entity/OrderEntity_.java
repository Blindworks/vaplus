package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.OrderItemEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(OrderEntity.class)
public class OrderEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<OrderEntity, String> number;
    public static volatile SingularAttribute<OrderEntity, BaseContractEntity> subsidyContract;
    public static volatile ListAttribute<OrderEntity, OrderItemEntity> orderItemList;
    public static volatile SingularAttribute<OrderEntity, CommissionableEmbeddable> commission;
    public static volatile SingularAttribute<OrderEntity, String> introduction;
    public static volatile SingularAttribute<OrderEntity, String> info;

}