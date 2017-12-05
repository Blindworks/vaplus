package de.vaplus.client.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.vaplus.api.FileControllerInterface;
import de.vaplus.api.OrderControllerInterface;
import de.vaplus.api.PdfControllerInterface;
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileSystemFile;
import de.vaplus.api.entity.Invoice;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/test/doc/*")
public class DocumentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@EJB
	private FileControllerInterface fileController;

	@EJB
	private OrderControllerInterface orderController;
	
	@EJB
	private PdfControllerInterface pdfController;

    public DocumentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		/*
		 * *
		System.out.println("generate Invoice");
		
		Invoice i = orderController.getInvoiceByNumber("RE-1-100001");

		System.out.println("Invoice Nr: "+i.getNumber());
		
		response.getOutputStream();
		
		java.io.File tmpFile;
		try {
			tmpFile = pdfController.writeInvoiceTmpFile(i);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try{

			fis = new FileInputStream(tmpFile);
			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(response.getOutputStream());

			int b;
			while ((b = bis.read()) != -1) {
				bos.write(b);
			}
			bos.flush();
		}
		catch(IOException ex) {

			System.err.println(ex.getMessage());
			response.sendError(404);

		}

		finally {
			if(fis!=null) fis.close();
			if(bis!=null) bis.close();
			if(bos!=null) bos.close();
		}
        
        tmpFile.delete();
        /*
        */
	}

}
