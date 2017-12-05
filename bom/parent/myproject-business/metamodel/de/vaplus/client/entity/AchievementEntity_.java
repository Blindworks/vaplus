package de.vaplus.client.entity;

import de.vaplus.client.entity.AchievementTargetEntity;
import de.vaplus.client.entity.AchievementUserGoalAttainmentEntity;
import de.vaplus.client.entity.BaseProductCombinationEntity;
import de.vaplus.client.entity.BaseProductEntity;
import de.vaplus.client.entity.ProductCategoryEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.ShopGroupEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.VOEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-03T11:27:19")
@StaticMetamodel(AchievementEntity.class)
public class AchievementEntity_ extends StatusBaseEntity_ {

    public static volatile ListAttribute<AchievementEntity, AchievementTargetEntity> sumTargetList;
    public static volatile SingularAttribute<AchievementEntity, Boolean> showOnCompanyDashboard;
    public static volatile SingularAttribute<AchievementEntity, Boolean> extensionOfTheTerm;
    public static volatile ListAttribute<AchievementEntity, AchievementTargetEntity> aquiredRevenueTargetList;
    public static volatile SingularAttribute<AchievementEntity, Boolean> debidCreditChange;
    public static volatile ListAttribute<AchievementEntity, AchievementTargetEntity> contractedRevenueTargetList;
    public static volatile SingularAttribute<AchievementEntity, Boolean> invisible;
    public static volatile SingularAttribute<AchievementEntity, AchievementTargetEntity> totalContractedRevenueTarget;
    public static volatile ListAttribute<AchievementEntity, ShopEntity> sourceShopList;
    public static volatile SingularAttribute<AchievementEntity, AchievementTargetEntity> totalPieceTarget;
    public static volatile SingularAttribute<AchievementEntity, Boolean> newContract;
    public static volatile ListAttribute<AchievementEntity, UserEntity> sourceUserList;
    public static volatile SingularAttribute<AchievementEntity, BigDecimal> contractedRevenueGoalAttainment;
    public static volatile ListAttribute<AchievementEntity, BaseProductEntity> selectedProductFilterList;
    public static volatile ListAttribute<AchievementEntity, UserEntity> payoutUserList;
    public static volatile ListAttribute<AchievementEntity, BaseProductCombinationEntity> selectedProductCombinationList;
    public static volatile SingularAttribute<AchievementEntity, BigDecimal> sumGoalAttainment;
    public static volatile ListAttribute<AchievementEntity, ProductCategoryEntity> selectedProductCategoryList;
    public static volatile SingularAttribute<AchievementEntity, AchievementTargetEntity> totalAquiredRevenueTarget;
    public static volatile SingularAttribute<AchievementEntity, Integer> weight;
    public static volatile ListAttribute<AchievementEntity, BaseProductEntity> selectedProductList;
    public static volatile SingularAttribute<AchievementEntity, AchievementTargetEntity> totalSumTarget;
    public static volatile ListAttribute<AchievementEntity, AchievementUserGoalAttainmentEntity> userGoalAttainmentList;
    public static volatile SingularAttribute<AchievementEntity, Boolean> payoutToSource;
    public static volatile SingularAttribute<AchievementEntity, BigDecimal> pieceGoalAttainment;
    public static volatile ListAttribute<AchievementEntity, BaseProductCombinationEntity> selectedProductCombinationFilterList;
    public static volatile ListAttribute<AchievementEntity, ShopGroupEntity> sourceShopGroupList;
    public static volatile SingularAttribute<AchievementEntity, String> name;
    public static volatile ListAttribute<AchievementEntity, AchievementTargetEntity> pieceTargetList;
    public static volatile SingularAttribute<AchievementEntity, BigDecimal> aquiredRevenueGoalAttainment;
    public static volatile ListAttribute<AchievementEntity, VOEntity> sourceVOList;

}