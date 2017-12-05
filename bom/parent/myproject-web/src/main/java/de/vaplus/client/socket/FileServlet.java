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
import de.vaplus.api.entity.DBFile;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileSystemFile;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/File/*")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@EJB
	private FileControllerInterface fileController;

    public FileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String code = request.getPathInfo().replaceAll("/", "");

		File file = fileController.getFile(code);


		if(file == null){
			response.sendError(404);
			return;
		}

		response.setContentType(file.getMimeType());

		if(file instanceof DBFile){
			DBFile dbFile = (DBFile) file;

			response.setContentLength(dbFile.getImageFile().length);

			BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
			try{
				output.write(dbFile.getImageFile(), 0, dbFile.getImageFile().length);
			}catch(Exception e){
				e.printStackTrace();
			}
			output.flush();
			output.close();

		}else if(file instanceof FileSystemFile){
			String filePath = fileController.getFilePath((FileSystemFile)file);
			
			if(filePath == null){
				response.sendError(404);
				return;
			}	
			
			if(! (new java.io.File(filePath)).exists() ){
				response.sendError(404);
				return;
			}	
			
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			try{

				fis = new FileInputStream(filePath);
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


		}
	}

}
