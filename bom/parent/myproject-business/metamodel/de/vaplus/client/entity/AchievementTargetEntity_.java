package de.vaplus.client.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(AchievementTargetEntity.class)
public class AchievementTargetEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<AchievementTargetEntity, String> payoutText;
    public static volatile SingularAttribute<AchievementTargetEntity, BigDecimal> payout;
    public static volatile SingularAttribute<AchievementTargetEntity, BigDecimal> commission;
    public static volatile SingularAttribute<AchievementTargetEntity, BigDecimal> target;

}