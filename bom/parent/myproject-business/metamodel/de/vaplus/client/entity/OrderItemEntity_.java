package de.vaplus.client.entity;

import de.vaplus.client.entity.OrderEntity;
import de.vaplus.client.entity.ProductEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(OrderItemEntity.class)
public class OrderItemEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<OrderItemEntity, BigDecimal> unitPrice;
    public static volatile SingularAttribute<OrderItemEntity, ProductEntity> product;
    public static volatile SingularAttribute<OrderItemEntity, BigDecimal> quantity;
    public static volatile SingularAttribute<OrderItemEntity, String> serial;
    public static volatile SingularAttribute<OrderItemEntity, CommissionableEmbeddable> commission;
    public static volatile SingularAttribute<OrderItemEntity, String> title;
    public static volatile SingularAttribute<OrderItemEntity, Boolean> blockedItem;
    public static volatile SingularAttribute<OrderItemEntity, OrderEntity> order;

}