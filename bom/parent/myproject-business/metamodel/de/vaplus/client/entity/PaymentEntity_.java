package de.vaplus.client.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(PaymentEntity.class)
public class PaymentEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<PaymentEntity, String> invoiceText;
    public static volatile SingularAttribute<PaymentEntity, String> name;
    public static volatile SingularAttribute<PaymentEntity, Boolean> direct;
    public static volatile SingularAttribute<PaymentEntity, String> description;
    public static volatile SingularAttribute<PaymentEntity, Boolean> systemPayment;
    public static volatile SingularAttribute<PaymentEntity, Integer> termOfPayment;

}