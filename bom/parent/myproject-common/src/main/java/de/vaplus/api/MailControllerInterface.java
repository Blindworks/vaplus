package de.vaplus.api;

import java.io.Serializable;

public interface MailControllerInterface extends Serializable {

	void sendMailToSupport(String subject, String body, String replyTo);


}
