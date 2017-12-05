package de.vaplus.client.entity.commission.vendor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import de.vaplus.api.entity.VendorCommissionAccountingFile;
import de.vaplus.api.entity.VendorCommissionAccountingItem;
import de.vaplus.client.entity.BaseEntity;

@Entity
@Table(name="VendorCommissionAccountingFile")
public class VendorCommissionAccountingFileEntity extends BaseEntity implements VendorCommissionAccountingFile{

	private static final long serialVersionUID = 324867763878458956L;

	@Column(name="vo", nullable = false, length=50)
	private String vo;
	
	@Column(name="year", nullable = false, length=4)
	private int year;
	
	@Column(name="month", nullable = false, length=2)
	private int month;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "file")
    private List<VendorCommissionAccountingItemEntity> itemList;

	public VendorCommissionAccountingFileEntity(){
		
	}
	
	public VendorCommissionAccountingFileEntity(String vo, int year, int month){
		setVo(vo);
		setYear(year);
		setMonth(month);
	}

	@PrePersist
	@PreUpdate
	public void initRelations(){
		if(getItemList().size() > 0){
			Iterator<? extends VendorCommissionAccountingItem> i = getItemList().iterator();
			while(i.hasNext()){
				((VendorCommissionAccountingItemEntity)i.next()).setFile(this);
			}
		}
	}
	
	@Override
	public String getVo() {
		return vo;
	}

	public void setVo(String vo) {
		this.vo = vo;
	}

	@Override
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@Override
	public List<? extends VendorCommissionAccountingItem> getItemList() {
		if(itemList == null)
			itemList = new LinkedList<VendorCommissionAccountingItemEntity>();
		return itemList;
	}

	public void setItemList(List<VendorCommissionAccountingItemEntity> itemList) {
		this.itemList = itemList;
	}
	
	
}