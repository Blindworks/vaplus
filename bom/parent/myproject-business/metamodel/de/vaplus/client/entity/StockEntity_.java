package de.vaplus.client.entity;

import de.vaplus.client.entity.embeddable.AddressEmbeddable;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T15:11:47")
@StaticMetamodel(StockEntity.class)
public class StockEntity_ extends StatusBaseEntity_ {

    public static volatile SingularAttribute<StockEntity, AddressEmbeddable> address;
    public static volatile SingularAttribute<StockEntity, String> name;

}