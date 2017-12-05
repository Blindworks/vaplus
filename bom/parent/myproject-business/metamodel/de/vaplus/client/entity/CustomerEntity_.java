package de.vaplus.client.entity;

import de.vaplus.client.entity.CountryEntity;
import de.vaplus.client.entity.ExternalCustomerEntity;
import de.vaplus.client.entity.NoteEntity;
import de.vaplus.client.entity.PaymentAccountEntity;
import de.vaplus.client.entity.ShopEntity;
import de.vaplus.client.entity.UserEntity;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2017-10-02T18:44:47")
@StaticMetamodel(CustomerEntity.class)
public class CustomerEntity_ extends BaseEntity_ {

    public static volatile SingularAttribute<CustomerEntity, NoteEntity> note;
    public static volatile SingularAttribute<CustomerEntity, String> firstname;
    public static volatile SingularAttribute<CustomerEntity, AddressEmbeddable> address;
    public static volatile SingularAttribute<CustomerEntity, ShopEntity> shop;
    public static volatile SingularAttribute<CustomerEntity, String> companyName;
    public static volatile SingularAttribute<CustomerEntity, String> contactPerson;
    public static volatile ListAttribute<CustomerEntity, NoteEntity> noteList;
    public static volatile SingularAttribute<CustomerEntity, String> title;
    public static volatile SingularAttribute<CustomerEntity, String> lastname;
    public static volatile ListAttribute<CustomerEntity, ExternalCustomerEntity> externalCustomerList;
    public static volatile SingularAttribute<CustomerEntity, UserEntity> accountManager;
    public static volatile SingularAttribute<CustomerEntity, Date> bday;
    public static volatile SingularAttribute<CustomerEntity, CountryEntity> nationality;
    public static volatile SingularAttribute<CustomerEntity, Boolean> company;
    public static volatile SingularAttribute<CustomerEntity, String> tel;
    public static volatile SingularAttribute<CustomerEntity, String> commercialRegisterId;
    public static volatile SingularAttribute<CustomerEntity, String> fax;
    public static volatile SingularAttribute<CustomerEntity, PaymentAccountEntity> paymentAccount;
    public static volatile SingularAttribute<CustomerEntity, String> email;

}