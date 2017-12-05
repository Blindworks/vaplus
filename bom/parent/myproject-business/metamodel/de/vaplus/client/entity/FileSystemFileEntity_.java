package de.vaplus.client.entity;

import de.vaplus.client.entity.DBFileEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T12:00:34")
@StaticMetamodel(FileSystemFileEntity.class)
public class FileSystemFileEntity_ extends FileEntity_ {

    public static volatile SingularAttribute<FileSystemFileEntity, DBFileEntity> thumbnail;
    public static volatile SingularAttribute<FileSystemFileEntity, String> filename;
    public static volatile SingularAttribute<FileSystemFileEntity, Long> size;
    public static volatile SingularAttribute<FileSystemFileEntity, String> relativeFilePath;

}