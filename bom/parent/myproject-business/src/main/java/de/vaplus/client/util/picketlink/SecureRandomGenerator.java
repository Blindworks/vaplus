package de.vaplus.client.util.picketlink;

import java.security.SecureRandom;
import java.util.Random;

public class SecureRandomGenerator {

	public static String generateSalt() {
    	SecureRandom secureRandom;

        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(new Random().nextLong());

        } catch (Exception e) {
            throw new IllegalStateException("Error getting SecureRandom instance", e);
        }

        return String.valueOf( secureRandom.nextLong());
    }
}
