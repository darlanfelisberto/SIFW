package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;

import br.edu.iffar.fw.classBag.db.dao.HabitanteUnidadeDAO;
import br.edu.iffar.fw.classBag.db.dao.ItemUnidadeDAO;
import br.edu.iffar.fw.classBag.db.dao.MatriculaDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoUnidadeDAO;
import br.edu.iffar.fw.classBag.db.dao.UnidadeDAO;
import br.edu.iffar.fw.classBag.db.model.HabitanteUnidade;
import br.edu.iffar.fw.classBag.db.model.ItemUnidade;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.TipoUnidade;
import br.edu.iffar.fw.classBag.db.model.Unidade;
import br.edu.iffar.fw.classBag.db.model.interfaces.SwitchTreeNode;
import br.edu.iffar.fw.classBag.db.model.interfaces.TreeNodeSearch;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;

/**
 * @author darlan
 *
 */
/**
 * @author darlan
 *
 */
@Named
@ViewScoped
public class MoradiaEstudantilBean implements Serializable {

	private static final long serialVersionUID = 22021991L;

	@Inject private MessagesUtil messagesUtil;
	@Inject private UnidadeDAO unidadeDAO;
	@Inject private TipoUnidadeDAO tipoUnidadeDAO;
	@Inject private ItemUnidadeDAO itemUndadeDAO;
	@Inject private MatriculaDAO matriculaDAO;
	@Inject private HabitanteUnidadeDAO habitanteUnidadeDAO;

	private Unidade root;

	private ItemUnidade itemUnidade;
	private HabitanteUnidade habitanteUnidade;
	private Unidade unidadeChildren;

	private Unidade nodeSelecionado;
	private Unidade nodeOperado;


	private List<TipoUnidade> listTipoUnidade = null;

	private int operacao = 0;

	// filtro

	private TreeNodeSearch treeNodeSearch;
	private int tipoBusca = 1;
	Map<UUID, TreeNodeSearch> resultado = new HashMap<UUID, TreeNodeSearch>();


	@PostConstruct
	public void init() {
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

		if(!u.getListUnidadeChildren().isEmpty()) {
			for (Unidade uni : u.getListUnidadeChildren()) {
				pesquisaFilter(stn, uni, result, nome);
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

	public void salvarHabitanteUnidade() {
		try {
			this.habitanteUnidade.setUnidade(this.nodeSelecionado);
			if (this.habitanteUnidade.isNovo()) {
				this.habitanteUnidadeDAO.persistT(this.habitanteUnidade);
				this.nodeSelecionado.addHabitanteUnidade(this.habitanteUnidade);
			} else {
				this.habitanteUnidadeDAO.mergeT(this.habitanteUnidade);
			}
			this.messagesUtil.addSuccess("Habitante da unidade salvo com sucesso.");
			this.cancelarTudo();
		} catch (RollbackException e) {
//			this.init();
			this.messagesUtil.addSuccess("O habitante não pode ser salvo.");
			this.messagesUtil.addError(e);
		}
	}

	public void salvarNovaUnidade() {
		try {
			this.nodeOperado.setParrent(this.nodeSelecionado);
			if (this.nodeOperado.isNovo()) {
				this.unidadeDAO.persistT(this.nodeOperado);
				this.nodeSelecionado.addUnidadeChildren(this.nodeOperado);
			} else {
				this.unidadeDAO.mergeT(this.nodeOperado);
			}
			this.messagesUtil.addSuccess("Unidade salva com sucesso.");
		} catch (RollbackException e) {
			this.messagesUtil.addSuccess("A unidade não pode ser salvo.");
			this.messagesUtil.addError(e);
		}
		this.clean();
	}

	public void saveItemUnidade() {
		try {
			this.itemUnidade.setUnidade(this.nodeSelecionado);
			if (itemUnidade.isNovo()) {
				this.itemUndadeDAO.persistT(this.itemUnidade);
				this.nodeSelecionado.addItemUnidade(this.itemUnidade);
			} else {
				this.itemUndadeDAO.mergeT(this.itemUnidade);
			}
			this.messagesUtil.addSuccess("Item da unidade salvo com sucesso.");
			this.cancelarTudo();
		} catch (RollbackException e) {
			this.messagesUtil.addSuccess("O Item não pode ser salvo.");
			this.messagesUtil.addError(e);
		}
	}

	/**
	 * este metodo remove uma unidade e o banco ira remover os filhos, item e
	 * habitante em cascata
	 * 
	 * 
	 * @author darlan
	 */
	public void removeUnidadade() {
		if (this.nodeSelecionado.getParent() == null) {
			this.messagesUtil.addError("Não é possivel remover a unidade inicial.");
			return;
		}

		try {
			this.unidadeDAO.removeTodosNodes(this.nodeSelecionado);
			this.init();
			this.clean();
			this.messagesUtil.addSuccess("Unidade removida com sucesso.");
		} catch (Exception e) {
			this.messagesUtil.addSuccess("Unidade não pode ser removida com sucesso.");
			e.printStackTrace();
		}
	}

	public void removeHabitante() {
		try {
			this.habitanteUnidadeDAO.removeT(this.habitanteUnidade);
			this.habitanteUnidade.getUnidade().getListHabitanteUnidade().remove(this.habitanteUnidade);
			this.cancelarTudo();
			this.messagesUtil.addSuccess("Habitante removido com sucesso.");
		} catch (RollbackException e) {
			this.messagesUtil.addSuccess("Não foi possivel remover o habitante.");
			this.messagesUtil.addError(e);
		}
	}

	public void removeItemUnidade() {
		try {
			this.itemUndadeDAO.removeT(this.itemUnidade);
			this.itemUnidade.getUnidade().getListItemUnidade().remove(this.itemUnidade);
			this.cancelarTudo();
			this.messagesUtil.addSuccess("Item da unidade removido com sucesso.");
		} catch (RollbackException e) {
			this.messagesUtil.addSuccess("Não foi possivel remover o item.");
			this.messagesUtil.addError(e);
		}
	}

	public void novoHabitante() {
		this.habitanteUnidade = new HabitanteUnidade();
		this.operacao = 0;
	}

	/**
	 * limpa todos os atributos inicializado ou usados, com execeção da unidade
	 * selecionada(nodeSelecionada)
	 * 
	 * @author darlan
	 */
	public void cancelarTudo() {
		if (this.nodeOperado != null) {
			this.nodeOperado.setSelected(false);
		}
		this.nodeOperado = null;
		this.habitanteUnidade = null;
		this.itemUnidade = null;
		this.operacao = 0;
	}

	public void novaUnidade() {
		this.nodeOperado = new Unidade();
		this.operacao = 0;
	}


	public void novoItemUnidade() {
		this.itemUnidade = new ItemUnidade();
	}

	public void atualizarUnidade() {
		try {
			this.unidadeDAO.mergeT(this.nodeSelecionado);
			this.messagesUtil.addSuccess("Unidade salva com sucesso.");
		} catch (RollbackException e) {
			this.messagesUtil.addError(e);
		}
	}

	public void moverPara() {
		if (this.nodeSelecionado.getParrent() == null) {
			this.messagesUtil.addError("Não é possível mover a unidade inicial.");
			return;
		}
		this.nodeOperado = this.nodeSelecionado;
		this.operacao = 1;
		this.messagesUtil.addSuccess("Seleciona a unidade destino.");
	}

	public void moverItemPara() {
		if (this.nodeSelecionado.getParrent() == null) {
			this.messagesUtil.addError("Não é possível mover a unidade inicial.");
			return;
		}

		this.messagesUtil.addSuccess("Seleciona a unidade destino do Item.");
		this.nodeOperado = this.nodeSelecionado;
		this.operacao = 10;
	}

	public void moverHabitantePara() {
		if (this.nodeSelecionado.getParrent() == null) {
			this.messagesUtil.addError("Não é possível mover a unidade inicial.");
			return;
		}

		this.nodeOperado = this.nodeSelecionado;
		this.operacao = 20;
		this.messagesUtil.addSuccess("Seleciona a unidade destino do Habitante.");
	}

	public List<TipoUnidade> getListAllTipoUnidade(){
		if(this.listTipoUnidade == null) {
			this.listTipoUnidade = this.tipoUnidadeDAO.listAllTipoUnidades();
		}
		return this.listTipoUnidade;
	}

	public String getNome() {
		return "dasdsa";
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

	/**
	 * limpar todas os atributos utilizados
	 * 
	 * @author darlan
	 */
	public void clean() {
		if (this.nodeOperado != null) {
			this.nodeOperado.setSelected(false);
		}
		if (this.nodeSelecionado != null) {
			this.nodeSelecionado.setSelected(false);
		}
		this.habitanteUnidade = null;
		this.itemUnidade = null;
		this.nodeOperado = null;
		this.nodeSelecionado = null;
		this.operacao = 0;
	}

	public void onSelectNode(NodeSelectEvent unidade) {
		if (this.operacao > 0) {
			this.nodeSelecionado = (Unidade) unidade.getTreeNode().getData();
			this.nodeOperado.setSelected(false);
			switch (this.operacao) {
			case 1:// move uma unidade pra outra unidade
				try {
					this.nodeOperado.getParrent().getListUnidadeChildren().remove(this.nodeOperado);
					this.nodeSelecionado.addUnidadeChildren(this.nodeOperado);
					this.unidadeDAO.mergeT(nodeOperado);
//					this.root = this.unidadeDAO.getRootNodeUnidade();
				} catch (RollbackException e) {
					this.messagesUtil.addError(e);
					this.init();
				}
				break;
			case 10: // um item de uma unidade para outra unidade
				try {
					this.itemUnidade.getUnidade().getListItemUnidade().remove(this.itemUnidade);
					nodeSelecionado.addItemUnidade(this.itemUnidade);
					this.itemUndadeDAO.mergeT(this.itemUnidade);
//					this.root = this.unidadeDAO.getRootNodeUnidade();
					this.messagesUtil.addSuccess(String.format("Item: %s, movido de %s para %s.", this.itemUnidade.getDesc(), this.nodeOperado.getDesc(), this.nodeSelecionado.getDesc()));
					this.clean();
				} catch (RollbackException e) {
					this.messagesUtil.addError(e);
					this.init();
				}

				break;
			case 20: // um item de uma unidade para outra unidade
				try {
					this.habitanteUnidade.getUnidade().getListHabitanteUnidade().remove(this.habitanteUnidade);
					this.nodeSelecionado.addHabitanteUnidade(this.habitanteUnidade);
					this.habitanteUnidadeDAO.mergeT(this.habitanteUnidade);
					// @formatter:off
					this.messagesUtil.addSuccess(String.format("Item: %s, movido de %s para %s.",
									this.habitanteUnidade.getMatricula().juntaNomeMatricula(),
									this.nodeOperado.getDesc(),
									this.nodeSelecionado.getDesc()));
				} catch (RollbackException e) {
					this.messagesUtil.addError(e);
					this.init();
				}

				break;

			default:
				break;
			}
			this.clean();
		}
		
		//deseleciona o elemento selecionado anteiormente
		if(this.nodeSelecionado != null) {
			this.nodeSelecionado.setSelected(false);
		}
		this.nodeSelecionado = (Unidade) unidade.getTreeNode().getData();
		this.nodeSelecionado.setSelected(true);
	}
	
	
	public void onSelectFilter2(SelectEvent<TreeNodeSearch> event) {
		this.clean();		
		this.treeNodeSearch = (TreeNodeSearch) this.resultado.get(this.treeNodeSearch.getMMId());
		this.nodeSelecionado = treeNodeSearch.getUnidade();
		this.nodeSelecionado.setSelected(true);		
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

	public Unidade getNodeOperado() {
		return nodeOperado;
	}

	public void setNodeOperado(Unidade nodeOperado) {
		this.nodeOperado = nodeOperado;
	}

	public ItemUnidade getItemUnidade() {
		return itemUnidade;
	}

	public void setItemUnidade(ItemUnidade itemUnidade) {
		this.itemUnidade = itemUnidade;
	}

	public HabitanteUnidade getHabitanteUnidade() {
		return habitanteUnidade;
	}

	public void setHabitanteUnidade(HabitanteUnidade habitanteUnidade) {
		this.habitanteUnidade = habitanteUnidade;
	}

	public Unidade getUnidadeChildren() {
		return unidadeChildren;
	}

	public void setUnidadeChildren(Unidade unidadeChildren) {
		this.unidadeChildren = unidadeChildren;
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
}
