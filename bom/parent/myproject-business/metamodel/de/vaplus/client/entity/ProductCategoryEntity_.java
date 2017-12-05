package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductCategoryEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(ProductCategoryEntity.class)
public class ProductCategoryEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<ProductCategoryEntity, String> color;
    public static volatile SingularAttribute<ProductCategoryEntity, Boolean> showInOverview;
    public static volatile SingularAttribute<ProductCategoryEntity, String> name;
    public static volatile ListAttribute<ProductCategoryEntity, ProductCategoryEntity> childCategoryList;
    public static volatile SingularAttribute<ProductCategoryEntity, ProductCategoryEntity> parentCategory;

}