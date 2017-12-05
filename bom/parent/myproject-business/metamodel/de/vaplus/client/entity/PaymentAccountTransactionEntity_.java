package de.vaplus.client.entity;

import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.PaymentAccountEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(PaymentAccountTransactionEntity.class)
public class PaymentAccountTransactionEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<PaymentAccountTransactionEntity, String> note;
    public static volatile SingularAttribute<PaymentAccountTransactionEntity, BigDecimal> amount;
    public static volatile SingularAttribute<PaymentAccountTransactionEntity, PaymentAccountEntity> sourceAccount;
    public static volatile SingularAttribute<PaymentAccountTransactionEntity, InvoiceEntity> invoiceReference;
    public static volatile SingularAttribute<PaymentAccountTransactionEntity, PaymentAccountEntity> destinationAccount;

}