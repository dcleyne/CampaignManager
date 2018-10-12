package bt.managers;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PasswordManager {
	private static PasswordManager instance;

	private PasswordManager() {
	}

	public static synchronized String encrypt(String plaintext) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA"); // step 2
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
		try {
			md.update(plaintext.getBytes("UTF-8")); // step 3
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}

		byte raw[] = md.digest(); // step 4
		String hash = new String(Base64.getEncoder().encode(raw)); // step 5
		return hash; // step 6
	}

	public static synchronized PasswordManager getInstance() // step 1
	{
		if (instance == null) {
			instance = new PasswordManager();
		}
		return instance;
	}
}
