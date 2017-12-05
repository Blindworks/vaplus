package de.vaplus.api.entity;

import java.math.BigDecimal;

public interface PaymentStatus extends Activity {

	Payment getPayment();

	void setPayment(Payment payment);

	boolean isOpen();

	void setOpen(boolean open);

	BigDecimal getCumulativeSum();

	void setCumulativeSum(BigDecimal cumulativeSum);

}
