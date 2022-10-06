package br.edu.iffar.fw.classBag.db.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.enun.TypeCredito;

@Entity
@Table(name = "tipo_credito")
@Cacheable
public class TipoCredito extends Model<TypeCredito>{

	private static final long serialVersionUID = 22021991L;

	@Id
	@Enumerated(EnumType.STRING)
	@Column(name ="tipo_credito_id")
	private TypeCredito tipoCreditoId;	
	
	@Column(name ="style_class")
	private String styleClass;
	
	@Column(name ="cor")
	private String cor;
	
	private String descricao;
	
	private int fator;
	
	public TipoCredito() {
	}
	
	public TipoCredito(TypeCredito tipoCreditoId) {
		this.tipoCreditoId = tipoCreditoId;
	}

	@Override
	public TypeCredito getMMId() {
		return this.tipoCreditoId;
	}

	
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public int getFator() {
		return fator;
	}

	public void setFator(int fator) {
		this.fator = fator;
	}

	public TypeCredito getTipoCreditoId() {
		return tipoCreditoId;
	}

	public void setTipoCreditoId(TypeCredito tipoCreditoId) {
		this.tipoCreditoId = tipoCreditoId;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
	
	@XmlTransient
	public boolean isTransSaida() {
		return this.tipoCreditoId.equals(TypeCredito.TRANS_SAIDA);
	}
	@XmlTransient
	public boolean isTransEntrada() {
		return this.tipoCreditoId.equals(TypeCredito.TRANS_ENTRADA);
	}
}
