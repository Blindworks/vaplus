package de.vaplus.client.entity;

import de.vaplus.client.entity.PaymentEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(PaymentStatusEntity.class)
public class PaymentStatusEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<PaymentStatusEntity, BigDecimal> cumulativeSum;
    public static volatile SingularAttribute<PaymentStatusEntity, PaymentEntity> payment;
    public static volatile SingularAttribute<PaymentStatusEntity, Boolean> open;

}