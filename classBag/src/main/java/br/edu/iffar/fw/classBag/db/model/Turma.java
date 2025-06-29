package br.edu.iffar.fw.classBag.db.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "turma")
public class Turma  extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "turma_id", unique = true, nullable = false)
	private UUID turmaId;

	@NotNull(message = "Informe o identificador da turma.")
	private Integer numero;

	@NotNull(message = "Informe o ano da turma.")
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
			listMatriculaTurma = listMatriculaTurma.stream().sorted((m1,m2)->m1.getUsuario().getPessoa().getNome().compareTo(m2.getUsuario().getPessoa().getNome())).collect(Collectors.toSet());
		}
		return listMatriculaTurma;
	}

	public void setListMatriculaTurma(Set<Matricula> listMatriculaTurma) {
		if(listMatriculaTurma != null) {
			listMatriculaTurma = listMatriculaTurma.stream().sorted((m1,m2)->m1.getUsuario().getPessoa().getNome().compareTo(m2.getUsuario().getPessoa().getNome())).collect(Collectors.toSet());
		}
		this.listMatriculaTurma = listMatriculaTurma;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	public void addMatricula(Matricula matricula) {
		if(this.listMatriculaTurma == null) {
			this.listMatriculaTurma = new HashSet<>();
		}
		this.listMatriculaTurma.add(matricula);
	}

	public void removeMatricula(Matricula matricula) {
		if(this.listMatriculaTurma != null) {
			this.listMatriculaTurma.remove(matricula);
		}
	}
	
	public Turma clone() {
		Turma novaTurma = new Turma();
		
		novaTurma.ano = this.ano + 1;
		novaTurma.setCurso(this.curso);
		novaTurma.setListMatriculaTurma(this.listMatriculaTurma);
		novaTurma.setDescricao(this.descricao);
		novaTurma.setNumero(this.numero + 10);
		
		return novaTurma;
	}
}
