package br.auth.oidc;


import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
//import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Pbkdf2Hash {
    public static Map<String, String> parametersDefault = new HashMap<>();

    static {
        parametersDefault.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256");
        parametersDefault.put("Pbkdf2PasswordHash.Iterations", "27500");
        parametersDefault.put("Pbkdf2PasswordHash.KeySizeBytes", "64");
        parametersDefault.put("Pbkdf2PasswordHash.SaltSizeBytes", "16");
    }

    private static final Set<String> SUPPORTED_ALGORITHMS = unmodifiableSet(new HashSet<>(asList(
            "PBKDF2WithHmacSHA224",
            "PBKDF2WithHmacSHA256",
            "PBKDF2WithHmacSHA384",
            "PBKDF2WithHmacSHA512"
            )));

    private static final String DEFAULT_ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int DEFAULT_ITERATIONS = 2048;
    private static final int DEFAULT_SALT_SIZE  = 32;         // 32-byte/256-bit salt
    private static final int DEFAULT_KEY_SIZE   = 32;         // 32-byte/256-bit key/hash

    private static final int MIN_ITERATIONS = 1024;
    private static final int MIN_SALT_SIZE  = 16;             // 16-byte/128-bit minimum salt
    private static final int MIN_KEY_SIZE   = 16;             // 16-byte/128-bit minimum key/hash

    private static final String PROPERTY_ALGORITHM  = "Pbkdf2PasswordHash.Algorithm";
    private static final String PROPERTY_ITERATIONS = "Pbkdf2PasswordHash.Iterations";
    private static final String PROPERTY_SALTSIZE   = "Pbkdf2PasswordHash.SaltSizeBytes";
    private static final String PROPERTY_KEYSIZE    = "Pbkdf2PasswordHash.KeySizeBytes";

    private String configuredAlgorithm  = DEFAULT_ALGORITHM;   // PBKDF2 algorithm to use
    private int configuredIterations    = DEFAULT_ITERATIONS;  // number of iterations
    private int configuredSaltSizeBytes = DEFAULT_SALT_SIZE;   // salt size in bytes
    private int configuredKeySizeBytes  = DEFAULT_KEY_SIZE;    // derived key (i.e., password hash) size in bytes
    
//    private final SecureRandom random = new SecureRandom();

    public Pbkdf2Hash initialize(Map<String, String> parameters) {

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (entry.getKey().equals(PROPERTY_ALGORITHM)) {
                if (!SUPPORTED_ALGORITHMS.contains(entry.getValue())) {
                    throw new IllegalArgumentException("Bad Algorithm parameter: " + entry.getValue());
                }
                configuredAlgorithm = entry.getValue();
            }
            else if (entry.getKey().equals(PROPERTY_ITERATIONS)) {
                try {
                    configuredIterations = Integer.parseInt(entry.getValue());
                }
                catch (Exception e) {
                    throw new IllegalArgumentException("Bad Iterations parameter: " + entry.getValue());
                }
                if (configuredIterations < MIN_ITERATIONS) {
                    throw new IllegalArgumentException("Bad Iterations parameter: " + entry.getValue());
                }
            }
            else if (entry.getKey().equals(PROPERTY_SALTSIZE)) {
                try {
                    configuredSaltSizeBytes = Integer.parseInt(entry.getValue());
                }
                catch (Exception e) {
                    throw new IllegalArgumentException("Bad SaltSizeBytes parameter: " + entry.getValue());
                }
                if (configuredSaltSizeBytes < MIN_SALT_SIZE) {
                    throw new IllegalArgumentException("Bad SaltSizeBytes parameter: " + entry.getValue());
                }
            }
            else if (entry.getKey().equals(PROPERTY_KEYSIZE)) {
                try {
                    configuredKeySizeBytes = Integer.parseInt(entry.getValue());
                }
                catch (Exception e) {
                    throw new IllegalArgumentException("Bad KeySizeBytes parameter: " + entry.getValue());
                }
                if (configuredKeySizeBytes < MIN_KEY_SIZE) {
                    throw new IllegalArgumentException("Bad KeySizeBytes parameter: " + entry.getValue());
                }
            }
            else {
                throw new IllegalArgumentException("Unrecognized parameter for Pbkdf2PasswordHash");
            }
        }
        return this;
    }

	public String generate(String password, String salt) {
//      byte[] salt = getRandomSalt(new byte[configuredSaltSizeBytes]);
		byte[] cod = pbkdf2(password.toCharArray(), Base64.getDecoder().decode(salt), configuredAlgorithm, configuredIterations, configuredKeySizeBytes);
		return Base64.getEncoder().encodeToString(cod);
//      return new EncodedPasswordHash(hash, toke, configuredAlgorithm, configuredIterations).getEncoded();
	}

	public String generate(String password, byte[] salt) {
		byte[] cod = pbkdf2(password.toCharArray(), salt, configuredAlgorithm, configuredIterations, configuredKeySizeBytes);
		return Base64.getEncoder().encodeToString(cod);
	}

	public String generate(char[] password, String salt) {
		byte[] cod = pbkdf2(password, Base64.getDecoder().decode(salt), configuredAlgorithm, configuredIterations, configuredKeySizeBytes);
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
		byte[] bname = new byte[configuredSaltSizeBytes];
		random.nextBytes(bname);
		return Base64.getEncoder().encodeToString(bname);
	}
}
