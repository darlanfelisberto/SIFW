package br.edu.iffar.fw.classBag.db.model;

import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;

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
