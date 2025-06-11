package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.edu.iffar.fw.classBag.db.SessionDataStore;
import br.edu.iffar.fw.classBag.db.model.*;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

import br.edu.iffar.fw.classBag.db.dao.MatriculaDAO;
import br.edu.iffar.fw.classBag.db.dao.PresencaDAO;
import br.edu.iffar.fw.classBag.db.dao.UnidadeDAO;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.interfaces.SwitchTreeNode;
import br.edu.iffar.fw.classBag.interfaces.TreeNodeSearch;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.relatorios.RelatoriosPath;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;
import org.primefaces.model.TreeNode;

@Named
@ViewScoped
public class MoradiaChamadaBean implements Serializable {

	private static final long serialVersionUID = 22021991L;

	@Inject private MessagesUtil messagesUtil;
	@Inject private UnidadeDAO unidadeDAO;
	@Inject private MatriculaDAO matriculaDAO;
	@Inject private SessionDataStore sessionDataStore;
	@Inject private PresencaDAO ausenciaDAO;
	@Inject private RelatoriosPath relatoriosPath;	

	private Unidade root;
	private HabitanteUnidade habitanteUnidade;

	private Unidade nodeSelecionado;
	
	private Presenca presenca;
	private Usuario userLogado;

	private LocalDate dtReferencia;
	
	private List<Presenca> listAusencias = null;

	private TreeNodeSearch treeNodeSearch;
	private int tipoBusca = 3;
	Map<UUID, TreeNodeSearch> resultado = new HashMap<UUID, TreeNodeSearch>();


	@PostConstruct
	public void init() {
		this.dtReferencia = LocalDate.now();
		this.userLogado = this.sessionDataStore.getUsuarioLogado();
		this.root = this.unidadeDAO.getRootNodeUnidade();
	}

	private class SwitchItem implements SwitchTreeNode {
		@Override
		public List<ItemUnidade> getList(Unidade u) {
			return u.getListItemUnidade();
		}
	}

	private class SwitchHabitante implements SwitchTreeNode {
		@Override
		public List<HabitanteUnidade> getList(Unidade u) {
			return u.getListHabitanteUnidade();
		}
	}

	public void pesquisaFilter(SwitchTreeNode stn, Unidade u, List<TreeNodeSearch> result, String nome) {
		if (!stn.getList(u).isEmpty()) {
			result.addAll(stn.getList(u).stream().filter(item -> item.search(nome))
					.collect(Collectors.toList())
			);
		}

		if(!u.getChildren().isEmpty()) {
			for (TreeNode<Unidade> uni : u.getChildren()) {
				pesquisaFilter(stn, uni.getData(), result, nome);
			}
		}
	}

	public void atualizaBean() {

	}
	
	public List<Matricula> autoCompleteMatricla(String nome) {
		return this.matriculaDAO.listMatriculaNotHabByLikeNome(nome);
	}

	public List<TreeNodeSearch> filtroAutoComplete(String desc) {
		this.resultado.clear();
		List<TreeNodeSearch> result = new ArrayList<TreeNodeSearch>();

		SwitchTreeNode stn = new SwitchItem();

		if (this.tipoBusca == 1) {
			stn = new SwitchItem();
		} else {
			stn = new SwitchHabitante();
		}

		pesquisaFilter(stn, this.root, result, desc.trim().toLowerCase());
		this.resultado.putAll(result.stream().collect(Collectors.toMap(TreeNodeSearch::getMMId, Function.identity())));
		return result;
	}

	
	public void novaAusencia() {
		this.presenca = new Presenca(this.habitanteUnidade,this.userLogado,dtReferencia);
	}
	
	public StreamedContent getDownloadChamada() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header", RelatoriosPath.PATH_JAR_JASPER_SUB_HEADER);
		map.put("data_referencia", Date.valueOf(this.dtReferencia));

		try {
			return this.relatoriosPath.getJasper("Lista_chamada_moradia_"+this.dtReferencia, map, RelatoriosPath.JASPER_CHAMADA_MORADIA);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void removerAusencia() {
		try {
			if(!this.presenca.isNovo()) {
				this.ausenciaDAO.removeT(this.presenca);
				this.habitanteUnidade.setAusencia(null);
			}
			this.messagesUtil.addSuccess("Ausência do aluno(a) foi removida com sucesso.");
		}catch (RollbackException e) {
			this.messagesUtil.addError(e);
		} catch (Exception e) {
			this.messagesUtil.addError("Não foi possível remo a ausênciado aluno(a).");
			e.printStackTrace();
		}
	}
	
	public void salvarAusencia() {
		try {
			if(this.presenca.isNovo()) {
				this.ausenciaDAO.persistT(this.presenca);
				this.habitanteUnidade.setAusencia(this.presenca);
			}else {
				this.ausenciaDAO.mergeT(this.presenca);
			}
			
			this.messagesUtil.addSuccess("Ausência do aluno(a) foi salva com sucesso.");
		}catch (RollbackException e) {
			this.messagesUtil.addError(e);
		} catch (Exception e) {
			this.messagesUtil.addError("Não foi possível salvar a ausênciado aluno(a).");
			e.printStackTrace();
		}
	}

	public Unidade getRoot() {
		return root;
	}

	public void setRoot(Unidade root) {
		this.root = root;
	}

	public void setSelectNode(Unidade unidade) {
		this.nodeSelecionado = unidade;
	}

	public void clean() {
		if (this.nodeSelecionado != null) {
			this.nodeSelecionado.setSelected(false);
		}
		this.habitanteUnidade = null;
		this.nodeSelecionado = null;
		this.presenca = null;
		this.listAusencias = null;
	}
	
	public void trocaData() {
		this.root = this.unidadeDAO.getRootNodeUnidade();
		this.clean();
	}

	public void onSelectNode(NodeSelectEvent unidade) {
				
		//deseleciona o elemento selecionado anteiormente
		if(this.nodeSelecionado != null) {
			this.nodeSelecionado.setSelected(false);
		}
		this.nodeSelecionado = (Unidade) unidade.getTreeNode().getData();
		this.nodeSelecionado.setSelected(true);
		
		this.listAusencias = this.ausenciaDAO.listAusenciaByDtReferenciaUnidade(dtReferencia, nodeSelecionado);
		
		this.nodeSelecionado.getListHabitanteUnidade().forEach(hab->{
			this.listAusencias.forEach(au ->{
				if(hab.equals(au.getHabitanteUnidade())) {
					hab.setAusencia(au);
				}
			});
		});
	}
	
	public void onNodeCollapse(NodeCollapseEvent event) {
		this.nodeSelecionado = (Unidade) event.getTreeNode().getData();
		this.nodeSelecionado.setSelected(false);
		this.nodeSelecionado.setExpanded(false);
		System.out.println("collapse");
    }
	
	public void onNodeExpand(NodeExpandEvent event) {
		this.nodeSelecionado = (Unidade) event.getTreeNode().getData();
		this.nodeSelecionado.setSelected(false);
		this.nodeSelecionado.setExpanded(true);
		System.out.println("expanded");
    }
	
	/**
	 * Usado no auto completa do filtro
	 */
	public void onSelectFilter2(SelectEvent<TreeNodeSearch> event) {
		this.clean();		
		this.treeNodeSearch = (TreeNodeSearch) this.resultado.get(this.treeNodeSearch.getMMId());
		this.nodeSelecionado = treeNodeSearch.getUnidade();
		this.nodeSelecionado.setSelected(true);
		this.nodeSelecionado.getParent().setExpanded(true);
		
	}
	
	public Unidade getSelectNode() {
		return this.nodeSelecionado;
	}

	public Unidade getNodeSelecionado() {
		return nodeSelecionado;
	}

	public void setNodeSelecionado(Unidade nodeSelecionado) {
		this.nodeSelecionado = nodeSelecionado;
	}

	public HabitanteUnidade getHabitanteUnidade() {
		return habitanteUnidade;
	}

	public void setHabitanteUnidade(HabitanteUnidade habitanteUnidade) {
		this.habitanteUnidade = habitanteUnidade;
	}

	public TreeNodeSearch getTreeNodeSearch() {
		return treeNodeSearch;
	}

	public void setTreeNodeSearch(TreeNodeSearch treeNodeSearch) {
		this.treeNodeSearch = treeNodeSearch;
	}

	public int getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(int tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public Presenca getPresenca() {
		return presenca;
	}

	public void setPresenca(Presenca ausencia) {
		this.presenca = ausencia;
	}

	public LocalDate getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(LocalDate dtReferencia) {
		this.dtReferencia = dtReferencia;
	}
	
	
}
