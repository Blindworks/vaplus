package de.vaplus.client.entity.embeddable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import de.vaplus.api.entity.embeddable.Commissionable;
import de.vaplus.helper.TaxHelper;

@Embeddable
public class CommissionableEmbeddable implements Commissionable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1368518334415998059L;

	@Column(name="points", precision = 10, scale = 4, nullable = false)
	private BigDecimal points;

	@Column(name="commission", precision = 10, scale = 4, nullable = false)
	private BigDecimal commission;

	@Column(name="price", precision = 10, scale = 4, nullable = false)
	private BigDecimal price;

	@Column(name="vat", precision = 10, scale = 4, nullable = true)
	private BigDecimal vat;

	@Column(name="tax", precision = 5, scale = 2, nullable = true)
	private BigDecimal tax;
	
	public CommissionableEmbeddable(){
		price = new BigDecimal(0);
		points = new BigDecimal(0);
		commission = new BigDecimal(0);
		vat = new BigDecimal(0);
	}

	@Override
	public BigDecimal getPrice() {
		if(price == null)
			price = new BigDecimal(0);
		return price;
	}

	@Override
	public void setPrice(BigDecimal price) {
		if(price == null)
			price = new BigDecimal(0);
		else
			this.price = price;
	}

	@Override
	public BigDecimal getVat() {
		if(vat == null)
			vat = new BigDecimal(0);
		return vat;
	}

	@Override
	public void setVat(BigDecimal vat) {
		if(vat == null)
			vat = new BigDecimal(0);
		else
			this.vat = vat;
	}

	@Override
	public BigDecimal getTax() {
		if(tax == null)
			tax = new BigDecimal(0);
		return tax;
	}

	@Override
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	@Override
	public BigDecimal getPoints() {
		if(points == null)
			points = new BigDecimal(0);
		return points;
	}

	@Override
	public void setPoints(BigDecimal points) {
		if(points == null)
			points = new BigDecimal(0);
		else
			this.points = points;
	}

	@Override
	public BigDecimal getCommission() {
		if(commission == null)
			commission = new BigDecimal(0);
		return commission;
	}

	@Override
	public void setCommission(BigDecimal commission) {
		if(commission == null)
			commission = new BigDecimal(0);
		else
			this.commission = commission;
	}
	
	@Transient
	@Override
	public Commissionable addCommissionable(Commissionable c2){
		return addCommissionable(c2,false);
	}
	
	@Transient
	@Override
	public Commissionable addCommissionable(Commissionable c2, boolean percentagePrice){
		
		if(c2 == null)
			return this;
		
		Commissionable commission = new CommissionableEmbeddable();

		if(this.getPoints() == null || c2.getPoints() == null || 
				this.getCommission() == null || c2.getCommission() == null || 
				this.getPrice() == null || c2.getPrice() == null)
			return commission;
		
		commission.setPoints( this.getPoints().add(c2.getPoints()) );
		commission.setCommission( this.getCommission().add(c2.getCommission()) );

		if(this.getTax() != null)
			commission.setTax(this.getTax());

//		if(commission.getTax() == null && c2.getTax() != null)
//			commission.setTax(c2.getTax());
		
		if(this.getVat() != null)
			commission.setVat(this.getVat());
		
		if(percentagePrice){

			commission.setPrice( TaxHelper.addPercentage(this.getPrice(), c2.getPrice()));
			
			if(this.getTax() == null)
				commission.setVat( TaxHelper.addPercentage(this.getVat(), c2.getPrice()));
			
		}else{
			commission.setPrice( this.getPrice().add(c2.getPrice()) );
			
			if(this.getTax() == null || this.getTax().compareTo(new BigDecimal(0)) == 0){
				
				if(c2.getTax() != null && c2.getTax().compareTo(new BigDecimal(0))> 0){
					commission.setTax(c2.getTax());
					commission.setVat( commission.getVat().add( TaxHelper.getVatFromNetPrice(c2.getPrice(), c2.getTax())));
				}
				
			}
			else{
				commission.setVat( commission.getVat().add(c2.getVat()));
			}
		}
		
		return commission;
	}
	
	@Transient
	@Override
	public BigDecimal getGrossPrice(){
		if(tax != null)
			return TaxHelper.addTax(price, tax);
		else if(vat != null)
			return TaxHelper.addVat(price, vat);
		else
			return new BigDecimal(0);
	}
	
	@Transient
	@Override
	public void setGrossPrice(BigDecimal grossPrice){
		if(tax != null)
			setPrice(TaxHelper.removeTax(grossPrice, tax));
	}

	@Override
	public void invert(){
		this.price = price.multiply(new BigDecimal(-1));
		this.points = points.multiply(new BigDecimal(-1));
		this.commission = commission.multiply(new BigDecimal(-1));
		this.vat = vat.multiply(new BigDecimal(-1));
	}
	
	@Override
	public String toString(){
		return "price: "+price+" points:"+points+" commission: "+commission+" vat:"+vat;
	}
}
