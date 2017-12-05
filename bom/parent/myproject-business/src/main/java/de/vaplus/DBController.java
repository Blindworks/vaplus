package de.vaplus;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;

import de.vaplus.api.DBControllerInterface;
import de.vaplus.api.PropertyControllerInterface;


@Singleton
@Startup
public class DBController implements DBControllerInterface{

	private static final long serialVersionUID = 4507672366365639362L;

	public static final String PROPERTY_DB_VERSION = "dbVersion";
	private static final long DB_VERSION = 228;

	@EJB
    private PropertyControllerInterface propertyController;

//	@EJB
//    private UserControllerInterface userController;


	public DBController(){
		// System.out.println("### INIT DBController ###");
	}

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	private void startPriorityUpdate(){
//		System.out.println("START DB PRIORITY UPDATE");

		Session session = em.unwrap(Session.class);

		Vector v = session.executeSQL("SHOW COLUMNS FROM `Property` LIKE 'deleted'");

		if(v.size() == 0){
			session.executeNonSelectingSQL("ALTER TABLE `Property` ADD `deleted` tinyint(1) NOT NULL;");
			return;
		}

		v = session.executeSQL("SHOW COLUMNS FROM `VO_CommissionCache` LIKE 'commissionHistoryList_id'");
		if(v.size() > 0){
			session.executeNonSelectingSQL("DROP TABLE VO_CommissionCache");
			session.executeNonSelectingSQL("CREATE TABLE `VO_CommissionCache` ( `VOEntity_id` bigint(20) NOT NULL, `commissionCacheList_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
			session.executeNonSelectingSQL("ALTER TABLE `VO_CommissionCache` ADD PRIMARY KEY (`VOEntity_id`,`commissionCacheList_id`), ADD KEY `FK_VO_CommissionCache_commissionCacheList_id` (`commissionCacheList_id`);");
			session.executeNonSelectingSQL("ALTER TABLE `VO_CommissionCache` ADD CONSTRAINT `FK_VO_CommissionCache_commissionCacheList_id` FOREIGN KEY (`commissionCacheList_id`) REFERENCES `CommissionCache` (`id`);");
			session.executeNonSelectingSQL("ALTER TABLE `VO_CommissionCache` ADD CONSTRAINT `FK_VO_CommissionCache_VOEntity_id` FOREIGN KEY (`VOEntity_id`) REFERENCES `VO` (`id`);");
			return;
		}


	}

	@SuppressWarnings("unused")
	private void startUpdate_228(UnitOfWork session) throws Exception{

		// BaseContract
		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` ADD `manualItem` TINYINT(1) NOT NULL DEFAULT 0;");
	
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_227(UnitOfWork session) throws Exception{
		

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` DROP FOREIGN KEY `FK_Invoice_cancelationInvoice_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` DROP `cancelationInvoice_id`;");

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `canceledinvoice_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD CONSTRAINT `FK_Invoice_canceledinvoice_id` FOREIGN KEY (`canceledinvoice_id`) REFERENCES `Invoice` (`id`);");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_226(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `cancelationInvoice_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD CONSTRAINT `FK_Invoice_cancelationInvoice_id` FOREIGN KEY (`cancelationInvoice_id`) REFERENCES `Invoice` (`id`);");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_225(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("INSERT INTO ShopDocNumberRanges (shop_id, invoiceNumber, orderNumber ,pickslipNumber) SELECT DISTINCT id, invoiceNumber, orderNumber ,pickslipNumber FROM Shop");

		session.executeNonSelectingSQL("ALTER TABLE Shop DROP COLUMN invoiceNumber");
		session.executeNonSelectingSQL("ALTER TABLE Shop DROP COLUMN orderNumber");
		session.executeNonSelectingSQL("ALTER TABLE Shop DROP COLUMN pickslipNumber");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_224(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("CREATE TABLE `ShopDocNumberRanges` ( `shop_id` bigint(20) NOT NULL, "
				+ "`invoiceNumber` BIGINT(20) NOT NULL, "
				+ "`orderNumber` BIGINT(20) NOT NULL, "
				+ "`pickslipNumber` BIGINT(20) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ShopDocNumberRanges` ADD PRIMARY KEY (`shop_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ShopDocNumberRanges` ADD CONSTRAINT `FK_ShopDocNumberRanges_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`);");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_223(UnitOfWork session) throws Exception{

		// BaseContract
		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` ADD `blockedItem` TINYINT(1) NOT NULL DEFAULT 0;");
	
	}

	@SuppressWarnings("unused")
	private void startUpdate_222(UnitOfWork session) throws Exception{

		// BaseContract
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `subsidyInvoice_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD CONSTRAINT `FK_BaseContract_subsidyInvoice_id` FOREIGN KEY (`subsidyInvoice_id`) REFERENCES `Invoice` (`id`);");
	
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_221(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `settlingAccount_id` bigint(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD CONSTRAINT `FK_PaymentStatus_settlingAccount_id` FOREIGN KEY (`settlingAccount_id`) REFERENCES `PaymentAccount` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccountTransaction` ADD `note` TEXT NULL;");
		
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_220(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `ContractStatus` CHANGE `active` `showInExtensionOfTheTermList` TINYINT(1) NOT NULL;");

		session.executeNonSelectingSQL("UPDATE `ContractStatus` SET `showInExtensionOfTheTermList` = 0 WHERE `id` != 1;");
		session.executeNonSelectingSQL("UPDATE `ContractStatus` SET `showInExtensionOfTheTermList` = 1 WHERE `id` = 1;");

	}
	
	@SuppressWarnings("unused")
	private void startUpdate_219(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("CREATE TABLE `PaymentStatus` ( `id` bigint(20) NOT NULL, "
				+ "`payment_id` BIGINT(20) NOT NULL, "
				+ "`open` TINYINT(1) NOT NULL, "
				+ "`cumulativeSum` DECIMAL(10,2) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentStatus` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentStatus` ADD CONSTRAINT `FK_PaymentStatus_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentStatus` ADD CONSTRAINT `FK_PaymentStatus_payment_id` FOREIGN KEY (`payment_id`) REFERENCES `Payment` (`id`);");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_218(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `OrderEntity` MODIFY `number` VARCHAR(20) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` MODIFY `number` VARCHAR(20) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_217(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` MODIFY `number` VARCHAR(20) NOT NULL;");
		
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_InvoiceEntity_number`;");
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_OrderEntity_number`;");
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_StockPickslip_number`;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_216(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `invoiceNumber` BIGINT(20) NOT NULL DEFAULT 100000;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `orderNumber` BIGINT(20) NOT NULL DEFAULT 200000;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `pickslipNumber` BIGINT(20) NOT NULL DEFAULT 300000;");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_215(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` DROP `drafterLine`;");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_214(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `ContractStatusChange` ADD `contract_id` BIGINT(20) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ContractStatusChange` ADD CONSTRAINT `FK_ContractStatusChange_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `BaseContract` (`id`);");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_213(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("CREATE TABLE `ContractStatusChange` ( `id` bigint(20) NOT NULL, "
				+ "`old_status` VARCHAR(100) NOT NULL, "
				+ "`new_status` VARCHAR(100) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ContractStatusChange` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ContractStatusChange` ADD CONSTRAINT `FK_ContractStatusChange_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_212(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `ContractStatus` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, "
				+ "`name` VARCHAR(100) NOT NULL, "
				+ "`editable` TINYINT(1) NOT NULL, "
				+ "`active` TINYINT(1) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");

		session.executeNonSelectingSQL("ALTER TABLE `ContractStatus` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ContractStatus` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("INSERT INTO `ContractStatus` (`id`, `creationDate`, `deleted`, `updateDate`, `name`, `editable`, `active`) VALUES "
				+ "(1, NOW(), '0', NOW(), 'aktiv', 0, 1),"
				+ "(2, NOW(), '0', NOW(), 'gekündigt', 0, 0),"
				+ "(3, NOW(), '0', NOW(), 'in Bearbeitung', 0, 1),"
				+ "(4, NOW(), '0', NOW(), 'verlängert', 0, 0),"
				+ "(5, NOW(), '0', NOW(), 'terminiert', 0, 0),"
				+ "(6, NOW(), '0', NOW(), 'ignoriert', 0, 0),"
				+ "(7, NOW(), '0', NOW(), 'AO-Kündigung', 0, 0)"
				+ ";");

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `status_id` bigint(20) NOT NULL DEFAULT 1;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD CONSTRAINT `FK_BaseContract_status_id` FOREIGN KEY (`status_id`) REFERENCES `ContractStatus` (`id`);");

	}


	@SuppressWarnings("unused")
	private void startUpdate_211(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `drafterLine` VARCHAR(255) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `docFooterLine1` VARCHAR(255) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `docFooterLine2` VARCHAR(255) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `docFooterLine3` VARCHAR(255) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_210(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD `completelyReImported` TINYINT(1) NOT NULL DEFAULT 0;");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_209(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `stockControlled` TINYINT(1) NOT NULL DEFAULT 1;");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_208(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `invoiceFile_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD CONSTRAINT `FK_Invoice_invoiceFile_id` FOREIGN KEY (`invoiceFile_id`) REFERENCES `File` (`id`);");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_207(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `dueDate` Datetime NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Payment` ADD `termOfPayment` int(3) NOT NULL DEFAULT '7';");

		session.executeNonSelectingSQL("UPDATE `Payment` SET `termOfPayment` = '0' WHERE `id` = 1;");
		session.executeNonSelectingSQL("UPDATE `Payment` SET `termOfPayment` = '0' WHERE `id` = 3;");
		session.executeNonSelectingSQL("UPDATE `Payment` SET `termOfPayment` = '0' WHERE `id` = 4;");
		session.executeNonSelectingSQL("UPDATE `Payment` SET `termOfPayment` = '0' WHERE `id` = 5;");
	}

	
	@SuppressWarnings("unused")
	private void startUpdate_206(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccountTransaction` ADD `invoiceReference_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccountTransaction` ADD CONSTRAINT `FK_PaymentAccTrans_invoiceRef_id` FOREIGN KEY (`invoiceReference_id`) REFERENCES `Invoice` (`id`);");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_205(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `paymentInformation` TEXT NULL;");
	}


	@SuppressWarnings("unused")
	private void startUpdate_204(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategorySalesCache` CHANGE `pieces` `pieces` DECIMAL(10,4) NOT NULL DEFAULT 0;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_203(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD `order_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD CONSTRAINT `FK_StockPickslip_order_id` FOREIGN KEY (`order_id`) REFERENCES `OrderEntity` (`id`);");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_202(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `OrderItem` CHANGE `quantity` `quantity` DECIMAL(10,4) NOT NULL;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_201(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_200(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `PaymentShopAccount` (`shop_id` bigint(20) NOT NULL, `payment_id` bigint(20) NOT NULL, `account_id` bigint(20) NOT NULL, `settlingAccount_id` bigint(20) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		
		session.executeNonSelectingSQL("ALTER TABLE `PaymentShopAccount` ADD PRIMARY KEY (`shop_id`, `payment_id`);");

		session.executeNonSelectingSQL("ALTER TABLE `PaymentShopAccount` ADD CONSTRAINT `FK_PaymentShopAccount_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentShopAccount` ADD CONSTRAINT `FK_PaymentShopAccount_payment_id` FOREIGN KEY (`payment_id`) REFERENCES `Payment` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentShopAccount` ADD CONSTRAINT `FK_PaymentShopAccount_account_id` FOREIGN KEY (`account_id`) REFERENCES `PaymentAccount` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentShopAccount` ADD CONSTRAINT `FK_PaymentShopAccount_settlingAccount_id` FOREIGN KEY (`settlingAccount_id`) REFERENCES `PaymentAccount` (`id`);");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_199(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");

		session.executeNonSelectingSQL("ALTER TABLE `Payment` DROP FOREIGN KEY `FK_Payment_account_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `Payment` DROP FOREIGN KEY `FK_Payment_settlingAccount_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `Payment` DROP `account_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `Payment` DROP `settlingAccount_id`;");

		session.executeNonSelectingSQL("TRUNCATE `PaymentAccount`;");
		session.executeNonSelectingSQL("TRUNCATE `InvoiceItem`;");
		session.executeNonSelectingSQL("TRUNCATE `Invoice`;");
		
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");
	}

	
	@SuppressWarnings("unused")
	private void startUpdate_198(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `PaymentAccountTransaction` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, "
				+ "`amount` DECIMAL(10,4) NOT NULL, "
				+ "`sourceAccount_id` BIGINT(20) NOT NULL, "
				+ "`destinationAccount_id` BIGINT(20) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");

		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccountTransaction` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccountTransaction` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccountTransaction` ADD CONSTRAINT `FK_PaymentAccountTrans_srcAccount_id` FOREIGN KEY (`sourceAccount_id`) REFERENCES `PaymentAccount` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccountTransaction` ADD CONSTRAINT `FK_PaymentAccountTrans_dstAccount_id` FOREIGN KEY (`destinationAccount_id`) REFERENCES `PaymentAccount` (`id`);");
	}

	
	@SuppressWarnings("unused")
	private void startUpdate_197(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` CHANGE `customerAccount_id` `customerAccount_id` BIGINT(20) NOT NULL;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_196(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (9000, NOW(), '0', NOW(), 'Fremd Kunden Konto');");

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `payment_id` BIGINT(20) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD CONSTRAINT `FK_Invoice_payment_id` FOREIGN KEY (`payment_id`) REFERENCES `Payment` (`id`);");
		
	}
	

	@SuppressWarnings("unused")
	private void startUpdate_195(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");
		
		session.executeNonSelectingSQL("TRUNCATE `PaymentAccount`;");
		
		session.executeNonSelectingSQL("TRUNCATE `Payment`;");

		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (1, NOW(), '0', NOW(), 'BAR Konto');");
		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (2, NOW(), '0', NOW(), 'BAR Ausgleichskonto');");
		
		session.executeNonSelectingSQL("INSERT INTO `Payment` (`id`, `creationDate`, `deleted`, `updateDate`, `effectiveDate`, `enabled`, `expiryDate`, `name`, `description`, `systemPayment`, `direct`, `invoiceText`, `account_id`, `settlingAccount_id`) "
				+ "VALUES (1, NOW(), '0', NOW(), NOW(), '1', NULL, 'BAR', 'BAR', '1', '1', '', '1', '2');");


		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (3, NOW(), '0', NOW(), 'Rechnungs Konto');");
		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (4, NOW(), '0', NOW(), 'Rechnungs Ausgleichskonto');");
		
		session.executeNonSelectingSQL("INSERT INTO `Payment` (`id`, `creationDate`, `deleted`, `updateDate`, `effectiveDate`, `enabled`, `expiryDate`, `name`, `description`, `systemPayment`, `direct`, `invoiceText`, `account_id`, `settlingAccount_id`) "
				+ "VALUES (2, NOW(), '0', NOW(), NOW(), '1', NULL, 'Rechnung', 'Rechnung', '1', '0', '', '3', '4');");


		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (5, NOW(), '0', NOW(), 'EC Konto');");
		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (6, NOW(), '0', NOW(), 'EC Ausgleichskonto');");
		
		session.executeNonSelectingSQL("INSERT INTO `Payment` (`id`, `creationDate`, `deleted`, `updateDate`, `effectiveDate`, `enabled`, `expiryDate`, `name`, `description`, `systemPayment`, `direct`, `invoiceText`, `account_id`, `settlingAccount_id`) "
				+ "VALUES (3, NOW(), '0', NOW(), NOW(), '1', NULL, 'EC', 'EC', '1', '1', '', '5', '6');");


		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (7, NOW(), '0', NOW(), 'CC Konto');");
		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (8, NOW(), '0', NOW(), 'CC Ausgleichskonto');");
		
		session.executeNonSelectingSQL("INSERT INTO `Payment` (`id`, `creationDate`, `deleted`, `updateDate`, `effectiveDate`, `enabled`, `expiryDate`, `name`, `description`, `systemPayment`, `direct`, `invoiceText`, `account_id`, `settlingAccount_id`) "
				+ "VALUES (4, NOW(), '0', NOW(), NOW(), '1', NULL, 'CC', 'CC', '1', '1', '', '7', '8');");


		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (9, NOW(), '0', NOW(), 'PayPal Konto');");
		session.executeNonSelectingSQL("INSERT INTO `PaymentAccount` (`id`, `creationDate`, `deleted`, `updateDate`, `name`) VALUES (10, NOW(), '0', NOW(), 'PayPal Ausgleichskonto');");
		
		session.executeNonSelectingSQL("INSERT INTO `Payment` (`id`, `creationDate`, `deleted`, `updateDate`, `effectiveDate`, `enabled`, `expiryDate`, `name`, `description`, `systemPayment`, `direct`, `invoiceText`, `account_id`, `settlingAccount_id`) "
				+ "VALUES (5, NOW(), '0', NOW(), NOW(), '1', NULL, 'PayPal', 'PayPal', '1', '1', '', '9', '10');");

		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_194(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `customerAccount_id` BIGINT(20) NULL;");
		
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD CONSTRAINT `FK_Invoice_customerAccount_id` FOREIGN KEY (`customerAccount_id`) REFERENCES `PaymentAccount` (`id`);");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_193(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD `account_id` BIGINT(20) NULL;");
		
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD CONSTRAINT `FK_Customer_account_id` FOREIGN KEY (`account_id`) REFERENCES `PaymentAccount` (`id`);");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_192(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `PaymentAccount` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, "
				+ "`name` VARCHAR(255) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		
		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccount` ADD PRIMARY KEY (`id`), ADD KEY `deleted` (`deleted`);");
		session.executeNonSelectingSQL("ALTER TABLE `PaymentAccount` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10000;");


		session.executeNonSelectingSQL("CREATE TABLE `Payment` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, "
				+ "`effectiveDate` datetime NULL, `enabled` tinyint(1) NOT NULL, `expiryDate` datetime NULL, "
				+ "`name` VARCHAR(255) NOT NULL, "
				+ "`description` VARCHAR(255) NOT NULL, "
				+ "`systemPayment` TINYINT(1) NOT NULL, "
				+ "`direct` TINYINT(1) NOT NULL, "
				+ "`invoiceText` TEXT NOT NULL, "
				+ "`account_id` BIGINT(20) NOT NULL, "
				+ "`settlingAccount_id` BIGINT(20) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		
		session.executeNonSelectingSQL("ALTER TABLE `Payment` ADD PRIMARY KEY (`id`), ADD KEY `deleted` (`deleted`);");
		session.executeNonSelectingSQL("ALTER TABLE `Payment` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");
		
		session.executeNonSelectingSQL("ALTER TABLE `Payment` ADD CONSTRAINT `FK_Payment_account_id` FOREIGN KEY (`account_id`) REFERENCES `PaymentAccount` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `Payment` ADD CONSTRAINT `FK_Payment_settlingAccount_id` FOREIGN KEY (`settlingAccount_id`) REFERENCES `PaymentAccount` (`id`);");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_191(UnitOfWork session) throws Exception{
	
		session.executeNonSelectingSQL(
		"INSERT INTO `Country` (`creationDate`, `deleted`, `updateDate`, `name`, `shortName`, `longName`, `nationality`, `country`, `iso31662`, `iso31663`, `bevCode`, `defaultSelection`) VALUES " +
		"(NOW(), '0', NOW(), 'Afghanistan', 'Afghanistan', 'die Islamische Republik Afghanistan', 'afghanisch', 'ASI', 'AF', 'AFG', '423', '0'), " +
		"(NOW(), '0', NOW(), 'Ägypten', 'Ägypten', 'die Arabische Republik Ägypten', 'ägyptisch', 'AFR', 'EG', 'EGY', '287', '0'), " +
		"(NOW(), '0', NOW(), 'Albanien', 'Albanien', 'die Republik Albanien', 'albanisch', 'EUR', 'AL', 'ALB', '121', '0'), " +
		"(NOW(), '0', NOW(), 'Algerien', 'Algerien', 'die Demokratische Volksrepublik Algerien', 'algerisch', 'AFR', 'DZ', 'DZA', '221', '0'), " +
		"(NOW(), '0', NOW(), 'Andorra', 'Andorra', 'das Fürstentum Andorra', 'andorranisch', 'EUR', 'AD', 'AND', '123', '0'), " +
		"(NOW(), '0', NOW(), 'Angola', 'Angola', 'die Republik Angola', 'angolanisch', 'AFR', 'AO', 'AGO', '223', '0'), " +
		"(NOW(), '0', NOW(), 'Antigua und Barbuda', 'Antigua und Barbuda', 'Antigua und Barbuda', 'antiguanisch', 'AME', 'AG', 'ATG', '320', '0'), " +
		"(NOW(), '0', NOW(), 'Äquatorialguinea', 'Äquatorialguinea', 'die Republik Äquatorialguinea', 'äquatorialguineisch', 'AFR', 'GQ', 'GNQ', '274', '0'), " +
		"(NOW(), '0', NOW(), 'Argentinien', 'Argentinien', 'die Argentinische Republik', 'argentinisch', 'AME', 'AR', 'ARG', '323', '0'), " +
		"(NOW(), '0', NOW(), 'Armenien', 'Armenien', 'die Republik Armenien', 'armenisch', 'ASI', 'AM', 'ARM', '422', '0'), " +
		"(NOW(), '0', NOW(), 'Aserbaidschan', 'Aserbaidschan', 'die Republik Aserbaidschan', 'aserbaidschanisch', 'ASI', 'AZ', 'AZE', '425', '0'), " +
		"(NOW(), '0', NOW(), 'Äthiopien', 'Äthiopien', 'die Demokratische Bundesrepublik Äthiopien', 'äthiopisch', 'AFR', 'ET', 'ETH', '225', '0'), " +
		"(NOW(), '0', NOW(), 'Australien', 'Australien', 'Australien', 'australisch', 'AOA', 'AU', 'AUS', '523', '0'), " +
		"(NOW(), '0', NOW(), 'Bahamas', 'Bahamas', 'das Commonwealth der Bahamas', 'bahamaisch', 'AME', 'BS', 'BHS', '324', '0'), " +
		"(NOW(), '0', NOW(), 'Bahrain', 'Bahrain', 'das Königreich Bahrain', 'bahrainisch', 'ASI', 'BH', 'BHR', '424', '0'), " +
		"(NOW(), '0', NOW(), 'Bangladesch', 'Bangladesch', 'die Volksrepublik Bangladesch', 'bangladeschisch', 'ASI', 'BD', 'BGD', '460', '0'), " +
		"(NOW(), '0', NOW(), 'Barbados', 'Barbados', 'Barbados', 'barbadisch', 'AME', 'BB', 'BRB', '322', '0'), " +
		"(NOW(), '0', NOW(), 'Belgien', 'Belgien', 'das Königreich Belgien', 'belgisch', 'EUR', 'BE', 'BEL', '124', '0'), " +
		"(NOW(), '0', NOW(), 'Belize', 'Belize', 'Belize', 'belizisch', 'AME', 'BZ', 'BLZ', '330', '0'), " +
		"(NOW(), '0', NOW(), 'Benin', 'Benin', 'die Republik Benin', 'beninisch', 'AFR', 'BJ', 'BEN', '229', '0'), " +
		"(NOW(), '0', NOW(), 'Bhutan', 'Bhutan', 'das Königreich Bhutan', 'bhutanisch', 'ASI', 'BT', 'BTN', '426', '0'), " +
		"(NOW(), '0', NOW(), 'Bolivien', 'Plurinationaler Staat Bolivien', 'der Plurinationale Staat Bolivien', 'bolivianisch', 'AME', 'BO', 'BOL', '326', '0'), " +
		"(NOW(), '0', NOW(), 'Bosnien und Herzegowina', 'Bosnien und Herzegowina', 'Bosnien und Herzegowina', 'bosnisch-herzegowinisch', 'EUR', 'BA', 'BIH', '122', '0'), " +
		"(NOW(), '0', NOW(), 'Botsuana', 'Botsuana', 'die Republik Botsuana', 'botsuanisch', 'AFR', 'BW', 'BWA', '227', '0'), " +
		"(NOW(), '0', NOW(), 'Brasilien', 'Brasilien', 'die Föderative Republik Brasilien', 'brasilianisch', 'AME', 'BR', 'BRA', '327', '0'), " +
		"(NOW(), '0', NOW(), 'Brunei Darussalam', 'Brunei Darussalam', 'Brunei Darussalam', 'bruneiisch', 'ASI', 'BN', 'BRN', '429', '0'), " +
		"(NOW(), '0', NOW(), 'Bulgarien', 'Bulgarien', 'die Republik Bulgarien', 'bulgarisch', 'EUR', 'BG', 'BGR', '125', '0'), " +
		"(NOW(), '0', NOW(), 'Burkina Faso', 'Burkina Faso', 'Burkina Faso', 'burkinisch', 'AFR', 'BF', 'BFA', '258', '0'), " +
		"(NOW(), '0', NOW(), 'Burundi', 'Burundi', 'die Republik Burundi', 'burundisch', 'AFR', 'BI', 'BDI', '291', '0'), " +
		"(NOW(), '0', NOW(), 'Chile', 'Chile', 'die Republik Chile', 'chilenisch', 'AME', 'CL', 'CHL', '332', '0'), " +
		"(NOW(), '0', NOW(), 'China', 'China', 'die Volksrepublik China', 'chinesisch', 'ASI', 'CN', 'CHN', '479', '0'), " +
		"(NOW(), '0', NOW(), 'Cookinseln', 'Cookinseln', 'die Cookinseln', 'neuseeländisch', 'AOA', 'CK', 'COK', '527', '0'), " +
		"(NOW(), '0', NOW(), 'Costa Rica', 'Costa Rica', 'die Republik Costa Rica', 'costa-ricanisch', 'AME', 'CR', 'CRI', '334', '0'), " +
		"(NOW(), '0', NOW(), 'Côte d\\'Ivoire', 'Côte d\\'Ivoire', 'die Republik Côte d\\'Ivoire', 'ivorisch', 'AFR', 'CI', 'CIV', '231', '0'), " +
		"(NOW(), '0', NOW(), 'Dänemark', 'Dänemark', 'das Königreich Dänemark', 'dänisch', 'EUR', 'DK', 'DNK', '126', '0'), " +
		"(NOW(), '0', NOW(), 'Deutschland', 'Deutschland', 'die Bundesrepublik Deutschland', 'deutsch', 'EUR', 'DE', 'DEU', '000', '1'), " +
		"(NOW(), '0', NOW(), 'Dominica', 'Dominica', 'das Commonwealth Dominica', 'dominicanisch', 'AME', 'DM', 'DMA', '333', '0'), " +
		"(NOW(), '0', NOW(), 'Dominikanische Republik', 'Dominikanische Republik', 'die Dominikanische Republik', 'dominikanisch', 'AME', 'DO', 'DOM', '335', '0'), " +
		"(NOW(), '0', NOW(), 'Dschibuti', 'Dschibuti', 'die Republik Dschibuti', 'dschibutisch', 'AFR', 'DJ', 'DJI', '230', '0'), " +
		"(NOW(), '0', NOW(), 'Ecuador', 'Ecuador', 'die Republik Ecuador', 'ecuadorianisch', 'AME', 'EC', 'ECU', '336', '0'), " +
		"(NOW(), '0', NOW(), 'El Salvador', 'El Salvador', 'die Republik El Salvador', 'salvadorianisch', 'AME', 'SV', 'SLV', '337', '0'), " +
		"(NOW(), '0', NOW(), 'Eritrea', 'Eritrea', 'der Staat Eritrea', 'eritreisch', 'AFR', 'ER', 'ERI', '224', '0'), " +
		"(NOW(), '0', NOW(), 'Estland', 'Estland', 'die Republik Estland', 'estnisch', 'EUR', 'EE', 'EST', '127', '0'), " +
		"(NOW(), '0', NOW(), 'Fidschi', 'Fidschi', 'die Republik Fidschi', 'fidschianisch', 'AOA', 'FJ', 'FJI', '526', '0'), " +
		"(NOW(), '0', NOW(), 'Finnland', 'Finnland', 'die Republik Finnland', 'finnisch', 'EUR', 'FI', 'FIN', '128', '0'), " +
		"(NOW(), '0', NOW(), 'Frankreich', 'Frankreich', 'die Französische Republik', 'französisch', 'EUR', 'FR', 'FRA', '129', '0'), " +
		"(NOW(), '0', NOW(), 'Gabun', 'Gabun', 'die Gabunische Republik', 'gabunisch', 'AFR', 'GA', 'GAB', '236', '0'), " +
		"(NOW(), '0', NOW(), 'Gambia', 'Gambia', 'die Republik Gambia', 'gambisch', 'AFR', 'GM', 'GMB', '237', '0'), " +
		"(NOW(), '0', NOW(), 'Georgien', 'Georgien', 'Georgien', 'georgisch', 'ASI', 'GE', 'GEO', '430', '0'), " +
		"(NOW(), '0', NOW(), 'Ghana', 'Ghana', 'die Republik Ghana', 'ghanaisch', 'AFR', 'GH', 'GHA', '238', '0'), " +
		"(NOW(), '0', NOW(), 'Grenada', 'Grenada', 'Grenada', 'grenadisch', 'AME', 'GD', 'GRD', '340', '0'), " +
		"(NOW(), '0', NOW(), 'Griechenland', 'Griechenland', 'die Hellenische Republik', 'griechisch', 'EUR', 'GR', 'GRC', '134', '0'), " +
		"(NOW(), '0', NOW(), 'Guatemala', 'Guatemala', 'die Republik Guatemala', 'guatemaltekisch', 'AME', 'GT', 'GTM', '345', '0'), " +
		"(NOW(), '0', NOW(), 'Guinea', 'Guinea', 'die Republik Guinea', 'guineisch', 'AFR', 'GN', 'GIN', '261', '0'), " +
		"(NOW(), '0', NOW(), 'Guinea-Bissau', 'Guinea-Bissau', 'die Republik Guinea-Bissau', 'guinea-bissauisch', 'AFR', 'GW', 'GNB', '259', '0'), " +
		"(NOW(), '0', NOW(), 'Guyana', 'Guyana', 'die Kooperative Republik Guyana', 'guyanisch', 'AME', 'GY', 'GUY', '328', '0'), " +
		"(NOW(), '0', NOW(), 'Haiti', 'Haiti', 'die Republik Haiti', 'haitianisch', 'AME', 'HT', 'HTI', '346', '0'), " +
		"(NOW(), '0', NOW(), 'Honduras', 'Honduras', 'die Republik Honduras', 'honduranisch', 'AME', 'HN', 'HND', '347', '0'), " +
		"(NOW(), '0', NOW(), 'Indien', 'Indien', 'die Republik Indien', 'indisch', 'ASI', 'IN', 'IND', '436', '0'), " +
		"(NOW(), '0', NOW(), 'Indonesien', 'Indonesien', 'die Republik Indonesien', 'indonesisch', 'ASI', 'ID', 'IDN', '437', '0'), " +
		"(NOW(), '0', NOW(), 'Irak', 'Irak', 'die Republik Irak', 'irakisch', 'ASI', 'IQ', 'IRQ', '438', '0'), " +
		"(NOW(), '0', NOW(), 'Iran', 'Islamische Republik Iran', 'die Islamische Republik Iran', 'iranisch', 'ASI', 'IR', 'IRN', '439', '0'), " +
		"(NOW(), '0', NOW(), 'Irland', 'Irland', 'Irland', 'irisch', 'EUR', 'IE', 'IRL', '135', '0'), " +
		"(NOW(), '0', NOW(), 'Island', 'Island', 'die Republik Island', 'isländisch', 'EUR', 'IS', 'ISL', '136', '0'), " +
		"(NOW(), '0', NOW(), 'Israel', 'Israel', 'der Staat Israel', 'israelisch', 'ASI', 'IL', 'ISR', '441', '0'), " +
		"(NOW(), '0', NOW(), 'Italien', 'Italien', 'die Italienische Republik', 'italienisch', 'EUR', 'IT', 'ITA', '137', '0'), " +
		"(NOW(), '0', NOW(), 'Jamaika', 'Jamaika', 'Jamaika', 'jamaikanisch', 'AME', 'JM', 'JAM', '355', '0'), " +
		"(NOW(), '0', NOW(), 'Japan', 'Japan', 'Japan', 'japanisch', 'ASI', 'JP', 'JPN', '442', '0'), " +
		"(NOW(), '0', NOW(), 'Jemen', 'Jemen', 'die Republik Jemen', 'jemenitisch', 'ASI', 'YE', 'YEM', '421', '0'), " +
		"(NOW(), '0', NOW(), 'Jordanien', 'Jordanien', 'das Haschemitische Königreich Jordanien', 'jordanisch', 'ASI', 'JO', 'JOR', '445', '0'), " +
		"(NOW(), '0', NOW(), 'Kambodscha', 'Kambodscha', 'Kambodscha', 'kambodschanisch', 'ASI', 'KH', 'KHM', '446', '0'), " +
		"(NOW(), '0', NOW(), 'Kamerun', 'Kamerun', 'die Republik Kamerun', 'kamerunisch', 'AFR', 'CM', 'CMR', '262', '0'), " +
		"(NOW(), '0', NOW(), 'Kanada', 'Kanada', 'Kanada', 'kanadisch', 'AME', 'CA', 'CAN', '348', '0'), " +
		"(NOW(), '0', NOW(), 'Kap Verde', 'Kap Verde', 'die Republik Kap Verde', 'kap-verdisch', 'AFR', 'CV', 'CPV', '242', '0'), " +
		"(NOW(), '0', NOW(), 'Kasachstan', 'Kasachstan', 'die Republik Kasachstan', 'kasachisch', 'ASI', 'KZ', 'KAZ', '444', '0'), " +
		"(NOW(), '0', NOW(), 'Katar', 'Katar', 'der Staat Katar', 'katarisch', 'ASI', 'QA', 'QAT', '447', '0'), " +
		"(NOW(), '0', NOW(), 'Kenia', 'Kenia', 'die Republik Kenia', 'kenianisch', 'AFR', 'KE', 'KEN', '243', '0'), " +
		"(NOW(), '0', NOW(), 'Kirgisistan', 'Kirgisistan', 'die Kirgisische Republik', 'kirgisisch', 'ASI', 'KG', 'KGZ', '450', '0'), " +
		"(NOW(), '0', NOW(), 'Kiribati', 'Kiribati', 'die Republik Kiribati', 'kiribatisch', 'AOA', 'KI', 'KIR', '530', '0'), " +
		"(NOW(), '0', NOW(), 'Kolumbien', 'Kolumbien', 'die Republik Kolumbien', 'kolumbianisch', 'AME', 'CO', 'COL', '349', '0'), " +
		"(NOW(), '0', NOW(), 'Komoren', 'Komoren', 'die Union der Komoren', 'komorisch', 'AFR', 'KM', 'COM', '244', '0'), " +
		"(NOW(), '0', NOW(), 'Kongo', 'Kongo', 'die Republik Kongo', 'kongolesisch', 'AFR', 'CG', 'COG', '245', '0'), " +
		"(NOW(), '0', NOW(), 'Kongo, Demokratische Republik', 'Demokratische Republik Kongo', 'die Demokratische Republik Kongo', 'der Demokratischen Republik Kongo', 'AFR', 'CD', 'COD', '246', '0'), " +
		"(NOW(), '0', NOW(), 'Korea, Demokratische Volksrepublik', 'Demokratische Volksrepublik Korea', 'die Demokratische Volksrepublik Korea', 'der Demokratischen Volksrepublik Korea', 'ASI', 'KP', 'PRK', '434', '0'), " +
		"(NOW(), '0', NOW(), 'Korea, Republik', 'Republik Korea', 'die Republik Korea', 'der Republik Korea', 'ASI', 'KR', 'KOR', '467', '0'), " +
		"(NOW(), '0', NOW(), 'Kosovo', 'Kosovo', 'die Republik Kosovo', 'kosovarisch', 'EUR', 'XK', 'XXK', '150', '0'), " +
		"(NOW(), '0', NOW(), 'Kroatien', 'Kroatien', 'die Republik Kroatien', 'kroatisch', 'EUR', 'HR', 'HRV', '130', '0'), " +
		"(NOW(), '0', NOW(), 'Kuba', 'Kuba', 'die Republik Kuba', 'kubanisch', 'AME', 'CU', 'CUB', '351', '0'), " +
		"(NOW(), '0', NOW(), 'Kuwait', 'Kuwait', 'der Staat Kuwait', 'kuwaitisch', 'ASI', 'KW', 'KWT', '448', '0'), " +
		"(NOW(), '0', NOW(), 'Laos', 'Demokratische Volksrepublik Laos', 'die Demokratische Volksrepublik Laos', 'laotisch', 'ASI', 'LA', 'LAO', '449', '0'), " +
		"(NOW(), '0', NOW(), 'Lesotho', 'Lesotho', 'das Königreich Lesotho', 'lesothisch', 'AFR', 'LS', 'LSO', '226', '0'), " +
		"(NOW(), '0', NOW(), 'Lettland', 'Lettland', 'die Republik Lettland', 'lettisch', 'EUR', 'LV', 'LVA', '139', '0'), " +
		"(NOW(), '0', NOW(), 'Libanon', 'Libanon', 'die Libanesische Republik', 'libanesisch', 'ASI', 'LB', 'LBN', '451', '0'), " +
		"(NOW(), '0', NOW(), 'Liberia', 'Liberia', 'die Republik Liberia', 'liberianisch', 'AFR', 'LR', 'LBR', '247', '0'), " +
		"(NOW(), '0', NOW(), 'Libyen', 'Libyen', 'Libyen', 'libysch', 'AFR', 'LY', 'LBY', '248', '0'), " +
		"(NOW(), '0', NOW(), 'Liechtenstein', 'Liechtenstein', 'das Fürstentum Liechtenstein', 'liechtensteinisch', 'EUR', 'LI', 'LIE', '141', '0'), " +
		"(NOW(), '0', NOW(), 'Litauen', 'Litauen', 'die Republik Litauen', 'litauisch', 'EUR', 'LT', 'LTU', '142', '0'), " +
		"(NOW(), '0', NOW(), 'Luxemburg', 'Luxemburg', 'das Großherzogtum Luxemburg', 'luxemburgisch', 'EUR', 'LU', 'LUX', '143', '0'), " +
		"(NOW(), '0', NOW(), 'Madagaskar', 'Madagaskar', 'die Republik Madagaskar', 'madagassisch', 'AFR', 'MG', 'MDG', '249', '0'), " +
		"(NOW(), '0', NOW(), 'Malawi', 'Malawi', 'die Republik Malawi', 'malawisch', 'AFR', 'MW', 'MWI', '256', '0'), " +
		"(NOW(), '0', NOW(), 'Malaysia', 'Malaysia', 'Malaysia', 'malaysisch', 'ASI', 'MY', 'MYS', '482', '0'), " +
		"(NOW(), '0', NOW(), 'Malediven', 'Malediven', 'die Republik Malediven', 'maledivisch', 'ASI', 'MV', 'MDV', '454', '0'), " +
		"(NOW(), '0', NOW(), 'Mali', 'Mali', 'die Republik Mali', 'malisch', 'AFR', 'ML', 'MLI', '251', '0'), " +
		"(NOW(), '0', NOW(), 'Malta', 'Malta', 'die Republik Malta', 'maltesisch', 'EUR', 'MT', 'MLT', '145', '0'), " +
		"(NOW(), '0', NOW(), 'Marokko', 'Marokko', 'das Königreich Marokko', 'marokkanisch', 'AFR', 'MA', 'MAR', '252', '0'), " +
		"(NOW(), '0', NOW(), 'Marshallinseln', 'Marshallinseln', 'die Republik Marshallinseln', 'marshallisch', 'AOA', 'MH', 'MHL', '544', '0'), " +
		"(NOW(), '0', NOW(), 'Mauretanien', 'Mauretanien', 'die Islamische Republik Mauretanien', 'mauretanisch', 'AFR', 'MR', 'MRT', '239', '0'), " +
		"(NOW(), '0', NOW(), 'Mauritius', 'Mauritius', 'die Republik Mauritius', 'mauritisch', 'AFR', 'MU', 'MUS', '253', '0'), " +
		"(NOW(), '0', NOW(), 'Mazedonien', 'ehemalige jugoslawische Republik Mazedonien', 'die ehemalige jugoslawische Republik Mazedonien', 'mazedonisch', 'EUR', 'MK', 'MKD', '144', '0'), " +
		"(NOW(), '0', NOW(), 'Mexiko', 'Mexiko', 'die Vereinigten Mexikanischen Staaten', 'mexikanisch', 'AME', 'MX', 'MEX', '353', '0'), " +
		"(NOW(), '0', NOW(), 'Mikronesien', 'Föderierte Staaten von Mikronesien', 'die Föderierten Staaten von Mikronesien', 'mikronesisch', 'AOA', 'FM', 'FSM', '545', '0'), " +
		"(NOW(), '0', NOW(), 'Moldau', 'Republik Moldau', 'die Republik Moldau', 'moldauisch', 'EUR', 'MD', 'MDA', '146', '0'), " +
		"(NOW(), '0', NOW(), 'Monaco', 'Monaco', 'das Fürstentum Monaco', 'monegassisch', 'EUR', 'MC', 'MCO', '147', '0'), " +
		"(NOW(), '0', NOW(), 'Mongolei', 'Mongolei', 'die Mongolei', 'mongolisch', 'ASI', 'MN', 'MNG', '457', '0'), " +
		"(NOW(), '0', NOW(), 'Montenegro', 'Montenegro', 'Montenegro', 'montenegrinisch', 'EUR', 'ME', 'MNE', '140', '0'), " +
		"(NOW(), '0', NOW(), 'Mosambik', 'Mosambik', 'die Republik Mosambik', 'mosambikanisch', 'AFR', 'MZ', 'MOZ', '254', '0'), " +
		"(NOW(), '0', NOW(), 'Myanmar', 'Myanmar', 'Republik der Union Myanmar', 'myanmarisch', 'ASI', 'MM', 'MMR', '427', '0'), " +
		"(NOW(), '0', NOW(), 'Namibia', 'Namibia', 'die Republik Namibia', 'namibisch', 'AFR', 'NA', 'NAM', '267', '0'), " +
		"(NOW(), '0', NOW(), 'Nauru', 'Nauru', 'die Republik Nauru', 'nauruisch', 'AOA', 'NR', 'NRU', '531', '0'), " +
		"(NOW(), '0', NOW(), 'Nepal', 'Nepal', 'die Demokratische Bundesrepublik Nepal', 'nepalesisch', 'ASI', 'NP', 'NPL', '458', '0'), " +
		"(NOW(), '0', NOW(), 'Neuseeland', 'Neuseeland', 'Neuseeland', 'neuseeländisch', 'AOA', 'NZ', 'NZL', '536', '0'), " +
		"(NOW(), '0', NOW(), 'Nicaragua', 'Nicaragua', 'die Republik Nicaragua', 'nicaraguanisch', 'AME', 'NI', 'NIC', '354', '0'), " +
		"(NOW(), '0', NOW(), 'Niederlande', 'Niederlande', 'das Königreich der Niederlande', 'niederländisch', 'EUR', 'NL', 'NLD', '148', '0'), " +
		"(NOW(), '0', NOW(), 'Niger', 'Niger', 'die Republik Niger', 'nigrisch', 'AFR', 'NE', 'NER', '255', '0'), " +
		"(NOW(), '0', NOW(), 'Nigeria', 'Nigeria', 'die Bundesrepublik Nigeria', 'nigerianisch', 'AFR', 'NG', 'NGA', '232', '0'), " +
		"(NOW(), '0', NOW(), 'Niue', 'Niue', 'Niue', 'neuseeländisch', 'AOA', 'NU', 'NIU', '533', '0'), " +
		"(NOW(), '0', NOW(), 'Norwegen', 'Norwegen', 'das Königreich Norwegen', 'norwegisch', 'EUR', 'NO', 'NOR', '149', '0'), " +
		"(NOW(), '0', NOW(), 'Oman', 'Oman', 'das Sultanat Oman', 'omanisch', 'ASI', 'OM', 'OMN', '456', '0'), " +
		"(NOW(), '0', NOW(), 'Österreich', 'Österreich', 'die Republik Österreich', 'österreichisch', 'EUR', 'AT', 'AUT', '151', '0'), " +
		"(NOW(), '0', NOW(), 'Pakistan', 'Pakistan', 'die Islamische Republik Pakistan', 'pakistanisch', 'ASI', 'PK', 'PAK', '461', '0'), " +
		"(NOW(), '0', NOW(), 'Palau', 'Palau', 'die Republik Palau', 'palauisch', 'AOA', 'PW', 'PLW', '537', '0'), " +
		"(NOW(), '0', NOW(), 'Panama', 'Panama', 'die Republik Panama', 'panamaisch', 'AME', 'PA', 'PAN', '357', '0'), " +
		"(NOW(), '0', NOW(), 'Papua-Neuguinea', 'Papua-Neuguinea', 'der Unabhängige Staat Papua-Neuguinea', 'papua-neuguineisch', 'AOA', 'PG', 'PNG', '538', '0'), " +
		"(NOW(), '0', NOW(), 'Paraguay', 'Paraguay', 'die Republik Paraguay', 'paraguayisch', 'AME', 'PY', 'PRY', '359', '0'), " +
		"(NOW(), '0', NOW(), 'Peru', 'Peru', 'die Republik Peru', 'peruanisch', 'AME', 'PE', 'PER', '361', '0'), " +
		"(NOW(), '0', NOW(), 'Philippinen', 'Philippinen', 'die Republik der Philippinen', 'philippinisch', 'ASI', 'PH', 'PHL', '462', '0'), " +
		"(NOW(), '0', NOW(), 'Polen', 'Polen', 'die Republik Polen', 'polnisch', 'EUR', 'PL', 'POL', '152', '0'), " +
		"(NOW(), '0', NOW(), 'Portugal', 'Portugal', 'die Portugiesische Republik', 'portugiesisch', 'EUR', 'PT', 'PRT', '153', '0'), " +
		"(NOW(), '0', NOW(), 'Ruanda', 'Ruanda', 'die Republik Ruanda', 'ruandisch', 'AFR', 'RW', 'RWA', '265', '0'), " +
		"(NOW(), '0', NOW(), 'Rumänien', 'Rumänien', 'Rumänien', 'rumänisch', 'EUR', 'RO', 'ROU', '154', '0'), " +
		"(NOW(), '0', NOW(), 'Russische Föderation', 'Russische Föderation', 'die Russische Föderation', 'russisch', 'EUR', 'RU', 'RUS', '160', '0'), " +
		"(NOW(), '0', NOW(), 'Salomonen', 'Salomonen', 'die Salomonen', 'salomonisch', 'AOA', 'SB', 'SLB', '524', '0'), " +
		"(NOW(), '0', NOW(), 'Sambia', 'Sambia', 'die Republik Sambia', 'sambisch', 'AFR', 'ZM', 'ZMB', '257', '0'), " +
		"(NOW(), '0', NOW(), 'Samoa', 'Samoa', 'der Unabhängige Staat Samoa', 'samoanisch', 'AOA', 'WS', 'WSM', '543', '0'), " +
		"(NOW(), '0', NOW(), 'San Marino', 'San Marino', 'die Republik San Marino', 'san-marinesisch', 'EUR', 'SM', 'SMR', '156', '0'), " +
		"(NOW(), '0', NOW(), 'São Tomé und Príncipe', 'São Tomé und Príncipe', 'die Demokratische Republik São Tomé und Príncipe', 'são-toméisch', 'AFR', 'ST', 'STP', '268', '0'), " +
		"(NOW(), '0', NOW(), 'Saudi-Arabien', 'Saudi-Arabien', 'das Königreich Saudi-Arabien', 'saudi-arabisch', 'ASI', 'SA', 'SAU', '472', '0'), " +
		"(NOW(), '0', NOW(), 'Schweden', 'Schweden', 'das Königreich Schweden', 'schwedisch', 'EUR', 'SE', 'SWE', '157', '0'), " +
		"(NOW(), '0', NOW(), 'Schweiz', 'Schweiz', 'die Schweizerische Eidgenossenschaft', 'schweizerisch', 'EUR', 'CH', 'CHE', '158', '0'), " +
		"(NOW(), '0', NOW(), 'Senegal', 'Senegal', 'die Republik Senegal', 'senegalesisch', 'AFR', 'SN', 'SEN', '269', '0'), " +
		"(NOW(), '0', NOW(), 'Serbien', 'Serbien', 'die Republik Serbien', 'serbisch', 'EUR', 'RS', 'SRB', '170', '0'), " +
		"(NOW(), '0', NOW(), 'Seychellen', 'Seychellen', 'die Republik Seychellen', 'seychellisch', 'AFR', 'SC', 'SYC', '271', '0'), " +
		"(NOW(), '0', NOW(), 'Sierra Leone', 'Sierra Leone', 'die Republik Sierra Leone', 'sierra-leonisch', 'AFR', 'SL', 'SLE', '272', '0'), " +
		"(NOW(), '0', NOW(), 'Simbabwe', 'Simbabwe', 'die Republik Simbabwe', 'simbabwisch', 'AFR', 'ZW', 'ZWE', '233', '0'), " +
		"(NOW(), '0', NOW(), 'Singapur', 'Singapur', 'die Republik Singapur', 'singapurisch', 'ASI', 'SG', 'SGP', '474', '0'), " +
		"(NOW(), '0', NOW(), 'Slowakei', 'Slowakei', 'die Slowakische Republik', 'slowakisch', 'EUR', 'SK', 'SVK', '155', '0'), " +
		"(NOW(), '0', NOW(), 'Slowenien', 'Slowenien', 'die Republik Slowenien', 'slowenisch', 'EUR', 'SI', 'SVN', '131', '0'), " +
		"(NOW(), '0', NOW(), 'Somalia', 'Somalia', 'die Republik Somalia', 'somalisch', 'AFR', 'SO', 'SOM', '273', '0'), " +
		"(NOW(), '0', NOW(), 'Spanien', 'Spanien', 'das Königreich Spanien', 'spanisch', 'EUR', 'ES', 'ESP', '161', '0'), " +
		"(NOW(), '0', NOW(), 'Sri Lanka', 'Sri Lanka', 'die Demokratische Sozialistische Republik Sri Lanka', 'sri-lankisch', 'ASI', 'LK', 'LKA', '431', '0'), " +
		"(NOW(), '0', NOW(), 'St. Kitts und Nevis', 'St. Kitts und Nevis', 'die Föderation St. Kitts und Nevis', 'von St. Kitts und Nevis', 'AME', 'KN', 'KNA', '370', '0'), " +
		"(NOW(), '0', NOW(), 'St. Lucia', 'St. Lucia', 'St. Lucia', 'lucianisch', 'AME', 'LC', 'LCA', '366', '0'), " +
		"(NOW(), '0', NOW(), 'St. Vincent und die Grenadinen', 'St. Vincent und die Grenadinen', 'St. Vincent und die Grenadinen', 'vincentisch', 'AME', 'VC', 'VCT', '369', '0'), " +
		"(NOW(), '0', NOW(), 'Südafrika', 'Südafrika', 'die Republik Südafrika', 'südafrikanisch', 'AFR', 'ZA', 'ZAF', '263', '0'), " +
		"(NOW(), '0', NOW(), 'Sudan', 'Sudan', 'die Republik Sudan', 'sudanesisch', 'AFR', 'SD', 'SDN', '277', '0'), " +
		"(NOW(), '0', NOW(), 'Südsudan', 'Südsudan', 'die Republik Südsudan', 'südsudanesisch', 'AFR', 'SS', 'SSD', '278', '0'), " +
		"(NOW(), '0', NOW(), 'Suriname', 'Suriname', 'die Republik Suriname', 'surinamisch', 'AME', 'SR', 'SUR', '364', '0'), " +
		"(NOW(), '0', NOW(), 'Swasiland', 'Swasiland', 'das Königreich Swasiland', 'swasiländisch', 'AFR', 'SZ', 'SWZ', '281', '0'), " +
		"(NOW(), '0', NOW(), 'Syrien', 'Arabische Republik Syrien', 'die Arabische Republik Syrien', 'syrisch', 'ASI', 'SY', 'SYR', '475', '0'), " +
		"(NOW(), '0', NOW(), 'Tadschikistan', 'Tadschikistan', 'die Republik Tadschikistan', 'tadschikisch', 'ASI', 'TJ', 'TJK', '470', '0'), " +
		"(NOW(), '0', NOW(), 'Tansania', 'Vereinigte Republik Tansania', 'die Vereinigte Republik Tansania', 'tansanisch', 'AFR', 'TZ', 'TZA', '282', '0'), " +
		"(NOW(), '0', NOW(), 'Thailand', 'Thailand', 'das Königreich Thailand', 'thailändisch', 'ASI', 'TH', 'THA', '476', '0'), " +
		"(NOW(), '0', NOW(), 'Timor-Leste', 'Timor-Leste', 'die Demokratische Republik Timor-Leste', 'von Timor-Leste', 'ASI', 'TL', 'TLS', '483', '0'), " +
		"(NOW(), '0', NOW(), 'Togo', 'Togo', 'die Republik Togo', 'togoisch', 'AFR', 'TG', 'TGO', '283', '0'), " +
		"(NOW(), '0', NOW(), 'Tonga', 'Tonga', 'das Königreich Tonga', 'tongaisch', 'AOA', 'TO', 'TON', '541', '0'), " +
		"(NOW(), '0', NOW(), 'Trinidad und Tobago', 'Trinidad und Tobago', 'die Republik Trinidad und Tobago', 'von Trinidad und Tobago', 'AME', 'TT', 'TTO', '371', '0'), " +
		"(NOW(), '0', NOW(), 'Tschad', 'Tschad', 'die Republik Tschad', 'tschadisch', 'AFR', 'TD', 'TCD', '284', '0'), " +
		"(NOW(), '0', NOW(), 'Tschechische Republik', 'Tschechische Republik', 'die Tschechische Republik', 'tschechisch', 'EUR', 'CZ', 'CZE', '164', '0'), " +
		"(NOW(), '0', NOW(), 'Tunesien', 'Tunesien', 'die Tunesische Republik', 'tunesisch', 'AFR', 'TN', 'TUN', '285', '0'), " +
		"(NOW(), '0', NOW(), 'Türkei', 'Türkei', 'die Republik Türkei', 'türkisch', 'EUR', 'TR', 'TUR', '163', '0'), " +
		"(NOW(), '0', NOW(), 'Turkmenistan', 'Turkmenistan', 'Turkmenistan', 'turkmenisch', 'ASI', 'TM', 'TKM', '471', '0'), " +
		"(NOW(), '0', NOW(), 'Tuvalu', 'Tuvalu', 'Tuvalu', 'tuvaluisch', 'AOA', 'TV', 'TUV', '540', '0'), " +
		"(NOW(), '0', NOW(), 'Uganda', 'Uganda', 'die Republik Uganda', 'ugandisch', 'AFR', 'UG', 'UGA', '286', '0'), " +
		"(NOW(), '0', NOW(), 'Ukraine', 'Ukraine', 'die Ukraine', 'ukrainisch', 'EUR', 'UA', 'UKR', '166', '0'), " +
		"(NOW(), '0', NOW(), 'Ungarn', 'Ungarn', 'Ungarn', 'ungarisch', 'EUR', 'HU', 'HUN', '165', '0'), " +
		"(NOW(), '0', NOW(), 'Uruguay', 'Uruguay', 'die Republik Östlich des Uruguay', 'uruguayisch', 'AME', 'UY', 'URY', '365', '0'), " +
		"(NOW(), '0', NOW(), 'Usbekistan', 'Usbekistan', 'die Republik Usbekistan', 'usbekisch', 'ASI', 'UZ', 'UZB', '477', '0'), " +
		"(NOW(), '0', NOW(), 'Vanuatu', 'Vanuatu', 'die Republik Vanuatu', 'vanuatuisch', 'AOA', 'VU', 'VUT', '532', '0'), " +
		"(NOW(), '0', NOW(), 'Vatikanstadt', 'Vatikanstadt', 'der Staat Vatikanstadt', 'vatikanisch', 'EUR', 'VA', 'VAT', '167', '0'), " +
		"(NOW(), '0', NOW(), 'Venezuela', 'Bolivarische Republik Venezuela', 'die Bolivarische Republik Venezuela', 'venezolanisch', 'AME', 'VE', 'VEN', '367', '0'), " +
		"(NOW(), '0', NOW(), 'Vereinigte Arabische Emirate', 'Vereinigte Arabische Emirate', 'die Vereinigten Arabischen Emirate', 'der Vereinigten Arabischen Emirate', 'ASI', 'AE', 'ARE', '469', '0'), " +
		"(NOW(), '0', NOW(), 'Vereinigte Staaten', 'Vereinigten Staaten', 'die Vereinigten Staaten von Amerika', 'amerikanisch', 'AME', 'US', 'USA', '368', '0'), " +
		"(NOW(), '0', NOW(), 'Vereinigtes Königreich', 'Vereinigtes Königreich', 'das Vereinigte Königreich Großbritannien und Nordirland', 'britisch', 'EUR', 'GB', 'GBR', '168', '0'), " +
		"(NOW(), '0', NOW(), 'Vietnam', 'Vietnam', 'die Sozialistische Republik Vietnam', 'vietnamesisch', 'ASI', 'VN', 'VNM', '432', '0'), " +
		"(NOW(), '0', NOW(), 'Weißrussland', 'Weißrussland', 'die Republik Weißrussland', 'weißrussisch', 'EUR', 'BY', 'BLR', '169', '0'), " +
		"(NOW(), '0', NOW(), 'Zentralafrikanische Republik', 'Zentralafrikanische Republik', 'die Zentralafrikanische Republik', 'zentralafrikanisch', 'AFR', 'CF', 'CAF', '289', '0'), " +
		"(NOW(), '0', NOW(), 'Zypern', 'Zypern', 'die Republik Zypern', 'zyprisch', 'EUR', 'CY', 'CYP', '181', '0');");
	
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_190(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `Country` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, "
				+ "`name` VARCHAR(255) NOT NULL, "
				+ "`shortName` VARCHAR(255) NOT NULL, "
				+ "`longName` VARCHAR(255) NOT NULL, "
				+ "`nationality` VARCHAR(255) NOT NULL, "
				+ "`country` CHAR(3) NOT NULL, "
				+ "`iso31662` CHAR(2) NOT NULL, "
				+ "`iso31663` CHAR(3) NOT NULL, "
				+ "`bevCode` CHAR(3) NOT NULL, "
				+ "`defaultSelection` TINYINT(1) NOT NULL "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		
		session.executeNonSelectingSQL("ALTER TABLE `Country` ADD PRIMARY KEY (`id`), ADD KEY `defaultSelection` (`defaultSelection`), ADD KEY `deleted` (`deleted`), ADD KEY `nationality` (`nationality`);");
		session.executeNonSelectingSQL("ALTER TABLE `Country` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");
		
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD `nationality_id` bigint(20) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD CONSTRAINT `FK_Customer_nationality_id` FOREIGN KEY (`nationality_id`) REFERENCES `Country` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_189(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` ADD `subtitle` MEDIUMTEXT NULL AFTER `title`;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_188(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` CHANGE `quantity` `quantity` DECIMAL(10,4) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_187(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `pickslip_id` BIGINT(20) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD CONSTRAINT `FK_Invoice_pickslip_id` FOREIGN KEY (`pickslip_id`) REFERENCES `StockPickslip` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_186(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `drafterLine` VARCHAR(255) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `recipientLine1` VARCHAR(255) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `recipientLine2` VARCHAR(255) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `recipientLine3` VARCHAR(255) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `recipientLine4` VARCHAR(255) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD `recipientLine5` VARCHAR(255) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_185(UnitOfWork session) throws Exception{
	
		session.executeNonSelectingSQL("CREATE TABLE `Invoice` ( "+
			  "`id` bigint(20) NOT NULL, "+
			  "`number` bigint(20) NOT NULL, "+
			  "`introduction` varchar(255) DEFAULT NULL, "+
			  "`closing` varchar(255) DEFAULT NULL, "+
			  "`commission` decimal(10,4) NOT NULL, "+
			  "`points` decimal(10,4) NOT NULL, "+
			  "`price` decimal(10,4) NOT NULL, "+
			  "`tax` decimal(5,2) DEFAULT NULL, "+
			  "`vat` decimal(10,4) DEFAULT NULL "+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		
		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_InvoiceEntity_number` BEFORE INSERT ON `Invoice` "+
				"FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),100000) + 1 FROM `Invoice`)");
		 
		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `number` (`number`); ");

		session.executeNonSelectingSQL("ALTER TABLE `Invoice` ADD CONSTRAINT `FK_Invoice_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");
		
		session.executeNonSelectingSQL("CREATE TABLE `InvoiceItem` ( "+
				  "`id` bigint(20) NOT NULL, "+
				  "`creationDate` datetime NOT NULL, "+
				  "`updateDate` datetime NOT NULL, "+
				  "`deleted` tinyint(1) NOT NULL, "+
				  "`title` varchar(255) NOT NULL, "+
				  "`quantity` int(11) NOT NULL, "+
				  "`serial` varchar(255) DEFAULT NULL, "+
				  "`tax` decimal(5,2) DEFAULT NULL, "+
				  "`commission` decimal(10,4) NOT NULL, "+
				  "`points` decimal(10,4) NOT NULL, "+
				  "`price` decimal(10,4) NOT NULL, "+
				  "`product_id` bigint(20) DEFAULT NULL, "+
				  "`invoice_id` bigint(20) NOT NULL, "+
				  "`unitPurchasePrice` decimal(10,2) NOT NULL, "+
				  "`unitPrice` decimal(10,2) NOT NULL, "+
				  "`vat` decimal(10,4) DEFAULT NULL "+
				") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1; ");
		
		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` ADD PRIMARY KEY (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");

		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` ADD CONSTRAINT `FK_InvoiceItem_product_id` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `InvoiceItem` ADD CONSTRAINT `FK_InvoiceItem_invoice_id` FOREIGN KEY (`invoice_id`) REFERENCES `Invoice` (`id`);");
	}
	

	@SuppressWarnings("unused")
	private void startUpdate_184(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `OrderEntity` ADD `number` BIGINT NULL AFTER `id`;");
	 
		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_OrderEntity_tmp` BEFORE UPDATE ON `OrderEntity` "+
					"FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),200000) + 1 FROM `OrderEntity`);");
	 
	 
		session.executeNonSelectingSQL("UPDATE `OrderEntity` SET `number`= null;");

		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_OrderEntity_number` BEFORE INSERT ON `OrderEntity` "+
					"FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),200000) + 1 FROM `OrderEntity`);");
	 
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_OrderEntity_tmp`;");
	 
		session.executeNonSelectingSQL("ALTER TABLE `OrderEntity` CHANGE `number` `number` BIGINT(20) NOT NULL;");
	 
		session.executeNonSelectingSQL("ALTER TABLE `OrderEntity` ADD UNIQUE(`number`);");
	 
	}
	

	@SuppressWarnings("unused")
	private void startUpdate_183(UnitOfWork session) throws Exception{
	
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD `number` BIGINT NULL AFTER `id`;");
	 
		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_StockPickslip_tmp` BEFORE UPDATE ON `StockPickslip` "+
					"FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),300000) + 1 FROM `StockPickslip`);");
	 
	 
		session.executeNonSelectingSQL("UPDATE `StockPickslip` SET `number`= null;");

		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_StockPickslip_number` BEFORE INSERT ON `StockPickslip` "+
					"FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),300000) + 1 FROM `StockPickslip`);");
	 
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_StockPickslip_tmp`;");
	 
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` CHANGE `number` `number` BIGINT(20) NOT NULL;");
	 
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD UNIQUE(`number`);");
	 
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_182(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD `pickslipReference_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD CONSTRAINT `FK_StockPickslip_pickslipReference_id` FOREIGN KEY (`pickslipReference_id`) REFERENCES `StockPickslip` (`id`);");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_181(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `stockQuantity` DECIMAL(10,4) NOT NULL DEFAULT '0' AFTER `maxPurchasePrice`;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_180(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `minPurchasePrice` DECIMAL(10,4) NOT NULL DEFAULT '0' AFTER `avergePurchasePrice`;");
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `maxPurchasePrice` DECIMAL(10,4) NOT NULL DEFAULT '0' AFTER `avergePurchasePrice`;");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_179(UnitOfWork session) throws Exception{
		
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `avergePurchasePrice` DECIMAL(10,4) NOT NULL DEFAULT '0' AFTER `purchasePrice`;");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_178(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `ProductStockCache` (`product_id` bigint(20) NOT NULL, `stock_id` bigint(20) NOT NULL, `quantity` decimal(10,4) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStockCache` ADD PRIMARY KEY (`product_id`, `stock_id`)");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStockCache` ADD CONSTRAINT `FK_ProductStockCacheshop_product_id` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStockCache` ADD CONSTRAINT `FK_ProductStockCacheshop_stock_id` FOREIGN KEY (`stock_id`) REFERENCES `Stock` (`id`);");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_177(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");

		session.executeNonSelectingSQL("TRUNCATE StockMovement;");
		session.executeNonSelectingSQL("TRUNCATE StockPickslip;");
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");
		
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD `stock_id` BIGINT(20) NOT NULL AFTER `id`, ADD INDEX (`stock_id`) ;");
		
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD CONSTRAINT `FK_StockPickslip_stock_id` FOREIGN KEY (`stock_id`) REFERENCES `Stock` (`id`);");
		
	}
	

	@SuppressWarnings("unused")
	private void startUpdate_176(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `UserGroupPermission` CHANGE `command` `command` CHAR(50) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL;");
		
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_175(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD INDEX(`uuid`);");
		session.executeNonSelectingSQL("ALTER TABLE `Holiday` ADD INDEX(`day`);");
		
	}

	
	@SuppressWarnings("unused")
	private void startUpdate_174(UnitOfWork session) throws Exception{
	
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD INDEX(`expiryDate`);");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_173(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD INDEX(`name`); ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD INDEX(`expiryDate`); ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD INDEX(`effectiveDate`); ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD INDEX(`deleted`); ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD INDEX(`enabled`); ");

		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD INDEX(`updateDate`); ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_172(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD `accountManager_user_id` BIGINT(20) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD CONSTRAINT `FK_Customer_accountManager_user_id` FOREIGN KEY (`accountManager_user_id`) REFERENCES `User` (`id`); ");
	}


	@SuppressWarnings("unused")
	private void startUpdate_171(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `crosscanData_authID` VARCHAR(60) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `crosscanData_storeID` VARCHAR(10) NULL;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_170(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `CrosscanData` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `updateDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL,  `cssid` CHAR(10) NOT NULL,  `companyid` VARCHAR(255) NULL,  `storeno` INT(11) NOT NULL,  `company` VARCHAR(255) NOT NULL,  `time` INT(11) NOT NULL,  `eventtype` INT(2) NOT NULL,  `eventvalue` INT(2) NOT NULL,  `valuename` VARCHAR(100) NOT NULL,  `amount` INT(11) NOT NULL,  `storetitle` VARCHAR(255) NOT NULL,  `storetown` VARCHAR(255) NOT NULL,  `shop_id` BIGINT(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `CrosscanData` ADD PRIMARY KEY (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `CrosscanData` ADD KEY (`time`)");
		session.executeNonSelectingSQL("ALTER TABLE `CrosscanData` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");
		session.executeNonSelectingSQL("ALTER TABLE `CrosscanData` ADD CONSTRAINT `FK_CrosscanData_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `CrosscanData` ADD UNIQUE( `time`, `shop_id`);");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_169(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `Supplier` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL,`effectiveDate` datetime DEFAULT NULL,`enabled` tinyint(1) NOT NULL,`expiryDate` datetime DEFAULT NULL,`name` varchar(255) NOT NULL,`updateDate` datetime NOT NULL,`addressline` varchar(255) DEFAULT NULL,`city` varchar(255) DEFAULT NULL,`country` varchar(255) DEFAULT NULL,`street` varchar(255) DEFAULT NULL,`streetNumber` varchar(255) DEFAULT NULL,`zip` varchar(255) DEFAULT NULL,`deleted` tinyint(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `Supplier` ADD PRIMARY KEY (`id`), ADD KEY `enabled_deleted` (`enabled`,`deleted`);");
		session.executeNonSelectingSQL("ALTER TABLE `Supplier` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");
		


		session.executeNonSelectingSQL("CREATE TABLE `StockPickslip` ( `id` bigint(20) NOT NULL, `src_stock_id` BIGINT(20) NULL, `dst_stock_id` BIGINT(20) NULL, `supplier_id` BIGINT(20) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD CONSTRAINT `FK_StockPickslip_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD CONSTRAINT `FK_StockPickslip_src_stock_id` FOREIGN KEY (`src_stock_id`) REFERENCES `Stock` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD CONSTRAINT `FK_StockPickslip_dst_stock_id` FOREIGN KEY (`dst_stock_id`) REFERENCES `Stock` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `StockPickslip` ADD CONSTRAINT `FK_StockPickslip_supplier_id` FOREIGN KEY (`supplier_id`) REFERENCES `Supplier` (`id`);");
		
		
		session.executeNonSelectingSQL("CREATE TABLE `StockMovement` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL,`updateDate` datetime NOT NULL,`deleted` tinyint(1) NOT NULL,  `stockPickslip_id` BIGINT(20) NOT NULL,  `product_id` BIGINT(20) NOT NULL,  `quantity` DECIMAL(10,2) NOT NULL,  `serial` VARCHAR(100) NULL,  `purchasePrice` DECIMAL(10,2) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `StockMovement` ADD PRIMARY KEY (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `StockMovement` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("ALTER TABLE `StockMovement` ADD CONSTRAINT `FK_StockMovement_stockPickslip_id` FOREIGN KEY (`stockPickslip_id`) REFERENCES `StockPickslip` (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `StockMovement` ADD CONSTRAINT `FK_StockMovement_product_id` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`)");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_168(UnitOfWork session) throws Exception{
		
		Vector vShop = session.executeSQL("SELECT * FROM Shop;");
		Vector vID;
		String stockId;

		ArrayRecord rShop;
		ArrayRecord rID;

		if(vShop.size() > 0){

			for(int j=0; j<vShop.size(); j++){
				rShop = (ArrayRecord) vShop.get(j);
				String addressline = "NULL";
				String zip ="";
				String city = "";
				String country = "";
				String street = "";
				String streetNumber = "";

				if(rShop.get("addressline") != null)
					addressline = "'"+rShop.get("addressline").toString()+"'";
				
				// old mistake in form. now corrected
				if(rShop.get("city") != null)
					zip = rShop.get("city").toString();
				
				if(rShop.get("zip") != null)
					city = rShop.get("zip").toString();
				
				if(rShop.get("country") != null)
					country = rShop.get("country").toString();
				
				if(rShop.get("street") != null)
					street = rShop.get("street").toString();
				
				if(rShop.get("streetNumber") != null)
					streetNumber = rShop.get("streetNumber").toString();
				
				
				
				
				session.executeNonSelectingSQL("INSERT INTO `Stock` (`creationDate`, `effectiveDate`, `enabled`, `expiryDate`, `name`, `updateDate`, `addressline`, `city`, `country`, `street`, `streetNumber`, `zip`, `deleted`) "
							+ "VALUES (NOW(), NOW(), "+rShop.get("enabled").toString()+", NULL, 'Lager "+rShop.get("name").toString().replace("'", "")+"' ,NOW(), "+addressline+", '"+city+"', '"+country+"', '"+street+"', '"+streetNumber+"', '"+zip+"' , false);");
				
				vID = session.executeSQL("SELECT LAST_INSERT_ID() as id");
				rID = (ArrayRecord) vID.get(0);
				stockId = rID.get("id").toString();

				session.executeNonSelectingSQL("UPDATE `Shop` SET `zip` = '"+zip+"', `city` = '"+city+"', `stock_id` = "+stockId+" WHERE id = "+rShop.get("id").toString()+";");
				
				
			}
		}
		
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_167(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `stock_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD CONSTRAINT `FK_Shop_stock_id` FOREIGN KEY (`stock_id`) REFERENCES `Stock` (`id`)");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_166(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `Stock` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL,`effectiveDate` datetime DEFAULT NULL,`enabled` tinyint(1) NOT NULL,`expiryDate` datetime DEFAULT NULL,`name` varchar(255) NOT NULL,`updateDate` datetime NOT NULL,`addressline` varchar(255) DEFAULT NULL,`city` varchar(255) DEFAULT NULL,`country` varchar(255) DEFAULT NULL,`street` varchar(255) DEFAULT NULL,`streetNumber` varchar(255) DEFAULT NULL,`zip` varchar(255) DEFAULT NULL,`deleted` tinyint(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `Stock` ADD PRIMARY KEY (`id`), ADD KEY `enabled_deleted` (`enabled`,`deleted`);");
		session.executeNonSelectingSQL("ALTER TABLE `Stock` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_165(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategory` ADD `parentCategory_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategory` ADD CONSTRAINT `FK_ProductCategory_parentCategory_id` FOREIGN KEY (`parentCategory_id`) REFERENCES `ProductCategory` (`id`)");
	}

	@SuppressWarnings("unused")
	private void startUpdate_164(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ManualCommission` ADD PRIMARY KEY (`id`), ADD KEY (`creator_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ManualCommission` ADD CONSTRAINT `FK_ManualCommission_id` FOREIGN KEY (`id`) REFERENCES `CommissionActivity` (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `ManualCommission` ADD CONSTRAINT `FK_ManualCommission_creator_id` FOREIGN KEY (`creator_id`) REFERENCES `User` (`id`)");
	}

	@SuppressWarnings("unused")
	private void startUpdate_163(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("DROP TABLE IF EXISTS `ManualCommission`;");
		session.executeNonSelectingSQL("CREATE TABLE `ManualCommission` (`id` bigint(20) NOT NULL, `creator_id` bigint(20) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_162(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `calendarNote` VARCHAR(255) NULL;");
	}
	
	@SuppressWarnings("unused")
	private void startUpdate_161(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `VacationRequest` ( `id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `user_id` BIGINT(20) NOT NULL, `manager_id` BIGINT(20) NULL, `status` TINYINT(2) NOT NULL, `statusChangedDate` datetime NOT NULL, `creatorNote` TEXT NULL, `managerNote` TEXT NULL, `vacationFrom` datetime NOT NULL, `vacationTo` datetime NOT NULL, `event_id` BIGINT(20) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

		session.executeNonSelectingSQL("ALTER TABLE `VacationRequest` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `VacationRequest` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");
		session.executeNonSelectingSQL("ALTER TABLE `VacationRequest` ADD KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `VacationRequest` ADD KEY (`status`);");

		session.executeNonSelectingSQL("ALTER TABLE `VacationRequest` ADD CONSTRAINT `FK_VacationRequest_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `VacationRequest` ADD CONSTRAINT `FK_VacationRequest_manager_id` FOREIGN KEY (`manager_id`) REFERENCES `User` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `VacationRequest` ADD CONSTRAINT `FK_VacationRequest_event_id` FOREIGN KEY (`event_id`) REFERENCES `Event` (`id`);");
		
	}

	@SuppressWarnings("unused")
	private void startUpdate_160(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `State` ADD INDEX `name` (`name`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_159(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity` ADD `targetDate` datetime NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_158(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `TaskActivity` ( `id` bigint(20) NOT NULL, `toEveryone` TINYINT(1) NOT NULL, `completedDate` datetime NULL, `completedByUser_id` bigint(20) NULL, `title` VARCHAR(255) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity` ADD CONSTRAINT `FK_TaskActivity_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity` ADD CONSTRAINT `FK_TaskActivity_completedByUser_id` FOREIGN KEY (`completedByUser_id`) REFERENCES `User` (`id`);");
		
		session.executeNonSelectingSQL("CREATE TABLE `TaskActivity_UserGroupReceipientList` ( `taskActivity_id` bigint(20) NOT NULL, `userGroup_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("CREATE TABLE `TaskActivity_ShopReceipientList` ( `taskActivity_id` bigint(20) NOT NULL, `shop_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("CREATE TABLE `TaskActivity_UserReceipientList` ( `taskActivity_id` bigint(20) NOT NULL, `user_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity_UserGroupReceipientList` ADD CONSTRAINT `FK_Task_UserGroup_task_id` FOREIGN KEY (`taskActivity_id`) REFERENCES `TaskActivity` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity_UserGroupReceipientList` ADD CONSTRAINT `FK_Task_UserGroup_userGroup_id` FOREIGN KEY (`userGroup_id`) REFERENCES `UserGroup` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity_ShopReceipientList` ADD CONSTRAINT `FK_Task_Shop_task_id` FOREIGN KEY (`taskActivity_id`) REFERENCES `TaskActivity` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity_ShopReceipientList` ADD CONSTRAINT `FK_Task_Shop_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity_UserReceipientList` ADD CONSTRAINT `FK_Task_User_task_id` FOREIGN KEY (`taskActivity_id`) REFERENCES `TaskActivity` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `TaskActivity_UserReceipientList` ADD CONSTRAINT `FK_Task_User_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_157(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `state_id` bigint(20) NOT NULL DEFAULT 7; ");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD CONSTRAINT `FK_Shop_state` FOREIGN KEY (`state_id`) REFERENCES `State` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `state_id` bigint(20) NOT NULL DEFAULT 7; ");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD CONSTRAINT `FK_User_state` FOREIGN KEY (`state_id`) REFERENCES `State` (`id`);");
	}
	

	@SuppressWarnings("unused")
	private void startUpdate_156(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Holiday` MODIFY `day` CHAR(8) NOT NULL; ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_155(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `State` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `name` VARCHAR(50) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `State` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `State` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");

		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Baden-Württemberg'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Bayern'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Berlin'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Brandenburg'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Bremen'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Hamburg'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Hessen'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Mecklenburg-Vorpommern'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Niedersachsen'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Nordrhein-Westfalen'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Rheinland-Pfalz'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Saarland'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Sachsen'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Sachsen-Anhalt'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Schleswig-Holstein'); ");
		session.executeNonSelectingSQL("INSERT INTO `State` (`creationDate`, `deleted`, `updateDate`, `name`) VALUES (CURRENT_DATE(), '0', CURRENT_DATE(), 'Thüringen'); ");
		


		session.executeNonSelectingSQL("CREATE TABLE `Holiday` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `name` VARCHAR(50) NOT NULL, `day` INT(11) NOT NULL, `state_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `Holiday` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `Holiday` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");
		session.executeNonSelectingSQL("ALTER TABLE `Holiday` ADD CONSTRAINT `FK_Holiday_state` FOREIGN KEY (`state_id`) REFERENCES `State` (`id`);");
		
	}


	@SuppressWarnings("unused")
	private void startUpdate_154(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` MODIFY `relation` BIGINT(20) NULL; ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_153(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` ADD `relationClass` VARCHAR(255) NULL; ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_152(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` ADD `relation` VARCHAR(255) NULL; ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_151(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `File` ADD `title` VARCHAR(255) NULL; ");
	}


	@SuppressWarnings("unused")
	private void startUpdate_150(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `FileSystemFile` ADD `thumbnail_id` BIGINT(20) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `FileSystemFile` ADD CONSTRAINT `FK_FileSystemFile_thumbnail_id` FOREIGN KEY (`thumbnail_id`) REFERENCES `DBFile` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_149(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD KEY (`accountingAssignment_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD KEY (`cancelationAccountingAssignment_id`);");


	}


	@SuppressWarnings("unused")
	private void startUpdate_148(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `FileSystemFile` ADD `size` INT(11) NOT NULL; ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_147(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` ADD `creator_id` BIGINT(20) NOT NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` ADD CONSTRAINT `FK_FileActivity_creator_id` FOREIGN KEY (`creator_id`) REFERENCES `User` (`id`);");
	}


	@SuppressWarnings("unused")
	private void startUpdate_146(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `FileActivity` ( `id` bigint(20) NOT NULL, `file_id` BIGINT(20) NOT NULL,  `invisible` TINYINT(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` ADD CONSTRAINT `FK_FileActivity_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `FileActivity` ADD CONSTRAINT `FK_FileActivity_file_id` FOREIGN KEY (`file_id`) REFERENCES `File` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_145(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `FileSystemFile` (`id` bigint(20) NOT NULL,`filename` VARCHAR(255) NOT NULL,`relativeFilePath` VARCHAR(255) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `FileSystemFile` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `FileSystemFile` ADD CONSTRAINT `FK_FileSystemFile_id` FOREIGN KEY (`id`) REFERENCES `File` (`id`);");
	}



	@SuppressWarnings("unused")
	private void startUpdate_144(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `ProductStatistic` (`id` bigint(20) NOT NULL, `name` VARCHAR(255) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `showOnOverview` TINYINT(1) NOT NULL, `weight` INT(11) NOT NULL, `newContract` TINYINT(1) NOT NULL, `extensionOfTheTerm` TINYINT(1) NOT NULL, `debidCreditChange` TINYINT(1) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatistic` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatistic` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatistic` ADD INDEX(`deleted`);" );
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatistic` ADD INDEX(`weight`);" );
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatistic` ADD INDEX(`showOnOverview`);" );

		session.executeNonSelectingSQL("CREATE TABLE `ProductStatisticSelectedProductCategory` (`productStatisticID` bigint(20) NOT NULL, `selectedProductCategoryID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCategory` ADD PRIMARY KEY (`productStatisticID`, `selectedProductCategoryID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCategory` ADD CONSTRAINT `FK_ProductStatisticSelectedProductCategory_productCategory` FOREIGN KEY (`selectedProductCategoryID`) REFERENCES `ProductCategory` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCategory` ADD CONSTRAINT `FK_ProductStatisticSelectedProductCategory_productStatistic` FOREIGN KEY (`productStatisticID`) REFERENCES `ProductStatistic` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `ProductStatisticSelectedProduct` (`productStatisticID` bigint(20) NOT NULL, `selectedProductID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProduct` ADD PRIMARY KEY (`productStatisticID`, `selectedProductID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProduct` ADD CONSTRAINT `FK_ProductStatisticSelectedProduct_product` FOREIGN KEY (`selectedProductID`) REFERENCES `BaseProduct` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProduct` ADD CONSTRAINT `FK_ProductStatisticSelectedProduct_productStatistic` FOREIGN KEY (`productStatisticID`) REFERENCES `ProductStatistic` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `ProductStatisticSelectedProductCombination` (`productStatisticID` bigint(20) NOT NULL, `selectedProductCombinationID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCombination` ADD PRIMARY KEY (`productStatisticID`, `selectedProductCombinationID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCombination` ADD CONSTRAINT `FK_ProductStatisticSelectedProductCombination_productCombination` FOREIGN KEY (`selectedProductCombinationID`) REFERENCES `BaseProductCombination` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCombination` ADD CONSTRAINT `FK_ProductStatisticSelectedProductCombination_productStatistic` FOREIGN KEY (`productStatisticID`) REFERENCES `ProductStatistic` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `ProductStatisticSelectedProductCombinationFilter` (`productStatisticID` bigint(20) NOT NULL, `selectedProductCombinationFilterID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCombinationFilter` ADD PRIMARY KEY (`productStatisticID`, `selectedProductCombinationFilterID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCombinationFilter` ADD CONSTRAINT `FK_ProductStatisticSelectedProductCombFilter_productComb` FOREIGN KEY (`selectedProductCombinationFilterID`) REFERENCES `BaseProductCombination` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductCombinationFilter` ADD CONSTRAINT `FK_ProductStatisticSelectedProductCombFilter_productStatistic` FOREIGN KEY (`productStatisticID`) REFERENCES `ProductStatistic` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `ProductStatisticSelectedProductFilter` (`productStatisticID` bigint(20) NOT NULL, `selectedProductID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductFilter` ADD PRIMARY KEY (`productStatisticID`, `selectedProductID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductFilter` ADD CONSTRAINT `FK_ProductStatisticSelectedProductFilter_product` FOREIGN KEY (`selectedProductID`) REFERENCES `BaseProduct` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticSelectedProductFilter` ADD CONSTRAINT `FK_ProductStatisticSelectedProductFilter_productStatistic` FOREIGN KEY (`productStatisticID`) REFERENCES `ProductStatistic` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `ProductStatisticCache` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `year` INT(4) NOT NULL, `month` TINYINT(2) NOT NULL, `pieces` INT(11) NOT NULL, `productStatistic_id` BIGINT(20) NOT NULL, `user_id` BIGINT(20) NULL, `shop_id` BIGINT(20) NULL, `vo_id` BIGINT(20) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticCache` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticCache` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticCache` ADD INDEX(`user_id`);" );
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticCache` ADD INDEX(`shop_id`);" );
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticCache` ADD INDEX(`vo_id`);" );
		session.executeNonSelectingSQL("ALTER TABLE `ProductStatisticCache` ADD CONSTRAINT `FK_ProductStatisticCache_productStatistic` FOREIGN KEY (`productStatistic_id`) REFERENCES `ProductStatistic` (`id`);");


	}




	@SuppressWarnings("unused")
	private void startUpdate_143(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `showOnCompanyDashboard` TINYINT(1) NOT NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD KEY (`showOnCompanyDashboard`);");

	}

	@SuppressWarnings("unused")
	private void startUpdate_142(UnitOfWork session) throws Exception{

		// Create EmploymentForm
		session.executeNonSelectingSQL("CREATE TABLE `UserCustomerHistory` "
				+ "(`user_id` bigint(20) NOT NULL, "
				+ "`customer_id` bigint(20) NOT NULL, "
				+ "`lastOpened` datetime NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");

		session.executeNonSelectingSQL("ALTER TABLE `UserCustomerHistory` ADD PRIMARY KEY (`user_id`, `customer_id`);");

		session.executeNonSelectingSQL("ALTER TABLE `UserCustomerHistory` ADD CONSTRAINT `FK_UserCustomerHistory_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `UserCustomerHistory` ADD CONSTRAINT `FK_UserCustomerHistory_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`id`)");

	}



	@SuppressWarnings("unused")
	private void startUpdate_141(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `weight` INT(11) NOT NULL DEFAULT 0; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD KEY (`weight`);");

	}

	@SuppressWarnings("unused")
	private void startUpdate_140(UnitOfWork session) throws Exception{
		/*
		 * INIT ALL USER -> SHOP RIGHTS
		 */

		Vector vShop = session.executeSQL("SELECT ID FROM Shop WHERE enabled = true and deleted = false");
		Vector vUser = session.executeSQL("SELECT ID FROM User WHERE enabled = true and deleted = false");

		ArrayRecord rShop, rUser;

		if(vShop.size() > 0  && vUser.size() > 0){

			for(int i=0; i<vUser.size(); i++){
				rUser = (ArrayRecord) vUser.get(i);

				for(int j=0; j<vShop.size(); j++){
					rShop = (ArrayRecord) vShop.get(j);

					session.executeNonSelectingSQL("INSERT INTO `UserAllowedShops` (`userID`, `shopID`) VALUES ('"+rUser.get("ID").toString()+"', '"+rShop.get("ID").toString()+"');");
				}
			}
		}

	}


	@SuppressWarnings("unused")
	private void startUpdate_139(UnitOfWork session) throws Exception{

		// Create EmploymentForm
		session.executeNonSelectingSQL("CREATE TABLE `UserAllowedShops` (`userID` bigint(20) NOT NULL, `shopID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `UserAllowedShops` ADD PRIMARY KEY (`userID`, `shopID`);");
		session.executeNonSelectingSQL("ALTER TABLE `UserAllowedShops` ADD CONSTRAINT `FK_UserAllowedShops_userID` FOREIGN KEY (`userID`) REFERENCES `User` (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `UserAllowedShops` ADD CONSTRAINT `FK_UserAllowedShops_shopID` FOREIGN KEY (`shopID`) REFERENCES `Shop` (`id`)");

	}

	@SuppressWarnings("unused")
	private void startUpdate_138(UnitOfWork session) throws Exception{


		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");
		session.executeNonSelectingSQL("UPDATE `Shop` SET `shopImage_id` = NULL;");
		session.executeNonSelectingSQL("UPDATE `User` SET `userImage_id` = NULL;");
		session.executeNonSelectingSQL("TRUNCATE DBFile");
		session.executeNonSelectingSQL("TRUNCATE File");
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");

	}


	@SuppressWarnings("unused")
	private void startUpdate_137(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `cancelationAccountingAssignment_id` BIGINT(20) NULL ;");

	}



	@SuppressWarnings("unused")
	private void startUpdate_136(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `ProductOption` ADD `generatedRevenue` DECIMAL(10,4) NULL ;");

	}


	@SuppressWarnings("unused")
	private void startUpdate_135(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `correctVO` TINYINT(1) NOT NULL ;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_134(UnitOfWork session) throws Exception{

		// Create EmploymentForm
		session.executeNonSelectingSQL("CREATE TABLE `UserPayroll` "
				+ "(`id` bigint(20) NOT NULL, "
				+ "`creationDate` datetime NOT NULL, "
				+ "`updateDate` datetime NOT NULL, "
				+ "`deleted` tinyint(1) NOT NULL, "

				+ "`month` INT(2) NOT NULL, "
				+ "`year` INT(4) NOT NULL, "
				+ "`user_id` BIGINT(20) NOT NULL, "
				+ "`basicSalary` DECIMAL(10,2) NOT NULL, "
				+ "`bonusSalary` DECIMAL(10,2) NOT NULL, "
				+ "`minimalBonusPointGoal` DECIMAL(10,4) NOT NULL, "
				+ "`employmentForm_id` BIGINT(20) NULL, "
				+ "`carGrossCatalogPrice` DECIMAL(10,2) NOT NULL, "
				+ "`homeOffice` TINYINT(1) NOT NULL, "
				+ "`distanceToWorkplace` INT(11) NOT NULL, "
				+ "`privateCarUsage` TINYINT(1) NOT NULL, "
				+ "`pointGoal` DECIMAL(10,4) NOT NULL, "
				+ "`pointsPerCommission` DECIMAL(10,4) DEFAULT NULL, "
				+ "`weeklyWorkingTime` INT(11) NOT NULL, "
				+ "`weeklyWorkingDays` INT(11) NOT NULL, "

				+ "`yieldedCommission` DECIMAL(10,4) NOT NULL, "
				+ "`commission` DECIMAL(10,2) NOT NULL, "
				+ "`expenses` DECIMAL(10,2) NOT NULL, "
				+ "`travelExpenses` DECIMAL(10,2) NOT NULL, "
				+ "`hoursWorked` DECIMAL(10,2) NOT NULL, "
				+ "`points` DECIMAL(10,4) NOT NULL, "

				+ "`note` longtext"

				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `UserPayroll` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `UserPayroll` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("ALTER TABLE `UserPayroll` ADD CONSTRAINT `FK_UserPayroll_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `UserPayroll` ADD CONSTRAINT `FK_UserPayroll_employmentForm_id` FOREIGN KEY (`employmentForm_id`) REFERENCES `EmploymentForm` (`id`)");

	}

	@SuppressWarnings("unused")
	private void startUpdate_133(UnitOfWork session) throws Exception{

		// Update User
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `privateCarUsage` TINYINT(1) NOT NULL DEFAULT '0';");

	}

	@SuppressWarnings("unused")
	private void startUpdate_132(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("INSERT INTO `EmploymentForm` (`creationDate`, `name`, `updateDate`, `deleted`) VALUES (NOW(), 'Vollzeit', NOW(), '0');");
		session.executeNonSelectingSQL("INSERT INTO `EmploymentForm` (`creationDate`, `name`, `updateDate`, `deleted`) VALUES (NOW(), 'Teilzeit', NOW(), '0');");
	}

	@SuppressWarnings("unused")
	private void startUpdate_131(UnitOfWork session) throws Exception{

		// Create EmploymentForm
		session.executeNonSelectingSQL("CREATE TABLE `EmploymentForm` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `name` varchar(255) NOT NULL, `updateDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `EmploymentForm` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `EmploymentForm` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");

		// Update User
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `bday` datetime DEFAULT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `stuffNumber` varchar(255) DEFAULT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `tel` varchar(255) DEFAULT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `basicSalary` decimal(10,2) NOT NULL DEFAULT '0.00';");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `bonusSalary` decimal(10,2) NOT NULL DEFAULT '0.00';");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `minimalBonusPointGoal` decimal(10,4) NOT NULL DEFAULT '0.0000';");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `employmentForm_id` BIGINT(20) DEFAULT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `carGrossCatalogPrice` decimal(10,2) NOT NULL DEFAULT '0.00';");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `homeOffice` TINYINT(1) NOT NULL DEFAULT '0';");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `distanceToWorkplace` INT(11) NOT NULL DEFAULT '0';");

		session.executeNonSelectingSQL("ALTER TABLE `User` ADD CONSTRAINT `FK_User_employmentForm_id` FOREIGN KEY (`employmentForm_id`) REFERENCES `EmploymentForm` (`id`)");

	}

	@SuppressWarnings("unused")
	private void startUpdate_130(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `CommissionActivity` (`id` bigint(20) NOT NULL, `commission` decimal(10,4) NOT NULL, `points` decimal(10,4) NOT NULL, `price` decimal(10,4) NOT NULL, `tax` decimal(5,2) DEFAULT NULL, `vat` decimal(10,4) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionActivity` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionActivity` ADD CONSTRAINT `FK_CommissionActivity_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`)");
		session.executeNonSelectingSQL("CREATE TABLE `BaseContractCancellation` (`id` bigint(20) NOT NULL, `contract_id` bigint(20) NOT NULL ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContractCancellation` ADD PRIMARY KEY (`id`), ADD KEY (`contract_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContractCancellation` ADD CONSTRAINT `FK_BaseContractCancellation_id` FOREIGN KEY (`id`) REFERENCES `CommissionActivity` (`id`)");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContractCancellation` ADD CONSTRAINT `FK_BaseContractCancellation_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `BaseContract` (`id`)");
	}


	@SuppressWarnings("unused")
	private void startUpdate_129(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedProductCombination` (`achievementID` bigint(20) NOT NULL, `selectedProductCombinationID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCombination` ADD PRIMARY KEY (`achievementID`, `selectedProductCombinationID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCombination` ADD CONSTRAINT `FK_AchievementSelectedProductCombination_productCombination` FOREIGN KEY (`selectedProductCombinationID`) REFERENCES `BaseProductCombination` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCombination` ADD CONSTRAINT `FK_AchievementSelectedProductCombination_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedProductCombinationFilter` (`achievementID` bigint(20) NOT NULL, `selectedProductCombinationFilterID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCombinationFilter` ADD PRIMARY KEY (`achievementID`, `selectedProductCombinationFilterID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCombinationFilter` ADD CONSTRAINT `FK_AchievementSelectedProductCombFilter_productComb` FOREIGN KEY (`selectedProductCombinationFilterID`) REFERENCES `BaseProductCombination` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCombinationFilter` ADD CONSTRAINT `FK_AchievementSelectedProductCombFilter_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");
	}


	@SuppressWarnings("unused")
	private void startUpdate_128(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `BaseProductCombination` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `updateDate` datetime NOT NULL, `BaseProduct_id` bigint(20) NOT NULL, `deleted` tinyint(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProductCombination` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProductCombination` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProductCombination` ADD CONSTRAINT `FK_BaseProductCombination_BaseProduct_id` FOREIGN KEY (`BaseProduct_id`) REFERENCES `BaseProduct` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `BaseProductCombination_ProductOption` ( `baseProductCombinationID` bigint(20) NOT NULL, `productOptionID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProductCombination_ProductOption` ADD PRIMARY KEY (`baseProductCombinationID`,`productOptionID`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProductCombination_ProductOption` ADD CONSTRAINT `FK_BaseProductComb_ProductOption_ProductCombID` FOREIGN KEY (`baseProductCombinationID`) REFERENCES `BaseProductCombination` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProductCombination_ProductOption` ADD CONSTRAINT `FK_BaseProductComb_ProductOption_Product_id` FOREIGN KEY (`productOptionID`) REFERENCES `BaseProduct` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_127(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `AchievementUserGoalAttainment` (`achievement_id` bigint(20) NOT NULL, `user_id` bigint(20) NOT NULL, `pieceGoalAttainment` DECIMAL(20,6) NULL, `sumGoalAttainment` DECIMAL(20,6) NULL, `aquiredRevenueGoalAttainment` DECIMAL(20,6) NULL, `contractedRevenueGoalAttainment` DECIMAL(20,6) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementUserGoalAttainment` ADD PRIMARY KEY (`achievement_id`, `user_id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementUserGoalAttainment` ADD CONSTRAINT `FK_AchievementUserGoalAttainment_achievement` FOREIGN KEY (`achievement_id`) REFERENCES `Achievement` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementUserGoalAttainment` ADD CONSTRAINT `FK_AchievementUserGoalAttainment_user` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);");

	}


	@SuppressWarnings("unused")
	private void startUpdate_126(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `pieceGoalAttainment` DECIMAL(20,6) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `sumGoalAttainment` DECIMAL(20,6) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `aquiredRevenueGoalAttainment` DECIMAL(20,6) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `contractedRevenueGoalAttainment` DECIMAL(20,6) NULL; ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_125(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `debidCreditChange` tinyint(1) NOT NULL; ");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedProductFilter` (`achievementID` bigint(20) NOT NULL, `selectedProductID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductFilter` ADD PRIMARY KEY (`achievementID`, `selectedProductID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductFilter` ADD CONSTRAINT `FK_AchievementSelectedProductFilter_product` FOREIGN KEY (`selectedProductID`) REFERENCES `BaseProduct` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductFilter` ADD CONSTRAINT `FK_AchievementSelectedProductFilter_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `newContract` tinyint(1) NOT NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `extensionOfTheTerm` tinyint(1) NOT NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD `debidCreditChange` tinyint(1) NOT NULL; ");
	}


	@SuppressWarnings("unused")
	private void startUpdate_124(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `AchievementTarget` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `target` DECIMAL(20,6) NULL, `payout` DECIMAL(20,6) NULL, `commission` DECIMAL(20,6) NULL, `payoutText` VARCHAR(255) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementTarget` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementTarget` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");

		session.executeNonSelectingSQL("CREATE TABLE `Achievement` (`id` bigint(20) NOT NULL, `name` VARCHAR(255) NOT NULL, `creationDate` datetime NOT NULL, `effectiveDate` datetime DEFAULT NULL, `enabled` tinyint(1) NOT NULL, `deleted` tinyint(1) NOT NULL, `expiryDate` datetime DEFAULT NULL, `updateDate` datetime NOT NULL, `invisible` TINYINT(1) NOT NULL, `payoutToSource` TINYINT(1) NOT NULL, `totalPieceTarget_id` BIGINT(20) NULL, `totalSumTarget_id` BIGINT(20) NULL, `totalAquiredRevenueTarget_id` BIGINT(20) NULL, `totalContractedRevenueTarget_id` BIGINT(20) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD INDEX(`creationDate`, `effectiveDate`, `enabled`, `deleted`, `expiryDate`, `invisible`);" );

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedProductCategory` (`achievementID` bigint(20) NOT NULL, `selectedProductCategoryID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCategory` ADD PRIMARY KEY (`achievementID`, `selectedProductCategoryID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCategory` ADD CONSTRAINT `FK_AchievementSelectedProductCategory_productCategory` FOREIGN KEY (`selectedProductCategoryID`) REFERENCES `ProductCategory` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProductCategory` ADD CONSTRAINT `FK_AchievementSelectedProductCategory_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedProduct` (`achievementID` bigint(20) NOT NULL, `selectedProductID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProduct` ADD PRIMARY KEY (`achievementID`, `selectedProductID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProduct` ADD CONSTRAINT `FK_AchievementSelectedProduct_product` FOREIGN KEY (`selectedProductID`) REFERENCES `BaseProduct` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedProduct` ADD CONSTRAINT `FK_AchievementSelectedProduct_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedShop` (`achievementID` bigint(20) NOT NULL, `selectedShopID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedShop` ADD PRIMARY KEY (`achievementID`, `selectedShopID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedShop` ADD CONSTRAINT `FK_AchievementSelectedShop_shop` FOREIGN KEY (`selectedShopID`) REFERENCES `Shop` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedShop` ADD CONSTRAINT `FK_AchievementSelectedShop_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedUser` (`achievementID` bigint(20) NOT NULL, `selectedUserID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedUser` ADD PRIMARY KEY (`achievementID`, `selectedUserID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedUser` ADD CONSTRAINT `FK_AchievementSelectedUser_user` FOREIGN KEY (`selectedUserID`) REFERENCES `User` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedUser` ADD CONSTRAINT `FK_AchievementSelectedUser_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedVO` (`achievementID` bigint(20) NOT NULL, `selectedVOID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedVO` ADD PRIMARY KEY (`achievementID`, `selectedVOID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedVO` ADD CONSTRAINT `FK_AchievementSelectedVO_vo` FOREIGN KEY (`selectedVOID`) REFERENCES `VO` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedVO` ADD CONSTRAINT `FK_AchievementSelectedVO_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSelectedShopGroup` (`achievementID` bigint(20) NOT NULL, `selectedShopGroupID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedShopGroup` ADD PRIMARY KEY (`achievementID`, `selectedShopGroupID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedShopGroup` ADD CONSTRAINT `FK_AchievementSelectedShopGroup_shopGroup` FOREIGN KEY (`selectedShopGroupID`) REFERENCES `ShopGroup` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSelectedShopGroup` ADD CONSTRAINT `FK_AchievementSelectedShopGroup_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementPayoutUser` (`achievementID` bigint(20) NOT NULL, `payoutUserID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementPayoutUser` ADD PRIMARY KEY (`achievementID`, `payoutUserID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementPayoutUser` ADD CONSTRAINT `FK_AchievementPayoutUser_user` FOREIGN KEY (`payoutUserID`) REFERENCES `User` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementPayoutUser` ADD CONSTRAINT `FK_AchievementPayoutUser_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementPieceTarget` (`achievementID` bigint(20) NOT NULL, `pieceTargetID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementPieceTarget` ADD PRIMARY KEY (`achievementID`, `pieceTargetID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementPieceTarget` ADD CONSTRAINT `FK_AchievementPieceTarget_target` FOREIGN KEY (`pieceTargetID`) REFERENCES `AchievementTarget` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementPieceTarget` ADD CONSTRAINT `FK_AchievementPieceTarget_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementSumTarget` (`achievementID` bigint(20) NOT NULL, `sumTargetID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSumTarget` ADD PRIMARY KEY (`achievementID`, `sumTargetID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSumTarget` ADD CONSTRAINT `FK_AchievementSumTarget_target` FOREIGN KEY (`sumTargetID`) REFERENCES `AchievementTarget` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementSumTarget` ADD CONSTRAINT `FK_AchievementSumTarget_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementAquiredRevenueTarget` (`achievementID` bigint(20) NOT NULL, `aquiredRevenueTargetID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementAquiredRevenueTarget` ADD PRIMARY KEY (`achievementID`, `aquiredRevenueTargetID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementAquiredRevenueTarget` ADD CONSTRAINT `FK_AchievementAquiredRevenueTarget_target` FOREIGN KEY (`aquiredRevenueTargetID`) REFERENCES `AchievementTarget` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementAquiredRevenueTarget` ADD CONSTRAINT `FK_AchievementAquiredRevenueTarget_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `AchievementContractedRevenueTarget` (`achievementID` bigint(20) NOT NULL, `contractedRevenueTargetID` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementContractedRevenueTarget` ADD PRIMARY KEY (`achievementID`, `contractedRevenueTargetID`); ");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementContractedRevenueTarget` ADD CONSTRAINT `FK_AchievementContractedRevenueTarget_target` FOREIGN KEY (`contractedRevenueTargetID`) REFERENCES `AchievementTarget` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AchievementContractedRevenueTarget` ADD CONSTRAINT `FK_AchievementContractedRevenueTarget_achievement` FOREIGN KEY (`achievementID`) REFERENCES `Achievement` (`id`);");


		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD CONSTRAINT `FK_Achievement_totalPieceTarget` FOREIGN KEY (`totalPieceTarget_id`) REFERENCES `AchievementTarget` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD CONSTRAINT `FK_Achievement_totalSumTarget` FOREIGN KEY (`totalSumTarget_id`) REFERENCES `AchievementTarget` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD CONSTRAINT `FK_Achievement_totalAquiredRevenueTarget` FOREIGN KEY (`totalAquiredRevenueTarget_id`) REFERENCES `AchievementTarget` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `Achievement` ADD CONSTRAINT `FK_Achievement_totalContractedRevenueTarget` FOREIGN KEY (`totalContractedRevenueTarget_id`) REFERENCES `AchievementTarget` (`id`);");


	}

	@SuppressWarnings("unused")
	private void startUpdate_123(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `baseProduct_id` `baseProduct_id` BIGINT(20) NULL, CHANGE `productCategory_id` `productCategory_id` BIGINT(20) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_122(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Customer` CHANGE `title` `title` VARCHAR(255) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_121(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `ShopAlias` (`shop_id` bigint(20) NOT NULL, `alias` VARCHAR(100) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ShopAlias` ADD PRIMARY KEY (`shop_id`,`alias`);");

		session.executeNonSelectingSQL("ALTER TABLE `ShopAlias` ADD CONSTRAINT `FK_ShopAlias_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`);");


	}

	@SuppressWarnings("unused")
	private void startUpdate_120(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` CHANGE `accountingAssignment_id` `accountingAssignment_id` BIGINT(20) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_119(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD `shop_id` BIGINT(20) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD CONSTRAINT `FK_Customer_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`); ");
	}


	@SuppressWarnings("unused")
	private void startUpdate_118(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` ADD `newContractSum` INT(11) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_117(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategorySalesCache` ADD `vo_id` BIGINT(20) NULL; ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategorySalesCache` ADD CONSTRAINT `FK_ProductCategorySalesCache_vo_id` FOREIGN KEY (`vo_id`) REFERENCES `VO` (`id`); ");
	}



	@SuppressWarnings("unused")
	private void startUpdate_116(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `VO` ADD `liveCommissionCache_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `VO` ADD CONSTRAINT `FK_VO_liveCommissionCache_id` FOREIGN KEY (`liveCommissionCache_id`) REFERENCES `CommissionCache` (`id`); ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_115(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` ADD `extensionOfTheTermSum` INT(11) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_114(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategory` ADD `showInOverview` tinyint(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_113(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `ProductCategorySalesCache` ("
			+"`id` bigint(20) NOT NULL,"
			+"`creationDate` datetime NOT NULL,"
			+"`updateDate` datetime NOT NULL,"
			+"`deleted` tinyint(1) NOT NULL,"
			+"`year` int(4) NOT NULL,"
			+"`month` int(1) NOT NULL,"
			+"`pieces` int(11) NOT NULL,"
			+"`productCategory_id` BIGINT(20) NOT NULL,"
			+"`user_id` BIGINT(20) NULL,"
			+"`shop_id` BIGINT(20) NULL"
			+") ENGINE=InnoDB DEFAULT CHARSET=latin1; ");

		session.executeNonSelectingSQL("ALTER TABLE `ProductCategorySalesCache` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategorySalesCache` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");

		session.executeNonSelectingSQL("ALTER TABLE `ProductCategorySalesCache`"
				+"ADD CONSTRAINT `FK_ProductCategorySalesCache_ProductCategory_id` FOREIGN KEY (`productCategory_id`) REFERENCES `ProductCategory` (`id`),"
				+"ADD CONSTRAINT `FK_ProductCategorySalesCache_Shop_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),"
				+"ADD CONSTRAINT `FK_ProductCategorySalesCache_User_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`); ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_112(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ShopGroup` ADD `effectiveDate` datetime NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ShopGroup` ADD `expiryDate` datetime NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ShopGroup` ADD `enabled` tinyint(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_111(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD INDEX(`multiUserEvent`);");

		session.executeNonSelectingSQL("CREATE TABLE `ShopGroup` ("
			+"`id` bigint(20) NOT NULL,"
			+"`creationDate` datetime NOT NULL,"
			+"`updateDate` datetime NOT NULL,"
			+"`deleted` tinyint(1) NOT NULL,"
			+"`name` varchar(255) NOT NULL"
			+") ENGINE=InnoDB DEFAULT CHARSET=latin1; ");

		session.executeNonSelectingSQL("ALTER TABLE `ShopGroup` ADD PRIMARY KEY (`id`); ");
		session.executeNonSelectingSQL("ALTER TABLE `ShopGroup` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT; ");

		session.executeNonSelectingSQL("CREATE TABLE `ShopGroupMember` ("
			+"`shopGroupID` bigint(20) NOT NULL,"
			+"`shopID` bigint(20) NOT NULL"
			+") ENGINE=InnoDB DEFAULT CHARSET=latin1; ");

		session.executeNonSelectingSQL("ALTER TABLE `ShopGroupMember` ADD PRIMARY KEY (`shopGroupID`, `shopID`); ");

		session.executeNonSelectingSQL("ALTER TABLE `ShopGroupMember`"
			+"ADD CONSTRAINT `FK_ShopGroupMember_ShopGroup_id` FOREIGN KEY (`shopGroupID`) REFERENCES `ShopGroup` (`id`),"
			+"ADD CONSTRAINT `FK_ShopGroupMember_Shop_id` FOREIGN KEY (`shopID`) REFERENCES `Shop` (`id`); ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_110(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ATTRIBUTEDTYPEENTITY` ADD INDEX(`DTYPE`);");
		session.executeNonSelectingSQL("ALTER TABLE `ROLETYPEENTITY` ADD INDEX(`NAME`);");
		session.executeNonSelectingSQL("ALTER TABLE `IDENTITYTYPEENTITY` ADD INDEX(`PARTITION_ID`);");
		session.executeNonSelectingSQL("ALTER TABLE `IDENTITYTYPEENTITY` ADD INDEX(`TYPENAME`);");
		session.executeNonSelectingSQL("ALTER TABLE `ACCOUNTTYPEENTITY` ADD INDEX(`LOGINNAME`);");
		session.executeNonSelectingSQL("ALTER TABLE `PERMISSIONTYPEENTITY` ADD INDEX(`RESOURCECLASS`);");
		session.executeNonSelectingSQL("ALTER TABLE `PERMISSIONTYPEENTITY` ADD INDEX(`RESOURCEIDENTIFIER`);");
		session.executeNonSelectingSQL("ALTER TABLE `VOType` ADD INDEX(`DTYPE`);");
		session.executeNonSelectingSQL("ALTER TABLE `File` ADD INDEX(`DTYPE`);");
		session.executeNonSelectingSQL("ALTER TABLE `File` ADD INDEX(`code`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD INDEX(`DTYPE`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD INDEX(`name`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD INDEX(`deleted`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_109(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `PARTITIONTYPEENTITY` ADD INDEX(`NAME`);");
		session.executeNonSelectingSQL("ALTER TABLE `PARTITIONTYPEENTITY` ADD INDEX(`TYPENAME`);");
		session.executeNonSelectingSQL("ALTER TABLE `PARTITIONTYPEENTITY` ADD INDEX(`CONFIGURATIONNAME`);");

		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD INDEX(`DTYPE`);");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD INDEX(`creationDate`);");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD INDEX(`effectiveDate`);");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD INDEX(`hideInTimeline`);");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD INDEX(`deleted`);");

		session.executeNonSelectingSQL("ALTER TABLE `User` ADD INDEX(`uuid`);");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD INDEX(`deleted`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_108(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` DROP `baseContract_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `accountingAssignment_id` BIGINT(20) NULL;");

		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `vendor_id` BIGINT(20) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD INDEX(`vendor_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD CONSTRAINT `FK_AccountingAssignment_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `Vendor` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `correctUser` TINYINT(1) NOT NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `correctDate` TINYINT(1) NOT NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `correctElements` TINYINT(1) NOT NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `correctCustomer` TINYINT(1) NOT NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `correctCommission` TINYINT(1) NOT NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `info` TEXT NOT NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `commissionMonth` INT NOT NULL , ADD `commissionYear` INT NOT NULL ;");

		session.executeNonSelectingSQL("CREATE TABLE `VOCommission` ( `id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `year` int(4) NOT NULL DEFAULT '0', `month` int(2) NOT NULL DEFAULT '0', `baseAirtime` decimal(10,2) NOT NULL, `bonusAirtime` decimal(10,2) NOT NULL, `repairs` decimal(10,2) NOT NULL, `servicePackages` decimal(10,2) NOT NULL, `vo_id` bigint(20) NOT NULL ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `VOCommission` ADD PRIMARY KEY (`id`), ADD KEY `vo_id` (`vo_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `VOCommission` CHANGE `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_107(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");
		session.executeNonSelectingSQL("TRUNCATE TABLE `CommissionTypeAssociation_ProductOption`;");
		session.executeNonSelectingSQL("TRUNCATE TABLE `CommissionTypeAssociation`;");
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");

		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` DROP INDEX unique_index;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD `commissionSubText` VARCHAR(255) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD UNIQUE `unique_index` (`venor_id`, `commissionText`, `commissionSubText`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_106(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` DROP `phoneContract_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `status` TINYINT(2) NOT NULL DEFAULT '0';");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD INDEX(`status`);" );
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `baseContract_id` BIGINT(20) NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` ADD `accountingAssignment_id` BIGINT(20) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` ADD KEY `FK_VendorCommissionAccountingItem_accAss_id` (`accountingAssignment_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD `date` DATETIME NOT NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` DROP `chargeback`;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_105(UnitOfWork session) throws Exception{


		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` DROP INDEX `FK_VendorCommissionAccountingItem_accAss_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` DROP `accountingAssignment_id`;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_104(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");
		session.executeNonSelectingSQL("TRUNCATE TABLE `CommissionTypeAssociation_ProductOption`;");
		session.executeNonSelectingSQL("TRUNCATE TABLE `CommissionTypeAssociation`;");
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");

		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` DROP FOREIGN KEY `FK_CommissionTypeAssociation_venor_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` DROP INDEX unique_index;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD INDEX `venor_id` (`venor_id`);");

		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD CONSTRAINT `FK_CommissionTypeAssociation_venor_id` FOREIGN KEY (`venor_id`) REFERENCES `Vendor` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` DROP `commissionText1`;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` DROP `commissionText2`;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` DROP `commissionText3`;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD `commissionText` VARCHAR(255) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD UNIQUE `unique_index` (`venor_id`, `commissionText`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_103(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `AccountingAssignment` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `phoneContract_id` BIGINT(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `AccountingAssignment` CHANGE `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` ADD `accountingAssignment_id` BIGINT(20) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` ADD KEY `FK_VendorCommissionAccountingItem_accAss_id` (`accountingAssignment_id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_102(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD `extensionOfTheTerm` TINYINT(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_101(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `UserAlias` (`user_id` bigint(20) NOT NULL, `alias` VARCHAR(100) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `UserAlias` ADD PRIMARY KEY (`user_id`,`alias`);");

		session.executeNonSelectingSQL("ALTER TABLE `UserAlias` ADD CONSTRAINT `FK_UserAlias_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);");


	}


	@SuppressWarnings("unused")
	private void startUpdate_100(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `VendorCommissionAccountingFile` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `vo` VARCHAR(50) NOT NULL, `year` int(4) NOT NULL, `month` int(2) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingFile` ADD PRIMARY KEY (`id`), ADD UNIQUE INDEX `unique` (`vo`,`year`,`month`);");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingFile` CHANGE `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("ALTER TABLE VendorCommissionAccountingItem DROP INDEX hash;");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` DROP `hash`");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` ADD `file_id` BIGINT(20) NOT NULL ;");

		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` ADD CONSTRAINT `FK_VendorCommissionAccountingItem_file_id` FOREIGN KEY (`file_id`) REFERENCES `VendorCommissionAccountingFile` (`id`);");


	}

	@SuppressWarnings("unused")
	private void startUpdate_99(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("INSERT INTO `EventType` (`creationDate`, `updateDate`, `deleted`, `uuid`, `title`, `shortName`, `multiUserEvent`, `editable`, `allDay`, `marginTime`, `externalEvent`, `workingTime`) VALUES (NOW(), NOW(), '0', '"+UUID.randomUUID().toString()+"', 'Elternzeit', 'EZ', '0', '0', '1', '0', '1', '1')");
	}

	@SuppressWarnings("unused")
	private void startUpdate_98(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `entranceDate` datetime NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `planingStartMonth` INT(2) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `planingStartYear` INT(4) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_97(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD INDEX `enabled_deleted` (`enabled`, `deleted`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD INDEX `status_index` (`enabled`, `effectiveDate`, `expiryDate`, `deleted`);");

		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD INDEX `enabled_deleted` (`enabled`, `deleted`);");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD INDEX `enabled_deleted` (`enabled`, `deleted`);");
		session.executeNonSelectingSQL("ALTER TABLE `User` CHANGE `uuid` `uuid` CHAR(37) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_96(UnitOfWork session) throws Exception{
//		userController.initUserStats();
	}

	@SuppressWarnings("unused")
	private void startUpdate_95(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `UserStats` (`user_id` BIGINT(20) NOT NULL, `year` INT(4) NOT NULL, `month` INT(2) NOT NULL, `overtime` INT(11) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `UserStats` ADD PRIMARY KEY (`user_id`, `year`, `month`);");
		session.executeNonSelectingSQL("ALTER TABLE `UserStats` ADD CONSTRAINT `FK_UserStats_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);");
	}


	@SuppressWarnings("unused")
	private void startUpdate_94(UnitOfWork session) throws Exception{
		Vector v = session.executeSQL("Select ID from `UserGroup` WHERE `name` = \"Mitarbeiter\"");

		if(v.size() == 0){
			return;
		}

		ArrayRecord r = (ArrayRecord) v.get(0);
		long userGroupId = (long) r.get("ID");

		session.executeNonSelectingSQL("INSERT INTO `UserGroupPermission` (`userGroup_id`, `resource`, `command`, `permission`) VALUES"+
				"('"+userGroupId+"', 'contract_list', 'read_own', 1);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_93(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `failedLogins` int(4) NOT NULL;");
		session.executeNonSelectingSQL("UPDATE `User` SET `enabled` = 1 ");
		session.executeNonSelectingSQL("UPDATE `Shop` SET `enabled` = 1 ");
	}


	@SuppressWarnings("unused")
	private void startUpdate_92(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `CommissionTypeAssociation` ( `id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `venor_id` bigint(20) NOT NULL, `commissionText1` char(255) NOT NULL, `commissionText2` char(255) NOT NULL, `commissionText3` char(255) NOT NULL, `chargeback` tinyint(1) NOT NULL, `baseProduct_id` bigint(20) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD PRIMARY KEY (`id`), ADD UNIQUE `unique_index` (`venor_id`, `commissionText1`, `commissionText2`, `commissionText3`);");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` CHANGE `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD CONSTRAINT `FK_CommissionTypeAssociation_venor_id` FOREIGN KEY (`venor_id`) REFERENCES `Vendor` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation` ADD CONSTRAINT `FK_CommissionTypeAssociation_baseProduct_id` FOREIGN KEY (`baseProduct_id`) REFERENCES `BaseProduct` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `CommissionTypeAssociation_ProductOption` ( `CommissionTypeAssociationEntity_id` bigint(20) NOT NULL, `ProductOptionList_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation_ProductOption` ADD CONSTRAINT `FK_CommissionTypeAss_ProductOption_parent_id` FOREIGN KEY (`CommissionTypeAssociationEntity_id`) REFERENCES `CommissionTypeAssociation` (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionTypeAssociation_ProductOption` ADD CONSTRAINT `FK_CommissionTypeAss_ProductOption_child_id` FOREIGN KEY (`ProductOptionList_id`) REFERENCES `ProductOption` (`id`);");
	}


	@SuppressWarnings("unused")
	private void startUpdate_91(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `VendorCommissionAccountingItem` (`id` bigint(20) NOT NULL, `DTYPE` VARCHAR(50) NOT NULL, `creationDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `updateDate` datetime NOT NULL, `hash` VARCHAR(50) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` ADD PRIMARY KEY (`id`), ADD UNIQUE INDEX `hash` (`hash`);");
		session.executeNonSelectingSQL("ALTER TABLE `VendorCommissionAccountingItem` CHANGE `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("CREATE TABLE `VodafoneCommissionAccountingItem` (`id` bigint(20) NOT NULL, `VOIDAbrechnung` VARCHAR(255) NULL, `VOIDAktivierung` VARCHAR(255) NULL, `AdressdatenVOIDAktivierung` VARCHAR(255) NULL, `Provisionskategorie` VARCHAR(255) NULL, `DatumAuftrag` VARCHAR(255) NULL, `DatumPreClearing` VARCHAR(255) NULL, `DatumAktivierung` VARCHAR(255) NULL, `DatumStornierung` VARCHAR(255) NULL, `Kundenname` VARCHAR(255) NULL, `RufnummerBarcodeWebOrderID` VARCHAR(255) NULL, `Verkaeufer` VARCHAR(255) NULL, `Provisionsmodell` VARCHAR(255) NULL, `Provisionsart` VARCHAR(255) NULL, `Artikelnummer` VARCHAR(255) NULL, `Artikelbezeichnung` VARCHAR(255) NULL, `IMEI` VARCHAR(255) NULL, `NettobetragEinzelEUR` VARCHAR(255) NULL, `Stornierungscode` VARCHAR(255) NULL, `Stornierungsgrund` VARCHAR(255) NULL, `UrspruenglicheAbrechnungsnummer` VARCHAR(255) NULL, `Kommentar` VARCHAR(255) NULL, `Kundennummer` VARCHAR(255) NULL, `Transaktionstyp` VARCHAR(255) NULL, `Tarifbeschreibung` VARCHAR(255) NULL, `Tarifcode` VARCHAR(255) NULL, `Preisplan` VARCHAR(255) NULL, `Zusatzdienstbeschreibung` VARCHAR(255) NULL, `Zusatzdienstcode` VARCHAR(255) NULL, `HardwareLieferscheinNr` VARCHAR(255) NULL, `HardwareLieferscheinDatum` VARCHAR(255) NULL, `CallYaTyp` VARCHAR(255) NULL, `SIMKartenNr` VARCHAR(255) NULL, `AuftragsVO` VARCHAR(255) NULL, `Vertrag` VARCHAR(255) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `VodafoneCommissionAccountingItem` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `VodafoneCommissionAccountingItem` ADD CONSTRAINT `FK_VodafoneCommissionAccountingItem_id` FOREIGN KEY (`id`) REFERENCES `VendorCommissionAccountingItem` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_90(UnitOfWork session) throws Exception{

		Vector v = session.executeSQL("Select ID from `ACCOUNTTYPEENTITY` WHERE `LOGINNAME` = \"supervisor\"");

		if(v.size() == 0){
			return;
		}

		ArrayRecord r = (ArrayRecord) v.get(0);

		session.executeNonSelectingSQL("UPDATE `User` SET `supervisor` = 1 WHERE `accountTypeEntity_id` = \""+r.get("ID")+"\"");
	}

	@SuppressWarnings("unused")
	private void startUpdate_89(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("INSERT INTO `UserGroup` (`id`, `creationDate`, `effectiveDate`, `enabled`, `expiryDate`, `updateDate`, `name`, `deleted`) VALUES (NULL, NOW(), NULL, '1', NULL, NOW(), 'Mitarbeiter', '0');");
		session.executeNonSelectingSQL("UPDATE `User` SET `userGroup_id` = LAST_INSERT_ID();");

		session.executeNonSelectingSQL("INSERT INTO `UserGroupPermission` (`userGroup_id`, `resource`, `command`, `permission`) VALUES"+
		"(LAST_INSERT_ID(), 'calendar', 'read', 1),"+
		"(LAST_INSERT_ID(), 'contract', 'create', 1),"+
		"(LAST_INSERT_ID(), 'contract', 'edit_own', 1),"+
		"(LAST_INSERT_ID(), 'contract', 'read', 1),"+
		"(LAST_INSERT_ID(), 'customer', 'create', 1),"+
		"(LAST_INSERT_ID(), 'customer', 'delete', 1),"+
		"(LAST_INSERT_ID(), 'customer', 'edit', 1),"+
		"(LAST_INSERT_ID(), 'customer', 'read', 1),"+
		"(LAST_INSERT_ID(), 'order', 'create', 1),"+
		"(LAST_INSERT_ID(), 'order', 'edit_own', 1),"+
		"(LAST_INSERT_ID(), 'order', 'read', 1),"+
		"(LAST_INSERT_ID(), 'support', 'read', 1);");


	}

	@SuppressWarnings("unused")
	private void startUpdate_88(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `supervisor` TINYINT(1) NOT NULL;");
	}


	@SuppressWarnings("unused")
	private void startUpdate_87(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `UserGroup` (`id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `effectiveDate` datetime DEFAULT NULL, `enabled` tinyint(1) NOT NULL, `deleted` tinyint(1) NOT NULL, `expiryDate` datetime DEFAULT NULL, `updateDate` datetime NOT NULL, `name` VARCHAR(255) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `UserGroup` ADD PRIMARY KEY (`id`), ADD UNIQUE INDEX `name` (`name`)");
		session.executeNonSelectingSQL("ALTER TABLE `UserGroup` CHANGE `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT;");

		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `userGroup_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD CONSTRAINT `FK_User_userGroup_id` FOREIGN KEY (`userGroup_id`) REFERENCES `UserGroup` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `UserGroupPermission` ( `userGroup_id` bigint(20) NOT NULL, `resource` char(40) NOT NULL, `command` char(10) NOT NULL, `permission` tinyint(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `UserGroupPermission` ADD PRIMARY KEY (`userGroup_id`, `resource`, `command`);");
		session.executeNonSelectingSQL("ALTER TABLE `UserGroupPermission` ADD CONSTRAINT `FK_UserGroupPermission_userGroup_id` FOREIGN KEY (`userGroup_id`) REFERENCES `UserGroup` (`id`);");

	}

	@SuppressWarnings("unused")
	private void startUpdate_86(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `UserPermission` ( `user_id` bigint(20) NOT NULL, `resource` char(40) NOT NULL, `command` char(10) NOT NULL, `permission` tinyint(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `UserPermission` ADD PRIMARY KEY (`user_id`, `resource`, `command`);");
		session.executeNonSelectingSQL("ALTER TABLE `UserPermission` ADD CONSTRAINT `FK_UserPermission_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_85(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `weeklyWorkingDays` INT(1) NOT NULL;");
	}


	@SuppressWarnings("unused")
	private void startUpdate_84(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `vacationDays` INT(3) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_83(UnitOfWork session) throws Exception{
		propertyController.updateStringProperty("yearViewCalendarId", UUID.randomUUID().toString());
	}

	@SuppressWarnings("unused")
	private void startUpdate_82(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `weeklyWorkingTime` INT(4) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_81(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD `workingTime` TINYINT(1) NOT NULL;");

		session.executeNonSelectingSQL("UPDATE `EventType` SET `workingTime` = 1 WHERE `shortName` IN ('A', 'S', 'SG', 'K', 'U');");
	}

	@SuppressWarnings("unused")
	private void startUpdate_80(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Event` ADD `pauseLength` INT(10) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Event` ADD `workingTime` TINYINT(1) NOT NULL;");

		session.executeNonSelectingSQL("UPDATE `Event` SET `workingTime` = 1;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_79(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD `note` TEXT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_78(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("UPDATE `EventType` SET `allDay` = 1 WHERE `shortName` IN ('K');");
	}

	@SuppressWarnings("unused")
	private void startUpdate_77(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD `externalEvent` TINYINT(1) NOT NULL;");
		session.executeNonSelectingSQL("UPDATE `EventType` SET `externalEvent` = 1 WHERE `shortName` IN ('S','SG','K','U');");
	}

	@SuppressWarnings("unused")
	private void startUpdate_76(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Event` ADD `eventType_id` BIGINT(20) NOT NULL, ADD INDEX (`eventType_id`);");
		session.executeNonSelectingSQL("UPDATE `Event` SET `eventType_id` = 1;");
		session.executeNonSelectingSQL("ALTER TABLE `Event` ADD CONSTRAINT `FK_Event_eventType_id` FOREIGN KEY (`eventType_id`) REFERENCES `EventType` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_75(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD `allDay` TINYINT(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD `preferedEventStartTime` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD `preferedEventEndTime` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD `marginTime` INT(3) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_74(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("INSERT INTO `EventType` (`creationDate`, `updateDate`, `deleted`, `uuid`, `title`, `shortName`, `multiUserEvent`, `editable`) VALUES (NOW(), NOW(), '0', '"+UUID.randomUUID().toString()+"', 'Arbeit', 'A', '0', '0')");
		session.executeNonSelectingSQL("INSERT INTO `EventType` (`creationDate`, `updateDate`, `deleted`, `uuid`, `title`, `shortName`, `multiUserEvent`, `editable`) VALUES (NOW(), NOW(), '0', '"+UUID.randomUUID().toString()+"', 'Schule', 'S', '0', '0')");
		session.executeNonSelectingSQL("INSERT INTO `EventType` (`creationDate`, `updateDate`, `deleted`, `uuid`, `title`, `shortName`, `multiUserEvent`, `editable`) VALUES (NOW(), NOW(), '0', '"+UUID.randomUUID().toString()+"', 'Schulung', 'SG', '0', '0')");
		session.executeNonSelectingSQL("INSERT INTO `EventType` (`creationDate`, `updateDate`, `deleted`, `uuid`, `title`, `shortName`, `multiUserEvent`, `editable`) VALUES (NOW(), NOW(), '0', '"+UUID.randomUUID().toString()+"', 'Krank', 'K', '0', '0')");
		session.executeNonSelectingSQL("INSERT INTO `EventType` (`creationDate`, `updateDate`, `deleted`, `uuid`, `title`, `shortName`, `multiUserEvent`, `editable`) VALUES (NOW(), NOW(), '0', '"+UUID.randomUUID().toString()+"', 'Urlaub', 'U', '0', '0')");
		session.executeNonSelectingSQL("INSERT INTO `EventType` (`creationDate`, `updateDate`, `deleted`, `uuid`, `title`, `shortName`, `multiUserEvent`, `editable`) VALUES (NOW(), NOW(), '0', '"+UUID.randomUUID().toString()+"', 'Meeting', 'M', '1', '0')");
	}


	@SuppressWarnings("unused")
	private void startUpdate_73(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `EventType` ( `id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `updateDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `uuid` varchar(255) NOT NULL, `title` varchar(255) NOT NULL, `shortName` varchar(10) NOT NULL, `multiUserEvent` tinyint(1) NOT NULL, `editable` tinyint(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `EventType` ADD PRIMARY KEY (`id`), ADD UNIQUE INDEX `EventType_uuid` (`uuid`), ADD UNIQUE INDEX `EventType_title` (`title`), ADD UNIQUE INDEX `EventType_shortName` (`shortName`);");
		session.executeNonSelectingSQL("ALTER TABLE `EventType` CHANGE `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_72(UnitOfWork session) throws Exception{
		propertyController.updateStringProperty("externalCalendarId", UUID.randomUUID().toString());
	}

	@SuppressWarnings("unused")
	private void startUpdate_71(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_start_0` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_start_1` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_start_2` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_start_3` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_start_4` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_start_5` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_start_6` CHAR(5) NULL;");

		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_end_0` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_end_1` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_end_2` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_end_3` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_end_4` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_end_5` CHAR(5) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `businessHours_end_6` CHAR(5) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_70(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategory` ADD `color` VARCHAR(30) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_69(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("TRUNCATE RELATIONSHIPIDENTITYTYPEENTITY");
		session.executeNonSelectingSQL("TRUNCATE RELATIONSHIPTYPEENTITY");
		session.executeNonSelectingSQL("TRUNCATE PERMISSIONTYPEENTITY");

		session.executeNonSelectingSQL("DELETE FROM `ATTRIBUTEDTYPEENTITY` WHERE DTYPE = 'RelationshipTypeEntity'");
	}

	@SuppressWarnings("unused")
	private void startUpdate_68(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductOption` ADD `forceExtensionOfTheTerm` TINYINT(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_67(UnitOfWork session) throws Exception{

		// substract tax from all contracts
		session.executeNonSelectingSQL("UPDATE `ContractItem` set `productPrice` = `productPrice` / 1.19");
		session.executeNonSelectingSQL("UPDATE `ContractItem` set `productTax` = 19");

		session.executeNonSelectingSQL("UPDATE `BaseContract` set `finalVat` = `finalPrice` - ( `finalPrice` / 1.19 )");
		session.executeNonSelectingSQL("UPDATE `BaseContract` set `finalPrice` = `finalPrice` / 1.19 ");
		session.executeNonSelectingSQL("UPDATE `BaseContract` set `revenueStepVat` = `revenueStepPrice` - ( `revenueStepPrice` / 1.19 )");
		session.executeNonSelectingSQL("UPDATE `BaseContract` set `revenueStepPrice` = `revenueStepPrice` / 1.19 ");

	}

	@SuppressWarnings("unused")
	private void startUpdate_66(UnitOfWork session) throws Exception{

		// substract tax from all products
		session.executeNonSelectingSQL("UPDATE `BaseProduct` set `price` = `price` / 1.19");

	}

	@SuppressWarnings("unused")
	private void startUpdate_65(UnitOfWork session) throws Exception{

		// BaseContract
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `subsidyOrder_id` BIGINT(20) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD INDEX(`subsidyOrder_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `revenueStepTax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `finalTax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `subsidyBudgetary` decimal(10,4) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `revenueStepVat` decimal(10,4) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `finalVat` decimal(10,4) NOT NULL;");


		// BaseProduct
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD `tax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD `vat` decimal(10,4) NULL;");

		// CommissionCache
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` ADD `tax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` ADD `vat` decimal(10,4) NULL;");

		// ContractItem
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` ADD `productTax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` ADD `voTax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` ADD `productVat` decimal(10,4) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` ADD `voVat` decimal(10,4) NULL;");
		session.executeNonSelectingSQL("UPDATE `ContractItem` set `productVat` = `productPrice` * 0.19;");

		// OrderEntity
		session.executeNonSelectingSQL("ALTER TABLE `OrderEntity` ADD `tax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `OrderEntity` ADD `vat` decimal(10,4) NULL;");

		// ProductCommissionVOTypeProductRelation
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` ADD `tax` decimal(5,2) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` ADD `vat` decimal(10,4) NULL;");

		// OrderItem
		session.executeNonSelectingSQL("ALTER TABLE `OrderItem` ADD `blockedItem` TINYINT(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `OrderItem` ADD `vat` decimal(10,4) NULL;");


	}



	@SuppressWarnings("unused")
	private void startUpdate_64(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `OrderItem` ADD `unitPrice` decimal(10,2) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_63(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `serialRequired` tinyint(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_62(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `OrderEntity` ( `id` bigint(20) NOT NULL, `info` varchar(255) DEFAULT NULL, `introduction` varchar(255) DEFAULT NULL, `commission` decimal(10,4) NOT NULL, `points` decimal(10,4) NOT NULL, `price` decimal(10,4) NOT NULL) ENGINE=InnoDB;");
		session.executeNonSelectingSQL("ALTER TABLE `OrderEntity` ADD PRIMARY KEY (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `OrderItem` ( `id` bigint(20) NOT NULL, `creationDate` datetime NOT NULL, `updateDate` datetime NOT NULL, `deleted` tinyint(1) NOT NULL, `title` varchar(255) NOT NULL, `quantity` int(11) NOT NULL, `serial` varchar(255) NULL, `tax` decimal(5,2) NULL, `commission` decimal(10,4) NOT NULL, `points` decimal(10,4) NOT NULL, `price` decimal(10,4) NOT NULL, `product_id` bigint(20) NULL, `order_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `OrderItem` ADD PRIMARY KEY (`id`), ADD KEY `FK_OrderItem_product_id` (`product_id`), ADD KEY `FK_OrderItem_order_id` (`order_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `OrderItem` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_61(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation_ProductOption` DROP `deleted`");
	}

	@SuppressWarnings("unused")
	private void startUpdate_60(UnitOfWork session) throws Exception{
		// empty!
	}

	@SuppressWarnings("unused")
	private void startUpdate_59(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `bookableAsForeignWare` tinyint(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_58(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `ean` varchar(40) NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `purchasePrice` decimal(10,2) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Product` ADD `description` text NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_57(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD `taxRate_id` BIGINT(20) NOT NULL, ADD INDEX (`taxRate_id`);");
		session.executeNonSelectingSQL("UPDATE `BaseProduct` SET `taxRate_id` = 1;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD CONSTRAINT `FK_TaxRate_id` FOREIGN KEY (`taxRate_id`) REFERENCES `TaxRate` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_56(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `TaxRate` (`id` bigint(20) NOT NULL, `deleted` tinyint(1) NOT NULL, `creationDate` datetime NOT NULL, `updateDate` datetime NOT NULL, `tax` decimal(5,2) NOT NULL, `defaultTaxRate` TINYINT(1) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `TaxRate` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `TaxRate` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;");
		session.executeNonSelectingSQL("INSERT INTO `TaxRate` (`deleted`, `creationDate`, `updateDate`, `tax`, `defaultTaxRate`) VALUES(0, NOW(), NOW(), 19.00, 1);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_55(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` ADD `subsidyBudgetary` decimal(10,2) NOT NULL;");
	}


	/*
	 *  Updates Lower V 0.1.16
	 */


	@SuppressWarnings("unused")
	private void startUpdate_54(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `VOTypeCommissionRevenueLevel` ADD `deleted` tinyint(1) NOT NULL;");
	}


	@SuppressWarnings("unused")
	private void startUpdate_53(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `ProductOption` ADD `weigth` int(10) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_52(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ContractRetailItem` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Customer` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ExternalCustomer` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `File` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `JobTitle` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategory` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation_ProductOption` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `RevenueLevel` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `SecureLink` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `UserProperty` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Vendor` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `VO` ADD `deleted` tinyint(1) NOT NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `VOType` ADD `deleted` tinyint(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_51(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` MODIFY `pointsPerCommission` DECIMAL(10,4) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_50(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `pointsPerCommission` DECIMAL(10,4) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_49(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `ProductCommissionVOTypeProductRelation_ProductOption` ( `ProductCommissionVOTypeProductRelationEntity_id` bigint(20) NOT NULL, `ProductOptionList_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation_ProductOption` ADD PRIMARY KEY (`ProductCommissionVOTypeProductRelationEntity_id`,`ProductOptionList_id`), ADD KEY `PrdctCmmssnVTypPrdctRltonProductOptionPrdctptnLstd` (`ProductOptionList_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation_ProductOption` ADD CONSTRAINT `PrdctCmmssnVTypPrdctRltonProductOptionPrdctptnLstd` FOREIGN KEY (`ProductOptionList_id`) REFERENCES `BaseProduct` (`id`), ADD CONSTRAINT `PrdctCmmssnVTypPrdctRPrdctCmmssnVTypPrdctRltnnttyd` FOREIGN KEY (`ProductCommissionVOTypeProductRelationEntity_id`) REFERENCES `ProductCommissionVOTypeProductRelation` (`id`);");

		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` ADD `effectiveDate` datetime NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` ADD `expiryDate` datetime NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` ADD `enabled` tinyint(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_48(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `creationDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `effectiveDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `enabled`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `expiryDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `updateDate`; ");
	}


	@SuppressWarnings("unused")
	private void startUpdate_47(UnitOfWork session) throws Exception{


		session.executeNonSelectingSQL("TRUNCATE TABLE `Event`");

		session.executeNonSelectingSQL("ALTER TABLE `Event` MODIFY `id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP FOREIGN KEY  `FK_Event_customer_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP FOREIGN KEY  `FK_Event_owner_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP FOREIGN KEY  `FK_Event_shop_id`;");

		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `owner_id`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `shop_id`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Event` DROP `customer_id`; ");

		session.executeNonSelectingSQL("ALTER TABLE `Event` ADD CONSTRAINT `FK_Event_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");

	}

	@SuppressWarnings("unused")
	private void startUpdate_46(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("DROP TABLE `Customer_Note`;");
	}


	@SuppressWarnings("unused")
	private void startUpdate_45(UnitOfWork session) throws Exception{


		session.executeNonSelectingSQL("UPDATE Customer set node_id = null");

		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");
		session.executeNonSelectingSQL("TRUNCATE TABLE `Customer_Note`");
		session.executeNonSelectingSQL("TRUNCATE TABLE `Note`");
		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");

		session.executeNonSelectingSQL("ALTER TABLE `Note` MODIFY `id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Note` DROP `creationDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Note` DROP `updateDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `Note` ADD CONSTRAINT `FK_Note_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");

	}

	@SuppressWarnings("unused")
	private void startUpdate_44(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD CONSTRAINT `FK_BaseContract_id` FOREIGN KEY (`id`) REFERENCES `Activity` (`id`);");

	}



	@SuppressWarnings("unused")
	private void startUpdate_43(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `customer_id`; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `shop_id`; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `user_id`; ");

	}

	@SuppressWarnings("unused")
	private void startUpdate_42(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` MODIFY `id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP FOREIGN KEY  `FK_BaseContract_customer_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP FOREIGN KEY  `FK_BaseContract_shop_id`;");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP FOREIGN KEY  `FK_BaseContract_user_id`;");

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `DTYPE`; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `creationDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `effectiveDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `enabled`; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `expiryDate`; ");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` DROP `updateDate`; ");

	}

	@SuppressWarnings("unused")
	private void startUpdate_41(UnitOfWork session) throws Exception{


		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=0;");

		session.executeNonSelectingSQL("TRUNCATE TABLE `BaseContract_ContractItem`");
		session.executeNonSelectingSQL("TRUNCATE TABLE `PhoneContract`");
		session.executeNonSelectingSQL("TRUNCATE TABLE `BaseContract`");
		session.executeNonSelectingSQL("TRUNCATE TABLE `ContractItem`");
		session.executeNonSelectingSQL("TRUNCATE TABLE `ContractRetailItem`");

		session.executeNonSelectingSQL("SET FOREIGN_KEY_CHECKS=1;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_40(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Activity` CHANGE `customer_id` `customer_id` bigint(20) NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` CHANGE `user_id` `user_id` bigint(20) NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` CHANGE `shop_id` `shop_id` bigint(20) NULL");
	}

	@SuppressWarnings("unused")
	private void startUpdate_39(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Activity` MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=1;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_38(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD `hideInTimeline` tinyint(1) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_37(UnitOfWork session) throws Exception{
		// no UPDATE
	}

	@SuppressWarnings("unused")
	private void startUpdate_36(UnitOfWork session) throws Exception{

		session.executeNonSelectingSQL("CREATE TABLE `Activity` (`id` bigint(20) NOT NULL, `DTYPE` varchar(31) DEFAULT NULL, `creationDate` datetime NOT NULL, `effectiveDate` datetime DEFAULT NULL, `enabled` tinyint(1) NOT NULL, `expiryDate` datetime DEFAULT NULL, `updateDate` datetime NOT NULL, `customer_id` bigint(20) NOT NULL, `shop_id` bigint(20) NOT NULL, `user_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD PRIMARY KEY (`id`), ADD KEY `FK_Activity_shop_id` (`shop_id`), ADD KEY `FK_Activity_customer_id` (`customer_id`), ADD KEY `FK_Activity_user_id` (`user_id`);");
		session.executeNonSelectingSQL("ALTER TABLE `Activity` ADD CONSTRAINT `FK_Activity_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`id`), ADD CONSTRAINT `FK_Activity_shop_id` FOREIGN KEY (`shop_id`) REFERENCES `Shop` (`id`), ADD CONSTRAINT `FK_Activity_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);");

	}

	@SuppressWarnings("unused")
	private void startUpdate_35(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Event` ADD UNIQUE (`uuid`)");
	}

	@SuppressWarnings("unused")
	private void startUpdate_34(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("UPDATE `BaseContract` SET `DTYPE` = 'PhoneContractEntity' WHERE `DTYPE` = 'CellPhoneContractEntity'");
	}

	@SuppressWarnings("unused")
	private void startUpdate_33(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `PrePaidTariff` DROP FOREIGN KEY  `FK_PrePaidTariff_id`;");

		session.executeNonSelectingSQL("ALTER TABLE `PrePaidTariff` ADD CONSTRAINT `FK_PrePaidTariff_id` FOREIGN KEY (`id`) REFERENCES `Tariff` (`id`);");
	}


	@SuppressWarnings("unused")
	private void startUpdate_32(UnitOfWork session) throws Exception{

		Vector v = session.executeSQL("Select id from `BaseProduct` WHERE `DTYPE` = \"TariffEntity\"");

		if(v.size() == 0){
			return;
		}

		Iterator<Object> i = v.iterator();
		ArrayRecord r;
		while(i.hasNext()){
			r = (ArrayRecord) i.next();

			session.executeNonSelectingSQL("INSERT INTO `CellPhoneTariff` (`id`) VALUES ('"+r.get("id")+"');");
		}


		// change all Tarif to CellPhoneTariff
		session.executeNonSelectingSQL("UPDATE `BaseProduct` SET `DTYPE` = \"CellPhoneTariffEntity\" WHERE `DTYPE` = \"TariffEntity\";");
	}

	@SuppressWarnings("unused")
	private void startUpdate_31(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `Product` DROP `price`;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_30(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `CellPhoneTariff` ( `id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `CellPhoneTariff` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `CellPhoneTariff` ADD CONSTRAINT `FK_CellPhoneTariff_id` FOREIGN KEY (`id`) REFERENCES `Tariff` (`id`);");

		session.executeNonSelectingSQL("CREATE TABLE `LandlineTariff` ( `id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `LandlineTariff` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `LandlineTariff` ADD CONSTRAINT `FK_LandlineTariff_id` FOREIGN KEY (`id`) REFERENCES `Tariff` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_29(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("CREATE TABLE `PrePaidTariff` ( `id` bigint(20) NOT NULL, `generatedRevenue` decimal(10,4) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
		session.executeNonSelectingSQL("ALTER TABLE `PrePaidTariff` ADD PRIMARY KEY (`id`);");
		session.executeNonSelectingSQL("ALTER TABLE `PrePaidTariff` ADD CONSTRAINT `FK_PrePaidTariff_id` FOREIGN KEY (`id`) REFERENCES `BaseProduct` (`id`);");
	}

	@SuppressWarnings("unused")
	private void startUpdate_28(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `orderNumber` VARCHAR(255) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_27(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("RENAME TABLE `CellPhoneContract` TO `PhoneContract` ");
	}

	@SuppressWarnings("unused")
	private void startUpdate_26(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` ADD `generatedRevenue` DECIMAL(10,4) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_25(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `VOTypeCommissionRevenueLevel` ADD `points` DECIMAL(10,4) NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_24(UnitOfWork session) throws Exception{
		session.executeNonSelectingSQL("ALTER TABLE `VOType` ADD `pointsPerCommission` DECIMAL(10,4) NOT NULL;");
	}

	@SuppressWarnings("unused")
	private void startUpdate_23(UnitOfWork session) throws Exception{


		Vector v = session.executeSQL("Select * from JobTitle");

		if(v.size() == 0){
			// no JobTile insert one
			session.executeNonSelectingSQL("INSERT INTO `JobTitle` (`creationDate`, `name`, `updateDate`) VALUES (NOW(), 'Mitarbeiter', NOW())");
			v = session.executeSQL("Select * from JobTitle");
		}

		if(v.size() == 0){
			throw new Exception("JobTitle update error");
		}

		ArrayRecord r = (ArrayRecord) v.firstElement();

		if(r.get("id") == null){
			throw new Exception("JobTitle update error");
		}

		session.executeNonSelectingSQL("UPDATE `User` set `jobTitle_id` = "+r.get("id")+" WHERE `jobTitle_id` IS NULL");

		session.executeNonSelectingSQL("ALTER TABLE `VOTypeCommissionRevenueLevel` CHANGE `revenueLevelVOType_id` `revenueLevelVOType_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VOTypeCommissionRevenueLevel` CHANGE `revenueLevel_id` `revenueLevel_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VOTypeCommissionRevenueLevel` CHANGE `commission` `commission` decimal(10,4) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `VOType` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VOType` CHANGE `name` `name` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VOType` CHANGE `shortName` `shortName` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VOType` CHANGE `vendor_id` `vendor_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `VO` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VO` CHANGE `name` `name` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VO` CHANGE `number` `number` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VO` CHANGE `voType_id` `voType_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VO` CHANGE `shop_id` `shop_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `VO` CHANGE `vendor_id` `vendor_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `Vendor` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Vendor` CHANGE `name` `name` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Vendor` CHANGE `defaultSelection` `defaultSelection` tinyint(1) NOT NULL");


		session.executeNonSelectingSQL("ALTER TABLE `UserProperty` CHANGE `user_id` `user_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `User` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `User` CHANGE `accountTypeEntity_id` `accountTypeEntity_id` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `User` CHANGE `jobTitle_id` `jobTitle_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `Shop` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` CHANGE `name` `name` varchar(255) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `SecureUserLink` CHANGE `user_id` `user_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `SecureLink` CHANGE `code` `code` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `SecureLink` CHANGE `operation` `operation` varchar(255) NOT NULL");


		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Event` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCategory` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `SecureLink` CHANGE `enabled` `enabled` tinyint(1) NOT NULL");

	}

	@SuppressWarnings("unused")
	private void startUpdate_22(UnitOfWork session){


		session.executeNonSelectingSQL("ALTER TABLE `RevenueLevel` CHANGE `name` `name` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `RevenueLevel` CHANGE `scaleFrom` `scaleFrom` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `RevenueLevel` CHANGE `scaleTo` `scaleTo` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `RevenueLevel` CHANGE `vendor_id` `vendor_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` CHANGE `commission` `commission` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` CHANGE `points` `points` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ProductCommissionVOTypeProductRelation` CHANGE `price` `price` decimal(10,4) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `ProductCategory` CHANGE `name` `name` varchar(255) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `ProductOption` CHANGE `revenueCommissionRelevant` `revenueCommissionRelevant` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ProductOption` CHANGE `percentagePrice` `percentagePrice` tinyint(1) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `Product` CHANGE `price` `price` decimal(10,4) NOT NULL");

	}


	@SuppressWarnings("unused")
	private void startUpdate_21(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `JobTitle` CHANGE `name` `name` varchar(255) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `File` CHANGE `code` `code` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `File` CHANGE `mimeType` `mimeType` varchar(255) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `ExternalCustomer` CHANGE `customerId` `extCustomerId` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ExternalCustomer` CHANGE `password` `password` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ExternalCustomer` CHANGE `customer_id` `customer_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `Event` CHANGE `allDay` `allDay` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Event` CHANGE `uuid` `uuid` varchar(40) NOT NULL");

	}


	@SuppressWarnings("unused")
	private void startUpdate_20(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `DBFile` CHANGE `mimeType` `imageFile` longblob NOT NULL");

	}

	@SuppressWarnings("unused")
	private void startUpdate_19(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `ContractRetailItem` CHANGE `product_id` `product_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractRetailItem` CHANGE `contract_id` `contract_id` bigint(20) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `name` `name` varchar(255) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `baseProduct_id` `baseProduct_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `vendor_id` `vendor_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `productCategory_id` `productCategory_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `productPoints` `productPoints` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `productCommission` `productCommission` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `productPrice` `productPrice` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `voPoints` `voPoints` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `voCommission` `voCommission` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `ContractItem` CHANGE `voPrice` `voPrice` decimal(10,4) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` CHANGE `year` `year` int(11) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` CHANGE `month` `month` int(11) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` CHANGE `pointGoal` `pointGoal` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` CHANGE `commission` `commission` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` CHANGE `points` `points` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `CommissionCache` CHANGE `price` `price` decimal(10,4) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `CellPhoneContract` CHANGE `callingNumber` `callingNumber` varchar(255) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `extensionOfTheTermPermission` `extensionOfTheTermPermission` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `leadTime` `leadTime` int(11) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `finalCommission` `finalCommission` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `finalPoints` `finalPoints` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `finalPrice` `finalPrice` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `revenueStepCommission` `revenueStepCommission` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `revenueStepPoints` `revenueStepPoints` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `revenueStepPrice` `revenueStepPrice` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `tariff_id` `tariff_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `customer_id` `customer_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `shop_id` `shop_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `user_id` `user_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `vo_id` `vo_id` bigint(20) NOT NULL");

	}


	@SuppressWarnings("unused")
	private void startUpdate_18(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `BaseContract` CHANGE `extensionOfTerm` `extensionOfTheTerm` tinyint(1) NOT NULL");

		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` CHANGE `commission` `commission` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` CHANGE `points` `points` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` CHANGE `price` `price` decimal(10,4) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` CHANGE `productCategory_id` `productCategory_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `BaseProduct` CHANGE `vendor_id` `vendor_id` bigint(20) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Tariff` CHANGE `extensionOfTheTerm` `extensionOfTheTerm` tinyint(1) NOT NULL");
		session.executeNonSelectingSQL("ALTER TABLE `Tariff` CHANGE `minimumTermOfContract` `minimumTermOfContract` INT(11) NOT NULL");

	}


	@SuppressWarnings("unused")
	private void startUpdate_17(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `User` CHANGE `color` `color` VARCHAR(30) CHARACTER SET latin1 COLLATE latin1_bin NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Shop` CHANGE `color` `color` VARCHAR(30) CHARACTER SET latin1 COLLATE latin1_bin NULL;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_16(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `Shop` ADD `color` CHAR(7) NULL ;");
		session.executeNonSelectingSQL("ALTER TABLE `User` ADD `color` CHAR(7) NULL ;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_15(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `Customer` CHANGE `lastname` `lastname` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_bin NULL;");

	}

	@SuppressWarnings("unused")
	private void startUpdate_14(UnitOfWork session){

		session.executeNonSelectingSQL("ALTER TABLE `Customer` CHANGE `commercialRegisterId` `commercialRegisterId` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_bin NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Customer` CHANGE `contactPerson` `contactPerson` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_bin NULL;");
		session.executeNonSelectingSQL("ALTER TABLE `Customer` CHANGE `firstname` `firstname` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_bin NULL;");

	}



	private void startUpdates(){

		long dbVersion = propertyController.getLongProperty(PROPERTY_DB_VERSION, 0);

		if(dbVersion >= DB_VERSION){
			System.err.println("Abort DB Update! DB alreasy in Version: "+dbVersion);
			return;
		}

		Method method;
		long activeVersion = dbVersion;
		Session session = em.unwrap(Session.class);
		UnitOfWork unitOfWork = session.acquireUnitOfWork();

		while(activeVersion < DB_VERSION){
			activeVersion++;

			System.out.println("start DB Update: "+activeVersion);

			try {

				method = this.getClass().getDeclaredMethod("startUpdate_"+activeVersion,UnitOfWork.class);

			} catch (NoSuchMethodException | SecurityException e ) {

				// METHOD NOT FOUND
				System.out.println("DB Update not found: "+activeVersion);
				continue;

			}

			try{

				method.invoke(this,unitOfWork);

//				unitOfWork.commitAndResume();

				propertyController.updateLongProperty(PROPERTY_DB_VERSION, activeVersion);

			} catch (Exception e ) {
				// UPDATE METHOD CALL FAILED
//				unitOfWork.revertAndResume();
				System.out.println("DB Update error in Version: "+activeVersion);
				e.printStackTrace();
				return;

			}
		}

//		unitOfWork.commitAndResume();

		System.out.println("DB Update finished!");
	}

	@Override
	@PostConstruct
	public void checkForDBUpdates(){
		System.out.println("DBController | checkForDBUpdates");

//		startPriorityUpdate();
//
//
//		long dbVersion = propertyController.getLongProperty(PROPERTY_DB_VERSION, 0);
//
//
//		System.out.println("DB Version: "+dbVersion);
//
//		if(dbVersion < DB_VERSION){
//			System.out.println("DB neets update!");
//			startUpdates();
//		}
//		else{
//			System.out.println("DB is up to date!");
//		}

	}

//	@Override
//	public String getDBVersion(){
//		return String.valueOf( propertyController.getLongProperty(PROPERTY_DB_VERSION, 0) );
//	}

	@Override
	public boolean isDBUpToDate(){
		long dbVersion = propertyController.getLongProperty(PROPERTY_DB_VERSION, 0);

		if(dbVersion >= DB_VERSION){
			return true;
		}
		else
			return false;
	}
	
//	@Override
	private void resetTrigger() throws Exception{
		

		Session session = em.unwrap(Session.class);
		
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_InvoiceEntity_number`;");
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_OrderEntity_number`;");
		session.executeNonSelectingSQL("DROP TRIGGER `trigger_StockPickslip_number`;");
		
		
		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_InvoiceEntity_number` BEFORE INSERT ON `Invoice` FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),100000) + 1 FROM `Invoice`)");
		
		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_OrderEntity_number` BEFORE INSERT ON `OrderEntity` FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),200000) + 1 FROM `OrderEntity`);");

		session.executeNonSelectingSQL("CREATE TRIGGER `trigger_StockPickslip_number` BEFORE INSERT ON `StockPickslip` FOR EACH ROW SET NEW.number = (SELECT COALESCE(Max(number),300000) + 1 FROM `StockPickslip`);");
	}
}
