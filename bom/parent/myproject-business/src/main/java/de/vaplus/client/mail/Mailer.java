package de.vaplus.client.mail;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Stateless
public class Mailer {
	
    @Resource(mappedName="java:jboss/mail/NoReply")
    private Session mailSession;
    
	@Asynchronous
    public void sendMail(String receipient, String subject, String content, String replyTo){
        {
 
            try{

        		String defaultMailFrom = System.getProperty("DEFAULT_MAIL_FROM");
        		
                MimeMessage m = new MimeMessage(mailSession);
                Address from = new InternetAddress(defaultMailFrom);
                Address[] to = new InternetAddress[] {new InternetAddress(receipient) };
 
                if(replyTo != null){
                    Address[] replyToAdresses = new InternetAddress[] {new InternetAddress(replyTo) };
                	m.setReplyTo(replyToAdresses);
                }
                
                m.setFrom(from);
                m.setRecipients(Message.RecipientType.TO, to);
                m.setSubject(subject);
                m.setSentDate(new java.util.Date());
//                m.setContent(content,contentType);
                m.setText(content, "utf-8", "html");
                Transport.send(m);
                
                
            }
            catch (javax.mail.MessagingException e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
