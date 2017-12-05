package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ProductOptionEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:14:52")
@StaticMetamodel(BaseProductCombinationEntity.class)
public class BaseProductCombinationEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<BaseProductCombinationEntity, BaseProductEntity> product;
    public static volatile ListAttribute<BaseProductCombinationEntity, ProductOptionEntity> productOptionList;

}