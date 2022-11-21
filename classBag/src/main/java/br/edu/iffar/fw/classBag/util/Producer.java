package br.edu.iffar.fw.classBag.util;

import java.io.IOException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

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
