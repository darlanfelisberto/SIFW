package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.interfaces.VinculosAtivosUsuarios;
import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "matricula")
public class Matricula extends Model<UUID> implements Serializable,VinculosAtivosUsuarios{

	private static final long serialVersionUID = 22021991L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "matricula_id")
	private UUID matriculaId;
	
	@Column(name = "matricula",unique = true)
	@NotNull(message = "Informe o numero da matricula.")
	private Integer idMatricula;
	
	@ManyToMany(mappedBy = "listMatricula")
	private Set<GrupoRefeicoes> listGrupoRefeicoes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_vinculo_id",referencedColumnName = "tipo_vinculo_id",unique = true)
	@NotNull(message = "Informe o tipo do vinculo.")
	private TipoVinculo tipoVinculo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id")
	@NotNull(message = "Informe o curso.")
	private Curso curso;
	
	@OneToMany(mappedBy="matricula",fetch = FetchType.LAZY,orphanRemoval = true,cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	@OrderBy(value = "momento desc")
	private List<SituacaoMatricula> listSituacaoMatricula;
	
	@OneToMany(mappedBy = "matricula", fetch = FetchType.LAZY)
	private List<Agendamento> listAgendamento;

	public Matricula(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Matricula() {
	}

	@Override
	public UUID getMMId() {
		return this.matriculaId;
	}
	
	@XmlTransient
	public SituacaoMatricula getUltimaSituacao() {
		this.listSituacaoMatricula.sort((s1, s2) -> s2.getMomento().compareTo(s1.getMomento()));

		return this.listSituacaoMatricula.get(0);
	}

	public Integer getIdMatricula() {
		return idMatricula;
	}

	public Set<GrupoRefeicoes> getListGrupoRefeicoes() {
		if(this.listGrupoRefeicoes == null) {
			this.listGrupoRefeicoes = new HashSet<GrupoRefeicoes>();
		}
		return listGrupoRefeicoes;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setIdMatricula(Integer matricula) {
		this.idMatricula = matricula;
	}

	public void setListGrupoRefeicoes(Set<GrupoRefeicoes> listGrupoRefeicoes) {
		this.listGrupoRefeicoes = listGrupoRefeicoes;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoVinculo getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(TipoVinculo tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}
	
	public String juntaNomeMatricula() {
		return this.usuario.getPessoa().getNome() + " - " + this.idMatricula;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<SituacaoMatricula> getListSituacaoMatricula() {
		return listSituacaoMatricula;
	}
	
	public void addSituacaoMatricula(SituacaoMatricula sm) {
		if(this.listSituacaoMatricula == null) {
			this.listSituacaoMatricula =  new ArrayList<SituacaoMatricula>();
		}
		this.listSituacaoMatricula.add(0, sm);
	}
	
	public String getLabel() {
		return "Aluno, " + this.tipoVinculo.getDescricao() + " - " + this.getIdMatricula();
	}

}
