package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductStatisticEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.VOEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(ProductStatisticCacheEntity.class)
public class ProductStatisticCacheEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<ProductStatisticCacheEntity, Integer> pieces;
    public static volatile SingularAttribute<ProductStatisticCacheEntity, ShopEntity> shop;
    public static volatile SingularAttribute<ProductStatisticCacheEntity, Integer> month;
    public static volatile SingularAttribute<ProductStatisticCacheEntity, Integer> year;
    public static volatile SingularAttribute<ProductStatisticCacheEntity, VOEntity> vo;
    public static volatile SingularAttribute<ProductStatisticCacheEntity, ProductStatisticEntity> productStatistic;
    public static volatile SingularAttribute<ProductStatisticCacheEntity, UserEntity> user;

}