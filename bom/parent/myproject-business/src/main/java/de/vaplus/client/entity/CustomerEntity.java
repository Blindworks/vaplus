package de.vaplus.client.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.internal.sessions.DirectToFieldChangeRecord;

import de.vaplus.api.entity.Country;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.Note;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.embeddable.Address;
import de.vaplus.client.changetracking.TrackChangesListener;
import de.vaplus.client.eao.BaseEao;
import de.vaplus.client.entity.embeddable.AddressEmbeddable;

/**
 * Entity implementation class for Entity: Customer
 *
 */
@Entity
@Table(name="Customer")
@NamedQueries({
    @NamedQuery(
        name = "Customer.findActive",
        query = "SELECT c FROM CustomerEntity c WHERE c.deleted = false  ORDER BY c.companyName, c.lastname,c.firstname ASC"
        ,
        hints = {
                @QueryHint(name=QueryHints.QUERY_RESULTS_CACHE, value=HintValues.TRUE),
            }
    	)
})
public class CustomerEntity extends BaseEntity implements Customer, SearchResult {

	private static final long serialVersionUID = -2530428408584665498L;

	@Column(name="company", nullable = false)
	private boolean company;

	@Column(name="title", nullable = false)
	private String title;

	@Column(name="firstname")
	private String firstname;

	@Column(name="lastname")
	private String lastname;

	@Column(name="companyName")
	private String companyName;

	@Column(name="contactPerson")
	private String contactPerson;

	@Column(name="commercialRegisterId")
	private String commercialRegisterId;
	
	@Column(name="email")
	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bday")
	private Date bday;

	@Column(name="tel")
	private String tel;

	@Column(name="fax")
	private String fax;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="node_id")
    private NoteEntity note;

	@OneToMany(mappedBy="customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NoteEntity> noteList;

	@OneToMany(mappedBy="customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExternalCustomerEntity> externalCustomerList;
	
	@Embedded
	private AddressEmbeddable address;

	@ManyToOne
    @JoinColumn(name="nationality_id", nullable = true)
    private CountryEntity nationality;

	@ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="shop_id", nullable = true)
    private ShopEntity shop;

	@ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="accountManager_user_id", nullable = true)
    private UserEntity accountManager;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_id", nullable = true)
    private PaymentAccountEntity paymentAccount;

	public CustomerEntity() {
		super();
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		
		if(title.equalsIgnoreCase("Firma"))
			this.setCompany(true);
		else
			this.setCompany(false);
		
		this.title = title;
	}
	
	@Override
	public String getCompanyName() {
		return companyName;
	}

	@Override
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String getCommercialRegisterId() {
		return commercialRegisterId;
	}

	@Override
	public void setCommercialRegisterId(String commercialRegisterId) {
		this.commercialRegisterId = commercialRegisterId;
	}

	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public String getLastname() {
		return lastname;
	}

	@Override
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Override
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	@Transient
	public String getName() {
		if(this.isCompany() && getCompanyName() != null && getCompanyName().length() > 0)
			return getCompanyName()+"";
		else
			return getFirstname()+" "+getLastname();
	}

	@Override
	@Transient
	public String getNameLF() {
		if(this.isCompany() && getCompanyName() != null && getCompanyName().length() > 0)
			return getCompanyName();
		else
			return getLastname()+", "+getFirstname();
	}

	@Override
	public String toString(){
		return getName();
	}
	
	@Override
	public Address getAddress() {
		if(address == null)
			address = new AddressEmbeddable();
		return address;
	}

	@Override
	public void setAddress(Address address) {
		this.address = (AddressEmbeddable) address;
	}

	@Override
	public Date getBday() {
		return bday;
	}

	@Override
	public void setBday(Date bday) {
		this.bday = bday;
	}

	@Override
	public boolean isCompany() {
		return company;
	}

	@Override
	public String getTel() {
		return tel;
	}

	@Override
	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public void setCompany(boolean company) {
		this.company = company;
	}

	@Override
	public String getContactPerson() {
		return contactPerson;
	}

	@Override
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Override
	public Note getNote() {
		if(note == null)
			note = new NoteEntity();
		return note;
	}

	@Override
	public void setNote(Note note) {
		this.note = (NoteEntity) note;
	}

	@Override
	public List<? extends Note> getNoteList() {
		if(noteList == null)
			noteList = new ArrayList<NoteEntity>();
		return noteList;
	}

	@Override
	public void setNoteList(List<? extends Note> noteList) {
		this.noteList = (List<NoteEntity>) noteList;
	}

	@Override
	public List<? extends ExternalCustomer> getExternalCustomerList() {
		if(externalCustomerList == null)
			externalCustomerList = new LinkedList<ExternalCustomerEntity>();
		return externalCustomerList;
	}

	@Override
	public void setExternalCustomerList(
			List<? extends ExternalCustomer> externalCustomerList) {
		this.externalCustomerList = (List<ExternalCustomerEntity>) externalCustomerList;
	}

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public void setShop(Shop shop) {
		this.shop = (ShopEntity) shop;
	}

	@Override
	public User getAccountManager() {
		return accountManager;
	}

	@Override
	public void setAccountManager(User user) {
		this.accountManager = (UserEntity) user;
	}

	@Override
	public Country getNationality() {
		return nationality;
	}

	@Override
	public void setNationality(Country nationality) {
		this.nationality = (CountryEntity) nationality;
	}

	@Override
	public PaymentAccount getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(PaymentAccount paymentAccount) {
		this.paymentAccount = (PaymentAccountEntity) paymentAccount;
	}


	@Override
	public void setDeleted() {
		
		super.setDeleted();
		this.setFirstname("deleted");
		this.setLastname("deleted");
		this.setEmail("deleted");
	}

}
