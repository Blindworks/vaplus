package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductCategoryEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:14:52")
@StaticMetamodel(ProductOptionEntity.class)
public class ProductOptionEntity_ extends BaseProductEntity_ {

    public static volatile SingularAttribute<ProductOptionEntity, Boolean> forceExtensionOfTheTerm;
    public static volatile SingularAttribute<ProductOptionEntity, Integer> weight;
    public static volatile SingularAttribute<ProductOptionEntity, ProductCategoryEntity> targetProductCategory;
    public static volatile SingularAttribute<ProductOptionEntity, BigDecimal> generatedRevenue;
    public static volatile SingularAttribute<ProductOptionEntity, Boolean> revenueCommissionRelevant;
    public static volatile SingularAttribute<ProductOptionEntity, Boolean> percentagePrice;

}