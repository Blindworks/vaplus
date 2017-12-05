package de.vaplus.client.querymapping;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:35")
@StaticMetamodel(SingleProductStockLevelValue.class)
public class SingleProductStockLevelValue_ { 

    public static volatile SingularAttribute<SingleProductStockLevelValue, String> stockName;
    public static volatile SingularAttribute<SingleProductStockLevelValue, String> serial;
    public static volatile SingularAttribute<SingleProductStockLevelValue, Integer> id;
    public static volatile SingularAttribute<SingleProductStockLevelValue, BigDecimal> purchasePrice;
    public static volatile SingularAttribute<SingleProductStockLevelValue, Long> stock_id;

}