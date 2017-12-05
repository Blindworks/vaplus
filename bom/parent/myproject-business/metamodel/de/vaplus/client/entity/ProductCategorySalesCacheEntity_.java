package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.VOEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T16:35:33")
@StaticMetamodel(ProductCategorySalesCacheEntity.class)
public class ProductCategorySalesCacheEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<ProductCategorySalesCacheEntity, BigDecimal> pieces;
    public static volatile SingularAttribute<ProductCategorySalesCacheEntity, ShopEntity> shop;
    public static volatile SingularAttribute<ProductCategorySalesCacheEntity, Integer> month;
    public static volatile SingularAttribute<ProductCategorySalesCacheEntity, Integer> year;
    public static volatile SingularAttribute<ProductCategorySalesCacheEntity, VOEntity> vo;
    public static volatile SingularAttribute<ProductCategorySalesCacheEntity, UserEntity> user;
    public static volatile SingularAttribute<ProductCategorySalesCacheEntity, ProductCategoryEntity> productCategory;

}