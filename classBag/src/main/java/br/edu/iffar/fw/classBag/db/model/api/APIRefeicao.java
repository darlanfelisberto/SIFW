//package br.edu.iffar.fw.classBag.db.model.api;
//
//import java.util.UUID;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//import br.edu.iffar.fw.classBag.db.Model;
//import br.edu.iffar.fw.classBag.db.model.Refeicao;
//import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
//
//@Entity
//@Table(name = "refeicao")
//@JsonIgnoreProperties({"grupoRefeicoes"})
//public class APIRefeicao extends Model<UUID>{
//
//	private static final long serialVersionUID = 22021991L;
//
//	@Id
//	@Column(name ="refeicao_id",insertable = true)
//	private UUID refeicaoId;	
//		
//	@NotNull(message = "Informe o valor da refeição.")
//	private Float valor;
//		
//	@ManyToOne(fetch = FetchType.LAZY,optional = false)
//	@JoinColumn(name="tipo_refeicao_id")
//	private TipoRefeicao tipoRefeicao;
//
//	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
//	@JoinColumn(name="grupo_refeicoes_id")
//	private APIGrupoRefeicoes grupoRefeicoes;
//	
//	@Override
//	public UUID getMMId() {
//		return this.refeicaoId;
//	}
//	
//	public Refeicao convertToRefeicao() {
//		return new Refeicao(this.refeicaoId);
//	}
//
//	public UUID getRefeicaoId() {
//		return refeicaoId;
//	}
//
//	public void setRefeicaoId(UUID refeicaoId) {
//		this.refeicaoId = refeicaoId;
//	}
//
//	public Float getValor() {
//		return valor;
//	}
//
//	public void setValor(Float valor) {
//		this.valor = valor;
//	}
//
//	public TipoRefeicao getTipoRefeicao() {
//		return tipoRefeicao;
//	}
//
//	public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
//		this.tipoRefeicao = tipoRefeicao;
//	}
//
//	public APIGrupoRefeicoes getGrupoRefeicoes() {
//		return grupoRefeicoes;
//	}
//
//	public void setGrupoRefeicoes(APIGrupoRefeicoes grupoRefeicoes) {
//		this.grupoRefeicoes = grupoRefeicoes;
//	}
//
//	@Override
//	public String toString() {
//		if(this.grupoRefeicoes.getTipoVinculo() == null) {
//			return this.tipoRefeicao.getDescricao() +", R$ "+this.valor +", "+ this.grupoRefeicoes.getDescricao();
//		}else {
//			return this.tipoRefeicao.getDescricao() +", R$ "+this.valor +", "+ this.grupoRefeicoes.getTipoVinculo().getDesc();
//		}
//	}
//	
//	
//	
//}