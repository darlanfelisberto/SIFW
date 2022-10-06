package br.edu.iffar.fw.classBag.sec;

import java.io.IOException;
import java.net.InetAddress;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.keycloak.admin.client.Keycloak;
import org.wildfly.security.http.oidc.OidcSecurityContext;

public class KeycloakAdminUtil {
	public static String AUTH_SERVER; 
	public static String AUTH_REALM;
	public static String AUTH_CLIENT;
	public static final String OIDC_JSON = "/WEB-INF/oidc.json";
	
	/**
	 * @see http://www.mastertheboss.com/jbossas/jboss-as-7/using-jboss-management-api-programmatically/
	 */
	static {
		try {
			final ModelNode request = new ModelNode();
			request.get(ClientConstants.OP).set("read-resource");
			request.get("recursive").set(true);

			//@formatter:off
			request.get(ClientConstants.OP_ADDR)
				.add("subsystem", "elytron-oidc-client")
				.add("secure-deployment", "sifw.war");

			ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("127.0.0.1"), 9990);

			ModelNode returnVal = client.execute(request).get("result");

			AUTH_SERVER = returnVal.get("auth-server-url").asStringOrNull();
			AUTH_REALM = returnVal.get("realm").asStringOrNull();
			AUTH_CLIENT = returnVal.get("resource").asStringOrNull();
			
		} catch (IOException e2) {
			e2.printStackTrace();
		}

//		JsonObject oo;
//		try {
//			oo = getKeycloakInfo();
//			
//			AUTH_SERVER = oo.get("auth-server-url").getAsString();
//			AUTH_REALM = oo.get("realm").getAsString();
//			AUTH_CLIENT = oo.get("resource").getAsString();
//			
//		} catch (JsonIOException | JsonSyntaxException | IOException e) {
//			e.printStackTrace();
//			oo = null;
//		}				
	}
	
	public static final String LINK_ADMIN_AUTH = AUTH_SERVER + "admin/realms/" + AUTH_REALM ;

	
	public static Keycloak getKeyCloak(OidcSecurityContext context) {
		return Keycloak.getInstance(AUTH_SERVER, AUTH_REALM, AUTH_CLIENT, context.getTokenString());
	}
	
//	public static JsonObject getKeycloakInfo() throws JsonIOException, JsonSyntaxException, IOException {
//		JsonElement e = JsonParser.parseReader(new FileReader(ConnectProducer.getAPP_REAL_PATH() + OIDC_JSON));
//		JsonObject keycloakInfo = e.getAsJsonObject();
//		return keycloakInfo;
//	}
	
}
