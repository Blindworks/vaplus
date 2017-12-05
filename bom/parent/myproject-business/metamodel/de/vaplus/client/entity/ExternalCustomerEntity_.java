package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.CustomerEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:12")
@StaticMetamodel(ExternalCustomerEntity.class)
public class ExternalCustomerEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<ExternalCustomerEntity, String> password;
    public static volatile ListAttribute<ExternalCustomerEntity, BaseContractEntity> contractList;
    public static volatile SingularAttribute<ExternalCustomerEntity, String> customerId;
    public static volatile SingularAttribute<ExternalCustomerEntity, CustomerEntity> customer;

}