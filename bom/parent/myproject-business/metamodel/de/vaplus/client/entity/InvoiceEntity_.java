package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.FileEntity;
import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.InvoiceItemEntity;
import de.vaplus.client.entity.PaymentAccountEntity;
import de.vaplus.client.entity.PaymentEntity;
import de.vaplus.client.entity.StockPickslipEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(InvoiceEntity.class)
public class InvoiceEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<InvoiceEntity, Date> dueDate;
    public static volatile SingularAttribute<InvoiceEntity, StockPickslipEntity> stockPickslip;
    public static volatile ListAttribute<InvoiceEntity, InvoiceItemEntity> invoiceItemList;
    public static volatile SingularAttribute<InvoiceEntity, InvoiceEntity> cancelationInvoice;
    public static volatile SingularAttribute<InvoiceEntity, String> number;
    public static volatile SingularAttribute<InvoiceEntity, FileEntity> invoiceFile;
    public static volatile SingularAttribute<InvoiceEntity, InvoiceEntity> canceledinvoice;
    public static volatile SingularAttribute<InvoiceEntity, String> closing;
    public static volatile SingularAttribute<InvoiceEntity, BaseContractEntity> subsidyContract;
    public static volatile SingularAttribute<InvoiceEntity, String> recipientLine5;
    public static volatile SingularAttribute<InvoiceEntity, String> recipientLine4;
    public static volatile SingularAttribute<InvoiceEntity, String> recipientLine3;
    public static volatile SingularAttribute<InvoiceEntity, String> recipientLine2;
    public static volatile SingularAttribute<InvoiceEntity, CommissionableEmbeddable> commission;
    public static volatile SingularAttribute<InvoiceEntity, PaymentEntity> payment;
    public static volatile SingularAttribute<InvoiceEntity, String> recipientLine1;
    public static volatile SingularAttribute<InvoiceEntity, String> paymentInformation;
    public static volatile SingularAttribute<InvoiceEntity, PaymentAccountEntity> customerAccount;
    public static volatile SingularAttribute<InvoiceEntity, String> introduction;

}