package de.vaplus.client.applicationbeans;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import de.vaplus.api.AccountingControllerInterface;
import de.vaplus.api.AchievementControllerInterface;
import de.vaplus.api.CustomerControllerInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.ShopControllerInterface;
import de.vaplus.api.TestControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.VOControllerInterface;
import de.vaplus.api.controller.ImportControllerInterface;
import de.vaplus.api.entity.Achievement;
import de.vaplus.api.entity.AchievementTarget;
import de.vaplus.api.entity.BaseProduct;
import de.vaplus.api.entity.FileSystemFile;
import de.vaplus.api.entity.ProductCategory;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.ShopGroup;
import de.vaplus.api.entity.User;
import de.vaplus.api.entity.VO;
import de.vaplus.client.beans.CustomerBean;
import de.vaplus.client.beans.PicketLinkBean;
import de.vaplus.client.mail.Mailer;

@SessionScoped
@ManagedBean(name="testBean")
public class TestBean implements Serializable{


//	@Inject
//	private Identity identity;

/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//	@EJB(beanName="TestSessionBean")
	@EJB
	private TestControllerInterface testController;

	@EJB
	private CustomerControllerInterface customerController;

	@EJB
	private AchievementControllerInterface achievementController;

	@EJB
	private AccountingControllerInterface accountingController;

	@EJB
	private ImportControllerInterface importController;

	@EJB
	private ProductControllerInterface productController;

	@EJB
	private ShopControllerInterface shopController;

	@EJB
	private UserControllerInterface userController;

	@EJB
	private VOControllerInterface voController;

	@EJB
	private FileControllerInterface fileController;

    @EJB
    private PicketLinkBean picketLinkBean;

	@Inject
	private CustomerBean customerBean;

	@Inject
	private FacesContext facesContext;

	@EJB
	private Mailer mailer;

	private String value1,value2;

	public static Future<String> result = null;

	public TestBean() {
		// TODO Auto-generated constructor stub
	}

	public void test() throws IOException, InterruptedException, ExecutionException, NamingException{

		
//		accountingController.updateAccountingAssignment(productController.getVendorById(1L), 2016, 10);
		
		testController.test();
		
		
	}

	public String getEcho() throws UnsupportedEncodingException, NoSuchAlgorithmException{

		return "xxx";
	}

	public String getRandom(){

		return String.valueOf( Math.random() );
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		System.out.println("SET VALUE 1: "+value1 );
		this.value1 = value1;
	}

	public String getValue2() {

		return value2;
	}

	public void setValue2(String value2) {
		System.out.println("SET VALUE 2: "+value2 );
		this.value2 = value2;
	}

	public void println(String value) {
		System.out.println("TestBean write: "+(new Date()) + " " + value );
	}

}
