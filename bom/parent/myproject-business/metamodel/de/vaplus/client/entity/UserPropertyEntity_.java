package de.vaplus.client.entity;

import de.vaplus.client.entity.UserEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(UserPropertyEntity.class)
public class UserPropertyEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<UserPropertyEntity, String> stringValue;
    public static volatile SingularAttribute<UserPropertyEntity, String> name;
    public static volatile SingularAttribute<UserPropertyEntity, BigDecimal> decimalValue;
    public static volatile SingularAttribute<UserPropertyEntity, Double> doubleValue;
    public static volatile SingularAttribute<UserPropertyEntity, UserEntity> user;
    public static volatile SingularAttribute<UserPropertyEntity, Long> longValue;

}