package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ExternalCustomer;
import de.vaplus.api.entity.Note;
import de.vaplus.client.entity.CustomerEntity;
import de.vaplus.client.entity.CustomerEntity_;
import de.vaplus.client.entity.EventEntity;
import de.vaplus.client.entity.ExternalCustomerEntity;
import de.vaplus.client.entity.ExternalCustomerEntity_;
import de.vaplus.client.entity.NoteEntity;
import de.vaplus.client.entity.UserEntity_;
import de.vaplus.client.entity.embeddable.AddressEmbeddable_;

@Stateless
public class CustomerEao extends BaseEao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3635046642027868600L;
	
//	@Inject
//    private EntityManager em;

	public CustomerEntity getCustomer(long id) {
		return super.find(CustomerEntity.class, id);
	}

	public List<CustomerEntity> getCustomerList() {
		
		List<CustomerEntity> list = super.createNamedQuery("Customer.findActive", CustomerEntity.class)
		            .getResultList();
		
		
		return list;
	}

	public List<CustomerEntity> findCustomer(String firstname, String lastname, boolean exactsearch){
		
		
		List<CustomerEntity> list = new ArrayList<CustomerEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<CustomerEntity> cQuery = cb.createQuery(CustomerEntity.class);
			Root<CustomerEntity> root = cQuery.from(CustomerEntity.class);
			EntityType<CustomerEntity> type = super.getMetamodel().entity(CustomerEntity.class);
			cQuery.select(root);
			
			if(exactsearch){
				cQuery.where(
					cb.and(
						cb.equal( root.get(CustomerEntity_.deleted), false ),
						cb.or(
						    cb.and(
						        cb.equal(
						            cb.lower(
						                root.get(
						                    type.getDeclaredSingularAttribute("firstname", String.class)
						                )
						            ), firstname.trim().toLowerCase()
						        ), 
						        cb.equal(
						            cb.lower(
						                root.get(
						                    type.getDeclaredSingularAttribute("lastname", String.class)
						                )
						            ), lastname.trim().toLowerCase()
						        )
						    ),
						    cb.and(
							        cb.equal(
							            cb.lower(
							                root.get(
							                    type.getDeclaredSingularAttribute("firstname", String.class)
							                )
							            ), lastname.trim().toLowerCase()
							        ), 
							        cb.equal(
							            cb.lower(
							                root.get(
							                    type.getDeclaredSingularAttribute("lastname", String.class)
							                )
							            ), firstname.trim().toLowerCase()
							        )
							    )
						    )
					)
				);
			}
			else{
				cQuery.where(
					cb.and(
						cb.equal( root.get(CustomerEntity_.deleted), false ),
						cb.or(
						    cb.and(
						        cb.like(
						            cb.lower(
						                root.get(
						                    type.getDeclaredSingularAttribute("firstname", String.class)
						                )
						            ), firstname.trim().toLowerCase() + "%"
						        ), 
						        cb.like(
						            cb.lower(
						                root.get(
						                    type.getDeclaredSingularAttribute("lastname", String.class)
						                )
						            ), lastname.trim().toLowerCase() + "%"
						        )
						    ),
						    cb.and(
							        cb.like(
							            cb.lower(
							                root.get(
							                    type.getDeclaredSingularAttribute("firstname", String.class)
							                )
							            ), lastname.trim().toLowerCase() + "%"
							        ), 
							        cb.like(
							            cb.lower(
							                root.get(
							                    type.getDeclaredSingularAttribute("lastname", String.class)
							                )
							            ), firstname.trim().toLowerCase() + "%"
							        )
							    )
						   )
					)
				);
			}
			
			list = super.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<CustomerEntity> findCustomer(String query){
		
		
		List<CustomerEntity> list = new ArrayList<CustomerEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<CustomerEntity> cQuery = cb.createQuery(CustomerEntity.class);
			Root<CustomerEntity> root = cQuery.from(CustomerEntity.class);
			

			Join <CustomerEntity,ExternalCustomerEntity> joinExternalCustomer = root.join( CustomerEntity_.externalCustomerList, JoinType.LEFT);
			cQuery.select(root);

			List<Predicate> criteria = new ArrayList<Predicate>();
			

			if (query.matches("-?\\d+(\\.\\d+)?")) {
			    // numeric
				
				try{
					long numericQuery = Long.valueOf(query);
					
					criteria.add(cb.equal( root.get( CustomerEntity_.id) , numericQuery ));
					
				}catch(NumberFormatException e){
					
				}
				
				
			}

			criteria.add(cb.like(
		        	cb.lower( root.get(CustomerEntity_.firstname)), 
		        	 "%" +query.toLowerCase() + "%"
		        ));
			
			criteria.add(cb.like(
		        	cb.lower( root.get(CustomerEntity_.lastname)), 
		        	"%" +query.toLowerCase() + "%"
		        ));
			
			criteria.add(cb.like(
	        		cb.lower(root.get(CustomerEntity_.companyName)), 
	        		"%" +query.toLowerCase() + "%"
		    ));
			
			criteria.add(cb.like(
		    		cb.lower( root.get(CustomerEntity_.email)), 
			     query.toLowerCase() + "%"
			));
			
			criteria.add(cb.like(
					cb.lower(root.get(CustomerEntity_.address).get(AddressEmbeddable_.city)),
				 query.toLowerCase() + "%"
			));
			
			criteria.add(cb.like(
					root.get(CustomerEntity_.address).get(AddressEmbeddable_.zip),
				 query.toLowerCase() + "%"
			));
			
			criteria.add(cb.like(
					cb.lower(root.get(CustomerEntity_.address).get(AddressEmbeddable_.street)), 
				 query.toLowerCase() + "%"
			));
			
			criteria.add(cb.like(
					cb.lower(joinExternalCustomer.get(ExternalCustomerEntity_.customerId)), 
				 query.toLowerCase()
			));
			
			cQuery.where(
					cb.and(
						cb.equal( root.get(CustomerEntity_.deleted), false ),
					    cb.or(
					    		criteria.toArray(new Predicate[0])
					    )
					)
				);
			
//			cQuery.groupBy((root.get(CustomerEntity_.id)));
			
			list = super.createQuery(cQuery).getResultList();
			
			Iterator<CustomerEntity> i = list.iterator();
			CustomerEntity c;
//			while(i.hasNext()){
//				c = i.next();
//				System.out.println("found entity: "+c.getId()+" "+c.getName());
//			}
			
			
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public List<CustomerEntity> findCustomerByCompanyName(String query){
		
		
		List<CustomerEntity> list = new ArrayList<CustomerEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<CustomerEntity> cQuery = cb.createQuery(CustomerEntity.class);
			Root<CustomerEntity> root = cQuery.from(CustomerEntity.class);
			EntityType<CustomerEntity> type = super.getMetamodel().entity(CustomerEntity.class);
			cQuery.select(root);
			
			cQuery.where(
					cb.and(
						cb.equal( root.get(CustomerEntity_.deleted), false ),
				        cb.equal(
				        	cb.lower(root.get(CustomerEntity_.companyName)), 
					        query.toLowerCase()
					    )
				    )
				);
			
			list = super.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public CustomerEntity saveCustomer(CustomerEntity customer) {
		return super.merge(customer);
	}

	public CustomerEntity factoryNewCustomer() {
		return new CustomerEntity();
	}

	public Note factoryNewNote() {
		return new NoteEntity();
	}
	
	public String getCityByZip(String country, String zip){
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<String> cQuery = cb.createQuery(String.class);
			Root<CustomerEntity> root = cQuery.from(CustomerEntity.class);
			cQuery.select(root.get(CustomerEntity_.address).get(AddressEmbeddable_.city));
			
			cQuery.where(
					cb.and(
							cb.equal(root.get(CustomerEntity_.address).get(AddressEmbeddable_.country), country),
							cb.equal(root.get(CustomerEntity_.address).get(AddressEmbeddable_.zip), zip)
					)
			);
			
			return super.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return "";
	}

	public List<? extends ExternalCustomer> getExternalCustomerList(
			Customer customer) {

		List<? extends ExternalCustomer> list = new ArrayList<ExternalCustomerEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<ExternalCustomerEntity> cQuery = cb.createQuery(ExternalCustomerEntity.class);
			Root<ExternalCustomerEntity> root = cQuery.from(ExternalCustomerEntity.class);
			cQuery.select(root);
			
			cQuery.where(
					cb.equal(root.get(ExternalCustomerEntity_.customer), customer)
			);
			
			list = super.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public ExternalCustomer getExternalCustomer(long id) {
		return super.find(ExternalCustomerEntity.class, id);
	}

	public List<? extends ExternalCustomer> findExternalCustomerByCustomerId(String customerId) {

		List<? extends ExternalCustomer> list = new ArrayList<ExternalCustomerEntity>();
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<ExternalCustomerEntity> cQuery = cb.createQuery(ExternalCustomerEntity.class);
			Root<ExternalCustomerEntity> root = cQuery.from(ExternalCustomerEntity.class);
			cQuery.select(root);
			
			cQuery.where(
					cb.like(root.get(ExternalCustomerEntity_.customerId), "%"+customerId+"%")
			);

			list = super.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public ExternalCustomer getExternalCustomerByCustomerId(String customerId) {
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<ExternalCustomerEntity> cQuery = cb.createQuery(ExternalCustomerEntity.class);
			Root<ExternalCustomerEntity> root = cQuery.from(ExternalCustomerEntity.class);
			cQuery.select(root);
			
			cQuery.where(
					cb.equal(root.get(ExternalCustomerEntity_.customerId), customerId)
			);

			return super.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return null;
	}

	public Customer getCustomer(String firstname, String lastname,
			String companyName, boolean company, String street,
			String streetNumber, String addressLine, String zip, String city) {

		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<CustomerEntity> cQuery = cb.createQuery(CustomerEntity.class);
			Root<CustomerEntity> root = cQuery.from(CustomerEntity.class);
			cQuery.select(root);
			

			List<Predicate> criteria = new ArrayList<Predicate>();
			
			criteria.add(cb.equal( root.get(CustomerEntity_.deleted), false ));
			
			if(firstname != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.firstname)), firstname.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.firstname)));
				
				
			if(lastname != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.lastname)), lastname.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.lastname)));
				
			
			if(companyName != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.companyName)), companyName.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.companyName)));
				
			
			if(street != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.street)), street.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.address).get(AddressEmbeddable_.street)));
				
			
			if(streetNumber != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.streetNumber)), streetNumber.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.address).get(AddressEmbeddable_.streetNumber)));
				
			
			if(addressLine != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.addressline)), addressLine.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.address).get(AddressEmbeddable_.addressline)));
				
			
			if(zip != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.zip)), zip.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.address).get(AddressEmbeddable_.zip)));
				
			
			if(city != null)
				criteria.add(cb.equal( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.city)), city.toLowerCase()));
			else
				criteria.add(cb.isNull(root.get(CustomerEntity_.address).get(AddressEmbeddable_.city)));
				
			
			criteria.add(cb.equal( root.get(CustomerEntity_.company), company));
			
			
			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));
			
			return super.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
			
		}
		
		return null;
		
	}

	public ExternalCustomer saveExternalCustomer(
			ExternalCustomer externalCustomer) {
		return super.merge(externalCustomer);
	}

	public ExternalCustomer reload(ExternalCustomer externalCustomer) {
		
		ExternalCustomer ec = super.find(ExternalCustomerEntity.class, externalCustomer.getId());
		super.refresh(ec);
		
		return ec;
	}
	
	private List<Predicate> getCustomerListFilter(CriteriaBuilder cb, Root<CustomerEntity> root, String searchQuery){
		
		List<Predicate> criteria = new ArrayList<Predicate>();
		
		
		
		if(searchQuery == null || searchQuery.length() < 1)
			return criteria;
		
		searchQuery = searchQuery.trim().toLowerCase();

		criteria.add(
			cb.and(
				cb.equal( root.get(CustomerEntity_.deleted), false ),
				cb.or(
						cb.like( cb.lower( root.get(CustomerEntity_.firstname)), searchQuery+"%" ),
						cb.like( cb.lower( root.get(CustomerEntity_.lastname)), searchQuery+"%" ),
						cb.like( cb.lower( root.get(CustomerEntity_.companyName)), searchQuery+"%" ),
						cb.like( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.zip)), searchQuery ),
						cb.like( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.city)), searchQuery+"%" ),
						cb.like( cb.lower( root.get(CustomerEntity_.address).get(AddressEmbeddable_.street)), searchQuery+"%" )
				)
			)
		);
		
		return criteria;
	}

	public List<? extends Customer> getCustomerList(int currentPage, int pageSize, String sortField, String sortOrder,
			String searchQuery) {
		
		List<? extends Customer> list = new ArrayList<Customer>();
		
		
		try{
	
			CriteriaBuilder cb = super.getCriteriaBuilder();
			CriteriaQuery<CustomerEntity> cQuery = cb.createQuery(CustomerEntity.class);
			Root<CustomerEntity> root = cQuery.from(CustomerEntity.class);
			cQuery.select(root);
			
			List<Predicate> criteria = getCustomerListFilter(cb, root, searchQuery);
			
			
			cQuery.where(
				    cb.and( criteria.toArray(new Predicate[0]) )
			);
			
			if(sortField != null && sortField.length() > 0 && sortOrder != null && sortOrder.length() > 0){

				Path<?> orderPath = null;
				Path<?> orderPath2 = null;
				Path<?> orderPath3 = null;
				Path<?> orderPath4 = null;
				
				if(sortField.equalsIgnoreCase("id")){
					orderPath = root.get(CustomerEntity_.id);
				}
				
				if(sortField.equalsIgnoreCase("name")){
					orderPath = root.get(CustomerEntity_.lastname);
					orderPath2 = root.get(CustomerEntity_.companyName);
				}
				
				if(sortField.equalsIgnoreCase("firstname")){
					orderPath = root.get(CustomerEntity_.firstname);
				}
				
				if(sortField.equalsIgnoreCase("address")){
					orderPath = root.get(CustomerEntity_.address).get(AddressEmbeddable_.country);
					orderPath2 = root.get(CustomerEntity_.address).get(AddressEmbeddable_.zip);
					orderPath3 = root.get(CustomerEntity_.address).get(AddressEmbeddable_.street);
					orderPath4 = root.get(CustomerEntity_.address).get(AddressEmbeddable_.streetNumber);
				}
			
				if(orderPath != null){
					if(sortOrder.equalsIgnoreCase("asc")){
						cQuery.orderBy(cb.asc(orderPath));
					}else if(sortOrder.equalsIgnoreCase("desc")){
						cQuery.orderBy(cb.desc(orderPath));
					}
				}
			
				if(orderPath2 != null){
					if(sortOrder.equalsIgnoreCase("asc")){
						cQuery.orderBy(cb.asc(orderPath2));
					}else if(sortOrder.equalsIgnoreCase("desc")){
						cQuery.orderBy(cb.desc(orderPath2));
					}
				}
			
				if(orderPath3 != null){
					if(sortOrder.equalsIgnoreCase("asc")){
						cQuery.orderBy(cb.asc(orderPath3));
					}else if(sortOrder.equalsIgnoreCase("desc")){
						cQuery.orderBy(cb.desc(orderPath3));
					}
				}
			
				if(orderPath4 != null){
					if(sortOrder.equalsIgnoreCase("asc")){
						cQuery.orderBy(cb.asc(orderPath4));
					}else if(sortOrder.equalsIgnoreCase("desc")){
						cQuery.orderBy(cb.desc(orderPath4));
					}
				}
			}
			
			TypedQuery<CustomerEntity> query = super.createQuery(cQuery);
			
			query.setFirstResult((currentPage - 1) * pageSize);
			query.setMaxResults(pageSize);
			

					
			list = query.getResultList();
		}
		catch(NoResultException e){
		}
		
		return list;
	}

	public long getCustomerCount() {

		CriteriaBuilder cb = super.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(CustomerEntity.class)));
		
		return super.createQuery(cq).getSingleResult();
	}

	public long getCustomerCount(int currentPage, int pageSize, String sortField, String sortOrder,
			String searchQuery) {
		
		CriteriaBuilder cb = super.getCriteriaBuilder();
		CriteriaQuery<Long> cQuery = cb.createQuery(Long.class);
		Root<CustomerEntity> root = cQuery.from(CustomerEntity.class);
		cQuery.select(cb.count(root));
		
		List<Predicate> criteria = getCustomerListFilter(cb, root, searchQuery);
		
		cQuery.where(
			    cb.and( criteria.toArray(new Predicate[0]) )
		);

		return super.createQuery(cQuery).getSingleResult();
		
	}

	public Customer refreshCustomer(Customer customer) {
		if(customer == null)
			return null;
		
		customer = super.find(CustomerEntity.class, customer.getId());
		if(customer == null)
			return null;
		
		super.refresh(customer);
		
		return customer;
	}


}
