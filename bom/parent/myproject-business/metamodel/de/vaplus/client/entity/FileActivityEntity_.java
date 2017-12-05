package de.vaplus.client.entity;

import de.vaplus.client.entity.FileEntity;
import de.vaplus.client.entity.UserEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(FileActivityEntity.class)
public class FileActivityEntity_ extends ActivityEntity_ {

    public static volatile SingularAttribute<FileActivityEntity, UserEntity> creator;
    public static volatile SingularAttribute<FileActivityEntity, FileEntity> file;
    public static volatile SingularAttribute<FileActivityEntity, Boolean> invisible;
    public static volatile SingularAttribute<FileActivityEntity, String> relationClass;
    public static volatile SingularAttribute<FileActivityEntity, Long> relation;

}