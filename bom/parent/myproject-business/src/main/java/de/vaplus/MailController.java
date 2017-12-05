package de.vaplus;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import de.vaplus.api.MailControllerInterface;
import de.vaplus.client.mail.Mailer;


@Stateless
public class MailController implements MailControllerInterface {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2455348282709278652L;
	@Inject
	private Mailer mailer;
	
	@Override
	@Asynchronous
	public void sendMailToSupport(String subject, String body, String replyTo){
		
		/* support@vaplus.de */
		String defaultMailFrom = System.getProperty("DEFAULT_MAIL_FROM");
		
		mailer.sendMail(defaultMailFrom, subject, body, replyTo);
	}

}
