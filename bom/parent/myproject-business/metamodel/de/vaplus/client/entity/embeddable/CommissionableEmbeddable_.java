package de.vaplus.client.entity.embeddable;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-15T20:40:34")
@StaticMetamodel(CommissionableEmbeddable.class)
public class CommissionableEmbeddable_ { 

    public static volatile SingularAttribute<CommissionableEmbeddable, BigDecimal> price;
    public static volatile SingularAttribute<CommissionableEmbeddable, BigDecimal> vat;
    public static volatile SingularAttribute<CommissionableEmbeddable, BigDecimal> commission;
    public static volatile SingularAttribute<CommissionableEmbeddable, BigDecimal> tax;
    public static volatile SingularAttribute<CommissionableEmbeddable, BigDecimal> points;

}