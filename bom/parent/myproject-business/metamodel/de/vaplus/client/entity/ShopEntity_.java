package de.vaplus.client.entity;

import de.vaplus.client.entity.CommissionCacheEntity;
import de.vaplus.client.entity.DBFileEntity;
import de.vaplus.client.entity.PaymentAccountEntity;
import de.vaplus.client.entity.ProductCategorySalesCacheEntity;
import de.vaplus.client.entity.ShopAliasEntity;
import de.vaplus.client.entity.StateEntity;
import de.vaplus.client.entity.StockEntity;
import de.vaplus.client.entity.VOEntity;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:11:47")
@StaticMetamodel(ShopEntity.class)
public class ShopEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<ShopEntity, String> businessHours_end_0;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_end_1;
    public static volatile ListAttribute<ShopEntity, CommissionCacheEntity> commissionCacheList;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_end_2;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_end_3;
    public static volatile SingularAttribute<ShopEntity, String> color;
    public static volatile ListAttribute<ShopEntity, VOEntity> shopVOList;
    public static volatile SingularAttribute<ShopEntity, String> contactPerson;
    public static volatile SingularAttribute<ShopEntity, String> uuid;
    public static volatile SingularAttribute<ShopEntity, PaymentAccountEntity> settlingAccount;
    public static volatile SingularAttribute<ShopEntity, CommissionCacheEntity> liveCommissionCache;
    public static volatile ListAttribute<ShopEntity, ShopAliasEntity> aliasList;
    public static volatile SingularAttribute<ShopEntity, String> crosscanData_authID;
    public static volatile SingularAttribute<ShopEntity, String> tel;
    public static volatile SingularAttribute<ShopEntity, StateEntity> state;
    public static volatile SingularAttribute<ShopEntity, String> fax;
    public static volatile SingularAttribute<ShopEntity, StockEntity> stock;
    public static volatile SingularAttribute<ShopEntity, String> email;
    public static volatile SingularAttribute<ShopEntity, String> crosscanData_storeID;
    public static volatile ListAttribute<ShopEntity, VOEntity> shopVOPermissionList;
    public static volatile SingularAttribute<ShopEntity, AddressEmbeddable> address;
    public static volatile SingularAttribute<ShopEntity, String> drafterLine;
    public static volatile SingularAttribute<ShopEntity, DBFileEntity> shopImage;
    public static volatile SingularAttribute<ShopEntity, String> calendarNote;
    public static volatile ListAttribute<ShopEntity, ProductCategorySalesCacheEntity> productCategorySalesCacheList;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_start_1;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_start_2;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_start_3;
    public static volatile SingularAttribute<ShopEntity, String> docFooterLine2;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_start_4;
    public static volatile SingularAttribute<ShopEntity, String> docFooterLine1;
    public static volatile SingularAttribute<ShopEntity, String> docFooterLine3;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_start_0;
    public static volatile SingularAttribute<ShopEntity, String> name;
    public static volatile SingularAttribute<ShopEntity, BigDecimal> pointGoal;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_start_5;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_end_4;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_start_6;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_end_5;
    public static volatile SingularAttribute<ShopEntity, String> businessHours_end_6;

}