package de.vaplus.client.backingbeans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.entity.BaseProduct;


@ManagedBean(name = "productListBean")
@RequestScoped
public class ProductListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1492798587616662728L;

	@EJB
	private ProductControllerInterface productController;

	public List<? extends BaseProduct> getProductList(){
		return productController.getBaseProductList();
	}

}
