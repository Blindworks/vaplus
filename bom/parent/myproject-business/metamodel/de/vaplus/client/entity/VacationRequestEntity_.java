package de.vaplus.client.entity;

import de.vaplus.client.entity.EventEntity;
import de.vaplus.client.entity.UserEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(VacationRequestEntity.class)
public class VacationRequestEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<VacationRequestEntity, String> creatorNote;
    public static volatile SingularAttribute<VacationRequestEntity, UserEntity> manager;
    public static volatile SingularAttribute<VacationRequestEntity, Date> statusChangedDate;
    public static volatile SingularAttribute<VacationRequestEntity, Date> vacationFrom;
    public static volatile SingularAttribute<VacationRequestEntity, String> managerNote;
    public static volatile SingularAttribute<VacationRequestEntity, EventEntity> event;
    public static volatile SingularAttribute<VacationRequestEntity, UserEntity> user;
    public static volatile SingularAttribute<VacationRequestEntity, Date> vacationTo;
    public static volatile SingularAttribute<VacationRequestEntity, Integer> status;

}