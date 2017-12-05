package de.vaplus.client.entity;

import de.vaplus.client.entity.CommissionCacheEntity;
import de.vaplus.client.entity.DBFileEntity;
import de.vaplus.client.entity.EmploymentFormEntity;
import de.vaplus.client.entity.JobTitleEntity;
import de.vaplus.client.entity.ProductCategorySalesCacheEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.StateEntity;
import de.vaplus.client.entity.UserAliasEntity;
import de.vaplus.client.entity.UserCustomerHistoryEntity;
import de.vaplus.client.entity.UserGroupEntity;
import de.vaplus.client.entity.UserPermissionEntity;
import de.vaplus.client.entity.UserStatsEntity;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.picketlink.idm.jpa.model.sample.simple.AccountTypeEntity;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T16:54:18")
@StaticMetamodel(UserEntity.class)
public class UserEntity_ extends StatusBaseEntity_ {

    public static volatile ListAttribute<UserEntity, CommissionCacheEntity> commissionCacheList;
    public static volatile SingularAttribute<UserEntity, String> firstname;
    public static volatile SingularAttribute<UserEntity, String> color;
    public static volatile SingularAttribute<UserEntity, BigDecimal> bonusSalary;
    public static volatile SingularAttribute<UserEntity, JobTitleEntity> jobTitle;
    public static volatile ListAttribute<UserEntity, UserPermissionEntity> permissionList;
    public static volatile SingularAttribute<UserEntity, String> title;
    public static volatile SingularAttribute<UserEntity, String> uuid;
    public static volatile SingularAttribute<UserEntity, BigDecimal> pointsPerCommission;
    public static volatile SingularAttribute<UserEntity, DBFileEntity> userImage;
    public static volatile SingularAttribute<UserEntity, Date> bday;
    public static volatile SingularAttribute<UserEntity, CommissionCacheEntity> liveCommissionCache;
    public static volatile ListAttribute<UserEntity, UserAliasEntity> aliasList;
    public static volatile SingularAttribute<UserEntity, Integer> planingStartYear;
    public static volatile SingularAttribute<UserEntity, Integer> planingStartMonth;
    public static volatile SingularAttribute<UserEntity, String> tel;
    public static volatile SingularAttribute<UserEntity, Integer> distanceToWorkplace;
    public static volatile SingularAttribute<UserEntity, StateEntity> state;
    public static volatile SingularAttribute<UserEntity, UserGroupEntity> userGroup;
    public static volatile SingularAttribute<UserEntity, String> email;
    public static volatile SingularAttribute<UserEntity, EmploymentFormEntity> formOfEmployment;
    public static volatile SingularAttribute<UserEntity, BigDecimal> minimalBonusPointGoal;
    public static volatile SingularAttribute<UserEntity, AddressEmbeddable> address;
    public static volatile ListAttribute<UserEntity, UserCustomerHistoryEntity> customerHistoryList;
    public static volatile SingularAttribute<UserEntity, AccountTypeEntity> accountTypeEntity;
    public static volatile SingularAttribute<UserEntity, String> stuffNumber;
    public static volatile SingularAttribute<UserEntity, Boolean> homeOffice;
    public static volatile ListAttribute<UserEntity, ProductCategorySalesCacheEntity> productCategorySalesCacheList;
    public static volatile ListAttribute<UserEntity, ShopEntity> allowedShops;
    public static volatile SingularAttribute<UserEntity, String> lastname;
    public static volatile ListAttribute<UserEntity, UserStatsEntity> statsList;
    public static volatile SingularAttribute<UserEntity, BigDecimal> basicSalary;
    public static volatile SingularAttribute<UserEntity, Integer> failedLogins;
    public static volatile SingularAttribute<UserEntity, BigDecimal> pointGoal;
    public static volatile SingularAttribute<UserEntity, Boolean> privateCarUsage;
    public static volatile SingularAttribute<UserEntity, Integer> vacationDays;
    public static volatile SingularAttribute<UserEntity, Integer> weeklyWorkingDays;
    public static volatile SingularAttribute<UserEntity, BigDecimal> carGrossCatalogPrice;
    public static volatile SingularAttribute<UserEntity, Integer> weeklyWorkingTime;
    public static volatile SingularAttribute<UserEntity, Boolean> supervisor;
    public static volatile SingularAttribute<UserEntity, Date> entranceDate;

}