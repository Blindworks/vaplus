package de.vaplus.client.entity;

import de.vaplus.client.entity.BaseContractCancellationEntity;
import de.vaplus.client.entity.BaseContractEntity;
import de.vaplus.client.entity.ContractItemEntity;
import de.vaplus.client.entity.ContractRetailItemEntity;
import de.vaplus.client.entity.ContractStatusEntity;
import de.vaplus.client.entity.ExternalCustomerEntity;
import de.vaplus.client.entity.InvoiceEntity;
import de.vaplus.client.entity.OrderEntity;
import de.vaplus.client.entity.VOEntity;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentEntity;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(BaseContractEntity.class)
public abstract class BaseContractEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<BaseContractEntity, BaseContractCancellationEntity> contractCancellation;
    public static volatile ListAttribute<BaseContractEntity, ContractItemEntity> cachedTariffOptionList;
    public static volatile SingularAttribute<BaseContractEntity, OrderEntity> subsidyOrder;
    public static volatile SingularAttribute<BaseContractEntity, String> orderNumber;
    public static volatile SingularAttribute<BaseContractEntity, Boolean> debidCreditChange;
    public static volatile SingularAttribute<BaseContractEntity, CommissionableEmbeddable> revenueStepCommission;
    public static volatile SingularAttribute<BaseContractEntity, Integer> leadTime;
    public static volatile SingularAttribute<BaseContractEntity, BigDecimal> generatedRevenue;
    public static volatile SingularAttribute<BaseContractEntity, BaseContractEntity> extendedContract;
    public static volatile SingularAttribute<BaseContractEntity, Boolean> extensionOfTheTermPermission;
    public static volatile SingularAttribute<BaseContractEntity, AccountingAssignmentEntity> accountingAssignment;
    public static volatile SingularAttribute<BaseContractEntity, ExternalCustomerEntity> externalCustomer;
    public static volatile SingularAttribute<BaseContractEntity, ContractItemEntity> cachedTariff;
    public static volatile SingularAttribute<BaseContractEntity, AccountingAssignmentEntity> cancelationAccountingAssignment;
    public static volatile SingularAttribute<BaseContractEntity, Boolean> extensionOfTerm;
    public static volatile SingularAttribute<BaseContractEntity, BigDecimal> subsidyBudgetary;
    public static volatile SingularAttribute<BaseContractEntity, VOEntity> vo;
    public static volatile SingularAttribute<BaseContractEntity, InvoiceEntity> subsidyInvoice;
    public static volatile SingularAttribute<BaseContractEntity, CommissionableEmbeddable> finalCommission;
    public static volatile ListAttribute<BaseContractEntity, ContractRetailItemEntity> contractRetailItemList;
    public static volatile SingularAttribute<BaseContractEntity, String> info;
    public static volatile SingularAttribute<BaseContractEntity, ContractStatusEntity> status;

}