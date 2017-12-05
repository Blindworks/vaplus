package de.vaplus.api.entity;

import java.util.List;

public interface VendorCommissionAccountingFile extends Base{

	int getYear();

	int getMonth();

	List<? extends VendorCommissionAccountingItem> getItemList();

	String getVo();

}
