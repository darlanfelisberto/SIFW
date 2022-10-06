package br.edu.iffar.fw.comendo.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.RollbackException;
import javax.validation.constraints.NotNull;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.wildfly.security.http.oidc.OidcSecurityContext;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.dao.ImagenDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoVinculoDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Servidor;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.sec.HasRoleBean;
import br.edu.iffar.fw.classBag.sec.KeycloakAdminUtil;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.relatorios.CarteirinhaUtil;

@Named
@ViewScoped
public class UsuariosBean implements Serializable, BreadCrumbControl{
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private MessagesUtil messages;

	@Inject private Instance<BackgroundUserCreate> insbuc;
	@Inject private CursosDAO cursosDAO;
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private ImagenDAO imagenDAO;
	@Inject private CarteirinhaUtil carteirinhaUtil;
	@Inject private OidcSecurityContext oidcSecurityContext; 
	@Inject private HasRoleBean hasRoleBean;
	@Inject private TipoVinculoDAO tipoVinculoDAO;
	

	
	private List<Curso> listCurso;
	private List<TipoVinculo> listTipoVinculo;
	private List<Usuario> listUsuarios;
	

	private boolean rendBusca;
	private boolean rendDataFile;
	private boolean rendCreateUsers;
	private boolean rendResultadoBusca;
	
	private boolean rendDadosUsuario;
	private boolean rendDadosMatricula;
	

	private int colunaRole;
	private boolean possuiRole = false;
	private boolean inativarMatriculasAusentes = false;
	private int tipoBusca = 1;
	private Curso curso;
	private BreadCrumb breadCrumb;
	
	private char delimitadorColuna;
	private Character delimitadorTexto;
	private String codificacaoArquivo;
	
	private boolean capturouImage = false;
	private String imageCut = "";
	private Imagen imagenCap;
	
	private boolean redimencionarPadrao = true;

	private int firstRecrd = 1;
	
	//sessao usuarios
	
	private String busca = "";
	private Usuario user;
	private Matricula matricula;
	private Servidor servidor;
	private Imagen imagen;
	private BackgroundUserCreate backgroundUserCreate;
	private DualListModel<String> listModelPermissaoes;

	private List<RoleRepresentation> roles;
	private List<SelectItem> listSelectIten = new ArrayList<SelectItem>();
	
	@PostConstruct
	private void init() {
		this.listSelectIten.add(new SelectItem(1, "CPF"));
		this.listSelectIten.add(new SelectItem(4, "Nome"));
		//this.listSelectIten.add(new SelectItem(4, "Nome"));

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
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = false;
		this.breadCrumb.setAtivo(1);
		this.busca = "";
		this.imageCut = "";
		this.imagenCap = null;
	}
	
	public void telaShowDataFile() {
		this.rendBusca = false;
		this.rendDataFile = true;
		this.rendCreateUsers = true;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = false;
		this.breadCrumb.setAtivo(2);
	}
	
	public void telaDadosUsuario() {
		this.rendBusca = false;
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = true;
		this.rendResultadoBusca = false;
		this.breadCrumb.setAtivo(3);
	}

	public void telaListaResultadoBusca() {
		this.rendBusca = false;
		this.rendDataFile = false;
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
			this.user = this.usuariosDAO.getUsuarioByCPF(this.busca.replace(".", "").replace("-", ""));

			if(this.user == null) {
				this.messages.addError("Usuário não foi encontrado.");
				return;
			}

			carregaDadosUserNecessario();
			this.telaDadosUsuario();
			this.breadCrumb.setAtivo(4);
		} else if(this.tipoBusca == 4) {// nome

			if(this.busca.trim().length() < 3) {
				this.messages.addError("Informe 3 caracteres para iniciar a busca.");
				return;
			}

			this.listUsuarios = this.usuariosDAO.listAllUsersByName(this.busca);
			this.telaListaResultadoBusca();
		}
	}

	public void selecionaUsuario() {
		this.carregaDadosUserNecessario();
		this.telaDadosUsuario();
	}

	public void carregaDadosUserNecessario() {
    	if(this.user == null) {
    		messages.addError("Nenhum usuário encontrado!");
    		return;
    	}else {
    		this.imagen = this.imagenDAO.findByUsuarioIfNullPattern(this.user);
    		this.telaDadosUsuario();
    	}
		this.carregaRoles();
	}

	public void carregaRoles() {
		try {
			listModelPermissaoes = new DualListModel<String>(new ArrayList<String>(), new ArrayList<String>());
    		Keycloak k =  KeycloakAdminUtil.getKeyCloak(this.oidcSecurityContext);
    		UsersResource u = k.proxy(UsersResource.class, new URI(KeycloakAdminUtil.LINK_ADMIN_AUTH +"/users"));
    		
			RealmResource r = k.realm(KeycloakAdminUtil.AUTH_REALM);

			r.roles().list().forEach(role -> {
				this.listModelPermissaoes.getSource().add(role.getName());
			});

    		List<UserRepresentation> l = u.search(this.user.getUserName());
    		if(l.size()==1) {
				u.get(l.get(0).getId()).roles().getAll().getRealmMappings().forEach(role -> {
					this.listModelPermissaoes.getTarget().add(role.getName());
				});
    		}
			k.close();
			System.out.println(1);
		} catch (JsonIOException | JsonSyntaxException | URISyntaxException  e) {
			e.printStackTrace();
			this.roles = null;
		}catch (Exception  e) {
			e.printStackTrace();
			this.roles = null;
		}
	}

	public void novoUsuario() {
		this.user = new Usuario();
		this.user.setTokenRU(UUID.randomUUID().toString());
//		this.matricula = new Matricula();
//		this.servidor = new Servidor();

		this.carregaRoles();
		this.imagen = null;
		this.imageCut = "";
		this.imagenCap = null;
		this.capturouImage = false;
		this.telaDadosUsuario();
		this.breadCrumb.setAtivo(4);
	}
    
    public void salvarUser() {
    	
		if(this.listModelPermissaoes.getTarget().isEmpty()) {
			this.messages.addError("Informe ao menos uma permissão!");
			return;
		}

    	try {
			if(!this.user.isNovoUsuario()) {// para edição
				this.usuariosDAO.updateT(this.user);
			} else {// criação de um uauario pelo btn novo

				if(this.usuariosDAO.findByUserName(user.getUserName()) != null) {
					this.messages.addError("Username já existe!");
					return;
				}

				if(this.usuariosDAO.getUsuarioByCPF(user.getCpf()) != null) {
					this.messages.addError("CPF já existe!");
					return;
				}

				this.backgroundUserCreate = insbuc.get();

				this.backgroundUserCreate.setDados(null, new StringBuffer(), null, null, 1, false, 0);
				this.backgroundUserCreate.unickRun(user, true, matricula, this.listModelPermissaoes.getTarget());
			}
			if(this.capturouImage) {
				this.imagenDAO.substituiImagen(this.imagen);
				this.capturouImage = false;
				this.imageCut = "";
				this.imagenCap = null;
			}
			this.telaFiltroBusca();
			this.messages.addSuccess("Informações do usuário foram salvas com sucesso.");
		} catch (RollbackException e) {
			this.messages.addError(e);
		} catch (IOException e) {
			e.printStackTrace();
			this.messages.addError("Não foi possivel salvar os dados do usuário.");
			this.messages.addError("Erro ao escrever mudanças na imagem do usuário.");
		}
    }
        
	public StreamedContent gerarCarterinhaQRCode(boolean nova) {
		return this.carteirinhaUtil.gerarCarterinhaQRCode(nova, this.user);
	}

	public void oncapture(CaptureEvent captureEvent) {
		if(this.imagenCap == null) {
			this.imagenCap = new Imagen(UUID.randomUUID().toString(), this.user, captureEvent.getData());
		} else {
			this.imagenCap.setBytes(captureEvent.getData());
		}
		
		this.capturouImage = true;
	}
	
    public void handleFileUpload(FileUploadEvent event) {
		if(this.imagenCap == null) {
			this.imagenCap = new Imagen(UUID.randomUUID().toString(), this.user, event.getFile().getContent());
		} else {
			this.imagenCap.setBytes(event.getFile().getContent());
		}

		this.capturouImage = true;
	}

	public void cortarImagen() {

		if(this.imagenCap != null && (this.imagen == null || this.imagen.equals(Imagen.SEMIMAGEN))) {
			this.imagen = new Imagen(UUID.randomUUID().toString(), this.user, null);
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

	public boolean isRendDataFile() {
		return rendDataFile;
	}

	public boolean isRendCreateUsers() {
		return rendCreateUsers;
	}	

	public List<Curso> getListCurso() {
		if(this.listCurso == null) {
			this.listCurso = this.cursosDAO.listAllCursos();
		}
		return listCurso;
	}

	public void setListCurso(List<Curso> listCurso) {
		this.listCurso = listCurso;
	}

	@NotNull(message = "Selecione um curso.")
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public int getColunaRole() {
		return colunaRole;
	}

	public void setColunaRole(int colunaRole) {
		this.colunaRole = colunaRole;
	}

	public boolean isPossuiRole() {
		return possuiRole;
	}

	public void setPossuiRole(boolean possuiRole) {
		this.possuiRole = possuiRole;
	}

	public boolean isInativarMatriculasAusentes() {
		return inativarMatriculasAusentes;
	}

	public void setInativarMatriculasAusentes(boolean inativarMatriculasAusentes) {
		this.inativarMatriculasAusentes = inativarMatriculasAusentes;
	}

	@Override
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

	public Usuario getUser() {
		return user;
	}
	    
    public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	}

	public List<RoleRepresentation> getRoles() {
		return roles;
	}

	public List<SelectItem> getListSelectIten() {
		return listSelectIten;
	}

	public char getDelimitadorColuna() {
		return delimitadorColuna;
	}

	public void setDelimitadorColuna(char delimitadorColuna) {
		this.delimitadorColuna = delimitadorColuna;
	}

	public Character getDelimitadorTexto() {
		return delimitadorTexto;
	}

	public void setDelimitadorTexto(Character delimitadorTexto) {
		this.delimitadorTexto = delimitadorTexto;
	}

	public void setCodificacaoArquivo(String codificacaoArquivo) {
		this.codificacaoArquivo = codificacaoArquivo;
	}

	public String getCodificacaoArquivo() {
		return codificacaoArquivo;
	}

	public int getFirstRecrd() {
		return firstRecrd;
	}

	public void setFirstRecrd(int firstRecrd) {
		this.firstRecrd = firstRecrd;
	}

	public boolean isRendDadosMatricula() {
		return rendDadosMatricula;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public List<TipoVinculo> getListTipoVinculoAlunos() {
		if(this.listTipoVinculo == null) {
			this.listTipoVinculo = this.tipoVinculoDAO.listTipoVinculoAlunos();
		}
		return listTipoVinculo;
	}

	public DualListModel<String> getListModelPermissaoes() {
		return listModelPermissaoes;
	}

	public void setListModelPermissaoes(DualListModel<String> listModelPermissaoes) {
		this.listModelPermissaoes = listModelPermissaoes;
	}

	public List<Usuario> getListUsuarios() {
		return listUsuarios;
	}

	public boolean isRendResultadoBusca() {
		return rendResultadoBusca;
	}
	
	public void setUser(Usuario user) {
		this.user = user;
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
}
