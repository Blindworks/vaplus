package de.vaplus;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.RevenueLevelVOType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.ProductCommissionVOTypeEntity;
import de.vaplus.client.entity.RevenueLevelVOTypeEntity;
import de.vaplus.client.entity.VOEntity;
import de.vaplus.client.entity.VOTypeEntity;


@Stateless
public class VOController implements VOControllerInterface {

	private static final long serialVersionUID = 2140720238470633267L;

	@EJB
	private ProductControllerInterface productController;
	
	@Inject
    private VOEao eao;


	@Override
	public VO getVO(long id) {
		return eao.getVO(id);
	}

	@Override
	public VO getVOByNumber(String voNumber) {
		return eao.getByNumber(voNumber);
	}

	@Override
	public VO saveVO(VO vo) {
		return eao.saveVO((VOEntity) vo);
	}

	@Override
	public List<? extends VO> getVOList(Vendor vendor) {
		return eao.getVOList(vendor);
	}

	@Override
	public List<? extends VOType> getVOTypeList(Vendor vendor) {
		return eao.getVOTypeList(vendor);
	}

	@Override
	public VOType factoryNewVOTypeRevenueLevel() {
		VOType voType = new RevenueLevelVOTypeEntity();
		voType.setPointsPerCommission(new BigDecimal(0));
		return voType;
	}

	@Override
	public VOType factoryNewVOTypeProductCommission() {
		VOType voType = new ProductCommissionVOTypeEntity();
		voType.setPointsPerCommission(new BigDecimal(0));
		return voType;
	}

	@Override
	public VOType saveVOType(VOType voType) {
		return eao.saveVOType((VOTypeEntity) voType);
	}
	
	@Override
	public VOType getVOType(long id) {
		return eao.getVOType(id);
	}

	@Override
	public List<? extends RevenueLevelVOType> getVOTypeRevenueLevelList(Vendor vendor) {
		return (List<? extends RevenueLevelVOType>) eao.getVOTypeRevenueLevelList(vendor);
	}

	@Override
	public List<? extends ProductCommissionVOType> getVOProductCommissionTypeList() {
		return (List<? extends ProductCommissionVOType>) eao.getVOTypeProductCommissionList();
	}

	@Override
	public VO factoryNewVO() {
		return new VOEntity();
	}

	@Override
	public List<? extends VO> getVOList(Shop shop, boolean includePermitted) {
		return eao.getVOList(shop, includePermitted);
	}

	@Override
	public List<? extends VO> getVOList() {
		return eao.getVOList();
	}	
	
	@Override
	public void allowAllProductsForVoType(VOType voType){
		if(voType == null)
			return;
		
		Iterator<? extends BaseProduct> i = productController.getBaseProductList().iterator();
		BaseProduct p;
		while(i.hasNext()){
			p = i.next();
			productController.addVOTypeToVoTypePermissionList(p, voType);
			p = productController.saveProduct(p);
		}
	}

}
