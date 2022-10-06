package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.edu.iffar.fw.classBag.db.Model;


/**
 * The persistent class for the cidades database table.
 * 
 */
@Entity
@Table(name="cidades")
@NamedQuery(name="Cidade.findAll", query="SELECT c FROM Cidade c")
public class Cidade extends Model<Integer>  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_cidade")
	private Integer idCidade;

	private String descricao;

	//bi-directional many-to-one association to Estado
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_estado")
	private Estado estado;

	//bi-directional many-to-one association to Curso
	@OneToMany(mappedBy="cidade")
	private List<Curso> cursos;

	public Cidade() {
	}

	public Integer getIdCidade() {
		return this.idCidade;
	}

	public void setIdCidade(Integer idCidade) {
		this.idCidade = idCidade;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Curso> getCursos() {
		return this.cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public Curso addCurso(Curso curso) {
		getCursos().add(curso);
		curso.setCidade(this);

		return curso;
	}

	public Curso removeCurso(Curso curso) {
		getCursos().remove(curso);
		curso.setCidade(null);

		return curso;
	}

	@Override
	public Integer getMMId() {
		return this.idCidade;
	}

}