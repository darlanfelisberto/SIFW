//package br.edu.iffar.fw.classBag.db.model.api;
//
//import java.util.Set;
//import java.util.UUID;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.OrderBy;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//import javax.validation.constraints.NotNull;
//import javax.xml.bind.annotation.XmlTransient;
//
//import br.edu.iffar.fw.classBag.db.Model;
//import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
//
//
//@Entity
//@Table(name = "grupo_refeicoes")
//public class APIGrupoRefeicoes  extends Model<UUID>{
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@Column(name="grupo_refeicoes_id",unique = true,insertable = true)
//	private UUID grupoRefeicoesId;
//	
//	@NotNull(message = "Informe a descrição do grupo.")
//	private String descricao;
//		
//	@NotNull(message = "Informe pelo menos um tipo de refeição.")
//	@OneToMany(mappedBy = "grupoRefeicoes",cascade = {CascadeType.ALL},orphanRemoval = true,fetch = FetchType.LAZY)
//	@OrderBy("tipoRefeicao ASC")
//	private Set<APIRefeicao> listRefeicao;
//
//	@NotNull(message = "Informe o tipo do vinculo.")
////	@ Column(name="vinculo_automatico",unique = true,nullable = false)
//	@Transient
//	@XmlTransient
//	private boolean vinculoAutomatico;
//	
//	@ManyToOne(optional = true,fetch = FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	@JoinColumn(name = "tipo_vinculo_id",referencedColumnName = "tipo_vinculo_id",unique = true)
//	private TipoVinculo tipoVinculo;
//	
//	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//	@JoinTable(
//			name="matricula_grupo",
//			joinColumns = {@JoinColumn(name="grupo_refeicoes_id")},
//			inverseJoinColumns = {@JoinColumn(name="matricula_id")}
//	)
//	private Set<APIMatricula> listMatricula;
//	
//	@Override
//	public UUID getMMId() {
//		return this.grupoRefeicoesId;
//	}	
//	
//	public UUID getGrupoRefeicoesId() {
//		return grupoRefeicoesId;
//	}
//
//	public void setGrupoRefeicoesId(UUID grupoRefeicoesId) {
//		this.grupoRefeicoesId = grupoRefeicoesId;
//	}
//
//	public String getDescricao() {
//		return descricao;
//	}
//
//	public void setDescricao(String descricao) {
//		this.descricao = descricao;
//	}
//
//	public Set<APIRefeicao> getListRefeicao() {
//		return listRefeicao;
//	}
//
//	public void setListRefeicao(Set<APIRefeicao> listRefeicao) {
//		this.listRefeicao = listRefeicao;
//	}
//
//	public TipoVinculo getTipoVinculo() {
//		return tipoVinculo;
//	}
//
//	public void setTipoVinculo(TipoVinculo tipoVinculo) {
//		this.tipoVinculo = tipoVinculo;
//	}
//
//	public Set<APIMatricula> getListMatricula() {
//		return listMatricula;
//	}
//
//	public void setListMatricula(Set<APIMatricula> listMatricula) {
//		this.listMatricula = listMatricula;
//	}
//
//	public void setVinculoAutomatico(boolean vinculoAutomatico) {
//		this.vinculoAutomatico = vinculoAutomatico;
//	}
//
//	@Transient
//	@XmlTransient
//	public Boolean getIsVinculoAluno() {
//		if(this.tipoVinculo == null) {
//			return null;
//		}
//		return true;
//	}
//	
//	@Transient
//	@XmlTransient
//	public boolean isVinculoAutomatico() {
//		//enrolado mas é isso
//		if(this.vinculoAutomatico)
//			return true;
//		if(this.tipoVinculo == null) {
//			return false;
//		}
//		return true;
//	}
//
//	@PrePersist
//	@PreUpdate
//	public void setaGrupoInRefeicao() {
//		if(this.listRefeicao == null) {return;}
//		this.listRefeicao.forEach(r->{
//			r.setGrupoRefeicoes(this);
//		});
//	}
//	
//	public void setaGrupoInMatricula() {
//		if(this.listMatricula == null) {return;}
//		this.listMatricula.forEach(m->{
//			m.getListGrupoRefeicoes().add(this);
//		});
//	}
//}
