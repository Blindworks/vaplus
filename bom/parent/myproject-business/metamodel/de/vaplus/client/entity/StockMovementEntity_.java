package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductEntity;
import de.vaplus.client.entity.StockPickslipEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(StockMovementEntity.class)
public class StockMovementEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<StockMovementEntity, ProductEntity> product;
    public static volatile SingularAttribute<StockMovementEntity, BigDecimal> quantity;
    public static volatile SingularAttribute<StockMovementEntity, String> serial;
    public static volatile SingularAttribute<StockMovementEntity, BigDecimal> purchasePrice;
    public static volatile SingularAttribute<StockMovementEntity, StockPickslipEntity> stockPickslip;

}