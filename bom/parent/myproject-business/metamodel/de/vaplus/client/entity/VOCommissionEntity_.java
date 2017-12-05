package de.vaplus.client.entity;

import de.vaplus.client.entity.VOEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(VOCommissionEntity.class)
public class VOCommissionEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<VOCommissionEntity, BigDecimal> repairs;
    public static volatile SingularAttribute<VOCommissionEntity, Integer> month;
    public static volatile SingularAttribute<VOCommissionEntity, Integer> year;
    public static volatile SingularAttribute<VOCommissionEntity, BigDecimal> baseAirtime;
    public static volatile SingularAttribute<VOCommissionEntity, VOEntity> vo;
    public static volatile SingularAttribute<VOCommissionEntity, BigDecimal> servicePackages;
    public static volatile SingularAttribute<VOCommissionEntity, BigDecimal> bonusAirtime;

}