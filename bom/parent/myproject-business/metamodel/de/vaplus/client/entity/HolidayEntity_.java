package de.vaplus.client.entity;

import de.vaplus.client.entity.StateEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(HolidayEntity.class)
public class HolidayEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<HolidayEntity, String> name;
    public static volatile SingularAttribute<HolidayEntity, StateEntity> state;
    public static volatile SingularAttribute<HolidayEntity, String> day;

}