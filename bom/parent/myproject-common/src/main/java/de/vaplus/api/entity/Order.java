package de.vaplus.api.entity;

import java.util.List;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface Order extends Activity{

	String getInfo();

	void setInfo(String info);

	String getIntroduction();

	void setIntroduction(String introduction);

	Commissionable getCommission();

	void setCommission(Commissionable commission);

	List<? extends OrderItem> getOrderItemList();

	void setOrderItemList(List<? extends OrderItem> orderItemList);

	BaseContract getSubsidyContract();

	void setSubsidyContract(BaseContract subsidyContract);

	String getNumber();

	void setNumber(String number);



}
