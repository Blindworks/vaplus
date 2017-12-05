package de.vaplus.client.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(EventTypeEntity.class)
public class EventTypeEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<EventTypeEntity, Boolean> allDay;
    public static volatile SingularAttribute<EventTypeEntity, String> preferedEventEndTime;
    public static volatile SingularAttribute<EventTypeEntity, Boolean> multiUserEvent;
    public static volatile SingularAttribute<EventTypeEntity, String> preferedEventStartTime;
    public static volatile SingularAttribute<EventTypeEntity, Boolean> editable;
    public static volatile SingularAttribute<EventTypeEntity, Integer> marginTime;
    public static volatile SingularAttribute<EventTypeEntity, Boolean> externalEvent;
    public static volatile SingularAttribute<EventTypeEntity, Boolean> workingTime;
    public static volatile SingularAttribute<EventTypeEntity, String> title;
    public static volatile SingularAttribute<EventTypeEntity, String> shortName;
    public static volatile SingularAttribute<EventTypeEntity, String> uuid;

}