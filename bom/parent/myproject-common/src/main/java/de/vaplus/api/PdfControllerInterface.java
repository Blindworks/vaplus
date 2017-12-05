package de.vaplus.api;

import java.io.File;
import java.io.Serializable;

import de.vaplus.api.entity.Invoice;

public interface PdfControllerInterface extends Serializable {

	File writeInvoiceTmpFile(Invoice invoice) throws Exception;

}
