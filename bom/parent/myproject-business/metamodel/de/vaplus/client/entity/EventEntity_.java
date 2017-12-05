package de.vaplus.client.entity;

import de.vaplus.client.entity.EventTypeEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(EventEntity.class)
public class EventEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<EventEntity, Boolean> allDay;
    public static volatile SingularAttribute<EventEntity, Integer> pauseLength;
    public static volatile SingularAttribute<EventEntity, Boolean> workingTime;
    public static volatile SingularAttribute<EventEntity, EventTypeEntity> eventType;
    public static volatile SingularAttribute<EventEntity, String> title;
    public static volatile SingularAttribute<EventEntity, String> uuid;

}