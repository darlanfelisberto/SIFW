package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.iffar.fw.classBag.db.dao.GrupoRefeicoesDAO;
import br.edu.iffar.fw.classBag.db.dao.RefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoRefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoVinculoDAO;
import br.edu.iffar.fw.classBag.db.model.GrupoRefeicoes;
import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
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
public class GrupoRefeicoesBean implements Serializable,BreadCrumbControl{

	private static final long serialVersionUID = 22021991L;
	
	
	@Inject private MessagesUtil messages;
	@Inject private GrupoRefeicoesDAO grupoRefeicoesDAO;
	@Inject private TipoRefeicaoDAO tipoRefeicaoDAO;
	@Inject private TipoVinculoDAO tipoVinculoDAO;
	@Inject private RefeicaoDAO refeicaoDAO;
	
	private BreadCrumb breadCrumb;
	private String descGrupo = "";
	private boolean all;
	
	private GrupoRefeicoes grupoRefeicoes;
	private Refeicao refeicao;
	
	private List<GrupoRefeicoes> listGrupoResfeicoes;
	private List<TipoRefeicao> listTipoRefeicao;
	private List<TipoVinculo> listTipoVinculo;
	
	private boolean rendOtpBusca = true;
	private boolean rendOtpGrupoRefeicoes = false;
	private boolean rendOtpListaGrupoRefeicoes = false;
	private boolean rendOtpTipoRefeicao = false;
	
	@PostConstruct
	private void init() {
		this.createBreadCrumb();	
		this.telaBuscaGrupoRefeicoes();
	}
	
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
				.addItem("Grupo de Refeições", BreadCrumb.RAIZ)// 1
				.addItem("Busca Grupo", "#{grupoRefeicoesBean.telaBuscaGrupoRefeicoes}", "frmMain", 1)// 2
				.addItem("Lista de grupos", "#{grupoRefeicoesBean.telaListaGrupoRefeicoes}", "frmMain", 2)// 3
				.addItem("Editar Grupo", "#{grupoRefeicoesBean.telaGrupoRefecoes}", "frmMain", 3)// 4
				.addItem("Editar Refeição de Grupo", 4)// 5
				.addItem("Novo Grupo", "#{grupoRefeicoesBean.telaGrupoRefecoes}", "frmMain", 1)// 6
				.addItem("Editar Refeição do Grupo", 6)// 7
			;
	}
	
	public void searchGroups() {
		if(this.descGrupo == null || this.descGrupo.isEmpty()) {
			this.all = true;
		}
		this.listGrupoResfeicoes = this.grupoRefeicoesDAO.listGroupsByDesc(this.descGrupo,this.all);
		this.telaListaGrupoRefeicoes();
	}
	
	public void deleteGrupoRefeicao() {
		if(this.grupoRefeicoesDAO.grupoRefeicaoEmUso(this.grupoRefeicoes)) {
			this.messages.addWarn("Não foi possivel Exluir o grupo de refeições.","Grupo possui agendamentos.");
			return;
		}
		try {
			this.grupoRefeicoesDAO.removeT(this.grupoRefeicoes);
			this.messages.addSuccess("Grupo de refeições removido com sucesso.");
			this.searchGroups();
		} catch (RollbackException e) {
			e.printStackTrace();
			this.messages.addError(e);
			this.messages.addError("Não foi possivel Exluir o grupo de refeições.");
		}
		catch (Exception e) {
			e.printStackTrace();
			this.messages.addError("Não foi possivel Exluir o grupo de refeições.");
		}
		
	}
	
	public void novoGrupoRefeicoes() {
		//<f:validateBean disabled="true"></f:validateBean>
		this.grupoRefeicoes = new GrupoRefeicoes(true);
		this.telaNovoGrupoRefeicoes();
	}
	
	public void editGroup() {
		this.grupoRefeicoes = this.grupoRefeicoesDAO.findById(this.grupoRefeicoes);
		this.telaGrupoRefecoes();
	}
	
	public void novaRefeicao() {
		this.refeicao = new Refeicao();
		this.telaEditTipoRefeicao();
	}
	
	public void addRefeicaoOrUpdate() {
		if(this.grupoRefeicoes.getListRefeicao() == null) {
			this.grupoRefeicoes.setListRefeicao(new ArrayList<Refeicao>());
		}
		if(!this.grupoRefeicoes.getListRefeicao().contains(this.refeicao)) {
			this.refeicao.setGrupoRefeicoes(this.grupoRefeicoes);
			this.grupoRefeicoes.getListRefeicao().add(this.refeicao);
		}

		this.telaGrupoRefecoes();
		this.refeicao = null;
	}
	
	public void cancelarAddTipoRefeicao() {
		this.telaGrupoRefecoes();
	}
	
	public void salvarGrupoRefeicao() {
		try {
//			if(this.grupoRefeicoes.isNovo()){
//				this.grupoRefeicoesDAO.persistT(this.grupoRefeicoes);
//			}else {
				this.grupoRefeicoes = this.grupoRefeicoesDAO.mergeT(this.grupoRefeicoes);
//			}
			this.messages.addSuccess("Grupo de refeições salvo com sucesso.");
			this.telaBuscaGrupoRefeicoes();
		} catch (RollbackException e) {
			e.printStackTrace();
			this.messages.addError(e);
			this.messages.addError("Não foi possivel salvar o grupo de refeições.");
		}
		catch (Exception e) {
			e.printStackTrace();
			this.messages.addError("Não foi possivel salvar o grupo de refeições.");
		}
	}
	
	public void deleteRefeicao() {
		if (this.refeicaoDAO.foiUtilizada(this.refeicao)) {
			this.messages.addWarn("Não foi possivel Exluir a refeição.", "A refeição possui agendamentos.");
			return;
		}

		this.grupoRefeicoes.getListRefeicao().remove(this.refeicao);
		this.salvarGrupoRefeicao();
		this.refeicao = null;
		this.telaGrupoRefecoes();
	}
	
	public void telaBuscaGrupoRefeicoes() {
		this.rendOtpBusca = true;
		this.rendOtpGrupoRefeicoes = false;
		this.rendOtpListaGrupoRefeicoes = false;
		this.rendOtpTipoRefeicao = false;
		this.breadCrumb.setAtivo(2);
	}
	
	public void telaListaGrupoRefeicoes() {
		this.rendOtpBusca = false;
		this.rendOtpGrupoRefeicoes = false;
		this.rendOtpListaGrupoRefeicoes = true;
		this.rendOtpTipoRefeicao = false;
		this.breadCrumb.setAtivo(3);
	}
	
	public void telaGrupoRefecoes() {
		this.rendOtpBusca = false;
		this.rendOtpGrupoRefeicoes = true;
		this.rendOtpListaGrupoRefeicoes = false;
		this.rendOtpTipoRefeicao = false;
		if(this.grupoRefeicoes.isNovo()) {
			this.breadCrumb.setAtivo(6);
		} else {
			this.breadCrumb.setAtivo(4);
		}
		this.refeicao = null;
	}
	
	public void telaNovoGrupoRefeicoes() {
		this.rendOtpBusca = false;
		this.rendOtpGrupoRefeicoes = true;
		this.rendOtpListaGrupoRefeicoes = false;
		this.rendOtpTipoRefeicao = false;
		this.breadCrumb.setAtivo(6);
	}
	public void telaEditTipoRefeicao() {
		this.rendOtpBusca = false;
		this.rendOtpGrupoRefeicoes = false;
		this.rendOtpListaGrupoRefeicoes = false;
		this.rendOtpTipoRefeicao = true;
		if(this.grupoRefeicoes.isNovo()) {
			this.breadCrumb.setAtivo(7);
		} else {
			this.breadCrumb.setAtivo(5);
		}
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

	public Refeicao getRefeicao() {
		return refeicao;
	}


	public boolean isRendOtpBusca() {
		return rendOtpBusca;
	}

	public boolean isRendOtpGrupoRefeicoes() {
		return rendOtpGrupoRefeicoes;
	}

	public boolean isRendOtpTipoRefeicao() {
		return rendOtpTipoRefeicao;
	}

	public void setRefeicao(Refeicao refeicao) {
		this.refeicao = refeicao;
	}

	public void setRendOtpBusca(boolean rendOtpBusca) {
		this.rendOtpBusca = rendOtpBusca;
	}

	public void setRendOtpGrupoRefeicoes(boolean rendOtpGrupoRefeicoes) {
		this.rendOtpGrupoRefeicoes = rendOtpGrupoRefeicoes;
	}

	public void setRendOtpTipoRefeicao(boolean rendOtpTipoRefeicao) {
		this.rendOtpTipoRefeicao = rendOtpTipoRefeicao;
	}

	public boolean isRendOtpListaGrupoRefeicoes() {
		return rendOtpListaGrupoRefeicoes;
	}

	public void setRendOtpListaGrupoRefeicoes(boolean rendOtpListaGrupoRefeicoes) {
		this.rendOtpListaGrupoRefeicoes = rendOtpListaGrupoRefeicoes;
	}

	public List<TipoRefeicao> getListTipoRefeicao() {
		if(this.listTipoRefeicao == null) {
			this.listTipoRefeicao =  this.tipoRefeicaoDAO.listAll();
		}
		return listTipoRefeicao;
	}
	
	public List<TipoVinculo> getListTipoVinculo(){
		if(this.listTipoVinculo == null) {
			this.listTipoVinculo = this.tipoVinculoDAO.listAll();
		}
		return this.listTipoVinculo;
	}
	
	
}
