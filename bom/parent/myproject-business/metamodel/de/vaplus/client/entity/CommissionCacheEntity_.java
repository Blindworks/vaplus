package de.vaplus.client.entity;

import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(CommissionCacheEntity.class)
public class CommissionCacheEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<CommissionCacheEntity, Integer> newContractSum;
    public static volatile SingularAttribute<CommissionCacheEntity, Integer> month;
    public static volatile SingularAttribute<CommissionCacheEntity, Integer> year;
    public static volatile SingularAttribute<CommissionCacheEntity, BigDecimal> pointGoal;
    public static volatile SingularAttribute<CommissionCacheEntity, Integer> extensionOfTheTermSum;
    public static volatile SingularAttribute<CommissionCacheEntity, CommissionableEmbeddable> commission;

}