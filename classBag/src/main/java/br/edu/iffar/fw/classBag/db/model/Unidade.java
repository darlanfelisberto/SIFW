package br.edu.iffar.fw.classBag.db.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.primefaces.model.TreeNode;

import br.edu.iffar.fw.classBag.db.Model;

@Entity
@Table(name = "unidade", schema = "moradia_estudantil")
public class Unidade extends Model<UUID> implements TreeNode<Unidade> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "unidade_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID unidadeId;

	@Column(length = 100, name = "descricao")
	@NotEmpty(message = "Imforme a descrição da Unidade.")
	private String desc;

	@Column(insertable = false, updatable = false)
	private Integer seq;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", referencedColumnName = "unidade_id")
	@OrderBy
	private List<Unidade> listUnidadeChildren = new ArrayList<Unidade>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", referencedColumnName = "unidade_id")
	private Unidade parrent;

	@ManyToOne
	@JoinColumn(name = "tipo_unidade_id")
	@NotNull(message = "Informe o tipo da unidade.")
	private TipoUnidade tipoUnidade;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidade")
	private List<ItemUnidade> listItemUnidade;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidade")
	private List<HabitanteUnidade> listHabitanteUnidade;

	public UUID getMMId() {
		return unidadeId;
	}

	public UUID getUnidadeId() {
		return unidadeId;
	}

	public void setUnidadeId(UUID unidadeId) {
		this.unidadeId = unidadeId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public List<Unidade> getListUnidadeChildren() {
		return listUnidadeChildren;
	}

	public void setListUnidadeChildren(List<Unidade> listUnidadeChildren) {
		this.listUnidadeChildren = listUnidadeChildren;
	}

	public Unidade getParrent() {
		return parrent;
	}

	public void setParrent(Unidade parrent) {
		this.parrent = parrent;
	}

	public TipoUnidade getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(TipoUnidade tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}

	public List<ItemUnidade> getListItemUnidade() {
		return listItemUnidade;
	}


	public void setListItemUnidade(List<ItemUnidade> listItemUnidade) {
		this.listItemUnidade = listItemUnidade;
	}


	public List<HabitanteUnidade> getListHabitanteUnidade() {
		listHabitanteUnidade.sort((h1, h2) -> h1.getMatricula().getUsuario().getNome().compareTo(h2.getMatricula().getUsuario().getNome()));
		return listHabitanteUnidade;
	}


	public void setListHabitanteUnidade(List<HabitanteUnidade> listHabitanteUnidade) {
		this.listHabitanteUnidade = listHabitanteUnidade;
	}

	public boolean isPossuiHabitantes() {
		if (this.listHabitanteUnidade == null) {
			return false;
		}
		return !this.listHabitanteUnidade.isEmpty();
	}

	public void addUnidadeChildren(Unidade u) {
		u.setParrent(this);
		if (!this.listUnidadeChildren.contains(u)) {
			this.listUnidadeChildren.add(u);
		}
	}

	public void addItemUnidade(ItemUnidade i) {
		i.setUnidade(this);
		if (this.listItemUnidade == null) {
			this.listItemUnidade = new ArrayList<ItemUnidade>();
		}
		if (!this.listItemUnidade.contains(i)) {
			this.listItemUnidade.add(i);
		}
	}

	public void addHabitanteUnidade(HabitanteUnidade h) {
		h.setUnidade(this);
		if (this.listHabitanteUnidade == null) {
			this.listHabitanteUnidade = new ArrayList<HabitanteUnidade>();
		}
		if (!this.listHabitanteUnidade.contains(h)) {
			this.listHabitanteUnidade.add(h);
		}
	}

	public boolean isNovo() {
		return this.unidadeId == null;
	}

	// ----------------------------

	/**
	 * atributos usado pelo primefaces, apenas
	 */
	@Transient
	private boolean expanded = false;
	@Transient
	private boolean selected;
	@Transient
	private boolean selectable = true;
	@Transient
	private String rowKey;

	public static final String DEFAULT_TYPE = "default";

	@Override
	public String getType() {
//		new DefaultTreeNode<T>()
//		return DEFAULT_TYPE;

		return this.tipoUnidade.getTipo();
	}

	@Override
	public void setType(String type) {
		System.out.println("setType");
	}

	@Override
	public Unidade getData() {
		return this;
	}

	@Override
	public List<TreeNode<Unidade>> getChildren() {
		listUnidadeChildren.sort((o1, o2) -> o1.getDesc().compareTo(
				o2.getDesc()));

		return (List<TreeNode<Unidade>>) (List<?>) this.listUnidadeChildren;
	}

	@Override
	public TreeNode<Unidade> getParent() {
		return this.parrent;
	}

	@Override
	public void setParent(TreeNode<Unidade> treeNode) {
		this.parrent = (Unidade) treeNode;
	}

	@Override
	public boolean isExpanded() {
		if(this.getTipoUnidade().getTipo().equals("root")) {
			return true;
		}
		return this.expanded;
	}

	@Override
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	@Override
	public int getChildCount() {
		return this.listUnidadeChildren.size();
	}

	@Override
	public boolean isLeaf() {
		if (this.listUnidadeChildren == null) {
			return true;
		}

		return this.listUnidadeChildren.isEmpty();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean value) {
		this.selected = value;
	}

	@Override
	public boolean isSelectable() {
		return this.selectable;
	}

	@Override
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@Override
	public boolean isPartialSelected() {
		return false;
	}

	@Override
	public void setPartialSelected(boolean value) {

	}

	@Override
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	@Override
	public String getRowKey() {
		return this.rowKey;
	}

	@Override
	public void clearParent() {
		this.parrent = null;

	}

}
