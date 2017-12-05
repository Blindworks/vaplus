package de.vaplus.client.util.picketlink;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512Generator {
	
	public static String generateHash(String password, String salt){
		
		String rawPassword = salt + password;
		
		String algorithm = "SHA-512";
		String encodedPassword = null;
		
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
			byte[] digest = messageDigest.digest(rawPassword.getBytes("UTF-8"));
            encodedPassword = Base64.encodeBytes(digest);
            
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return encodedPassword;
	}
}
