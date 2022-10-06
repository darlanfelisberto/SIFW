package br.edu.iffar.fw.classBag.sec;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.wildfly.security.http.oidc.OidcSecurityContext;

@Named
@RequestScoped
public class HasRoleBean {
	
	public static final String ROLE_IFFAR_ADMIN = "IFFAR_ADMIN";
	public static final String ROLE_IFFAR_MORADIA = "IFFAR_MORADIA";

	@Inject private OidcSecurityContext oidcSecurityContext; 
		
	public boolean hasRole(String role) {
		boolean autorizado = this.oidcSecurityContext.getToken().getRealmAccessClaim().getRoles().stream()
				.filter((per)->{
					return per.equals(role);
				})
				.findAny()
				.isPresent();
		
		return autorizado;
	}
	
	//is funciona como get para tipo primitivo e apara acesso do xhtmlm é necessario ser assim
	public boolean isHasIffarAdmin() {
		return this.hasRole(ROLE_IFFAR_ADMIN);
	}
	
	public boolean isHasIffarMoradia() {
		return this.hasRole(ROLE_IFFAR_MORADIA);
	}
}
