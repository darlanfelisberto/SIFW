/*
 * // * To change this license header, choose License Headers in Project
 * Properties. // * To change this template file, choose Tools | Templates // *
 * and open the template in the editor. //
 */
//package br.edu.iffar.catra.util;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//import javax.swing.JOptionPane;
//
//import org.keycloak.adapters.KeycloakDeploymentBuilder;
//import org.keycloak.adapters.installed.KeycloakInstalled;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.KeycloakBuilder;
//import org.keycloak.representations.adapters.config.AdapterConfig;
//
//import br.edu.iffar.catra.comunica.KeycloakSession;
//import br.edu.iffar.catra.forms.FormCatra;
//
///**
// *
// * @author qwerty
// */
//public class LoginListenerThread extends Thread{
//    
//
//    
//    private CallBack<KeycloakSession> callBack;
//    
//    private static LoginListenerThread ll = null;
//    
//	static {
//		try {
//
//			Properties props = KeycloakSession.getProperties();
//
//			AdapterConfig config = new AdapterConfig();
//
//			config.setRealm(props.getProperty("api.keycloak.realm"));
//			config.setAuthServerUrl(props.getProperty("api.keycloak.auth-server-url"));
//			config.setSslRequired(props.getProperty("api.keycloak.ssl-required"));
//			config.setResource(props.getProperty("api.keycloak.resource"));
//
//			if(!props.getProperty("api.keycloak.credentials.secret").trim().isEmpty()) {
//				Map<String, Object> credential = new HashMap<String, Object>();
//				credential.put("secret", props.getProperty("api.keycloak.credentials.secret"));
//				config.setCredentials(credential);
//			}
//////			config.setPublicClient(true);
//			config.setConfidentialPort(0);
//			config.setPkce(true);
//////			config.setVerifyTokenAudience(true);
////
//////	        InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/keycloak.json");
//			LoginListenerThread.deployment = KeycloakDeploymentBuilder.build(config);
//		}
//		catch (IOException e) {
//			String msg = "Arquivo catra.properties não encontrado!";
//			Log.info(msg);
//			JOptionPane.showMessageDialog(null, msg);
//		}
//	}
//    
//    public static LoginListenerThread getInstance(CallBack<KeycloakSession> call){
//        if(ll != null){
//        	interruptLogin();
//            ll = null;
//        }
//        return ll = new LoginListenerThread(call);
//    }
//    
//    public static void interruptLogin() {
//    	try {
//    		if(ll != null){
//        		
//                ll.interrupt();
//                ll = null;
//            }
//	    } catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("OK - LoginListener thread stop");
//		}
//    }
//
//    private LoginListenerThread(CallBack<KeycloakSession> callBack) {
//        this.callBack = callBack;
//    }
//    
//    public void run() {
//         this.callBack.callBack(this.startLogin());
//			FormCatra.getInstance().toFront();
//    }
//    
//	public KeycloakSession startLogin() {
////		if (deployment == null) {
////			FormCatra.getInstance().showMessageError("Configurações de servidor de altenticação e Web Service não encontrada.");
////			return null;
////		}
//
//		KeycloakInstalled k = null;// new KeycloakInstalled(deployment);
//       
//		try {
//			Properties props = KeycloakSession.getProperties();
//
//			Keycloak keycloak = KeycloakBuilder.builder()
//					.serverUrl(props.getProperty("api.keycloak.auth-server-url"))
//					.realm(props.getProperty("api.keycloak.realm"))
//					.username("user")
//					.password("pass")
//					.clientId(props.getProperty("api.keycloak.resource"))
//					.clientSecret(props.getProperty("api.keycloak.credentials.secret"))
//					.build();
////            k.loginDesktop();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//		return null;// new KeycloakSession(k);
//    }
//    
//    //outra forma de login online abaixo
//    
////    public void refreshToken() throws IOException, ServerRequest.HttpFailure, VerificationException {
////        AccessTokenResponse tokenResponse = ServerRequest.invokeRefresh(deployment, refreshToken);
////        parseAccessToken(tokenResponse);
////    }
////
////    private void parseAccessToken(AccessTokenResponse tokenResponse) throws VerificationException {
////        this.tokenResponse = tokenResponse;
////        this.refreshToken = tokenResponse.getRefreshToken();
////
////        AdapterTokenVerifier.VerifiedTokens tokens = AdapterTokenVerifier.verifyTokens(tokenResponse.getToken(), tokenResponse.getIdToken(), deployment);
////        this.accessToken = tokens.getAccessToken();
////        this.idToken = tokens.getIdToken();
////    }
//
////    public boolean loginCatracab() {
////        outra forma possivel de implementar o login, mas infelismente existe um padrao de projeto quandousamos oauth, qu diz que nao devemos manipular as senhas do usuario
////    	
////    	AuthzClient authzClient = AuthzClient.create();
////
////    	AccessTokenResponse tokenResponse = authzClient.obtainAccessToken("catraca", "catraca");
////
////        try {
////        	this.parseAccessToken(tokenResponse);
////            for (String role : this.accessToken.getRealmAccess().getRoles()) {
////            	System.out.println(role);
////            	if(role.equals("IFFAR_RU_CATRACA")) {
////            		return true;
////            	}
////			}
////		} catch (VerificationException e) {
////			e.printStackTrace();
////		}
////        return false;
////    }
//
//}
