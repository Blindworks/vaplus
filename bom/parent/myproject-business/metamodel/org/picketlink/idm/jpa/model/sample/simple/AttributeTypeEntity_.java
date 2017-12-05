package org.picketlink.idm.jpa.model.sample.simple;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T09:10:31.760+0100")
@StaticMetamodel(AttributeTypeEntity.class)
public class AttributeTypeEntity_ {
	public static volatile SingularAttribute<AttributeTypeEntity, Long> id;
	public static volatile SingularAttribute<AttributeTypeEntity, AttributedTypeEntity> owner;
	public static volatile SingularAttribute<AttributeTypeEntity, String> typeName;
	public static volatile SingularAttribute<AttributeTypeEntity, String> name;
	public static volatile SingularAttribute<AttributeTypeEntity, String> value;
}
