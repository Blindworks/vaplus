package de.vaplus.client.entity;

import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.ProductEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(InvoiceItemEntity.class)
public class InvoiceItemEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<InvoiceItemEntity, BigDecimal> unitPrice;
    public static volatile SingularAttribute<InvoiceItemEntity, Boolean> manualItem;
    public static volatile SingularAttribute<InvoiceItemEntity, ProductEntity> product;
    public static volatile SingularAttribute<InvoiceItemEntity, BigDecimal> quantity;
    public static volatile SingularAttribute<InvoiceItemEntity, String> subTitle;
    public static volatile SingularAttribute<InvoiceItemEntity, BigDecimal> unitPurchasePrice;
    public static volatile SingularAttribute<InvoiceItemEntity, String> serial;
    public static volatile SingularAttribute<InvoiceItemEntity, CommissionableEmbeddable> commission;
    public static volatile SingularAttribute<InvoiceItemEntity, InvoiceEntity> invoice;
    public static volatile SingularAttribute<InvoiceItemEntity, String> title;
    public static volatile SingularAttribute<InvoiceItemEntity, Boolean> blockedItem;

}