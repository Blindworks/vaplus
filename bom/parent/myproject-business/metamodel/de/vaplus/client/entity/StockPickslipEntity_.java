package de.vaplus.client.entity;

import de.vaplus.client.entity.OrderEntity;
import de.vaplus.client.entity.StockEntity;
import de.vaplus.client.entity.StockMovementEntity;
import de.vaplus.client.entity.StockPickslipEntity;
import de.vaplus.client.entity.SupplierEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(StockPickslipEntity.class)
public class StockPickslipEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<StockPickslipEntity, StockEntity> dst_stock;
    public static volatile SingularAttribute<StockPickslipEntity, Boolean> completelyReImported;
    public static volatile SingularAttribute<StockPickslipEntity, String> number;
    public static volatile ListAttribute<StockPickslipEntity, StockPickslipEntity> pickslipReimportList;
    public static volatile ListAttribute<StockPickslipEntity, StockMovementEntity> stockMovementList;
    public static volatile SingularAttribute<StockPickslipEntity, StockPickslipEntity> pickslipReference;
    public static volatile SingularAttribute<StockPickslipEntity, SupplierEntity> supplier;
    public static volatile SingularAttribute<StockPickslipEntity, StockEntity> stock;
    public static volatile SingularAttribute<StockPickslipEntity, StockEntity> src_stock;
    public static volatile SingularAttribute<StockPickslipEntity, OrderEntity> order;

}