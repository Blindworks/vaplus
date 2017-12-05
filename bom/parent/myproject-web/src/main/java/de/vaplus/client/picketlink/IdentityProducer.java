package de.vaplus.client.picketlink;

import javax.faces.bean.ApplicationScoped;
import javax.ws.rs.Produces;

import org.picketlink.Identity;
import org.picketlink.annotations.PicketLink;
import org.picketlink.internal.DefaultIdentity;

@ApplicationScoped
public class IdentityProducer {

	@Produces
	@PicketLink
	public Identity getIdentity() {
		System.out.println("getIdentity");
		return new DefaultIdentity();
	}
}
