package br.auth.oidc;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import com.nimbusds.jose.JWSAlgorithm;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

/**
 * Contains constant specific to OpenId Connect specification
 * http://openid.net/specs/openid-connect-core-1_0.html
 *
 */
public interface OpenIdConstant {
	
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
    String RESPONSE_TYPE = "response_type";
    String CLIENT_ID = "client_id";
    String SCOPE = "scope";
    String REDIRECT_URI = "redirect_uri";
    String RESPONSE_MODE = "response_mode";
    String STATE = "state";
    String NONCE = "nonce";
    String DISPLAY = "display";
    String PROMPT = "prompt";
    String MAX_AGE = "max_age";
    String UI_LOCALES = "ui_locales";
    String CLAIMS_LOCALES = "claims_locales";
    String ID_TOKEN_HINT = "id_token_hint";
    String LOGIN_HINT = "login_hint";
    String ACR_VALUES = "acr_values";

    String SID = "sid";//sessao_id

    String CODE = "code";
    String TOKEN = "token";
    String ID_TOKEN = "id_token";
    String POST_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri";

    // Access Token request/response parameters
    String GRANT_TYPE = "grant_type";
    String AUTHORIZATION_CODE = "authorization_code";
    String CLIENT_SECRET = "client_secret";
    String ACCESS_TOKEN = "access_token";
    String IDENTITY_TOKEN = "id_token";
    String TOKEN_TYPE = "token_type";
    String EXPIRES_IN = "expires_in";
    String REFRESH_TOKEN = "refresh_token";
    String ERROR_PARAM = "error";
    String SESSION_STATE = "session_state";
    String ERROR_DESCRIPTION_PARAM = "error_description";

    //claims
    String ISSUER_IDENTIFIER = "iss";
    String SUBJECT_IDENTIFIER = "sub";
    String EXPIRATION_IDENTIFIER = "exp";
    String AUDIENCE = "aud";
    String AUTHORIZED_PARTY = "azp";
    String ACCESS_TOKEN_HASH = "at_hash";
    
    //claims access token
    

    // OpenID Provider Metadata
    String AUTHORIZATION_ENDPOINT = "authorization_endpoint";
    String TOKEN_ENDPOINT = "token_endpoint";
    String USERINFO_ENDPOINT = "userinfo_endpoint";
    String END_SESSION_ENDPOINT = "end_session_endpoint";
    String REGISTRATION_ENDPOINT = "registration_endpoint";
    String REVOGATION_ENDPOINT = "revocation_endpoint";
    String JWKS_URI = "jwks_uri";

    String ISSUER = "issuer";
    String SCOPES_SUPPORTED = "scopes_supported";
    String ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED = "id_token_signing_alg_values_supported";
    String RESPONSE_TYPES_SUPPORTED = "response_types_supported";
    String RESPONSE_MODES_SUPPORTED = "response_modes_supported";
    String TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED = "token_endpoint_auth_methods_supported";
    String TOKEN_ENDPOINT_AUTH_SIGNING_ALG_VALUES_SUPPORTED = "token_endpoint_auth_signing_alg_values_supported";
    String DISPLAY_VALUES_SUPPORTED = "display_values_supported";
    String CLAIMS_SUPPORTED = "claims_supported";
    String CLAIM_TYPES_SUPPORTED = "claim_types_supported";
    String SUBJECT_TYPES_SUPPORTED = "subject_types_supported";

    List<String> AUTHORIZATION_CODE_FLOW_TYPES
            = unmodifiableList(asList(
                    CODE,
                    TOKEN,
                    ID_TOKEN
            ));
    List<String> IMPLICIT_FLOW_TYPES
            = unmodifiableList(asList(
                    CODE + " " + TOKEN,
                    CODE + " " +ID_TOKEN
            ));
    List<String> HYBRID_FLOW_TYPES
            = unmodifiableList(asList(
                    TOKEN + " " + ID_TOKEN,
                    CODE + " " + TOKEN + " " +ID_TOKEN
            ));


    // Scopes
    String OPENID_SCOPE = "openid"; //required
    String PROFILE_SCOPE = "profile";
    String EMAIL_SCOPE = "email";
    String PHONE_SCOPE = "phone";
    String OFFLINE_ACCESS_SCOPE = "offline_access";

    // profile scope claims
    String NAME = "name";
    String USER_PRINCIPAL_NAME = "upn";
    String FAMILY_NAME = "family_name";
    String GIVEN_NAME = "given_name";
    String MIDDLE_NAME = "middle_name";
    String NICKNAME = "nickname";
    String PREFERRED_USERNAME = "preferred_username";
    String GROUPS = "groups";
    String PROFILE = "profile";
    String PICTURE = "picture";
    String WEBSITE = "website";
    String GENDER = "gender";
    String BIRTHDATE = "birthdate";
    String ZONEINFO = "zoneinfo";
    String LOCALE = "locale";
    String UPDATED_AT = "updated_at";

    // email scope claims
    String EMAIL = "email";
    String EMAIL_VERIFIED = "email_verified";

    // address scope claims
    String ADDRESS = "address";

    // phone scope claims
    String PHONE_NUMBER = "phone_number";
    String PHONE_NUMBER_VERIFIED = "phone_number_verified";

    String DEFAULT_JWT_SIGNED_ALGORITHM = "RS256";
    String DEFAULT_HASH_ALGORITHM = "SHA-256";

    // Original user Request
    String ORIGINAL_REQUEST = "oidc.original.request";

    String SECRET_BASIC = "client_secret_basic";
    String SECRET_POST = "client_secret_post";

    List<String> SCOPES_SUPPORTED_LIST = asList(
            OPENID_SCOPE,
            EMAIL_SCOPE,
            PROFILE_SCOPE
    );
    List<String> TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED_LIST = asList(SECRET_POST,SECRET_BASIC);
    List<String> CLAIMS_SUPPORTED_LIST = asList(
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

    String PLAIN = "plain";
    String CODE_CHALLENGE_METHODS_SUPPORTED = "code_challenge_methods_supported";
    List<String> CODE_CHALLENGE_METHODS_SUPPORTED_LIST = asList(PLAIN, "S256");

    static String WELL_KNOW = criaWellKnow();

    static String criaWellKnow(){

        List<String> RESPONSE_TYPE_SUPPORTED_LIST = new ArrayList<>();
        RESPONSE_TYPE_SUPPORTED_LIST.addAll(AUTHORIZATION_CODE_FLOW_TYPES);
        RESPONSE_TYPE_SUPPORTED_LIST.addAll(IMPLICIT_FLOW_TYPES);
        RESPONSE_TYPE_SUPPORTED_LIST.addAll(HYBRID_FLOW_TYPES);
        RESPONSE_TYPE_SUPPORTED_LIST.add("none");

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        jsonBuilder.add(ISSUER, OidcTokenUtil.ISSUR)
                .add(AUTHORIZATION_ENDPOINT, OidcTokenUtil.ISSUR + AUTH_END_POINT_LINK)
                .add(TOKEN_ENDPOINT, OidcTokenUtil.ISSUR + TOKEN_END_POINT_LINK)
                .add(USERINFO_ENDPOINT, OidcTokenUtil.ISSUR + "/" + USERINFO_ENDPOINT)
                .add(END_SESSION_ENDPOINT, OidcTokenUtil.ISSUR + END_SESSION_ENDPOINT_LINK)
                .add(JWKS_URI, OidcTokenUtil.ISSUR + CERTS_END_POINT_LINK)
                .add(RESPONSE_TYPES_SUPPORTED, Json.createArrayBuilder(RESPONSE_TYPE_SUPPORTED_LIST))
                .add(SUBJECT_TYPES_SUPPORTED, Json.createArrayBuilder().add("public"))
                .add(ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED, Json.createArrayBuilder().add(JWSAlgorithm.RS256.getName()))
                .add(SCOPES_SUPPORTED, Json.createArrayBuilder(SCOPES_SUPPORTED_LIST))
                .add(TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED, Json.createArrayBuilder(TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED_LIST))
                .add(CLAIMS_SUPPORTED, Json.createArrayBuilder(CLAIMS_SUPPORTED_LIST))
                .add(CODE_CHALLENGE_METHODS_SUPPORTED, Json.createArrayBuilder(CODE_CHALLENGE_METHODS_SUPPORTED_LIST));

        return jsonBuilder.build().toString();
    }
}
