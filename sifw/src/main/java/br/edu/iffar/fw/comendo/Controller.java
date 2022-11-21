//package br.edu.iffar.fw.comendo;
//
//import java.io.IOException;
//
//import jakarta.enterprise.context.RequestScoped;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.keycloak.KeycloakSecurityContext;
//import org.keycloak.adapters.AdapterDeploymentContext;
//import org.keycloak.adapters.KeycloakDeployment;
//import org.keycloak.common.util.KeycloakUriBuilder;
//import org.keycloak.constants.ServiceUrlConstants;
////import org.keycloak.representations.AccessToken;
//
///**
// * Controller simplifies access to the server environment from the JSP.
// *
// * @author Stan Silvert ssilvert@redhat.com (C) 2015 Red Hat Inc.
// */
//@Named
//@RequestScoped
//public class Controller {
//	
//	@Inject
//	private KeycloakSecurityContext keySec;
////	
////	@Inject
////	SecurityContext sc;
////	
////	@Inject 
////	private HttpServletRequest httpServletRequest;
//	
//	public String getAuth() {
//		
////		AccessToken a = keySec.getToken();
//		keySec.getToken().getRealmAccess().getRoles().stream().forEach(System.out::println);
//		
//		System.out.println(keySec.getToken().getEmail());
//		
//		return keySec.getToken().getEmail();
//	}
//
//    public boolean isLoggedIn(HttpServletRequest req) {
//        return getSession(req) != null;
//    }
//
//    public void handleLogsout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        if (isLogoutAction(req)) {
//            req.logout();
//            res.sendRedirect(req.getContextPath());
//        }
//    }
//
//    public boolean isLogoutAction(HttpServletRequest req) {
//        return getAction(req).equals("logout");
//    }
//
//    public String getAccountUri(HttpServletRequest req) {
//        KeycloakSecurityContext session = getSession(req);
//        String baseUrl = getAuthServerBaseUrl(req);
//        String realm = session.getRealm();
//        return KeycloakUriBuilder.fromUri(baseUrl).path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH)
//                .queryParam("referrer", "authz-servlet")
//                .queryParam("referrer_uri", getReferrerUri(req)).build(realm).toString();
//    }
//
//    private String getReferrerUri(HttpServletRequest req) {
//        StringBuffer uri = req.getRequestURL();
//        String q = req.getQueryString();
//        if (q != null) {
//            uri.append("?").append(q);
//        }
//        return uri.toString();
//    }
//
//    private String getAuthServerBaseUrl(HttpServletRequest req) {
//        AdapterDeploymentContext deploymentContext = (AdapterDeploymentContext) req.getServletContext().getAttribute(AdapterDeploymentContext.class.getName());
//        KeycloakDeployment deployment = deploymentContext.resolveDeployment(null);
//        return deployment.getAuthServerBaseUrl();
//    }
//
//    private KeycloakSecurityContext getSession(HttpServletRequest req) {
//        return (KeycloakSecurityContext) req.getAttribute(KeycloakSecurityContext.class.getName());
//    }
//
//    private String getAction(HttpServletRequest req) {
//        if (req.getParameter("action") == null) return "";
//        return req.getParameter("action");
//    }
//}