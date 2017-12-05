package de.vaplus.client.entity;

import de.vaplus.client.entity.AchievementEntity;
import de.vaplus.client.entity.UserEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-03T11:42:56")
@StaticMetamodel(AchievementUserGoalAttainmentEntity.class)
public class AchievementUserGoalAttainmentEntity_ { 

    public static volatile SingularAttribute<AchievementUserGoalAttainmentEntity, BigDecimal> contractedRevenueGoalAttainment;
    public static volatile SingularAttribute<AchievementUserGoalAttainmentEntity, BigDecimal> pieceGoalAttainment;
    public static volatile SingularAttribute<AchievementUserGoalAttainmentEntity, AchievementEntity> achievement;
    public static volatile SingularAttribute<AchievementUserGoalAttainmentEntity, BigDecimal> aquiredRevenueGoalAttainment;
    public static volatile SingularAttribute<AchievementUserGoalAttainmentEntity, UserEntity> user;
    public static volatile SingularAttribute<AchievementUserGoalAttainmentEntity, BigDecimal> sumGoalAttainment;

}