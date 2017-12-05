package de.vaplus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.naming.InitialContext;

import de.vaplus.api.AccountingControllerInterface;
import de.vaplus.api.ExceptionControllerInterface;
import de.vaplus.api.MailControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.BaseContract;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.ProductOption;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.api.entity.VOCommission;
import de.vaplus.api.entity.Vendor;
import de.vaplus.api.entity.VendorCommissionAccountingFile;
import de.vaplus.api.entity.VendorCommissionAccountingItem;
import de.vaplus.api.entity.VodafoneCommissionAccountingItem;
import de.vaplus.client.eao.AccountingEao;
import de.vaplus.client.eao.ContractEao;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentEntity;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentLineEntity;
import de.vaplus.client.entity.commission.vendor.CommissionTypeAssociationEntity;


@Stateless
public class ExceptionController implements ExceptionControllerInterface {

	private static final long serialVersionUID = -8376341270023119257L;

	@EJB
	private MailControllerInterface mailController;
	
	@EJB
	private PropertyControllerInterface propertyController;

	@Override
	public void logException(Throwable t){
		logException(t,null);
	}

	@Override
	@Asynchronous
	public void logException(Throwable t, String info){
		
//		if(t instanceof IllegalStateException)

		System.out.println("logException: "+t.getLocalizedMessage());
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		pw.println("Tenant: "+propertyController.getLicenseName()+"<br/>");
		pw.println("App Version: "+propertyController.getAppVerson()+"<br/>");
		pw.println("DB Version: "+propertyController.getDBVersion()+"<br/>");
		
		pw.println("Date: "+new Date()+"<br/>");
		pw.println("Exception: "+t.getClass().getSimpleName()+"<br/>");
		pw.println("Message: "+t.getLocalizedMessage()+"<br/>");
		
		if(info != null)
			pw.println(info);

		pw.println("<br/><br/><br/>");
		pw.println("Stacktrace: <br/>");
		
		t.printStackTrace(pw);
		
		
		mailController.sendMailToSupport(t.getClass().getSimpleName()+" in Host "+propertyController.getLicenseName(), sw.toString(), null);
	
	}
}

