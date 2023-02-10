package br.auth.oidc;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Pbkdf2Hash {
    public static Map<String, String> parametersDefault = new HashMap<>();

    private static final String DEFAULT_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int DEFAULT_ITERATIONS = 27500;
    private static final int DEFAULT_SALT_SIZE  = 16;         // 32-byte/256-bit salt
    private static final int DEFAULT_KEY_SIZE   = 64;         // 32-byte/256-bit key/hash

	public String generate(String password, String salt) {
		byte[] cod = pbkdf2(password.toCharArray(), Base64.getDecoder().decode(salt), DEFAULT_ALGORITHM, DEFAULT_ITERATIONS, DEFAULT_KEY_SIZE);
		return Base64.getEncoder().encodeToString(cod);
	}

	public String generate(String password, byte[] salt) {
		byte[] cod = pbkdf2(password.toCharArray(), salt, DEFAULT_ALGORITHM, DEFAULT_ITERATIONS, DEFAULT_KEY_SIZE);
		return Base64.getEncoder().encodeToString(cod);
	}

	public String generate(char[] password, String salt) {
		byte[] cod = pbkdf2(password, Base64.getDecoder().decode(salt), DEFAULT_ALGORITHM, DEFAULT_ITERATIONS, DEFAULT_KEY_SIZE);
		return Base64.getEncoder().encodeToString(cod);
	}

    public boolean verify(String um, String dois) {
        return compareBytes(um.getBytes(),dois.getBytes());
    }
    
    public static boolean compareBytes(byte[] array1, byte[] array2) {
        int diff = array1.length ^ array2.length;
        for (int i = 0; i < array1.length; i++) {
            diff |= array1[i] ^ array2[i%array2.length];
        }
        return diff == 0;
    }

    private byte[] pbkdf2(char[] password, byte[] salt, String algorithm, int iterations, int keySizeBytes) {
        try {
            return SecretKeyFactory.getInstance(algorithm).generateSecret(
                    new PBEKeySpec(password, salt, iterations, keySizeBytes * 8)).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

	public String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] bname = new byte[DEFAULT_SALT_SIZE];
		random.nextBytes(bname);
		return Base64.getEncoder().encodeToString(bname);
	}
}
