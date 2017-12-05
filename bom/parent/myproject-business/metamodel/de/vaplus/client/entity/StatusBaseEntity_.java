package de.vaplus.client.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(StatusBaseEntity.class)
public abstract class StatusBaseEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<StatusBaseEntity, Date> expiryDate;
    public static volatile SingularAttribute<StatusBaseEntity, Date> effectiveDate;
    public static volatile SingularAttribute<StatusBaseEntity, Boolean> enabled;

}