package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.edu.iffar.fw.classBag.db.Model;


/**
 * The persistent class for the cursos database table.
 * 
 */
@Entity
@Table(name="cursos")
@NamedQuery(name="Curso.findAll", query="SELECT c FROM Curso c")
public class Curso extends Model<UUID> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="curso_id")
	private UUID cursoId;

	private Integer ativo;

	private String codigo;

	@Column(name="id_area_curso")
	private Integer idAreaCurso;

	@Column(name="id_curso")
	private Integer idCurso;

	@Column(name="id_eixo_conhecimento")
	private Integer idEixoConhecimento;

	@Column(name="id_grau_academico")
	private Integer idGrauAcademico;

	@Column(name="id_modalidade_educacao")
	private Integer idModalidadeEducacao;

	@Column(name="id_tipo_oferta_curso")
	private Integer idTipoOfertaCurso;

	@Column(name="id_unidade")
	private Integer idUnidade;

	private String nivel;

	private String nome;

	//bi-directional many-to-one association to Cidade
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_cidade")
	private Cidade cidade;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_vinculo_id",referencedColumnName = "tipo_vinculo_id")
	private TipoVinculo tipoVinculo;	

	public Curso() {
	}

	public UUID getCursoId() {
		return this.cursoId;
	}

	public void setCursoId(UUID cursoId) {
		this.cursoId = cursoId;
	}

	public Integer getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getIdAreaCurso() {
		return this.idAreaCurso;
	}

	public void setIdAreaCurso(Integer idAreaCurso) {
		this.idAreaCurso = idAreaCurso;
	}

	public Integer getIdCurso() {
		return this.idCurso;
	}

	public void setIdCurso(Integer idCurso) {
		this.idCurso = idCurso;
	}

	public Integer getIdEixoConhecimento() {
		return this.idEixoConhecimento;
	}

	public void setIdEixoConhecimento(Integer idEixoConhecimento) {
		this.idEixoConhecimento = idEixoConhecimento;
	}

	public Integer getIdGrauAcademico() {
		return this.idGrauAcademico;
	}

	public void setIdGrauAcademico(Integer idGrauAcademico) {
		this.idGrauAcademico = idGrauAcademico;
	}

	public Integer getIdModalidadeEducacao() {
		return this.idModalidadeEducacao;
	}

	public void setIdModalidadeEducacao(Integer idModalidadeEducacao) {
		this.idModalidadeEducacao = idModalidadeEducacao;
	}

	public Integer getIdTipoOfertaCurso() {
		return this.idTipoOfertaCurso;
	}

	public void setIdTipoOfertaCurso(Integer idTipoOfertaCurso) {
		this.idTipoOfertaCurso = idTipoOfertaCurso;
	}

	public Integer getIdUnidade() {
		return this.idUnidade;
	}

	public void setIdUnidade(Integer idUnidade) {
		this.idUnidade = idUnidade;
	}

	public String getNivel() {
		return this.nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cidade getCidade() {
		return this.cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	
	public TipoVinculo getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(TipoVinculo tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}

	@Override
	public UUID getMMId() {
		return this.cursoId;
	}

}