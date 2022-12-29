package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.keycloak.representations.idm.RoleRepresentation;
import org.omnifaces.util.selectitems.SelectItemsBuilder;
import org.primefaces.model.DualListModel;

import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.dao.GrupoRefeicoesDAO;
import br.edu.iffar.fw.classBag.db.dao.MatriculaDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoVinculoDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Servidor;
import br.edu.iffar.fw.classBag.db.model.SituacaoMatricula;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;
import jakarta.validation.constraints.NotNull;

@Named
@ViewScoped
public class MatriculaBean implements Serializable, BreadCrumbControl{
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private MessagesUtil messages;

	@Inject private CursosDAO cursosDAO;
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private TipoVinculoDAO tipoVinculoDAO;
	@Inject private MatriculaDAO matriculaDAO;
	@Inject private GrupoRefeicoesDAO grupoRefeicoesDAO;
	
	private StringBuffer saidaTextoImportUser = new StringBuffer();
	
	private List<CSVRecord> listRescord;
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

	private int firstRecrd = 1;
	
	//sessao usuarios
	
	private String busca = "";
	private Usuario user;
	private Matricula matricula;
	private Servidor servidor;
	private Imagen imagen;
	private DualListModel<String> listModelPermissaoes;

	private List<RoleRepresentation> roles;
	private List<SelectItem> listSelectIten = new ArrayList<SelectItem>();
	
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
				.addItem("Busca de usuário", "#{matriculaBean.telaFiltroBusca()}", "frmMain", BreadCrumb.RAIZ)// 1
				.addItem("Lista de usuários", "#{matriculaBean.telaListaResultadoBusca()}", "frmMain", 1)// 2
				.addItem("Dados do usuário", "#{matriculaBean.telaDadosUsuario()}", "frmMain", 2)// 3
				.addItem("Dados do usuário", "#{matriculaBean.telaDadosUsuario()}}", "frmMain", 1)// 4
				.addItem("Dados da matrícula", "#{matriculaBean.telaDadosMatricula()}", "frmMain", 4)// 5
				.addItem("Editar matrícula", "#{matriculaBean.telaDadosMatricula()}", "frmMain", 5);// 6
			;
	}
	
	public void telaFiltroBusca() {
		this.rendBusca = true;
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = false;
		this.rendDadosMatricula = false;
		this.breadCrumb.setAtivo(1);
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
		this.rendDadosMatricula = false;
		this.breadCrumb.setAtivo(3);
	}

	public void telaListaResultadoBusca() {
		this.rendBusca = false;
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = true;
		this.rendDadosMatricula = false;
		this.breadCrumb.setAtivo(2);
	}

	public void telaDadosMatricula() {
		this.rendBusca = false;
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = false;
		this.rendDadosMatricula = true;
		this.breadCrumb.setAtivo(5);
	}

    
    public void buscarUsuarioProCpf() {
    	
		if(this.tipoBusca == 1) {
			this.user = this.usuariosDAO.getUsuarioByCPF(this.busca.replace(".", "").replace("-", ""));

			if(this.user == null) {
				this.messages.addError("Usuário não foi encontrado.");
				return;
			}

			this.user.getListMatricula();
			this.telaDadosUsuario();
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
		this.telaDadosUsuario();
	}

    
    public List<SelectItem> getListDelimitatadorColuna() {
    	return new SelectItemsBuilder().add(",", ",").add(";", ";").add("|", "|").buildList();
    }

    public List<SelectItem> getListDelimitadorTexto() {
    	return new SelectItemsBuilder().add(null, "Não usar").add("\"", "\"").add("\'", "\'").buildList();
    }
    
	public void salvarMatricula() {
    	
    	try {
			for (Matricula mat : this.user.getListMatricula()) {
				this.matriculaDAO.updateT(mat);
			}
    		this.messages.addSuccess("Informações do usuário foram salvas com sucesso.");
			this.telaFiltroBusca();
		} catch (RollbackException e) {
			this.messages.addError("Não foi possível salvar os dados das matrículas!");
			this.messages.addError(e);
		}
    }

	public void addMatricula() {
		this.matricula = new Matricula(this.user);
		this.telaDadosMatricula();
	}

	public void addMatriculaCancela() {
		this.matricula = null;
		this.telaDadosUsuario();
	}

	public void addMatriculaListaUsuario() {
		Matricula m = this.matriculaDAO.findByIdMatricula(this.matricula.getIdMatricula());
		if(m != null && !m.equals(this.matricula)) {
			this.messages.addError("Já existe esse numéro de matrícula!");
			return;
		}

		if(this.matricula.getListSituacaoMatricula() == null || this.matricula.getListSituacaoMatricula().isEmpty()) {
			this.messages.addError("Informa a situação da matrícula!");
			return;
		}
		this.user.addMatricula(this.matricula);
		this.matricula = null;
		this.telaDadosUsuario();
	}

	public void removeMatricula() {
		if(this.grupoRefeicoesDAO.isVinculadaAGrupo(matricula)) {
			this.messages.addError("Não foi possivel remover a matícula, pois está vinculada a um grupo de refeições!");
			return;
		}
		
		try {
			this.matriculaDAO.removeT(this.matricula);
			this.user.getListMatricula().remove(this.matricula);
			this.matricula = null;
			this.telaDadosUsuario();
			this.messages.addSuccess("Matícula removida com sucesso!");
		} catch (RollbackException e) {
			this.messages.addError("Não foi possivel remover a matícula!");
			this.messages.addError(e);
			e.printStackTrace();
		}
	}

	public void addSituacaoAtiva() {
		this.matricula.addSituacaoMatricula(new SituacaoMatricula(TypeSituacao.ATIVA, LocalDateTime.now(), matricula));
	}

	public void addSituacaoInativa() {
		this.matricula.addSituacaoMatricula(new SituacaoMatricula(TypeSituacao.INATIVA, LocalDateTime.now(), matricula));
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
	
	public String getSaidaTextoImportUser() {
		return this.saidaTextoImportUser.toString();
	}

	public int getSizeListRecord() {
		if(this.listRescord == null)
			return 0;
		return this.listRescord.size();
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
}
