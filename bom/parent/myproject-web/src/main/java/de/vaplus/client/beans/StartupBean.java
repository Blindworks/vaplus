package de.vaplus.client.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.vaplus.api.LicenseControllerInterface;

//@Singleton
@ManagedBean(eager = true)
@ApplicationScoped
public class StartupBean implements ServletContextListener{

	@EJB
    private LicenseControllerInterface licenseController;

	@Inject
	private FacesContext facesContext;

	public ServletContext servletContext;

	@PostConstruct
	private void startup() {
		
		checkLicense();
		System.out.println("startup DONE");

	}
	

	@Schedule( minute="0", hour="*/3", persistent=false)
	public void checkLicense() {

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String virtualServerName = servletContext.getVirtualServerName();

		licenseController.checkLicense(virtualServerName);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		System.out.println("contextInitialized ..");

		servletContext = sce.getServletContext();

		System.out.println(servletContext);

//		String realDownloadDirName = servletContext.getRealPath("/") + DOWNLOAD_DIRECTORY;
//		File downloadDir = new File(realDownloadDirName);
//		if (downloadDir.exists()) {
//		    removeOldFiles(downloadDir.listFiles());
//		}
		System.out.println("contextInitialized .. END");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}
}
