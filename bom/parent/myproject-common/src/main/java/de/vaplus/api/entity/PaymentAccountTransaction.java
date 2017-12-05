package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface PaymentAccountTransaction extends Base{

	BigDecimal getAmount();

	void setAmount(BigDecimal amount);

	PaymentAccount getSourceAccount();

	void setSourceAccount(PaymentAccount sourceAccount);

	PaymentAccount getDestinationAccount();

	void setDestinationAccount(PaymentAccount destinationAccount);

	Invoice getInvoiceReference();

	void setInvoiceReference(Invoice invoiceReference);

	String getNote();

	void setNote(String note);

}
