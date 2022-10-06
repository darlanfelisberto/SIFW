package br.edu.iffar.fw.classBag.sec;

import java.io.IOException;
import java.util.Arrays;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import javax.servlet.http.HttpServletRequest;

import org.wildfly.security.http.oidc.OidcPrincipal;
import org.wildfly.security.http.oidc.OidcSecurityContext;

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
						.filter((per)->{
							return Arrays.stream(this.role.getValue().replace(" ", "").split(","))
									.filter(rol-> rol.equals(per))
									.findAny()
									.isPresent();
						})
						.findAny()
						.isPresent();
				if(autorizado) {
					this.nextHandler.apply(faceletContext, parent);
				}
			}
		}
	}

}