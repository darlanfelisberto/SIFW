package br.auth.oidc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

import static br.edu.iffar.fw.classShared.constantes.InitConstantes.*;
import static br.edu.iffar.fw.authClassShared.models.OpenIdConstant.*;

public class OidcKeysUtil implements Serializable{

    private static final long serialVersionUID = 1L;
    static RSAKey pairRsaKey;
    public static String WELL_KNOW = criaWellKnow();
    
    static {    	
    	readKey();   
    }
    
    
    static String criaWellKnow(){
    	
	    List<String> RESPONSE_TYPE_SUPPORTED_LIST = new ArrayList<String>();
	    RESPONSE_TYPE_SUPPORTED_LIST.addAll(AUTHORIZATION_CODE_FLOW_TYPES);
	    RESPONSE_TYPE_SUPPORTED_LIST.addAll(IMPLICIT_FLOW_TYPES);
	    RESPONSE_TYPE_SUPPORTED_LIST.addAll(HYBRID_FLOW_TYPES);
	    RESPONSE_TYPE_SUPPORTED_LIST.add("none");
	
	    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
	
	    jsonBuilder.add(ISSUER, OIDC_ISSUR)
	            .add(AUTHORIZATION_ENDPOINT, OIDC_ISSUR + AUTH_END_POINT_LINK)
	            .add(TOKEN_ENDPOINT, OIDC_ISSUR + TOKEN_END_POINT_LINK)
	            .add(USERINFO_ENDPOINT, OIDC_ISSUR + "/" + USERINFO_ENDPOINT)
	            .add(END_SESSION_ENDPOINT, OIDC_ISSUR + END_SESSION_ENDPOINT_LINK)
	            .add(JWKS_URI, OIDC_ISSUR + CERTS_END_POINT_LINK)
	            .add(RESPONSE_TYPES_SUPPORTED, Json.createArrayBuilder(RESPONSE_TYPE_SUPPORTED_LIST))
	            .add(SUBJECT_TYPES_SUPPORTED, Json.createArrayBuilder().add("public"))
	            .add(ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED, Json.createArrayBuilder().add(JWSAlgorithm.RS256.getName()))
	            .add(SCOPES_SUPPORTED, Json.createArrayBuilder(SCOPES_SUPPORTED_LIST))
	            .add(TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED, Json.createArrayBuilder(TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED_LIST))
	            .add(CLAIMS_SUPPORTED, Json.createArrayBuilder(CLAIMS_SUPPORTED_LIST))
	            .add(CODE_CHALLENGE_METHODS_SUPPORTED, Json.createArrayBuilder(CODE_CHALLENGE_METHODS_SUPPORTED_LIST));
	
	    return jsonBuilder.build().toString();
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
