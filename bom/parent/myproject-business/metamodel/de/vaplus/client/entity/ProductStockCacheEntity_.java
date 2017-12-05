package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductEntity;
import de.vaplus.client.entity.StockEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:13:22")
@StaticMetamodel(ProductStockCacheEntity.class)
public class ProductStockCacheEntity_ { 

    public static volatile SingularAttribute<ProductStockCacheEntity, ProductEntity> product;
    public static volatile SingularAttribute<ProductStockCacheEntity, BigDecimal> quantity;
    public static volatile SingularAttribute<ProductStockCacheEntity, StockEntity> stock;

}