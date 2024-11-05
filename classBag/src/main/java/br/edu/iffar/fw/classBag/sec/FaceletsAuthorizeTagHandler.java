package br.edu.iffar.fw.classBag.sec;

import java.io.IOException;
import java.util.Arrays;

import org.wildfly.security.http.oidc.OidcPrincipal;
import org.wildfly.security.http.oidc.OidcSecurityContext;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagHandler;
import jakarta.servlet.http.HttpServletRequest;

@SuppressWarnings("unchecked")
public class FaceletsAuthorizeTagHandler extends TagHandler {

	private final TagAttribute role;

	/**
	 * @see TagHandler#TagHandler(TagConfig)
	 */
	public FaceletsAuthorizeTagHandler(TagConfig config) {
		
		super(config);
		this.role = this.getAttribute("role");
	}

	/**
	 * @see TagHandler#apply(FaceletContext, UIComponent)
	 */
	@Override
	public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException {
		HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		OidcPrincipal<OidcSecurityContext> oidcPrincipal= (OidcPrincipal<OidcSecurityContext>) r.getUserPrincipal();  
		
		if(oidcPrincipal != null) {
			if(this.role.getValue().equals("*")) {//para usuario logado
				this.nextHandler.apply(faceletContext, parent);
			}else {
				boolean autorizado = oidcPrincipal.getOidcSecurityContext().getToken().getRealmAccessClaim().getRoles().stream()
						.anyMatch((per)->{
							return Arrays.asList(this.role.getValue().replace(" ", "").split(",")).contains(per);
						});
				if(autorizado) {
					this.nextHandler.apply(faceletContext, parent);
				}
			}
		}
	}

}