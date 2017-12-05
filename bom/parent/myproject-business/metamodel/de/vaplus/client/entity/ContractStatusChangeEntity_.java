package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseContractEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(ContractStatusChangeEntity.class)
public class ContractStatusChangeEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<ContractStatusChangeEntity, String> newStatus;
    public static volatile SingularAttribute<ContractStatusChangeEntity, String> oldStatus;
    public static volatile SingularAttribute<ContractStatusChangeEntity, BaseContractEntity> contract;

}