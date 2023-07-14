package br.auth.models;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import com.nimbusds.jose.JWSAlgorithm;

import br.edu.iffar.fw.classBag.init.InitConstantes;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

/**
 * Contains constant specific to OpenId Connect specification
 * http://openid.net/specs/openid-connect-core-1_0.html
 *
 */
public class OpenIdConstant extends InitConstantes{
	
		
    public static final String TOKEN_END_POINT_LINK = "/token";
    public static final String WELL_KNOW_END_POINT_LINK = "/.well-known/openid-configuration";
    public static final String CERTS_END_POINT_LINK = "/certs";
    public static final String AUTH_END_POINT_LINK = "/auth";
    public static final String LOGIN_AUTH_END_POINT_LINK = "/loginAuth";
    public static final String END_SESSION_ENDPOINT_LINK = "/logout";

    public static final String AUTHORIZATION_HEADER = "Authorization";
	
	
	//server
    public static final String BEARER_TYPE = "Bearer";
    public static final String BASIC_TYPE = "Basic";

    // Authorization Code request/response parameters
    public static final String RESPONSE_TYPE = "response_type";
    public static final String CLIENT_ID = "client_id";
    public static final String SCOPE = "scope";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String RESPONSE_MODE = "response_mode";
    public static final String STATE = "state";
    public static final String NONCE = "nonce";
    public static final String DISPLAY = "display";
    public static final String PROMPT = "prompt";
    public static final String MAX_AGE = "max_age";
    public static final String UI_LOCALES = "ui_locales";
    public static final String CLAIMS_LOCALES = "claims_locales";
    public static final String ID_TOKEN_HINT = "id_token_hint";
    public static final String LOGIN_HINT = "login_hint";
    public static final String ACR_VALUES = "acr_values";

    public static final String SID = "sid";//sessao_id

    public static final String CODE = "code";
    public static final String TOKEN = "token";
    public static final String ID_TOKEN = "id_token";
    public static final String POST_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri";

    // Access Token request/response parameters
    public static final String GRANT_TYPE = "grant_type";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String CLIENT_SECRET = "client_secret";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String IDENTITY_TOKEN = "id_token";
    public static final String TOKEN_TYPE = "token_type";
    public static final String EXPIRES_IN = "expires_in";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ERROR_PARAM = "error";
    public static final String SESSION_STATE = "session_state";
    public static final String ERROR_DESCRIPTION_PARAM = "error_description";

    //claims
    public static final String ISSUER_IDENTIFIER = "iss";
    public static final String SUBJECT_IDENTIFIER = "sub";
    public static final String EXPIRATION_IDENTIFIER = "exp";
    public static final String AUDIENCE = "aud";
    public static final String AUTHORIZED_PARTY = "azp";
    public static final String ACCESS_TOKEN_HASH = "at_hash";
    
    //claims access token
    

    // OpenID Provider Metadata
    public static final String AUTHORIZATION_ENDPOINT = "authorization_endpoint";
    public static final String TOKEN_ENDPOINT = "token_endpoint";
    public static final String USERINFO_ENDPOINT = "userinfo_endpoint";
    public static final String END_SESSION_ENDPOINT = "end_session_endpoint";
    public static final String REGISTRATION_ENDPOINT = "registration_endpoint";
    public static final String REVOGATION_ENDPOINT = "revocation_endpoint";
    public static final String JWKS_URI = "jwks_uri";

    public static final String ISSUER = "issuer";
    public static final String SCOPES_SUPPORTED = "scopes_supported";
    public static final String ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED = "id_token_signing_alg_values_supported";
    public static final String RESPONSE_TYPES_SUPPORTED = "response_types_supported";
    public static final String RESPONSE_MODES_SUPPORTED = "response_modes_supported";
    public static final String TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED = "token_endpoint_auth_methods_supported";
    public static final String TOKEN_ENDPOINT_AUTH_SIGNING_ALG_VALUES_SUPPORTED = "token_endpoint_auth_signing_alg_values_supported";
    public static final String DISPLAY_VALUES_SUPPORTED = "display_values_supported";
    public static final String CLAIMS_SUPPORTED = "claims_supported";
    public static final String CLAIM_TYPES_SUPPORTED = "claim_types_supported";
    public static final String SUBJECT_TYPES_SUPPORTED = "subject_types_supported";

    public static final List<String> AUTHORIZATION_CODE_FLOW_TYPES
            = unmodifiableList(asList(
                    CODE,
                    TOKEN,
                    ID_TOKEN
            ));
    public static final List<String> IMPLICIT_FLOW_TYPES
            = unmodifiableList(asList(
                    CODE + " " + TOKEN,
                    CODE + " " +ID_TOKEN
            ));
    public static final List<String> HYBRID_FLOW_TYPES
            = unmodifiableList(asList(
                    TOKEN + " " + ID_TOKEN,
                    CODE + " " + TOKEN + " " +ID_TOKEN
            ));


    // Scopes
    public static final String OPENID_SCOPE = "openid"; //required
    public static final String PROFILE_SCOPE = "profile";
    public static final String EMAIL_SCOPE = "email";
    public static final String PHONE_SCOPE = "phone";
    public static final String OFFLINE_ACCESS_SCOPE = "offline_access";

    // profile scope claims
    public static final String NAME = "name";
    public static final String USER_PRINCIPAL_NAME = "upn";
    public static final String FAMILY_NAME = "family_name";
    public static final String GIVEN_NAME = "given_name";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String NICKNAME = "nickname";
    public static final String PREFERRED_USERNAME = "preferred_username";
    public static final String GROUPS = "groups";
    public static final String PROFILE = "profile";
    public static final String PICTURE = "picture";
    public static final String WEBSITE = "website";
    public static final String GENDER = "gender";
    public static final String BIRTHDATE = "birthdate";
    public static final String ZONEINFO = "zoneinfo";
    public static final String LOCALE = "locale";
    public static final String UPDATED_AT = "updated_at";

    // email scope claims
    public static final String EMAIL = "email";
    public static final String EMAIL_VERIFIED = "email_verified";

    // address scope claims
    public static final String ADDRESS = "address";

    // phone scope claims
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PHONE_NUMBER_VERIFIED = "phone_number_verified";

    public static final String DEFAULT_JWT_SIGNED_ALGORITHM = "RS256";
    public static final String DEFAULT_HASH_ALGORITHM = "SHA-256";

    // Original user Request
    public static final String ORIGINAL_REQUEST = "oidc.original.request";

    public static final String SECRET_BASIC = "client_secret_basic";
    public static final String SECRET_POST = "client_secret_post";

    public static final List<String> SCOPES_SUPPORTED_LIST = asList(
            OPENID_SCOPE,
            EMAIL_SCOPE,
            PROFILE_SCOPE
    );
    public static final List<String> TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED_LIST = asList(SECRET_POST,SECRET_BASIC);
    public static final List<String> CLAIMS_SUPPORTED_LIST = asList(
            AUDIENCE,
            EMAIL,
            EMAIL_VERIFIED,
            EXPIRATION_IDENTIFIER,
            FAMILY_NAME,
            GIVEN_NAME,
            "iat",
            ISSUER_IDENTIFIER,
            LOCALE,
            NAME,
            PICTURE,
            SUBJECT_IDENTIFIER
         );

    public static final String PLAIN = "plain";
    public static final String CODE_CHALLENGE_METHODS_SUPPORTED = "code_challenge_methods_supported";
    public static final List<String> CODE_CHALLENGE_METHODS_SUPPORTED_LIST = asList(PLAIN, "S256");


    static String criaWellKnow(){

        List<String> RESPONSE_TYPE_SUPPORTED_LIST = new ArrayList<>();
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
    
	public static String WELL_KNOW;
    static{
    	WELL_KNOW = criaWellKnow();
    }
}
