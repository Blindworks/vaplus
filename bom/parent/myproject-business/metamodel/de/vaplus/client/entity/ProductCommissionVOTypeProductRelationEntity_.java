package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ProductCommissionVOTypeEntity;
import de.vaplus.client.entity.ProductOptionEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:14:52")
@StaticMetamodel(ProductCommissionVOTypeProductRelationEntity.class)
public class ProductCommissionVOTypeProductRelationEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<ProductCommissionVOTypeProductRelationEntity, ProductCommissionVOTypeEntity> productCommissionVOType;
    public static volatile SingularAttribute<ProductCommissionVOTypeProductRelationEntity, BaseProductEntity> product;
    public static volatile SingularAttribute<ProductCommissionVOTypeProductRelationEntity, BigDecimal> subsidyBudgetary;
    public static volatile SingularAttribute<ProductCommissionVOTypeProductRelationEntity, CommissionableEmbeddable> commission;
    public static volatile ListAttribute<ProductCommissionVOTypeProductRelationEntity, ProductOptionEntity> productOptionList;

}