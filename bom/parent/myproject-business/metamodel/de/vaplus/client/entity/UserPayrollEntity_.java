package de.vaplus.client.entity;

import de.vaplus.client.entity.EmploymentFormEntity;
import de.vaplus.client.entity.UserEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(UserPayrollEntity.class)
public class UserPayrollEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<UserPayrollEntity, String> note;
    public static volatile SingularAttribute<UserPayrollEntity, Integer> year;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> bonusSalary;
    public static volatile SingularAttribute<UserPayrollEntity, Boolean> homeOffice;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> yieldedCommission;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> points;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> pointsPerCommission;
    public static volatile SingularAttribute<UserPayrollEntity, Integer> month;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> basicSalary;
    public static volatile SingularAttribute<UserPayrollEntity, Boolean> privateCarUsage;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> pointGoal;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> commission;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> carGrossCatalogPrice;
    public static volatile SingularAttribute<UserPayrollEntity, Integer> distanceToWorkplace;
    public static volatile SingularAttribute<UserPayrollEntity, Integer> weeklyWorkingDays;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> travelExpenses;
    public static volatile SingularAttribute<UserPayrollEntity, UserEntity> user;
    public static volatile SingularAttribute<UserPayrollEntity, EmploymentFormEntity> formOfEmployment;
    public static volatile SingularAttribute<UserPayrollEntity, Integer> weeklyWorkingTime;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> hoursWorked;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> minimalBonusPointGoal;
    public static volatile SingularAttribute<UserPayrollEntity, BigDecimal> expenses;

}