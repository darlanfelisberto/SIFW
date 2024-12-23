package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;

import br.edu.iffar.fw.classBag.db.SessionDataStore;
import br.edu.iffar.fw.classBag.db.dao.ImagenDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.sec.KeycloakAdminUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class HeaderBean implements Serializable {

	private static final long serialVersionUID = 22021991L;
		
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private ImagenDAO imagenDAO;

	@Inject
	SessionDataStore sessionDataStore;
	
	@Inject
	private HttpServletRequest request;
	
	private Usuario user = null;
	private Imagen imagen = null;
	private String linkAccount = "";
	
	@PostConstruct
	public void init() {
		this.user = this.sessionDataStore.getUsuarioLogado();
		this.imagen = this.imagenDAO.imagenUsuarioLogado();
	}
	
	public Usuario getUsuarioLogado() {
		return this.user;
	}
	
	public String logoff() {
		try {
			request.logout();
			return "/app/index.xhtml?faces-redirect=true";
		} catch (Exception  e) {
			e.printStackTrace();
			return "/app/index.xhtml?faces-redirect=true";
		}
	}
    
	public Usuario getUsuario() {
		return this.user;
	}
		
	public Imagen getImagen() {
		return imagen;
	}
	
	public String getLinkAccount() {
		if(this.linkAccount.isEmpty()) {
			try {
				StringBuffer link  = new StringBuffer();
				link.append(KeycloakAdminUtil.AUTH_SERVER);// keycloakInfo.get("auth-server-url").getAsString());
				link.append("realms/");
				link.append(KeycloakAdminUtil.AUTH_REALM);// keycloakInfo.get("realm").getAsString());
				link.append("/account");
				
				this.linkAccount = link.toString();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return this.linkAccount;
	}
}
