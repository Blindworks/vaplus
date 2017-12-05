package de.vaplus.client.entity;

import de.vaplus.client.entity.PaymentAccountEntity;
import de.vaplus.client.entity.PaymentEntity;
import de.vaplus.client.entity.ShopEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(PaymentShopAccountEntity.class)
public class PaymentShopAccountEntity_ { 

    public static volatile SingularAttribute<PaymentShopAccountEntity, ShopEntity> shop;
    public static volatile SingularAttribute<PaymentShopAccountEntity, PaymentAccountEntity> settlingAccount;
    public static volatile SingularAttribute<PaymentShopAccountEntity, PaymentEntity> payment;
    public static volatile SingularAttribute<PaymentShopAccountEntity, PaymentAccountEntity> account;

}