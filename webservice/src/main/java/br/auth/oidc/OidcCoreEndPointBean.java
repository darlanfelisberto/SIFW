package br.auth.oidc;

import static br.auth.oidc.OpenIdConstant.AUTHORIZATION_CODE;
import static br.auth.oidc.OpenIdConstant.AUTHORIZATION_HEADER;
import static br.auth.oidc.OpenIdConstant.AUTH_END_POINT_LINK;
import static br.auth.oidc.OpenIdConstant.CERTS_END_POINT_LINK;
import static br.auth.oidc.OpenIdConstant.CLIENT_ID;
import static br.auth.oidc.OpenIdConstant.CLIENT_SECRET;
import static br.auth.oidc.OpenIdConstant.CODE;
import static br.auth.oidc.OpenIdConstant.END_SESSION_ENDPOINT;
import static br.auth.oidc.OpenIdConstant.END_SESSION_ENDPOINT_LINK;
import static br.auth.oidc.OpenIdConstant.ERROR_PARAM;
import static br.auth.oidc.OpenIdConstant.GRANT_TYPE;
import static br.auth.oidc.OpenIdConstant.LOGIN_AUTH_END_POINT_LINK;
import static br.auth.oidc.OpenIdConstant.NONCE;
import static br.auth.oidc.OpenIdConstant.OPENID_SCOPE;
import static br.auth.oidc.OpenIdConstant.REDIRECT_URI;
import static br.auth.oidc.OpenIdConstant.REFRESH_TOKEN;
import static br.auth.oidc.OpenIdConstant.RESPONSE_TYPE;
import static br.auth.oidc.OpenIdConstant.SCOPE;
import static br.auth.oidc.OpenIdConstant.STATE;
import static br.auth.oidc.OpenIdConstant.TOKEN_END_POINT_LINK;
import static br.auth.oidc.OpenIdConstant.USERINFO_ENDPOINT;
import static br.auth.oidc.OpenIdConstant.WELL_KNOW;
import static br.auth.oidc.OpenIdConstant.WELL_KNOW_END_POINT_LINK;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_HTML;
import static java.util.UUID.randomUUID;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.auth.dao.AuthLoginDAO;
import br.auth.dao.ClienteDAO;
import br.auth.dao.UsuarioDAO;
import br.auth.models.AuthLogin;
import br.auth.models.AuthUser;
import br.auth.models.Cliente;
import br.auth.util.ThymeleafUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.transaction.RollbackException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Response;

@Path("/auth/realms/{realm}")
@RequestScoped
public class OidcCoreEndPointBean {

//    @Inject Template formLogin;
//    @Inject Template erroAuth;
    @Inject
    ClienteDAO clienteDAO;
    @Inject
    UsuarioDAO usuarioDAO;
    @Inject
    AuthLoginDAO authLoginDAO;
    @Inject
    OidcKeysUtil oidcKeysUtil;
    @Inject
    OidcTokenUtil oidcTokenUtil;
    
    @Inject
    ThymeleafUtil thymeleafUtil;
    

    @GET
    @Path(WELL_KNOW_END_POINT_LINK)
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response getConfiguration2(@PathParam("realm") String name) {
        return Response.ok(WELL_KNOW).build();
    }

    @GET
    @Path(CERTS_END_POINT_LINK)
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response getJwkFile() {
        return Response.ok(oidcKeysUtil.getPairKey().toJSONObject()).build();
    }

    @GET
    @Path(USERINFO_ENDPOINT)
    @RolesAllowed({"user"})
    @Produces(APPLICATION_JSON)
    public Response userInfoEndpoint(){
        System.out.println("user end point");
        return Response.ok("{\"teste\":1}").build();
    }

    @GET
    @Path(AUTH_END_POINT_LINK)
    @PermitAll
    public Response authEndpoint(
            @QueryParam(CLIENT_ID) String clientId, @QueryParam(SCOPE) String scope,
            @QueryParam(RESPONSE_TYPE) String responseType, @QueryParam(NONCE) String nonce,
            @QueryParam(STATE) String state, @QueryParam(REDIRECT_URI) String redirectUri) throws URISyntaxException {

//        if (!CODE.equals(responseType) || !TOKEN.equals(responseType)) {
//            return Response.ok(thymeleafUtil.processes("erroAuth"), TEXT_HTML).build();
//        }

        if (!OPENID_SCOPE.equals(scope)) {
        	return Response.ok(thymeleafUtil.processes("erroAuth"), TEXT_HTML).build();
        }

        if (state == null) {
        	return Response.ok(thymeleafUtil.processes("erroAuth"), TEXT_HTML).build();
        }

        return retornaLoginPage(clientId,scope,state,redirectUri,false);
    }

    public Response retornaLoginPage(String clientId,String scope,String state,String redirectUri,boolean msgErros){
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setNoStore(true);
        Map<String, Object> variable = new HashMap<>();
        variable.put(CLIENT_ID, clientId);
        variable.put(SCOPE, scope);
        variable.put(STATE, state);
        variable.put(REDIRECT_URI, redirectUri);
        variable.put("msgError", msgErros);
        
        return Response.ok(thymeleafUtil.processes("formLogin",variable), TEXT_HTML)
                .cacheControl(cc)
                .build();
    }

    public Response createJsonError(String tipo) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add(ERROR_PARAM, tipo);
        return Response.serverError().entity(jsonBuilder.build()).build();
    }

    @GET
    @Path(LOGIN_AUTH_END_POINT_LINK)
    @PermitAll
    public Response loginauth() {
        return Response.ok("<p> nada para mostrar</p>", TEXT_HTML).build();
    }

    @POST
    @Path(LOGIN_AUTH_END_POINT_LINK)
    @PermitAll
    public Response loginAuth(@FormParam(CLIENT_ID) String clientId, @FormParam(SCOPE) String scope,
                              @FormParam(RESPONSE_TYPE) String responseType, @FormParam(NONCE) String nonce,
                              @FormParam(STATE) String state, @FormParam(REDIRECT_URI) String redirectUri,
                              @FormParam("username") String username, @FormParam("password") String password
                            ) throws URISyntaxException {

        Cliente client = this.clienteDAO.findClienteByName(clientId);

        if (client == null) {
            return this.createJsonError("invalid_client_id");
        }
        AuthUser user = this.usuarioDAO.findUsuarioByUsername(username);
        if (user == null || !user.authenticate(password)) {
            return retornaLoginPage(clientId,scope,state,redirectUri,true);
        }

        AuthLogin authLogin = new AuthLogin(UUID.fromString(state), randomUUID(), user, client);

        try {
			this.authLoginDAO.persistT(authLogin);
		} catch (RollbackException e) {
			e.printStackTrace();
			return Response.ok(thymeleafUtil.processes("erroAuth"), TEXT_HTML).build();
		}
        Response.ResponseBuilder rb;
        if (client.getResponseType().equals(ResponseTypeEnum.CODE)){
            rb = this.oidcTokenUtil.createUrlRedirectCodeResponse(authLogin,redirectUri);
        }else{
            rb = this.oidcTokenUtil.createTokenResponse(authLogin,redirectUri);
        }

        return rb.build();
    }

    /**
     * Como n??o mantenho status dos tokens, n??o ?? necessario faze nada
     *
     * @param authorizationHeader
     * @param refresh
     * @return
     */
    @POST
    @Path(END_SESSION_ENDPOINT_LINK)
    public Response logoutEntPoint(@HeaderParam(AUTHORIZATION_HEADER) String authorizationHeader,
                                   @FormParam(REFRESH_TOKEN) String refresh){
        System.out.println(END_SESSION_ENDPOINT);
        //esse endpoint deve retornar um 204 nocontent
        return Response.noContent().build();
    }

    /**
     * https://connect2id.com/products/server/docs/api/token
     *
     * @param authorizationHeader
     * @param clientId
     * @param clientSecret
     * @param grantType
     * @param code
     * @param state
     * @param redirectUri
     * @return
     * @throws URISyntaxException
     */
    @POST
    @Path(TOKEN_END_POINT_LINK)
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response tokenEndpointSigneddsadsad(
            @HeaderParam(AUTHORIZATION_HEADER) String authorizationHeader,
            @FormParam(CLIENT_ID) String clientId, @FormParam(CLIENT_SECRET) String clientSecret,
            @FormParam(GRANT_TYPE) String grantType, @FormParam(CODE) String code,
            @FormParam(STATE) String state,
            @FormParam(REDIRECT_URI) String redirectUri) throws URISyntaxException {

        if (!AUTHORIZATION_CODE.equals(grantType)) {
            this.createJsonError("invalid_grant_type");
        }

        AuthLogin authLogin = this.authLoginDAO.findAuthLoginByCode(code);

        if (authLogin == null) {
            return createJsonError("invalid_auth_code");
        } else{
            authLogin.setUsado(true);
            if (!authLogin.aindaValido()) {
                return createJsonError("invalid_auth_code");
            }
        }

        if (authorizationHeader != null) {
            if (!authLogin.verifyHeaderAuthorization(authorizationHeader)) {
                return this.createJsonError("invalid_client_id");
            }
        } else {
            if (!authLogin.verifyHeaderAuthorization(clientId, clientSecret)) {
                return this.createJsonError("invalid_client_secret");
            }
        }

        try {
			this.authLoginDAO.updateT(authLogin);
		} catch (RollbackException e) {
			e.printStackTrace();
			return Response.ok(thymeleafUtil.processes("erroAuth"), TEXT_HTML).build();
		}

        return this.oidcTokenUtil.createResponse(authLogin,redirectUri).build();
    }
}