package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.vaplus.api.entity.ProductStatistic;
import de.vaplus.client.entity.ProductStatisticEntity;

@Stateless
public class ProductStatisticEao implements Serializable {

	private static final long serialVersionUID = -1494929240535058960L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public ProductStatisticEntity saveProductStatistic(ProductStatisticEntity productStatistic) {
		return em.merge(productStatistic);
	}

	public List<? extends ProductStatistic> getProductStatisticList() {
		List<? extends ProductStatisticEntity> list = em.createNamedQuery("ProductStatistic.getList", ProductStatisticEntity.class)
	            .getResultList();
	
		return list;
	}

	public List<? extends ProductStatistic> getOverviewProductStatisticList() {
		List<? extends ProductStatisticEntity> list = em.createNamedQuery("ProductStatistic.getOverviewList", ProductStatisticEntity.class)
	            .getResultList();
	
		return list;
	}

}
