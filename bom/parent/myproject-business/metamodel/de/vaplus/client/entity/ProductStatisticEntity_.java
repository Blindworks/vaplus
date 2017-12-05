package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseProductCombinationEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.ProductStatisticCacheEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T17:43:33")
@StaticMetamodel(ProductStatisticEntity.class)
public class ProductStatisticEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<ProductStatisticEntity, Boolean> extensionOfTheTerm;
    public static volatile SingularAttribute<ProductStatisticEntity, Boolean> showOnOverview;
    public static volatile ListAttribute<ProductStatisticEntity, BaseProductCombinationEntity> selectedProductCombinationFilterList;
    public static volatile SingularAttribute<ProductStatisticEntity, Boolean> debidCreditChange;
    public static volatile SingularAttribute<ProductStatisticEntity, String> name;
    public static volatile ListAttribute<ProductStatisticEntity, BaseProductEntity> selectedProductFilterList;
    public static volatile SingularAttribute<ProductStatisticEntity, Integer> weight;
    public static volatile ListAttribute<ProductStatisticEntity, BaseProductEntity> selectedProductList;
    public static volatile ListAttribute<ProductStatisticEntity, BaseProductCombinationEntity> selectedProductCombinationList;
    public static volatile SingularAttribute<ProductStatisticEntity, Boolean> newContract;
    public static volatile ListAttribute<ProductStatisticEntity, ProductStatisticCacheEntity> productStatisticCacheList;
    public static volatile ListAttribute<ProductStatisticEntity, ProductCategoryEntity> selectedProductCategoryList;

}