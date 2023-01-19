//package br.edu.iffar.fw.comendo.bean;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Serializable;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//import org.hibernate.validator.constraints.br.CPF;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.resource.UsersResource;
//import org.keycloak.representations.idm.RoleRepresentation;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.omnifaces.util.selectitems.SelectItemsBuilder;
//import org.primefaces.event.CaptureEvent;
//import org.primefaces.event.FileUploadEvent;
//import org.primefaces.model.StreamedContent;
//import org.primefaces.model.file.UploadedFile;
//import org.wildfly.security.http.oidc.OidcSecurityContext;
//
//import com.google.gson.JsonIOException;
//import com.google.gson.JsonSyntaxException;
//
//import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
//import br.edu.iffar.fw.classBag.db.dao.ImagenDAO;
//import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
//import br.edu.iffar.fw.classBag.db.model.Curso;
//import br.edu.iffar.fw.classBag.db.model.Imagen;
//import br.edu.iffar.fw.classBag.db.model.Usuario;
//import br.edu.iffar.fw.classBag.sec.HasRoleBean;
//import br.edu.iffar.fw.classBag.sec.KeycloakAdminUtil;
//import br.edu.iffar.fw.classBag.util.BreadCrumb;
//import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
//import br.edu.iffar.fw.classBag.util.MessagesUtil;
//import br.edu.iffar.relatorios.CarteirinhaUtil;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.Resource;
//import jakarta.enterprise.concurrent.ManagedExecutorService;
//import jakarta.enterprise.inject.Instance;
//import jakarta.faces.model.SelectItem;
//import jakarta.faces.view.ViewScoped;
//import jakarta.inject.Inject;
//import jakarta.inject.Named;
//import jakarta.transaction.RollbackException;
//import jakarta.validation.constraints.NotNull;
//
//@Named
//@ViewScoped
//public class ImportarUsuariosBean implements Serializable, BreadCrumbControl {
//
//	private static final long serialVersionUID = 22021991L;
//
//	@Inject private MessagesUtil messages;
//
//	@Resource private ManagedExecutorService mes;
//	@Inject private Instance<BackgroundUserCreate> insbuc;
//	@Inject private CursosDAO cursosDAO;
//	@Inject private UsuariosDAO usuariosDAO;
//	@Inject private ImagenDAO imagenDAO;
//	@Inject private CarteirinhaUtil carteirinhaUtil;
//	@Inject private OidcSecurityContext oidcSecurityContext;
//	@Inject private HasRoleBean hasRoleBean;
//
//	private StringBuffer saidaTextoImportUser = new StringBuffer();
//
//	private UploadedFile file;
//	private CSVParser parcer;
//	private List<CSVRecord> listRescord;
//	private List<Curso> listCurso;
//
//	private boolean rendBusca;
//	private boolean rendDataFile;
//	private boolean rendCreateUsers;
//
//	private boolean rendDadosUsuario;
//
//	private int colunaRole;
//	private boolean possuiRole = false;
//	private boolean inativarMatriculasAusentes = false;
//	private int tipoBusca = 3;
//	private Curso curso;
//	private BreadCrumb breadCrumb;
//
//	private char delimitadorColuna;
//	private Character delimitadorTexto;
//	private String codificacaoArquivo;
//
//	private boolean capturouImage = false;
//
//	private int firstRecrd = 1;
//
//	//sessao usuarios
//
//	@CPF
//	private String busca = "";
//	private Usuario user;
//	private Imagen imagen;
//	private List<RoleRepresentation> roles;
//	private List<SelectItem> listSelectIten = new ArrayList<SelectItem>();
//
//	@PostConstruct
//	private void init() {
////		this.listSelectIten.add(new SelectItem(1, "CPF"));
////		this.listSelectIten.add(new SelectItem(4, "Nome"));
//		//this.listSelectIten.add(new SelectItem(4, "Nome"));
//
//		if(hasRoleBean.isHasIffarAdmin()) {
//			this.listSelectIten.add(new SelectItem(2, "Importar Usuários"));
//			this.listSelectIten.add(new SelectItem(3, "Gerar Carterinhas"));
//		}
//
//		this.createBreadCrumb();
//		this.telaFiltroBusca();
//	}
//
//	public void createBreadCrumb() {
//		this.breadCrumb = new BreadCrumb()
//			.inicializa()
//				.addItem("Busca de usuário", "#{importarUsuariosBean.telaFiltroBusca()}", "frmMain", BreadCrumb.RAIZ)// 1
//				.addItem("Importar usuários", "#{importarUsuariosBean.telaShowDataFile()}", "frmMain", 1)// 2
//				.addItem("Dados do usuário", "#{importarUsuariosBean.telaDadosUsuario()}", "frmMain", 1);// 3
//			;
//	}
//
//	public void telaFiltroBusca() {
//		this.rendBusca = true;
//		this.rendDataFile = false;
//		this.rendCreateUsers = false;
//		this.rendDadosUsuario = false;
//		this.breadCrumb.setAtivo(1);
//	}
//
//	public void telaShowDataFile() {
//		this.rendBusca = false;
//		this.rendDataFile = true;
//		this.rendCreateUsers = true;
//		this.rendDadosUsuario = false;
//		this.breadCrumb.setAtivo(2);
//	}
//
//	public void telaDadosUsuario() {
//		this.rendBusca = false;
//		this.rendDataFile = false;
//		this.rendCreateUsers = false;
//		this.rendDadosUsuario = true;
//		this.breadCrumb.setAtivo(3);
//	}
//
//    public UploadedFile getFile() {
//        return file;
//    }
//
//    public void setFile(UploadedFile file) {
//        this.file = file;
//    }
//
//    public void upload() {
//    	try {
////    		this.file = event.getFile();
//////	        if (this.file != null) {
//////	        	messages.addSuccess("Arquivo ", this.file.getFileName() + " enviado.");
//////	        }
//	    	this.parcer = CSVFormat.DEFAULT
//	    			.withDelimiter(this.delimitadorColuna)
//	    			.withQuote(delimitadorTexto)
//	    			.parse(new InputStreamReader(this.file.getInputStream(),this.codificacaoArquivo));
//
//	        this.listRescord = this.parcer.getRecords();
//	        this.listCurso = this.cursosDAO.listAllCursos();
//	        this.telaShowDataFile();
//    	} catch (IOException e) {
//    		messages.addSuccess("Erro de parse no arquivo");
//			e.printStackTrace();
//			this.telaFiltroBusca();
//		}
//    }
//
//    public void buscarUsuarioProCpf() {
//		this.user = this.usuariosDAO.getUsuarioByCPF(this.busca.replace(".", "").replace("-", ""));
//
//    	if(this.user == null) {
//    		messages.addError("Nenhum usuário encontrado!");
//    		return;
//    	}else {
//    		this.imagen = this.imagenDAO.findByUsuarioIfNullPattern(this.user);
//    		this.telaDadosUsuario();
//    	}
//		try {
//    		Keycloak k =  KeycloakAdminUtil.getKeyCloak(this.oidcSecurityContext);
//    		UsersResource u = k.proxy(UsersResource.class, new URI(KeycloakAdminUtil.LINK_ADMIN_AUTH +"/users"));
//
//    		List<UserRepresentation> l = u.search(this.user.getUserName());
//    		if(l.size()==1) {
//				this.roles = u.get(l.get(0).getId()).roles().getAll().getRealmMappings();
//			} else {
//				this.roles = null;
//    		}
//		} catch (JsonIOException | JsonSyntaxException | URISyntaxException  e) {
//			e.printStackTrace();
//			this.roles = null;
//		}catch (Exception  e) {
//			e.printStackTrace();
//			this.roles = null;
//		}
//	}
//
//	public void initCreateUsers() {
//
//		BackgroundUserCreate buc =  insbuc.get();
//		buc.setDados(this.listRescord,this.saidaTextoImportUser,null,this.curso,(this.possuiRole?this.colunaRole:-1),this.inativarMatriculasAusentes,this.firstRecrd);
//		mes.execute(buc);
//		System.out.println("continuou");
//    }
//
//    public void handleFileUpload(FileUploadEvent event) {
//    	try {
//    		this.file = event.getFile();
////	        if (this.file != null) {
////	        	messages.addSuccess("Arquivo ", this.file.getFileName() + " enviado.");
////	        }
//	    	this.parcer = CSVFormat.DEFAULT
//	    			.withDelimiter(this.delimitadorColuna)
//	    			.withQuote(delimitadorTexto)
//	    			.parse(new InputStreamReader(this.file.getInputStream(),this.codificacaoArquivo));
//
//	        this.listRescord = this.parcer.getRecords();
//	        this.listCurso = this.cursosDAO.listAllCursos();
//	        this.telaShowDataFile();
//    	} catch (IOException e) {
//    		messages.addSuccess("Erro de parse no arquivo");
//			e.printStackTrace();
//			this.telaFiltroBusca();
//		}
//    }
//
//    public List<SelectItem> getListDelimitatadorColuna() {
//    	return new SelectItemsBuilder().add(",", ",").add(";", ";").add("|", "|").buildList();
//    }
//
//    public List<SelectItem> getListCodificacaoArquivo() {
//    	return new SelectItemsBuilder().add("WINDOWS-1252", "WINDOWS-1252").add("ISO-8859-1", "ISO-8859-1").add("UTF-8", "UTF-8").buildList();
//    }
//
//    public List<SelectItem> getListDelimitadorTexto() {
//    	return new SelectItemsBuilder().add(null, "Não usar").add("\"", "\"").add("\'", "\'").buildList();
//    }
//
//    public void salvarUser() {
//
//    	try {
//			if(this.capturouImage) {
//				this.imagenDAO.substituiImagen(this.imagen);
//				this.capturouImage = false;
//			}
//
//			this.usuariosDAO.updateT(this.user);
//			this.messages.addSuccess("Informações do usuário foram salvas com sucesso.");
//		} catch (RollbackException e) {
//			this.messages.addError(e);
//		} catch (IOException e) {
//			e.printStackTrace();
//			this.messages.addError("Não foi possivel salvar os dados do usuário.");
//			this.messages.addError("Erro ao escrever mudanças na imagem do usuário.");
//		}
//    }
//
//	public StreamedContent gerarCarterinhaQRCode(boolean nova) {
//		return this.carteirinhaUtil.gerarCarterinhaQRCode(nova, this.user);
//	}
//
//	public void oncapture(CaptureEvent captureEvent) {
//		if(this.imagen == null || this.imagen.equals(Imagen.SEMIMAGEN)) {
////	    	SecureRandom random = new SecureRandom();
////	    	byte []bname = new byte[32];
////	    	random.nextBytes(bname);
////    		Base64.getEncoder().encodeToString(bname).replaceAll("\\W", "_")
//			this.imagen = new Imagen(UUID.randomUUID().toString(), this.user, captureEvent.getData());
//		} else {
//			this.imagen.setBytes(captureEvent.getData());
//		}
//
//		this.capturouImage = true;
//	}
//
//	public int getTipoBusca() {
//		return tipoBusca;
//	}
//
//	public void setTipoBusca(int tipoBusca) {
//		this.tipoBusca = tipoBusca;
//	}
//
//	public boolean isRendBusca() {
//		return rendBusca;
//	}
//
//	public boolean isRendDataFile() {
//		return rendDataFile;
//	}
//
//	public boolean isRendCreateUsers() {
//		return rendCreateUsers;
//	}
//
//	public String getSaidaTextoImportUser() {
//		return this.saidaTextoImportUser.toString();
//	}
//
//	public int getSizeListRecord() {
//		if(this.listRescord == null)
//			return 0;
//		return this.listRescord.size();
//	}
//
//	public List<Curso> getListCurso() {
//		return listCurso;
//	}
//
//	public void setListCurso(List<Curso> listCurso) {
//		this.listCurso = listCurso;
//	}
//
//	@NotNull(message = "Seleciona um curso.")
//	public Curso getCurso() {
//		return curso;
//	}
//
//	public void setCurso(Curso curso) {
//		this.curso = curso;
//	}
//
//	public int getColunaRole() {
//		return colunaRole;
//	}
//
//	public void setColunaRole(int colunaRole) {
//		this.colunaRole = colunaRole;
//	}
//
//	public boolean isPossuiRole() {
//		return possuiRole;
//	}
//
//	public void setPossuiRole(boolean possuiRole) {
//		this.possuiRole = possuiRole;
//	}
//
//	public boolean isInativarMatriculasAusentes() {
//		return inativarMatriculasAusentes;
//	}
//
//	public void setInativarMatriculasAusentes(boolean inativarMatriculasAusentes) {
//		this.inativarMatriculasAusentes = inativarMatriculasAusentes;
//	}
//
//	@Override
//	public BreadCrumb getBreadCrumb() {
//		return this.breadCrumb;
//	}
//
//	public String getBusca() {
//		return busca;
//	}
//
//	public void setBusca(String busca) {
//		this.busca = busca;
//	}
//
//	public boolean isRendDadosUsuario() {
//		return rendDadosUsuario;
//	}
//
//	public Usuario getUser() {
//		return user;
//	}
//
//    public Imagen getImagen() {
//		return imagen;
//	}
//
//	public void setImagen(Imagen imagen) {
//		this.imagen = imagen;
//	}
//
//	public List<RoleRepresentation> getRoles() {
//		return roles;
//	}
//
//	public List<SelectItem> getListSelectIten() {
//		return listSelectIten;
//	}
//
//	public char getDelimitadorColuna() {
//		return delimitadorColuna;
//	}
//
//	public void setDelimitadorColuna(char delimitadorColuna) {
//		this.delimitadorColuna = delimitadorColuna;
//	}
//
//	public Character getDelimitadorTexto() {
//		return delimitadorTexto;
//	}
//
//	public void setDelimitadorTexto(Character delimitadorTexto) {
//		this.delimitadorTexto = delimitadorTexto;
//	}
//
//	public void setCodificacaoArquivo(String codificacaoArquivo) {
//		this.codificacaoArquivo = codificacaoArquivo;
//	}
//
//	public String getCodificacaoArquivo() {
//		return codificacaoArquivo;
//	}
//
//	public int getFirstRecrd() {
//		return firstRecrd;
//	}
//
//	public void setFirstRecrd(int firstRecrd) {
//		this.firstRecrd = firstRecrd;
//	}
//
//
//}
