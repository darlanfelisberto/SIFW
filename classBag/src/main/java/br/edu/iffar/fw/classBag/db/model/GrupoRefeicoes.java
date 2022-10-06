package br.edu.iffar.fw.classBag.db.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.edu.iffar.fw.classBag.db.Model;

@Entity
@Table(name = "grupo_refeicoes")
public class GrupoRefeicoes  extends Model<UUID>{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="grupo_refeicoes_id",unique = true,nullable = false)
	private UUID grupoRefeicoesId;
	
	@NotNull(message = "Informe a descrição do grupo.")
	private String descricao;
		
	@NotNull(message = "Informe pelo menos um tipo de refeição.")
	@OneToMany(mappedBy = "grupoRefeicoes",cascade = {CascadeType.ALL},orphanRemoval = true,fetch = FetchType.LAZY)
	@OrderBy("tipoRefeicao ASC")
	@Fetch(FetchMode.SUBSELECT)
	private List<Refeicao> listRefeicao;

	@NotNull(message = "Informe uma sigla para o grupo.")
	private String sigla;
	
	@NotNull(message = "Informe o tipo do vinculo.")
//	@ Column(name="vinculo_automatico",unique = true,nullable = false)
	@Transient
	@XmlTransient
	private boolean vinculoAutomatico;
	
	@ManyToOne(optional = true,fetch = FetchType.LAZY,targetEntity = TipoVinculo.class)
	@JoinColumn(name = "tipo_vinculo_id",referencedColumnName = "tipo_vinculo_id",unique = true)
	private TipoVinculo tipoVinculo;
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(
			name="matricula_grupo",
			joinColumns = {@JoinColumn(name="grupo_refeicoes_id")},
			inverseJoinColumns = {@JoinColumn(name="matricula_id")}
	)
	private Set<Matricula> listMatricula;
	
	@Transient
	@XmlTransient private boolean novo = false;

	public GrupoRefeicoes() {
	}

	public GrupoRefeicoes(boolean novo) {
		super();
		this.novo = novo;
	}

	@Override
	public UUID getMMId() {
		return this.grupoRefeicoesId;
	}
	
	@Transient
	@XmlTransient
	public Boolean getIsVinculoAluno() {
		if(this.tipoVinculo == null) {
			return null;
		}
		return true;
	}

	public UUID getGrupoRefeicoesId() {
		return grupoRefeicoesId;
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Refeicao> getListRefeicao() {
		return listRefeicao;
	}

	public TipoVinculo getTipoVinculo() {
		return tipoVinculo;
	}

	public void setGrupoRefeicoesId(UUID grupoRefeicoesId) {
		this.grupoRefeicoesId = grupoRefeicoesId;
	}

	public void setDescricao(String desc) {
		this.descricao = desc;
	}

	public void setListRefeicao(List<Refeicao> listRefeicao) {
		this.listRefeicao = listRefeicao;
	}

	public void setTipoVinculo(TipoVinculo tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}

	@Transient
	@XmlTransient
	public boolean isVinculoAutomatico() {
		//enrolado mas é isso
		if(this.vinculoAutomatico)
			return true;
		if(this.tipoVinculo == null) {
			return false;
		}
		return true;
	}

	public Set<Matricula> getListMatricula() {
		if(this.listMatricula == null) {
			this.listMatricula = new HashSet<Matricula>();
		}
		return listMatricula;
	}

	public void setVinculoAutomatico(boolean vinculoAutomatico) {
		if(!vinculoAutomatico) {
			this.tipoVinculo = null;
		}
		this.vinculoAutomatico = vinculoAutomatico;
	}

	public void setListMatricula(HashSet<Matricula> listMatricula) {
		this.listMatricula = listMatricula;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public boolean isNovo() {
		return novo;
	}
	
	public String getLabelSOM() {
		StringBuffer s = new StringBuffer();
		s.append(this.sigla + " - " + this.descricao);
		if(this.tipoVinculo != null) {
			s.append(" - " + this.tipoVinculo.getDescricao());
		}
		return s.toString();
	}
}
