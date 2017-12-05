package de.vaplus.api;

import java.io.InputStream;
import java.io.Serializable;

public interface CommissionAccountingControllerInterface extends Serializable {

	void importVendorAccountingCsv(InputStream is) throws Exception;



}
