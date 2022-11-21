package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.csv.CSVRecord;

import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.dao.GrupoRefeicoesDAO;
import br.edu.iffar.fw.classBag.db.dao.ServidorDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoVinculoDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.db.model.Servidor;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;

@Named
@ViewScoped
public class ServidorBean implements Serializable, BreadCrumbControl{
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private MessagesUtil messages;

	@Inject private CursosDAO cursosDAO;
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private TipoVinculoDAO tipoVinculoDAO;
	@Inject private ServidorDAO servidorDAO;
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
	private boolean rendDadosServidor;
	

	private int tipoBusca = 1;
	private Curso curso;
	private BreadCrumb breadCrumb;
	
	private String busca = "";
	private Usuario user;
	private Servidor servidor;
	private Imagen imagen;

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
				.addItem("Busca de usuário", "#{servidorBean.telaFiltroBusca()}", "frmMain", BreadCrumb.RAIZ)// 1
				.addItem("Lista de usuários", "#{servidorBean.telaListaResultadoBusca()}", "frmMain", 1)// 2
				.addItem("Dados do usuário", "#{servidorBean.telaDadosUsuario()}", "frmMain", 2)// 3
				.addItem("Dados do usuário", "#{servidorBean.telaDadosUsuario()}}", "frmMain", 1)// 4
				.addItem("Dados da matrícula", "#{servidorBean.telaDadosServidor()}", "frmMain", 4)// 5
				.addItem("Editar matrícula", "#{servidorBean.telaDadosServidor()}", "frmMain", 5);// 6
			;
	}
	
	public void telaFiltroBusca() {
		this.rendBusca = true;
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = false;
		this.rendDadosServidor = false;
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
		this.rendDadosServidor = false;
		this.breadCrumb.setAtivo(3);
	}

	public void telaListaResultadoBusca() {
		this.rendBusca = false;
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = true;
		this.rendDadosServidor = false;
		this.breadCrumb.setAtivo(2);
	}

	public void telaDadosServidor() {
		this.rendBusca = false;
		this.rendDataFile = false;
		this.rendCreateUsers = false;
		this.rendDadosUsuario = false;
		this.rendResultadoBusca = false;
		this.rendDadosServidor = true;
		this.breadCrumb.setAtivo(5);
	}

    
    public void buscarUsuarioProCpf() {
    	
		if(this.tipoBusca == 1) {
			this.user = this.usuariosDAO.getUsuarioByCPF(this.busca.replace(".", "").replace("-", ""));

			if(this.user == null) {
				this.messages.addError("Usuário não foi encontrado.");
				return;
			}

			this.user.getListServidor();
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

//	public void salvarServidor() {
//    	
//    	try {
//			for (Servidor mat : this.user.getListServidor()) {
//				this.servidorDAO.updateT(mat);
//			}
//    		this.messages.addSuccess("Informações do usuário foram salvas com sucesso.");
//			this.telaFiltroBusca();
//		} catch (RollbackException e) {
//			this.messages.addError("Não foi possível salvar os dados das matrículas!");
//			this.messages.addError(e);
//		}
//    }

	public void addServidor() {
		this.servidor = new Servidor(this.user);
		this.telaDadosServidor();
	}

	public void addServidorCancela() {
		this.servidor = null;
		this.telaDadosUsuario();
	}

	public void addServidorListaUsuario() {
		Servidor s = this.servidorDAO.findServidorBySiape(this.servidor.getSiape());
		if(s != null && !s.equals(this.servidor)) {
			this.messages.addError("Já existe um servidor com esse siape!");
			return;
		}

		this.user.addServidor(this.servidor);
		
		try {
			if(this.servidor.isNovo()) {
				this.servidorDAO.persistT(this.servidor);
			}else {
				this.servidorDAO.updateT(this.servidor);
			}
    		this.messages.addSuccess("Informações do vinculo(servidor) foram salvas com sucesso.");
			this.telaFiltroBusca();
		} catch (RollbackException e) {
			this.messages.addError("Não foi possível salvar os dados do vinculo(servidor)!");
			this.messages.addError(e);
		}
		
		this.servidor = null;
		this.telaDadosUsuario();
	}

	public void removeServidor() {
		if(!this.servidorDAO.poderRemover(this.servidor)) {
			this.messages.addError("Não foi possível remover o vinulo(servidor), pois está vinculada a um agendamento!");
			return;
		}
		try {
			this.servidorDAO.removeT(this.servidor);
			this.user.getListServidor().remove(this.servidor);
			this.servidor = null;
			this.telaDadosUsuario();
			this.messages.addSuccess("Vínculo(servidor) removida com sucesso!");
		} catch (RollbackException e) {
			this.messages.addError("Não foi possivel remover a Vínculo(servidor)!");
			this.messages.addError(e);
			e.printStackTrace();
		}
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

	public List<SelectItem> getListSelectIten() {
		return listSelectIten;
	}
	public boolean isRendDadosServidor() {
		return rendDadosServidor;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public List<TipoVinculo> getListTipoVinculo() {
		if(this.listTipoVinculo == null) {
			this.listTipoVinculo = this.tipoVinculoDAO.listTipoVinculoServidores();
		}
		return listTipoVinculo;
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
	
	public TypeSituacao[] getListSituacoes(){
		return TypeSituacao.values();
	}
}
