package de.vaplus.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxHelper {

	
	public static BigDecimal addPercentage(BigDecimal netPrice, BigDecimal percentage){
		if(netPrice == null || percentage == null)
			return new BigDecimal(0);
		return netPrice.multiply(percentage.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP).add(new BigDecimal(1)));
	}
	
	public static BigDecimal removePercentage(BigDecimal grossPrice, BigDecimal percentage){
		if(grossPrice == null || percentage == null)
			return new BigDecimal(0);
		return grossPrice.divide(percentage.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP).add(new BigDecimal(1)), 4, RoundingMode.HALF_UP);
	}
	
	public static BigDecimal addTax(BigDecimal netPrice, BigDecimal tax){
		if(netPrice == null || tax == null)
			return new BigDecimal(0);
		return addPercentage(netPrice, tax);
	}
	
	public static BigDecimal removeTax(BigDecimal grossPrice, BigDecimal tax){
		if(grossPrice == null || tax == null)
			return new BigDecimal(0);
		return removePercentage(grossPrice, tax);
	}
	
	public static BigDecimal addVat(BigDecimal netPrice, BigDecimal vat){
		if(netPrice == null || vat == null)
			return new BigDecimal(0);
		return netPrice.add(vat);
	}
	
	public static BigDecimal removeVat(BigDecimal grossPrice, BigDecimal vat){
		if(grossPrice == null || vat == null)
			return new BigDecimal(0);
		return grossPrice.subtract(vat);
	}
	
	public static BigDecimal getVatFromNetPrice(BigDecimal netPrice, BigDecimal tax){
		if(netPrice == null || tax == null)
			return new BigDecimal(0);
		return netPrice.multiply(tax.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP));
	}
	
	public static BigDecimal getVatFromGrossPrice(BigDecimal grossPrice, BigDecimal tax){
		if(grossPrice == null || tax == null)
			return new BigDecimal(0);
		return grossPrice.subtract( removeTax(grossPrice, tax) );
	}
	
	
}
