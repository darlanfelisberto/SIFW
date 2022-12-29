package br.auth.oidc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OidcKeysUtil {

    static final int SIZE_RSA_KEY = 3072;
    static final String NAME_KEY_PAIR = "/WEB-INF/pairKeyJWK.json";
    static RSAKey pairRsaKey;
    static String PATH_KEY_FILE;
    static public  String PATH_WAR =  OidcKeysUtil.class.getResource("").getPath().split("/WEB-INF")[0];

    static {
    	
    	if(System.getProperty("os.name").contains("Win") && PATH_WAR.charAt(0) == '/') {
    	//TODO parece que tem algum bug que fica uma / antes da uri do path, verificar futuramente 17.0.5.8
    		PATH_WAR = PATH_WAR.substring(1);
    	}
    	PATH_KEY_FILE = PATH_WAR + NAME_KEY_PAIR;
        readKey();
    }

    public static void generateKeys() {
        RSAKey jwk2 = null;
        try {
            jwk2 = new RSAKeyGenerator(SIZE_RSA_KEY)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key
                    .keyID(UUID.randomUUID().toString()) // give the key a unique ID
                    .generate();
            Files.createFile(Paths.get(PATH_KEY_FILE));
            OutputStream ops = Files.newOutputStream(Paths.get(PATH_KEY_FILE));
            ops.write(jwk2.toJSONString().getBytes());
            ops.close();
            
        } catch (JOSEException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    static void readKey() {
        //https://connect2id.com/products/nimbus-jose-jwt/examples/jwk-generation
    	if(!Files.exists(Paths.get(PATH_KEY_FILE))){
    		System.out.println(NAME_KEY_PAIR + "Não encontrado.");
    		
    		generateKeys();
    	}
        try {
            FileInputStream f = new FileInputStream(PATH_KEY_FILE);
            String result = new String(f.readAllBytes());
            f.close();

            pairRsaKey = RSAKey.parse(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JWKSet getPairKey(){
        return new JWKSet(pairRsaKey);
    }

}
