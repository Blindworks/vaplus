package de.vaplus;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import de.vaplus.api.ControllerClientInterface;
import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.LicenseControllerInterface;
import de.vaplus.api.ProductControllerInterface;
import de.vaplus.api.PropertyControllerInterface;
import de.vaplus.api.controller.stub.AuthResultStub;
import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.FileSystemFile;
import de.vaplus.api.interfaces.AuthResult;
import de.vaplus.client.eao.FileEao;
import de.vaplus.client.eao.VOEao;
import de.vaplus.client.entity.DBFileEntity;
import de.vaplus.client.entity.FileActivityEntity;
import de.vaplus.client.entity.FileEntity;
import de.vaplus.client.entity.FileSystemFileEntity;
import de.vaplus.helper.FileHelper;
import de.vaplus.interfaces.LocalPropertyControllerInterface;

@Stateless
public class LicenseController implements LicenseControllerInterface {

	private static final long serialVersionUID = 3686929997868375827L;
	private static final String salt = "6BVNCH4wjCYVi8wPAD^s^uMsnMLMvXeGM=aw7";

	@EJB
	private LocalPropertyControllerInterface propertyController;
	
	@EJB
	private ControllerClientInterface controllerClient;
	
	private String instanceId;
	

	@Asynchronous
	@Override
	public void checkLicense(String serverName) {

		getLicenseId(serverName);
		System.out.println("License-Check for " + serverName);

		authInstance(serverName);
	}

	private void authInstance(String serverName) {
		
		StringWriter writer = new StringWriter();
		
		JsonGenerator generator = Json.createGenerator(writer);

        generator.writeStartArray();
        
    	generator.writeStartObject();
        generator.write("serverName", serverName);
        generator.write("appVersion", propertyController.getAppVerson());
        generator.write("dbVersion", propertyController.getDBVersion());
        generator.write("licenseName", propertyController.getOldLicenseName());
        
        try {
            generator.write("hostname", java.net.InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        generator.writeEnd();
        generator.writeEnd();
    	generator.close();
		
		String param = writer.toString();
		String paramHash = getSaltedHash(param);

//		System.out.println("instanceId: "+instanceId);
//		System.out.println("param: "+param);
//		System.out.println("paramHash: "+paramHash);
		
		try{
			AuthResultStub authResult = controllerClient.authInstance(instanceId, param, paramHash);
			
	
			if(authResult != null){

				System.out.println("Instance authorized!");
				
					System.out.println("getAuthHash: "+authResult.getAuthHash());
					System.out.println("getBaseFilePath: "+authResult.getBaseFilePath());
					System.out.println("getLicenseName: "+authResult.getLicenseName());
					System.out.println("getStorageLimit: "+authResult.getStorageLimit());
					
					propertyController.setLicenseName(authResult.getLicenseName());
					propertyController.setBaseFilePath(authResult.getBaseFilePath());
					propertyController.setStorageLimit(authResult.getStorageLimit());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String getLicenseId(String serverName) {

		if (instanceId == null) {

			String key = serverName;
			instanceId = "";

			try {
				key += "_" + java.net.InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			System.out.println("getLicenseId KEY: "+key);
			
			instanceId = getSaltedHash(key);
			
//			System.out.println("getLicenseId KEY: "+instanceId);
		}

		return instanceId;
	}
	
	private String getSaltedHash(String s){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes("UTF-8"));
			byte[] bytes = md.digest(s.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}
