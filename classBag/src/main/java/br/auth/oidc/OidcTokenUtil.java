package br.auth.oidc;

import static br.auth.oidc.OpenIdConstant.ACCESS_TOKEN;
import static br.auth.oidc.OpenIdConstant.AUTHORIZED_PARTY;
import static br.auth.oidc.OpenIdConstant.BEARER_TYPE;
import static br.auth.oidc.OpenIdConstant.CODE;
import static br.auth.oidc.OpenIdConstant.EMAIL;
import static br.auth.oidc.OpenIdConstant.EMAIL_VERIFIED;
import static br.auth.oidc.OpenIdConstant.EXPIRES_IN;
import static br.auth.oidc.OpenIdConstant.FAMILY_NAME;
import static br.auth.oidc.OpenIdConstant.GIVEN_NAME;
import static br.auth.oidc.OpenIdConstant.GROUPS;
import static br.auth.oidc.OpenIdConstant.IDENTITY_TOKEN;
import static br.auth.oidc.OpenIdConstant.LOCALE;
import static br.auth.oidc.OpenIdConstant.NAME;
import static br.auth.oidc.OpenIdConstant.NONCE;
import static br.auth.oidc.OpenIdConstant.OPENID_SCOPE;
import static br.auth.oidc.OpenIdConstant.PREFERRED_USERNAME;
import static br.auth.oidc.OpenIdConstant.SCOPE;
import static br.auth.oidc.OpenIdConstant.SESSION_STATE;
import static br.auth.oidc.OpenIdConstant.SID;
import static br.auth.oidc.OpenIdConstant.TOKEN_TYPE;
import static br.auth.oidc.OpenIdConstant.USER_PRINCIPAL_NAME;
import static java.util.UUID.randomUUID;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbusds.jose.HeaderParameterNames;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import br.auth.models.AuthLogin;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class OidcTokenUtil {

    public static final int expira = 1000 * 60 * 200;
    public static final String ISSUR = "http://localhost:8080/service/auth/realms/fw";

    @Inject OidcKeysUtil oidcKeysUtil;

    public String createIdToken(AuthLogin authLogin, String nonce) {
        Date now = new Date();

        JWTClaimsSet.Builder jstClaimsBuilder =
                new JWTClaimsSet.Builder()
                        .issuer(ISSUR)
                        .subject(authLogin.getAuthUser().getMMId().toString())
                        .audience(List.of(authLogin.getCliente().getNome()))
                        .expirationTime(new Date(now.getTime() + expira))
                        .notBeforeTime(now)
                        .issueTime(now)
                        .jwtID(randomUUID().toString())
                        .claim(CODE, authLogin.getCode())
                        .claim(PREFERRED_USERNAME, authLogin.getAuthUser().getUsername())
                        .claim(GROUPS, authLogin.getAuthUser().getArrayPermissoes());

        if (nonce != null) {
            jstClaimsBuilder.claim(NONCE, nonce);
        }

        jstClaimsBuilder.claim(USER_PRINCIPAL_NAME, authLogin.getAuthUser().getUsername()); //principal for microprofile jwt

        JWSSigner signer = null;
        SignedJWT signedJWT = null;
        try {
            signer = new RSASSASigner(OidcKeysUtil.pairRsaKey);

            signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(OidcKeysUtil.pairRsaKey.getKeyID()).build(),
                    jstClaimsBuilder.build()
            );

            signedJWT.sign(signer);


        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return signedJWT.serialize();
    }

    public String createAccessToken(AuthLogin authLogin,String nonce) {
        //header https://www.rfc-editor.org/rfc/rfc7515.txt
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(OidcKeysUtil.pairRsaKey.getKeyID())//inform qual chave foi usada na assinatura
                .type(JOSEObjectType.JWT)
                .build();

        Date now = new Date();

        JWTClaimsSet.Builder jstClaimsBuilder = new JWTClaimsSet.Builder()
                .issuer(ISSUR)
                .subject(authLogin.getAuthUser().getMMId().toString())//id do usuario na api
                .audience(List.of(authLogin.getCliente().getNome()))
                .expirationTime(new Date(now.getTime() + expira))
                .notBeforeTime(now)
                .issueTime(now)
                .jwtID(randomUUID().toString())
                .claim(CODE, authLogin.getCode())
                .claim(GROUPS, authLogin.getAuthUser().getArrayPermissoes());

        if(nonce != null){
            jstClaimsBuilder.claim(NONCE, nonce);
        }

        addRolesForWildflyOidcClient(jstClaimsBuilder, authLogin);

        jstClaimsBuilder.claim(HeaderParameterNames.TYPE, BEARER_TYPE);

        jstClaimsBuilder.claim(USER_PRINCIPAL_NAME, authLogin.getAuthUser().getUsername()); //principal for microprofile jwt
        jstClaimsBuilder.claim(AUTHORIZED_PARTY, authLogin.getCliente().getNome());
        jstClaimsBuilder.claim(SCOPE, OPENID_SCOPE);
        jstClaimsBuilder.claim(SID, authLogin.getState());
        jstClaimsBuilder.claim(EMAIL_VERIFIED, false);
        jstClaimsBuilder.claim(NAME, authLogin.getAuthUser().getUsername());
        jstClaimsBuilder.claim(PREFERRED_USERNAME, authLogin.getAuthUser().getUsername());
        jstClaimsBuilder.claim(LOCALE, "pt-BR");
        jstClaimsBuilder.claim(GIVEN_NAME, authLogin.getAuthUser().getNome());
        jstClaimsBuilder.claim(FAMILY_NAME, authLogin.getAuthUser().getNome());
        jstClaimsBuilder.claim(EMAIL, authLogin.getAuthUser().getEmail());

        JWSSigner signer = null;
        SignedJWT signedJWT = null;
        try {
            signer = new RSASSASigner(OidcKeysUtil.pairRsaKey);

            signedJWT = new SignedJWT(header, jstClaimsBuilder.build());

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void addRolesForWildflyOidcClient(JWTClaimsSet.Builder jstClaimsBuilder,AuthLogin authLogin){
        Map<String, Object> roles = new HashMap<>();
        roles.put("roles", authLogin.getAuthUser().getArrayPermissoes());
        jstClaimsBuilder.claim("realm_access", roles);
    }

    public Response.ResponseBuilder createResponse(AuthLogin authLogin, String redirectUri) throws URISyntaxException {
       return switch (authLogin.getCliente().getResponseType()){
            case CODE -> this.createCodeResponse(authLogin,redirectUri);
            case TOKEN -> this.createTokenResponse(authLogin,redirectUri);
            default -> null;
        };
    }

    public Response.ResponseBuilder createCodeResponse(AuthLogin authLogin, String redirectUri) throws URISyntaxException {
        Response.ResponseBuilder rb = Response.ok();
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        jsonBuilder.add(SESSION_STATE, authLogin.getState().toString());
        jsonBuilder.add(IDENTITY_TOKEN, this.createIdToken(authLogin,null));
        jsonBuilder.add(ACCESS_TOKEN, this.createAccessToken(authLogin,null));
        jsonBuilder.add(TOKEN_TYPE, OpenIdConstant.BEARER_TYPE);
        jsonBuilder.add(EXPIRES_IN, OidcTokenUtil.expira);

        //https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.3.3
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setNoStore(true);
        return rb.location(authLogin.getCliente().urlRedirecionamento(redirectUri))
                .entity(jsonBuilder.build())
                .cacheControl(cc);
    }

    public Response.ResponseBuilder createTokenResponse(AuthLogin authLogin, String redirectUri) throws URISyntaxException {
        //JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        String paramUrl = "?" + SESSION_STATE + "=" + authLogin.getState().toString()
                + "&" + ACCESS_TOKEN + "=" +this.createAccessToken(authLogin,null)
                + "&" + TOKEN_TYPE + "=" + OpenIdConstant.BEARER_TYPE
                + "&" + EXPIRES_IN + "=" + OidcTokenUtil.expira;

        //https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.3.3
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setNoStore(true);
        return Response.seeOther(authLogin.getCliente().urlRedirecionamento(redirectUri + paramUrl))
                .cacheControl(cc);
    }

    public Response.ResponseBuilder createUrlRedirectCodeResponse(AuthLogin authLogin, String redirectUri) throws URISyntaxException {
        //https://openid.net/specs/openid-connect-core-1_0.html#rfc.section.3.1.3.3
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setNoStore(true);
        return Response.seeOther(authLogin.getCliente().urlRedirecionamento(redirectUri + authLogin.queryParam()))
                .cacheControl(cc);
    }
}

