
package br.edu.iffar.catra.comunica;

import java.io.FileInputStream;
/**
 *
 * @author darlan
 */
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.ServerRequest.HttpFailure;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.admin.client.Keycloak;
//import org.keycloak.authorization.client.AuthzClient;
//import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.adapters.config.AdapterConfig;

import br.edu.iffar.catra.control.PrincipalControl;
import br.edu.iffar.catra.util.Log;

public class KeycloakSession {

    private static KeycloakSession keyS;
	private static Properties props;
	private static KeycloakDeployment deployment;

    static {
		try {
			props = KeycloakSession.getProperties();

			AdapterConfig config = new AdapterConfig();

			config.setRealm(props.getProperty("api.keycloak.realm"));
			config.setAuthServerUrl(props.getProperty("api.keycloak.auth-server-url"));
			config.setSslRequired(props.getProperty("api.keycloak.ssl-required"));
			config.setResource(props.getProperty("api.keycloak.resource"));

			if(!props.getProperty("api.keycloak.credentials.secret").trim().isEmpty()) {
				Map<String, Object> credential = new HashMap<String, Object>();
				credential.put("secret", props.getProperty("api.keycloak.credentials.secret"));
				config.setCredentials(credential);
			}
			config.setConfidentialPort(0);
			config.setPkce(true);
//	        InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/keycloak.json");
			deployment = KeycloakDeploymentBuilder.build(config);

		} catch (IOException e) {
			e.printStackTrace();
			Log.info("Arquivo catra.properties não encontrado, usando localhost!");
		}
    }

	public static final String MODO_OPERACAO = props.getProperty("catra.modo");
	public static final String PROTOCOLO = props.getProperty("api.protocolo");
	public static final String IP = props.getProperty("api.ip");
	public static final String ROOT = props.getProperty("api.root");
	public static final String PORTA = props.getProperty("api.porta");
	public static final String PATH_CATRA_AUTH_API = props.getProperty("api.path_catra_auth_api");
	public static final String REST_API = PROTOCOLO + IP + PORTA + ROOT;

	public static int STATUS_ONLINE = 2;
	public static int STATUS_OFFLINE = 1;
	public static int STATUS_NAO_LOGADO = 0;

	public static String MODO_OPERACAO_TESTE = "teste";

	private Keycloak keycloak = null;
	private AdapterTokenVerifier.VerifiedTokens tokens = null;


	private int logado = STATUS_NAO_LOGADO;

	public static KeycloakSession getCatracaInstance() {
		if(keyS == null) {
			keyS = new KeycloakSession();
		}
        return keyS;
    }

	public static Properties getProperties() throws IOException {
		Properties props = new Properties();

		FileInputStream file = new FileInputStream(Paths.get("catra.properties").toAbsolutePath().toString());

		props.load(file);

		return props;
	}

	public boolean isLogado() {
		return this.logado > STATUS_NAO_LOGADO;
	}

	public boolean isOnline() {
		return this.logado == STATUS_ONLINE;
	}

	public void loginOnline(String usuario, String senha) throws NotAuthorizedException {
		try {
			this.keycloak = Keycloak.getInstance(deployment.getAuthServerBaseUrl(),
					deployment.getRealm(), usuario, senha,
					deployment.getResourceName(),
					(String) deployment.getResourceCredentials().get("secret"));

			AccessTokenResponse a = this.keycloak.tokenManager().getAccessToken();

			this.tokens = AdapterTokenVerifier.verifyTokens(a.getToken(), a.getIdToken(), deployment);
			
			if(!this.tokens.getAccessToken().getRealmAccess().getRoles().stream().filter(roleItem -> PrincipalControl.ROLE_CATRACA.equals(roleItem)).findAny().isPresent()) {
				logof();
				throw new NotAuthorizedException("Você não tem permissao para realizar este login!");
			}
			PrincipalControl.getPrincipalControl().usarApiOnline(this.getNameUser());
			this.logado = STATUS_ONLINE;
		}
		catch (NotAuthorizedException e) {
			e.printStackTrace();
			this.keycloak = null;
			throw new NotAuthorizedException("Usuário não autorizado/encontrado!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginOffLine() {
		this.logado = STATUS_OFFLINE;
	}

	public void logof() {
		this.keycloak = null;
		keyS = null;
		this.logado = STATUS_NAO_LOGADO;
    	PrincipalControl.getPrincipalControl().logof();
    }

    public <T> T get(Client client, String path, GenericType<T> responseType) throws IOException, HttpFailure, VerificationException {
        return this.configBuilder(client, path).get(responseType);
    }

    public <T> T get(Client client, String path, Class<T> responseType) throws IOException, HttpFailure, VerificationException {
        return this.configBuilder(client, path).get(responseType);
    }

    public Response post(Client client, String path, Entity<?> responseType) throws IOException, HttpFailure, VerificationException {
        return this.configBuilder(client, path).post(responseType);
    }

    public Builder configBuilder(Client client, String path) throws IOException, HttpFailure, VerificationException {
        Builder b = client.target(KeycloakSession.REST_API + path)
                .request()
                .accept(MediaType.APPLICATION_JSON);
        return autorizeHeader(b);
    }

    public Builder autorizeHeader(Builder builder) throws IOException, HttpFailure, VerificationException {
        return builder.header(HttpHeaders.AUTHORIZATION, autorizatonHeader());
    }

    public String autorizatonHeader() throws IOException, HttpFailure, VerificationException {
		return "Bearer " + this.keycloak.tokenManager().getAccessTokenString();
    }

    public boolean isLogged() {
		return this.keycloak != null;
    }


    public String getToken() {
		return this.keycloak.tokenManager().getAccessTokenString();
    }

    public String getNameUser() {
		return this.tokens.getAccessToken().getName();
    }
}
