package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.VendorEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:14:52")
@StaticMetamodel(ContractItemEntity.class)
public class ContractItemEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<ContractItemEntity, BaseProductEntity> baseProduct;
    public static volatile SingularAttribute<ContractItemEntity, VendorEntity> vendor;
    public static volatile SingularAttribute<ContractItemEntity, CommissionableEmbeddable> voCommission;
    public static volatile SingularAttribute<ContractItemEntity, String> productName;
    public static volatile SingularAttribute<ContractItemEntity, ProductCategoryEntity> productCategory;
    public static volatile SingularAttribute<ContractItemEntity, CommissionableEmbeddable> productCommission;

}