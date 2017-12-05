package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductStockCacheEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:14:52")
@StaticMetamodel(ProductEntity.class)
public class ProductEntity_ extends BaseProductEntity_ {

    public static volatile SingularAttribute<ProductEntity, String> ean;
    public static volatile SingularAttribute<ProductEntity, Boolean> bookableAsForeignWare;
    public static volatile SingularAttribute<ProductEntity, BigDecimal> avergePurchasePrice;
    public static volatile SingularAttribute<ProductEntity, BigDecimal> stockQuantity;
    public static volatile SingularAttribute<ProductEntity, String> description;
    public static volatile SingularAttribute<ProductEntity, Boolean> serialRequired;
    public static volatile ListAttribute<ProductEntity, ProductStockCacheEntity> ProductStockCacheList;
    public static volatile SingularAttribute<ProductEntity, BigDecimal> purchasePrice;
    public static volatile SingularAttribute<ProductEntity, BigDecimal> maxPurchasePrice;
    public static volatile SingularAttribute<ProductEntity, Boolean> stockControlled;
    public static volatile SingularAttribute<ProductEntity, BigDecimal> minPurchasePrice;

}