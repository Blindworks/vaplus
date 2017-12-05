package org.picketlink.idm.jpa.model.sample.simple;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T09:10:31.848+0100")
@StaticMetamodel(RelationshipIdentityTypeEntity.class)
public class RelationshipIdentityTypeEntity_ {
	public static volatile SingularAttribute<RelationshipIdentityTypeEntity, Long> identifier;
	public static volatile SingularAttribute<RelationshipIdentityTypeEntity, String> descriptor;
	public static volatile SingularAttribute<RelationshipIdentityTypeEntity, IdentityTypeEntity> identityType;
	public static volatile SingularAttribute<RelationshipIdentityTypeEntity, RelationshipTypeEntity> owner;
}
