package br.edu.iffar.fw.classBag.util;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.wildfly.security.http.oidc.OidcPrincipal;
import org.wildfly.security.http.oidc.OidcSecurityContext;

//import org.keycloak.KeycloakPrincipal;
//import org.keycloak.KeycloakSecurityContext;

@RequestScoped
public class Producer {

	@Inject
	private HttpServletRequest request;
		
	@Context private SecurityContext context;

	@Produces
	public OidcSecurityContext getSecurityContext() {
		OidcPrincipal<?> oidcPrincipal = (OidcPrincipal<?>) request.getUserPrincipal();
		if(oidcPrincipal == null) {
			try {
				//redireciona para pagina inicial e força o login novamente
				FacesContext fc = FacesContext.getCurrentInstance();
				String url = fc.getExternalContext().getRequestContextPath();
				fc.getExternalContext().redirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return oidcPrincipal.getOidcSecurityContext();
	}

	@Produces
	public HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
	
//	@Produces
//	public FacesContext getFacesContext() {
//		return FacesContext.getCurrentInstance();
//	}
//	
	
}
