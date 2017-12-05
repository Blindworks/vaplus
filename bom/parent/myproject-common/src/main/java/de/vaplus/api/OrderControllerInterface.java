package de.vaplus.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import de.vaplus.api.entity.Invoice;
import de.vaplus.api.entity.InvoiceItem;
import de.vaplus.api.entity.Order;
import de.vaplus.api.entity.OrderItem;
import de.vaplus.api.entity.Product;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.embeddable.Commissionable;

public interface OrderControllerInterface extends Serializable{

	Order factoryOrder();

	Order saveOrder(Order order) throws Exception;


	void removeRetailItemFromOrder(Order order, OrderItem orderItem);

	void addRetailItemToOrder(Order order, Product product, String serial,
			BigDecimal quantity) throws Exception;

	void calculateCommission(Order selectedOrder);

//	void calculteSubsidy(Order order);

	List<? extends Order> getOrderList(Shop shop, User user, Date from, Date to);

	Invoice getInvoiceByNumber(String number);

	Invoice factoryInvoice();

	Invoice saveInvoice(Invoice invoice) throws Exception;

	void removeInvoiceItemFromInvoice(Invoice invoice, InvoiceItem invoiceItem);

	void calculateCommission(Invoice invoice);

	InvoiceItem addInvoiceItemToInvoice(Invoice invoice, Product product, BigDecimal quantity, String serial)
			throws Exception;

	void addOrderItemToOrder(Order order, String title, BigDecimal quantity, String serial, Commissionable commission,
			BigDecimal unitPrice, boolean blockedItem);

	List<? extends Invoice> getInvoiceList(Shop shop, User user, Date from, Date to);

	Order getOrderByNumber(String number);

	InvoiceItem factoryInvoiceItem();

	void addInvoiceItemToInvoice(Invoice invoice, InvoiceItem invoiceItem) throws Exception;

	void calculteSubsidy(Invoice invoice);

}
