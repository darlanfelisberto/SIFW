package br.edu.iffar.fw.classBag.db.model;

import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_unidade", schema = "moradia_estudantil")
@Cacheable
public class TipoUnidade extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tipo_unidade_id")
	private UUID tipoUnidadeId;

	@Column(name = "descricao")
	private String desc;

	private String tipo;

	public UUID getMMId() {
		return tipoUnidadeId;
	}

	public UUID getTipoUnidadeId() {
		return tipoUnidadeId;
	}

	public void setTipoUnidadeId(UUID tipoUnidadeId) {
		this.tipoUnidadeId = tipoUnidadeId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
