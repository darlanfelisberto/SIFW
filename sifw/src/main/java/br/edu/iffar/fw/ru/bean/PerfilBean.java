package br.edu.iffar.fw.ru.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.Usuario;
import org.primefaces.event.CaptureEvent;
import org.primefaces.model.StreamedContent;

import br.edu.iffar.fw.classBag.bo.UsuarioBO;
import br.edu.iffar.fw.classBag.db.dao.ImagenDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.excecoes.SenhaException;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.relatorios.CarteirinhaUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.RollbackException;

@Named
@ViewScoped
public class PerfilBean implements Serializable {

	private static final long serialVersionUID = 22021991L;
		
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private ImagenDAO imagenDAO;
	@Inject private CarteirinhaUtil carterinhaUtil;
	@Inject private MessagesUtil messagesUtil;
	@Inject
	private HeaderBean headerBean;

	@Inject private UsuarioBO usuarioBO;
	
	private Usuario user = null;
	private Imagen imagen = null;
	private Imagen newImage;
	
	@PostConstruct
	public void init() {
		this.imagen = this.headerBean.getImagen();
		this.user = this.headerBean.getUsuario();

		if(this.user == null) {
			this.user = this.usuariosDAO.getUsuarioLogado();
			if(this.user == null) {
				this.logout();
			}
		}
	}

	public Usuario getUsuarioLogado() {
		return this.user;
	}
	
	public void saveNovaImagen(){
		if (this.imagen == null) {
			this.messagesUtil.addError("A imagem deve ser capturada antes de salvar.");
			return;
		}
		try {
			this.imagen = this.imagenDAO.substituiImagen(this.imagen);
			this.messagesUtil.addSuccess("Imagem salva com sucesso.");
		} catch (IOException|RollbackException e) {
			this.messagesUtil.addError("A imagem nao pode ser salva.");
			e.printStackTrace();
		}
	}
	
	public void logout() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest) ec.getRequest();
			HttpServletResponse response = (HttpServletResponse) ec.getResponse();
			request.logout();
			response.sendRedirect(request.getContextPath());
			
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

    public void oncapture(CaptureEvent captureEvent) {
		if(this.imagen == null || this.imagen.equals(Imagen.SEMIMAGEN)) {
	    	this.imagen= new Imagen(UUID.randomUUID().toString(),this.getUsuario(),captureEvent.getData());
    	}else {
    		this.imagen.setBytes(captureEvent.getData());
    	}
    }

	public StreamedContent gerarNewMyQRCode(boolean nova) {
		Usuario u = usuariosDAO.getUsuarioLogado();
		return this.carterinhaUtil.gerarCarterinhaQRCode(nova, u);
	}

	public void trocarSenha() {
		try {
			this.usuarioBO.init(this.user).trocaSenha().salvarAuth();
			this.messagesUtil.addSuccess("Senha atualizada com sucesso.");
		} catch (SenhaException e) {
			this.messagesUtil.addError(e.getMessage());
		} catch (RollbackException e) {
			this.messagesUtil.addError(e);
		}
	}
	
	public Usuario getUsuario() {
		return this.user;
	}
		
	public Imagen getImagen() {
		return imagen;
	}

	public Imagen getNewImage() {
		return newImage;
	}

	public void setNewImage(Imagen newImage) {
		this.newImage = newImage;
	}

	public UsuarioBO getUsuarioBO() {
		return usuarioBO;
	}
}