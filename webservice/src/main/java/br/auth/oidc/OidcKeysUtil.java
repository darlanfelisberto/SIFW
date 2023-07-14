package br.auth.oidc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import static  br.edu.iffar.fw.classBag.init.InitConstantes.OIDC_JWT_SIZE;
import static  br.edu.iffar.fw.classBag.init.InitConstantes.OIDC_JWK_PATH;
import static  br.edu.iffar.fw.classBag.init.InitConstantes.OIDC_JWT_FILENAME;


public class OidcKeysUtil implements Serializable{

    private static final long serialVersionUID = 1L;
    static RSAKey pairRsaKey;
       
    static {    	
    	readKey();   
    }

    public static void generateKeys() {
        RSAKey jwk2 = null;
        try {
            jwk2 = new RSAKeyGenerator(OIDC_JWT_SIZE)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key
                    .keyID(UUID.randomUUID().toString()) // give the key a unique ID
                    .generate();
            Files.createFile(Paths.get(OIDC_JWK_PATH + OIDC_JWT_FILENAME));
            OutputStream ops = Files.newOutputStream(Paths.get(OIDC_JWK_PATH + OIDC_JWT_FILENAME));
            ops.write(jwk2.toJSONString().getBytes());
            ops.close();
            
        } catch (JOSEException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        catch (Exception e) {
			e.printStackTrace();
		}
    }

    static void readKey() {
        //https://connect2id.com/products/nimbus-jose-jwt/examples/jwk-generation
    	if(!Files.exists(Paths.get(OIDC_JWK_PATH + OIDC_JWT_FILENAME))){
    		System.out.println(OIDC_JWK_PATH + OIDC_JWT_FILENAME + " Não encontrado.");
    		
    		generateKeys();
    	}
        try {
            FileInputStream f = new FileInputStream(OIDC_JWK_PATH + OIDC_JWT_FILENAME);
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
