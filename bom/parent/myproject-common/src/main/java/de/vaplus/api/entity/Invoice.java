package de.vaplus.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.vaplus.api.entity.embeddable.Commissionable;

public interface Invoice extends Activity{

	String getNumber();

	void setNumber(String number);

	String getIntroduction();

	void setIntroduction(String introduction);

	String getClosing();

	void setClosing(String closing);

	Commissionable getCommission();

	void setCommission(Commissionable commission);

	List<? extends InvoiceItem> getInvoiceItemList();

	void setInvoiceItemList(List<? extends InvoiceItem> invoiceItemList);

	String getRecipientLine1();

	void setRecipientLine1(String recipientLine1);

	String getRecipientLine2();

	void setRecipientLine2(String recipientLine2);

	String getRecipientLine3();

	void setRecipientLine3(String recipientLine3);

	String getRecipientLine4();

	void setRecipientLine4(String recipientLine4);

	String getRecipientLine5();

	void setRecipientLine5(String recipientLine5);

//	String getDrafterLine();

//	void setDrafterLine(String drafterLine);

	StockPickslip getStockPickslip();

	void setStockPickslip(StockPickslip stockPickslip);

	PaymentAccount getCustomerAccount();

	void setCustomerAccount(PaymentAccount customerAccount);

	Payment getPayment();

	void setPayment(Payment payment);

	String getPaymentInformation();

	void setPaymentInformation(String paymentInformation);

	Date getDueDate();

	void setDueDate(Date dueDate);

	File getInvoiceFile();

	void setInvoiceFile(File invoiceFile);

	Map<BigDecimal, BigDecimal> getVatList();

	Invoice getCancelationInvoice();

	void setCancelationInvoice(Invoice cancelationInvoice);

	Invoice getCanceledinvoice();

	void setCanceledinvoice(Invoice canceledinvoice);

	boolean isCanceled();

	boolean isCancelation();

	BaseContract getSubsidyContract();

	void setSubsidyContract(BaseContract subsidyContract);

	boolean isNoSubsidy();

	void setNoSubsidy(boolean noSubsidy);

}
