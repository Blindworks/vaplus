package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.ProductEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(ContractRetailItemEntity.class)
public class ContractRetailItemEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<ContractRetailItemEntity, ProductEntity> product;
    public static volatile SingularAttribute<ContractRetailItemEntity, String> serial;
    public static volatile SingularAttribute<ContractRetailItemEntity, BaseContractEntity> baseContract;

}