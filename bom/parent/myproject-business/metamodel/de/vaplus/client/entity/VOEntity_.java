package de.vaplus.client.entity;

import de.vaplus.client.entity.CommissionCacheEntity;
import de.vaplus.client.entity.ProductCategorySalesCacheEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.VOCommissionEntity;
import de.vaplus.client.entity.VOTypeEntity;
import de.vaplus.client.entity.VendorEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:11:47")
@StaticMetamodel(VOEntity.class)
public class VOEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<VOEntity, String> number;
    public static volatile ListAttribute<VOEntity, CommissionCacheEntity> commissionCacheList;
    public static volatile SingularAttribute<VOEntity, VOTypeEntity> voType;
    public static volatile SingularAttribute<VOEntity, ShopEntity> shop;
    public static volatile SingularAttribute<VOEntity, CommissionCacheEntity> liveCommissionCache;
    public static volatile SingularAttribute<VOEntity, VendorEntity> vendor;
    public static volatile SingularAttribute<VOEntity, String> name;
    public static volatile ListAttribute<VOEntity, ProductCategorySalesCacheEntity> productCategorySalesCacheList;
    public static volatile ListAttribute<VOEntity, ShopEntity> permittedShopShopList;
    public static volatile ListAttribute<VOEntity, VOCommissionEntity> voCommissionList;

}