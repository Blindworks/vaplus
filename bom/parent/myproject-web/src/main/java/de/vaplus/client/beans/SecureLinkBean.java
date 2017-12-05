package de.vaplus.client.beans;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.vaplus.api.SecureLinkControllerInterface;
import de.vaplus.api.UserControllerInterface;
import de.vaplus.api.entity.SecureLink;
import de.vaplus.api.entity.SecureUserLink;
import de.vaplus.client.util.picketlink.Base64;

@ManagedBean(name="secureLinkBean")
@RequestScoped
public class SecureLinkBean {
	
	@EJB
    private SecureLinkControllerInterface secureLinkController;
	
	@EJB
    private UserControllerInterface userController;

    @EJB
    private PicketLinkBean picketLinkBean;
	
	@Inject
	private FacesContext facesContext;

    @ManagedProperty(value="#{param.c}")
    private String secureCode;

    @ManagedProperty(value="#{param.c2}")
    private String secureCode2;
    
    private static final String SECURE_CODE_HASH_SALT = "34987gnf8q47xfma78ygblwr";
    
    private String secureCodeHash;

	private SecureLink secureLink;
	
	// USER VARIABLES
	private String username;
	private String password;
	private String password2;

	public SecureLinkBean() {

	}

	public String getSecureCode(){
		return secureCode;
	}
	
	public void setSecureCode(String secureCode){
		this.secureCode = secureCode;
		setSecureCodeHash(generateHash(secureCode, SECURE_CODE_HASH_SALT));
	}

	public String getSecureCode2() {
		return secureCode2;
	}

	public void setSecureCode2(String secureCode2) {
		this.secureCode2 = secureCode2;
	}
	
	public String getSecureCodeHash(){
		return secureCodeHash;
	}
	
	public void setSecureCodeHash(String secureCodeHash){
		this.secureCodeHash = secureCodeHash;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public SecureLink getSecureLink() {
    	
    	if(secureLink == null)
    		loadSecureLink(null);
    	
		return secureLink;
	}

	public void setSecureLink(SecureLink secureLink) {
		this.secureLink = secureLink;
	}
	
	public SecureUserLink getSecureUserLink() {
		
		if(getSecureLink() instanceof SecureUserLink){
			return (SecureUserLink) getSecureLink();
	    }
		return null;
	}
	
	private void clearCodes(){
		secureCode = null;
		secureCode2 = null;
		secureCodeHash = null;
		secureLink = null;
	}
	
    private void loadSecureLink(String code) {

    	if(code == null){
    		code = secureCode;
    	}
    	if(code == null){
    		code = secureCode2;
    	}
    	
        if(code == null || code.length() == 0){
        	return;
        }

	    secureLink = secureLinkController.getSecureLinkByCode(code);
    }
    
	public boolean isSecureLink(){
		
    	if(getSecureLink() != null)
    		return true;
    	
    	return false;
	}
    
    public boolean isUserChangePassword(){
    	
    	if(getSecureUserLink() == null){
    		return false;
    	}
    	
    	if(getSecureUserLink().getOperation().equalsIgnoreCase(SecureLinkControllerInterface.OPERATION_CHANGE_PASSWORD))
    		return true;
    	
    	return false;
    }
    
    public boolean isSetUserLoginName(){
    	if(!isUserChangePassword())
    		return false;
    	
    	return ! userController.isLoginNameSet(getSecureUserLink().getUser());
    }
    
	public String callOperation(){
		
		userController.setLoginPassword(getSecureUserLink().getUser(), password);

		// check Secure Link!
		if(secureCodeHash.compareTo(generateHash(secureCode2,SECURE_CODE_HASH_SALT)) != 0){
	    	System.err.println("#### HASH NOT OK ###");
	    	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Fehler in Datenprüfung!", null));
	    	clearCodes();
	    	return null;
		}
		
		if(getSecureUserLink() != null){
			
			if(getSecureUserLink().getOperation().equalsIgnoreCase(SecureLinkControllerInterface.OPERATION_CHANGE_PASSWORD)){
				
				return callChangePwd();
			}
			else{
				System.err.println("SecureLinkBean SecureUserLink operation not handled: "+getSecureUserLink().getOperation());
		    	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Fehler in Verarbeitung!", null));
			}
		}
		else{
			System.err.println("SecureLinkBean SecureLink handled!");
	    	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Fehler in Verarbeitung!", null));
		}

    	return null;
	}
	
	private String callChangePwd(){
		
		if(isSetUserLoginName()){
			
			username = username.replaceAll("[^a-zA-Z0-9\\.\\-_@]", "");
			
			if(username == null || username.length() == 0){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte Benutzername angeben!", null));
				return null;
			}
			
			if(username.length() < 4){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Benutzername muss mindestens 4 Zeichen lang sein!", null));
				return null;
			}
			
			if(! userController.isLoginNameFree(username)){
				facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Benutzername bereits vergeben!", null));
				return null;
			}
			
		}else{
			//load username to check
			username = picketLinkBean.getUserLoginName(getSecureUserLink().getUser());
		}
		
		if(password.length() == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Bitte Passwort angeben!", null));
			return null;
		}
		
		if(password.compareTo(password2) != 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Passwort Wiederholdung falsch!", null));
			return null;
		}
		
		if(username != null && password.compareTo(username) == 0){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Passwort darf nicht dem Benutzernamen entsprechen!", null));
			return null;
		}
		
		if(password.length() < 8 || userController.checkPasswordStrength(password) < 3){
			facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_WARN, "Passwort entspricht nicht den Mindestanforderungen!", null));
			return null;
		}
		

		if(isSetUserLoginName()){
//			userController.setLoginName(getSecureUserLink().getUser(), username);
			picketLinkBean.changeUserLoginName(getSecureUserLink().getUser(), username);
		}
		
		picketLinkBean.changeUserPassword(getSecureUserLink().getUser(), password);

//		userController.setLoginPassword(getSecureUserLink().getUser(), password);
		
		facesContext.addMessage(null , new FacesMessage(FacesMessage.SEVERITY_INFO, "Neue Anmeldedaten gespeichert. Sie können Sich nun anmelden.", null));
		
		secureLinkController.expireSecureLink(secureLink);
		
		return "login";
	}
	
	private String generateHash(String s, String salt){
		String rawString = salt + s;
		
		String algorithm = "SHA-512";
		String encodedString = null;
		
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
			byte[] digest = messageDigest.digest(rawString.getBytes("UTF-8"));
			encodedString = Base64.encodeBytes(digest, Base64.DONT_BREAK_LINES);
	        
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return encodedString;
	}
}
