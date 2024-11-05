package br.edu.iffar.fw.ru.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import br.com.feliva.authClass.models.AuthUser;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.feliva.authClass.dao.AuthUserDAO;
import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.bo.UsuarioBO;
import br.edu.iffar.fw.classBag.db.dao.ImagenDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.excecoes.UsuarioException;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.relatorios.CarteirinhaUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Named
@ViewScoped
public class UsuariosBean implements Serializable, BreadCrumbControl{
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private MessagesUtil messages;

	@Inject private UsuariosDAO usuariosDAO;
	@Inject private ImagenDAO imagenDAO;
	@Inject private CarteirinhaUtil carteirinhaUtil;
	@Inject private AuthUserDAO authUserDAO;
	@Inject private UsuarioBO usuarioBO;

	private List<Usuario> listUsuarios;

	private boolean rendBusca;
	private boolean rendCreateUsers;
	private boolean rendResultadoBusca;
	private boolean rendDadosUsuario;
	private int tipoBusca = 1;
	private BreadCrumb breadCrumb;
	
	private boolean capturouImage = false;
	private String imageCut = "";
	private Imagen imagenCap;
	private boolean redimencionarPadrao = true;

	@Size(min = 4)
	@NotNull
	private String busca = "";
	private Usuario userSel;
	private Imagen imagen;
	private DualListModel<Permissao> listModelPermissaoes;

	private List<Permissao> roles;
	private final List<SelectItem> listSelectIten = new ArrayList<SelectItem>();
	
	@PostConstruct
	private void init() {
		this.listSelectIten.add(new SelectItem(1, "CPF"));
		this.listSelectIten.add(new SelectItem(4, "Nome"));

		this.createBreadCrumb();
		this.telaFiltroBusca();
	}
	
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
				.addItem("Busca de usuário", "#{usuariosBean.telaFiltroBusca()}", "frmMain", BreadCrumb.RAIZ)// 1
				.addItem("Lista de usuários", "#{usuariosBean.telaListaResultadoBusca()}", "frmMain", 1)// 2
				.addItem("Dados do usuário", "#{usuariosBean.telaDadosUsuario()}", "frmMain", 2)// 3
				.addItem("Dados do usuário", "#{usuariosBean.telaListaResultadoBusca()}", "frmMain", 1);// 4
			;
	}
	
	public void telaFiltroBusca() {
		this.rendBusca = true;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = false;
		this.breadCrumb.setAtivo(1);
		this.busca = "";
		this.imageCut = "";
		this.imagenCap = null;
	}
	
	public void telaDadosUsuario() {
		this.imageCut = "";
		this.imagenCap = null;
		this.capturouImage = false;
		this.rendBusca = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = true;
		this.rendResultadoBusca = false;
		this.breadCrumb.setAtivo(3);
	}

	public void telaListaResultadoBusca() {
		this.rendBusca = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = true;
		this.breadCrumb.setAtivo(2);
	}
	
	public void cancelaCadastro() {
		this.imageCut = "";
		this.imagenCap = null;
		this.imagen = null;
		this.telaFiltroBusca();
	}
 
    public void buscarUsuarioProCpf() {
		if(this.tipoBusca == 1) {
			this.userSel = this.usuariosDAO.getUsuarioByCPF(this.busca.replace(".", "").replace("-", ""));

			if(this.userSel == null) {
				this.messages.addError("Usuário não foi encontrado.");
				return;
			}
			carregaDadosUserNecessario();
			this.telaDadosUsuario();
			this.breadCrumb.setAtivo(4);
		} else if(this.tipoBusca == 4) {// nome
			this.listUsuarios = this.usuariosDAO.listAllUsersByName(this.busca);
			this.telaListaResultadoBusca();
		}
	}

	public void selecionaUsuario() {
		this.carregaDadosUserNecessario();
		this.telaDadosUsuario();
	}

	public void carregaDadosUserNecessario() {
		this.imagen = this.imagenDAO.findByUsuarioIfNullPattern(this.userSel);
		this.userSel.getPessoa().setAuthUser(this.authUserDAO.findByUsername(this.userSel.getPessoa().getAuthUser().getUsername()));
		carregaPermissoesDisponiveis();
		this.telaDadosUsuario();
	}

	private void carregaPermissoesDisponiveis(){
		this.listModelPermissaoes = new DualListModel<Permissao>();
		this.listModelPermissaoes.setTarget((this.userSel.getPessoa().getAuthUser().getSetPermissao() != null ? this.userSel.getPessoa().getAuthUser().getSetPermissao().stream().toList():new ArrayList<Permissao>()));

		this.listModelPermissaoes.setSource(this.usuarioBO.init(this.userSel).buscaPermissoesDisponiveis());
	}

	public void novoUsuario() {
		this.userSel = Usuario.createUsuario(AuthUser.createNew());
		carregaPermissoesDisponiveis();
		this.telaDadosUsuario();
		this.breadCrumb.setAtivo(4);
	}

    public void salvarUser() {
    	try {
			this.usuarioBO.init(this.userSel)
					.trocaSenhaNullPass()
					.setPermissao(listModelPermissaoes.getTarget())
					.salvarUser();

			if(this.capturouImage) {
				this.imagenDAO.substituiImagen(this.imagen);
				this.capturouImage = false;
				this.imageCut = "";
				this.imagenCap = null;
			}
			this.telaFiltroBusca();
			this.messages.addSuccess("Informações do usuário foram salvas com sucesso.");
		}  catch (UsuarioException e) {
			this.messages.addError(e.getMessage());
		}  catch (IOException e) {
			e.printStackTrace();
			this.messages.addError("Erro ao escrever mudanças na imagem do usuário.");
		}  catch (Exception e) {
			e.printStackTrace();
			this.messages.addError("Não foi possivel salvar os dados do usuário.");
		}
	}
        
	public StreamedContent gerarCarterinhaQRCode(boolean nova) {
		return this.carteirinhaUtil.gerarCarterinhaQRCode(nova, this.userSel);
	}

	public void oncapture(CaptureEvent captureEvent) {
		if(this.imagenCap == null) {
			this.imagenCap = new Imagen(UUID.randomUUID().toString(), this.userSel, captureEvent.getData());
		} else {
			this.imagenCap.setBytes(captureEvent.getData());
		}
		
		this.capturouImage = true;
	}
	
    public void handleFileUpload(FileUploadEvent event) {
		if(this.imagenCap == null) {
			this.imagenCap = new Imagen(UUID.randomUUID().toString(), this.userSel, event.getFile().getContent());
		} else {
			this.imagenCap.setBytes(event.getFile().getContent());
		}

		this.capturouImage = true;
	}

	public void cortarImagen() {

		if(this.imagenCap != null && (this.imagen == null || this.imagen.equals(Imagen.SEMIMAGEN))) {
			this.imagen = new Imagen(UUID.randomUUID().toString(), this.userSel, null);
		}
		
		BufferedImage imgFinal = new BufferedImage(Imagen.WIDTH, Imagen.HEIGHT, BufferedImage.TYPE_INT_RGB);;
	
		try {
			int widthI = Imagen.WIDTH;
			int heightI = Imagen.HEIGHT;
			BufferedImage imagem = ImageIO.read(new ByteArrayInputStream(this.imagenCap.getBytes()));
			
			int widthCut = 0,heightCut = 0,xCut = 0,yCut = 0;
			
			if(this.imageCut != null) {
				JsonElement e = JsonParser.parseString(this.imageCut);
				JsonObject imageCut = e.getAsJsonObject();
				
				widthCut = imageCut.get("width").getAsInt();
				heightCut = imageCut.get("height").getAsInt();
				xCut = imageCut.get("x").getAsInt();
				yCut = imageCut.get("y").getAsInt();
			}
			
			if(!redimencionarPadrao) {
				widthI = imagem.getWidth();
				heightI = imagem.getHeight();
				if(this.imageCut ==null) {
					imgFinal = new BufferedImage(widthI, heightI, BufferedImage.TYPE_INT_RGB);;
				}else{
					widthI = widthCut;
					heightI = heightCut;
					imgFinal = new BufferedImage(widthCut, heightCut, BufferedImage.TYPE_INT_RGB);;
				}
			}
			Graphics2D g = imgFinal.createGraphics();
			
			if(this.imageCut != null) {
				imagem = imagem.getSubimage(xCut, yCut, widthCut, heightCut);
				this.imageCut = "";
			}
			
			if(this.redimencionarPadrao) {
				g.drawImage(imagem, 0, 0, Imagen.WIDTH, Imagen.HEIGHT, null);	
			}else {
				g.drawImage(imagem, 0, 0, widthI, heightI, null);
			}
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(imgFinal, "png", baos);
			this.imagen.setBytes(baos.toByteArray());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Imagen nao encontrada");
		}
	}

	public void cancelarCorteImagen() {
		this.imageCut = "";
		this.imagenCap = null;
	}

	public int getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(int tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public boolean isRendBusca() {
		return rendBusca;
	}

	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}
	
	public String getBusca() {
		return busca;
	}

	public void setBusca(String busca) {
		this.busca = busca;
	}

	public boolean isRendDadosUsuario() {
		return rendDadosUsuario;
	}

	public Usuario getUserSel() {
		return this.userSel;
	}
	    
    public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	}

	public List<Permissao> getRoles() {
		return roles;
	}

	public List<SelectItem> getListSelectIten() {
		return listSelectIten;
	}

	public DualListModel<Permissao> getListModelPermissaoes() {
		return listModelPermissaoes;
	}

	public void setListModelPermissaoes(DualListModel<Permissao> listModelPermissaoes) {
		this.listModelPermissaoes = listModelPermissaoes;
	}

	public List<Usuario> getListUsuarios() {
		return listUsuarios;
	}

	public boolean isRendResultadoBusca() {
		return rendResultadoBusca;
	}
	
	public void setUser(Usuario user) {
		this.userSel = user;
	}

	public String getImageCut() {
		return imageCut;
	}

	public void setImageCut(String imageCut) {
		this.imageCut = imageCut;
	}

	public Imagen getImagenCap() {
		return imagenCap;
	}

	public void setImagenCap(Imagen imagenCap) {
		this.imagenCap = imagenCap;
	}

	public boolean isRedimencionarPadrao() {
		return redimencionarPadrao;
	}

	public void setRedimencionarPadrao(boolean redimencionarPadrao) {
		this.redimencionarPadrao = redimencionarPadrao;
	}

	public UsuarioBO getUsuarioBO() {
		return usuarioBO;
	}
}
