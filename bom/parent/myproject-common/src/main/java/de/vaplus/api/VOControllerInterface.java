package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.ProductCommissionVOType;
import de.vaplus.api.entity.RevenueLevelVOType;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOType;
import de.vaplus.api.entity.Vendor;

public interface VOControllerInterface extends Serializable {

	VO getVO(long id);

	List<? extends VO> getVOList(Vendor vendor);

	VO saveVO(VO vo);

	List<? extends VOType> getVOTypeList(Vendor vendor);

	VOType factoryNewVOTypeRevenueLevel();

	VOType factoryNewVOTypeProductCommission();

	VOType saveVOType(VOType voType);

	VOType getVOType(long id);

	List<? extends RevenueLevelVOType> getVOTypeRevenueLevelList(Vendor vendor);

	List<? extends ProductCommissionVOType> getVOProductCommissionTypeList();

	VO factoryNewVO();

	List<? extends VO> getVOList(Shop shop, boolean includePermitted);

	List<? extends VO> getVOList();

	VO getVOByNumber(String voNumber);

	void allowAllProductsForVoType(VOType voType);


}
