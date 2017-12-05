package de.vaplus.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.Payment;
import de.vaplus.api.entity.PaymentAccount;
import de.vaplus.api.entity.PaymentAccountTransaction;
import de.vaplus.api.entity.PaymentShopAccount;
import de.vaplus.api.entity.PaymentStatus;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;

public interface PaymentControllerInterface extends Serializable {

	List<? extends Payment> getPaymentList();

	Payment getPaymentById(long id);

	List<? extends Payment> getPaymentList(boolean showDisabled);

	Payment savePayment(Payment selectedPayment);

	Payment factoryNewPayment();

	PaymentAccount getCustomerAccount(Customer c);

	PaymentShopAccount getPaymentShopAccount(Payment p, Shop s);

	PaymentAccount getPaymentAccount(long id);

	PaymentAccount getPseudoCustomerAccount();

	PaymentAccountTransaction newPaymentAccountTransaction(BigDecimal amount, PaymentAccount sourceAccount,
			PaymentAccount destinationAccount, Invoice invoiceReference);

	List<? extends PaymentAccountTransaction> getAccountTransactions(PaymentAccount paymentAccount);

	BigDecimal getAccountBalance(PaymentAccount paymentAccount);

	BigDecimal getAccountIncoming(PaymentAccount paymentAccount);

	BigDecimal getAccountOutgoing(PaymentAccount paymentAccount);

//	boolean getCashRegisterStatus(Shop shop);

	List<? extends PaymentStatus> getPaymentStatusList(Payment payment, Shop shop);

	BigDecimal getCurrentPaymentCumulativeSum(Payment payment, Shop shop);

	boolean getPaymentStatus(Payment payment, Shop shop);

	BigDecimal getPaymentLastCumulativeSum(Payment payment, Shop shop);

	void openPayments(Shop shop, User user, String comment, BigDecimal cashRegisterCountSum) throws Exception;

	BigDecimal getPaymentAccountBalance(Payment payment, Shop shop);

	void closePayments(Shop shop, User user, String comment, BigDecimal cashRegisterCountSum) throws Exception;

	PaymentAccount getSettlingAccount(Shop activeShop);

	PaymentAccountTransaction newPaymentAccountTransaction(BigDecimal amount, PaymentAccount sourceAccount,
			PaymentAccount destinationAccount, String invoiceReference);

	PaymentStatus getLastPaymentStatus(Payment payment, Shop activeShop);

	PaymentAccount getCashCustomerAccount();

	List<? extends PaymentStatus> getPaymentStatusList(Payment payment, Shop shop, int month, int year);

	List<? extends PaymentStatus> getPaymentStatus(Payment payment, Shop shop, int month, int year);


}
