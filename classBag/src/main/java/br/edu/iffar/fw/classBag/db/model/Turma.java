package br.edu.iffar.fw.classBag.db.model;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.edu.iffar.fw.classBag.db.Model;

@Entity
@Table(name = "turma")
public class Turma  extends Model<UUID>{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "turma_id", unique = true, nullable = false)
	private UUID turmaId;

	@NotEmpty(message = "Informe o identificador da turma.")
	private Integer numero;

	@NotEmpty(message = "Informe o ano da turma.")
	private Integer ano;
	
	private String descricao;

	//@formatter:off
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },fetch = FetchType.LAZY)
	@JoinTable(name = "matricula_turma", 
		joinColumns = {@JoinColumn(name = "turma_id")},inverseJoinColumns = {@JoinColumn(name="matricula_id")})
	private Set<Matricula> listMatriculaTurma;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id")
	@NotNull(message = "Informe o curso.")
	private Curso curso;

	public Turma() {
	}

	public UUID getMMId() {
		return this.turmaId;
	}

	public UUID getTurmaId() {
		return turmaId;
	}

	public void setTurmaId(UUID turmaId) {
		this.turmaId = turmaId;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Set<Matricula> getListMatriculaTurma() {
		if(listMatriculaTurma != null) {
			listMatriculaTurma = listMatriculaTurma.stream().sorted((m1,m2)->m1.getUsuario().getNome().compareTo(m2.getUsuario().getNome())).collect(Collectors.toSet());
		}
		return listMatriculaTurma;
	}

	public void setListMatriculaTurma(Set<Matricula> listMatriculaTurma) {
		if(listMatriculaTurma != null) {
			listMatriculaTurma = listMatriculaTurma.stream().sorted((m1,m2)->m1.getUsuario().getNome().compareTo(m2.getUsuario().getNome())).collect(Collectors.toSet());
		}
		this.listMatriculaTurma = listMatriculaTurma;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
}
