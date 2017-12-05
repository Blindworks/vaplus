package de.vaplus.client.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_ { 

    public static volatile SingularAttribute<BaseEntity, Date> updateDate;
    public static volatile SingularAttribute<BaseEntity, Boolean> deleted;
    public static volatile SingularAttribute<BaseEntity, Long> id;
    public static volatile SingularAttribute<BaseEntity, Date> creationDate;

}