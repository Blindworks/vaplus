package de.vaplus.client.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;

import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.client.entity.embeddable.CommissionableEmbeddable;

@Entity
@Table(name="OrderEntity")
@NamedQueries({
    @NamedQuery(
        name = "Order.getOrderByNumber",
        query = "SELECT o FROM OrderEntity o WHERE o.number = :number",
        hints = {
                @QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheThenDatabase),
            }
    )
})
public class OrderEntity extends ActivityEntity implements Order {

	private static final long serialVersionUID = 7254460227003284936L;

	private static final int ORDER_STATUS_NEW = 0;
	private static final int ORDER_STATUS_INCOMPLETE = 3;
	private static final int ORDER_STATUS_COMPLETE = 6;

    @Column(name="number", nullable = false)
    private String number;

	@Column(name="info")
	private String info;
	
	@Column(name="introduction")
	private String introduction;
	
	@Embedded
	private CommissionableEmbeddable commission;
	
	@OneToMany(mappedBy="order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItemEntity> orderItemList;

    @OneToOne(mappedBy="subsidyOrder", fetch = FetchType.LAZY) 
    private BaseContractEntity subsidyContract;

	public OrderEntity() {
		super();
		commission = new CommissionableEmbeddable();
	}
	
	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	@PrePersist
	public void initRelations(){
		if(getOrderItemList().size() > 0){
			Iterator<OrderItemEntity> i = (Iterator<OrderItemEntity>) getOrderItemList().iterator();
			while(i.hasNext()){
				i.next().setOrder(this);
			}
		}
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String getIntroduction() {
		return introduction;
	}

	@Override
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Override
	public Commissionable getCommission() {
		if(commission == null)
			commission = new CommissionableEmbeddable();
		return commission;
	}

	@Override
	public void setCommission(Commissionable commission) {
		this.commission = (CommissionableEmbeddable) commission;
	}

	@Override
	public List<? extends OrderItem> getOrderItemList() {
		if(orderItemList == null)
			orderItemList = new LinkedList<OrderItemEntity>();
		return orderItemList;
	}

	@Override
	public void setOrderItemList(List<? extends OrderItem> orderItemList) {
		this.orderItemList = (List<OrderItemEntity>) orderItemList;
	}

	@Override
	public BaseContract getSubsidyContract() {
		return subsidyContract;
	}

	@Override
	public void setSubsidyContract(BaseContract subsidyContract) {
		this.subsidyContract = (BaseContractEntity) subsidyContract;
	}

   
}
