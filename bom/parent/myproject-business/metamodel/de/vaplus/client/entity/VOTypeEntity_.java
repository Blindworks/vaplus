package de.vaplus.client.entity;

import de.vaplus.client.entity.VendorEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(VOTypeEntity.class)
public abstract class VOTypeEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<VOTypeEntity, BigDecimal> pointsPerCommission;
    public static volatile SingularAttribute<VOTypeEntity, VendorEntity> vendor;
    public static volatile SingularAttribute<VOTypeEntity, String> name;
    public static volatile SingularAttribute<VOTypeEntity, String> shortName;

}