package de.vaplus.client.entity;

import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.TaxRateEntity;
import de.vaplus.client.entity.VOTypeEntity;
import de.vaplus.client.entity.VendorEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:14:52")
@StaticMetamodel(BaseProductEntity.class)
public abstract class BaseProductEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<BaseProductEntity, TaxRateEntity> taxRate;
    public static volatile SingularAttribute<BaseProductEntity, VendorEntity> vendor;
    public static volatile SingularAttribute<BaseProductEntity, String> name;
    public static volatile SingularAttribute<BaseProductEntity, CommissionableEmbeddable> commission;
    public static volatile ListAttribute<BaseProductEntity, VOTypeEntity> voTypePermissionList;
    public static volatile SingularAttribute<BaseProductEntity, ProductCategoryEntity> productCategory;

}