package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.util.List;

import br.edu.iffar.fw.classBag.db.dao.GrupoRefeicoesDAO;
import br.edu.iffar.fw.classBag.db.dao.MatriculaDAO;
import br.edu.iffar.fw.classBag.db.model.GrupoRefeicoes;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;

@Named
@ViewScoped
public class VincularAlunosGRBean implements Serializable,BreadCrumbControl{

	private static final long serialVersionUID = 22021991L;
	
	
	@Inject private MessagesUtil messages;
	@Inject private GrupoRefeicoesDAO grupoRefeicoesDAO;
	@Inject private MatriculaDAO matriculaDAO;
	
	private BreadCrumb breadCrumb;
	private String descGrupo;
	private Matricula matricula;
	
	private GrupoRefeicoes grupoRefeicoes;
	
	private List<GrupoRefeicoes> listGrupoResfeicoes;
	
	private boolean rendOtpBusca = true;
	private boolean rendOtpGrupoRefeicoes = false;
	private boolean rendOtpListaGrupoRefeicoes = false;
	
	@PostConstruct
	private void init() {
		this.createBreadCrumb();	
		this.telaBuscaGrupoRefeicoes();
	}
	
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
				.addItem("Vincular Alunos", BreadCrumb.RAIZ)
			.addItem("Buscar Grupo de Refeições","#{vincularAlunosGRBean.telaBuscaGrupoRefeicoes}","frmPrincipal", 1)//1
			.addItem("Lista de resultados da busca", "#{vincularAlunosGRBean.telaListaGrupoRefeicoes}","frmPrincipal" ,2)
			.addItem("Vincular Alunos","#{vincularAlunosGRBean.telaGrupoRefecoes}","frmPrincipal", 3)
			;
	}
	
	public void searchGroups() {
		this.listGrupoResfeicoes = this.grupoRefeicoesDAO.listGroupsManualLinkVinculoByDesc(this.descGrupo,(this.descGrupo == null || this.descGrupo.isEmpty()));
		this.telaListaGrupoRefeicoes();
	}
	public void editGroup() {
		//remover possiveis alterações feitas em movimentações com o breadcrumb
		this.grupoRefeicoes = this.grupoRefeicoesDAO.findGrupoRefeicoesMatriculaEager(this.grupoRefeicoes);
		this.telaGrupoRefecoes();
	}
	
	public void addAluno() {
		if(this.matricula == null) {
			this.messages.addError("Selecione uma Matricula.");
			return;
		}
		if(this.grupoRefeicoes.getListMatricula().contains(this.matricula)) {
			this.messages.addError("Matricula já foi selecionada.");
			return;
		}
		this.matricula.getListGrupoRefeicoes().add(this.grupoRefeicoes);
		this.grupoRefeicoes.getListMatricula().add(this.matricula);
		this.matricula = null;
	}
	
	public void removeMatricula() {
		this.grupoRefeicoes.getListMatricula().remove(this.matricula);
	}
	
	public void salvarGrupoRefeicao() {
		try {
			this.grupoRefeicoesDAO.mergeT(this.grupoRefeicoes);
			this.grupoRefeicoes = this.grupoRefeicoesDAO.findGrupoRefeicoesMatriculaEager(this.grupoRefeicoes);
			this.messages.addSuccess("Grupo de refeições foi salvo com sucesso.");
		} catch (RollbackException e) {
			this.messages.addError(e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Matricula> autoComplete(String texto) {
		return this.matriculaDAO.listMatriculaByLikeNome(texto.toLowerCase());
	}
		
	public void telaBuscaGrupoRefeicoes() {
		this.rendOtpBusca = true;
		this.rendOtpGrupoRefeicoes = false;
		this.rendOtpListaGrupoRefeicoes = false;
		this.breadCrumb.setAtivo(2);
	}
	
	public void telaListaGrupoRefeicoes() {
		this.rendOtpBusca = false;
		this.rendOtpGrupoRefeicoes = false;
		this.rendOtpListaGrupoRefeicoes = true;
		this.breadCrumb.setAtivo(3);
	}
	
	public void telaGrupoRefecoes() {
		this.rendOtpBusca = false;
		this.rendOtpGrupoRefeicoes = true;
		this.rendOtpListaGrupoRefeicoes = false;
		this.breadCrumb.setAtivo(4);
	}
	
	@Override
	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}

	public String getDescGrupo() {
		return descGrupo;
	}

	public void setDescGrupo(String descGrupo) {
		this.descGrupo = descGrupo;
	}

	public List<GrupoRefeicoes> getListGrupoResfeicoes() {
		return listGrupoResfeicoes;
	}

	public GrupoRefeicoes getGrupoRefeicoes() {
		return grupoRefeicoes;
	}

	public void setGrupoRefeicoes(GrupoRefeicoes grupoRefeicoes) {
		this.grupoRefeicoes = grupoRefeicoes;
	}

	public boolean isRendOtpBusca() {
		return rendOtpBusca;
	}

	public boolean isRendOtpGrupoRefeicoes() {
		return rendOtpGrupoRefeicoes;
	}


	public void setRendOtpBusca(boolean rendOtpBusca) {
		this.rendOtpBusca = rendOtpBusca;
	}

	public void setRendOtpGrupoRefeicoes(boolean rendOtpGrupoRefeicoes) {
		this.rendOtpGrupoRefeicoes = rendOtpGrupoRefeicoes;
	}

	public boolean isRendOtpListaGrupoRefeicoes() {
		return rendOtpListaGrupoRefeicoes;
	}

	public void setRendOtpListaGrupoRefeicoes(boolean rendOtpListaGrupoRefeicoes) {
		this.rendOtpListaGrupoRefeicoes = rendOtpListaGrupoRefeicoes;
	}
	
//	public List<Matricula> getListMatriculasGroup() {
//		return listMatriculasGroup;
//	}
//
//	public void setListMatriculasGroup(List<Matricula> listMatriculasGroup) {
//		this.listMatriculasGroup = listMatriculasGroup;
//	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
}
