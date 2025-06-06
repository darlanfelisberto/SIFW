package br.edu.iffar.fw.classBag.db.model;

import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import br.edu.iffar.fw.classBag.interfaces.TreeNodeSearch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "item_unidade", schema = "moradia_estudantil")
public class ItemUnidade extends Model implements TreeNodeSearch {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "item_unidade_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID itemUnidadeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unidade_id")
	private Unidade unidade;

	@Column(name = "descricao")
	@NotNull(message = "Informe uma descrição para o item.")
	private String desc;

	private Integer patrimonio;


	public ItemUnidade() {
	}

	public UUID getMMId() {
		return itemUnidadeId;
	}

	public UUID getItemUnidadeId() {
		return itemUnidadeId;
	}

	public void setItemUnidadeId(UUID itemUnidadeId) {
		this.itemUnidadeId = itemUnidadeId;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getPatrimonio() {
		return patrimonio;
	}

	public void setPatrimonio(Integer patrimonio) {
		this.patrimonio = patrimonio;
	}

	public boolean isNovo() {
		return this.itemUnidadeId == null;
	}

	@Override
	public String label() {
		if (this.patrimonio == null) {
			return this.desc;
		} else {
			return this.desc + " - " + this.patrimonio;
		}
	}

	@Override
	public boolean search(String termo) {
		if (this.desc.toLowerCase().contains(termo)) {
			return true;
		}
		if (this.patrimonio != null && this.patrimonio.toString().toLowerCase().contains(termo)) {
			return true;
		}
		return false;
	}

}
