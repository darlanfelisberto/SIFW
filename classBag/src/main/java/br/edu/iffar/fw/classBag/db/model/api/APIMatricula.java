//package br.edu.iffar.fw.classBag.db.model.api;
//
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//import br.edu.iffar.fw.classBag.db.Model;
//import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
//
//@Entity
//@Table(name = "matricula")
//@JsonIgnoreProperties({"listGrupoRefeicoes"})
//public class APIMatricula extends Model<UUID> implements Serializable{
////
//	private static final long serialVersionUID = 22021991L;
////
//	@Id
//	@Column(name = "matricula_id",insertable = true,updatable = false)
//	private UUID matriulaId;
////	
//	@Column(name = "matricula",unique = true)
//	@NotNull(message = "Informe o numero da matricula.")
//	private Integer idMatricula;
////	
//	@ManyToMany(mappedBy = "listMatricula")
//	private Set<APIGrupoRefeicoes> listGrupoRefeicoes = new HashSet<APIGrupoRefeicoes>();
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "usuario_id")
//	private APIUsuario usuario;
////	
//	@ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	@JoinColumn(name = "tipo_vinculo_id",referencedColumnName = "tipo_vinculo_id")
//	@NotNull(message = "Informe o tipo do vinculo da matricula.")
//	private TipoVinculo tipoVinculo;	
////	
//	@Override
//	public UUID getMMId() {
//		return this.matriulaId;
//	}
//	public UUID getMatriulaId() {
//		return matriulaId;
//	}
//	public void setMatriulaId(UUID matriulaId) {
//		this.matriulaId = matriulaId;
//	}
//	public Integer getIdMatricula() {
//		return idMatricula;
//	}
//	public void setIdMatricula(Integer idMatricula) {
//		this.idMatricula = idMatricula;
//	}
//	public APIUsuario getUsuario() {
//		return usuario;
//	}
//	public void setUsuario(APIUsuario usuario) {
//		this.usuario = usuario;
//	}
//	public TipoVinculo getTipoVinculo() {
//		return tipoVinculo;
//	}
//	public void setTipoVinculo(TipoVinculo tipoVinculo) {
//		this.tipoVinculo = tipoVinculo;
//	}
//	public Set<APIGrupoRefeicoes> getListGrupoRefeicoes() {
//		return listGrupoRefeicoes;
//	}
//	public void setListGrupoRefeicoes(Set<APIGrupoRefeicoes> listGrupoRefeicoes) {
//		this.listGrupoRefeicoes = listGrupoRefeicoes;
//	}
//	
//}
