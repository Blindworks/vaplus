package de.vaplus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.SearchControllerInterface;
import de.vaplus.api.entity.SearchResult;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.eao.CustomerEao;
import de.vaplus.client.eao.ProductEao;
import de.vaplus.client.eao.UserEao;
import de.vaplus.client.entity.CustomerEntity;


@Stateless
public class SearchController implements SearchControllerInterface {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7699179901755346111L;

	@Inject
	private CustomerEao customerEao;
	
	@Inject
	private UserEao userEao;
	
	@Inject
	private ProductEao productEao;
	
	@Inject
	private ContractEao contractEao;
	
	@Override
	public List<? extends SearchResult> getResults(String query) {
		
		List<SearchResult> results = new ArrayList<SearchResult>();
		
		List<CustomerEntity> customerList = new ArrayList<CustomerEntity>();
		
		if(query.contains(",")){
			String[] splits = query.split(",");
			if(splits.length == 2)
				customerList = customerEao.findCustomer(splits[0], splits[1], false);
			else
				customerList = customerEao.findCustomer(query);
		}else{
			customerList = customerEao.findCustomer(query);
		}
		
		Iterator<CustomerEntity> i = customerList.iterator();
		CustomerEntity c;
		while(i.hasNext()){
			c = i.next();
			if(! results.contains(c))
				results.add(c);
		}
		

		results.addAll(userEao.findUser(query));
		results.addAll(productEao.findProduct(query));
		
		results.addAll(contractEao.findContract(query));
		
		

		return results;
	}


}
