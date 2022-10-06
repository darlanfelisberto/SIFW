package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.edu.iffar.fw.classBag.enun.TypeSituacao;


/**
 * The persistent class for the situacao_matricula database table.
 * 
 */
@Entity
@Table(name="situacao_matricula")
@NamedQuery(name="SituacaoMatricula.findAll", query="SELECT s FROM SituacaoMatricula s")
public class SituacaoMatricula extends br.edu.iffar.fw.classBag.db.Model<UUID> implements Serializable {
	private static final long serialVersionUID = 22021991L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="situacao_id", unique=true, nullable=false)
	private UUID situacaoId;

	@Column(name = "momento")
	private LocalDateTime momento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "matricula_id")
	private Matricula matricula;

	@Column(nullable=false, length=7)
	@Enumerated(EnumType.STRING)
	private TypeSituacao situacao;

	public SituacaoMatricula() {
	}
	
	public SituacaoMatricula(TypeSituacao situacao, LocalDateTime momento,Matricula matricula) {
		this.situacao = situacao;
		this.momento = momento;
		this.matricula = matricula;
	}


	public UUID getSituacaoId() {
		return this.situacaoId;
	}

	public void setSituacaoId(UUID situacaoId) {
		this.situacaoId = situacaoId;
	}


	public LocalDateTime getMomento() {
		return momento;
	}

	public void setMomento(LocalDateTime momento) {
		this.momento = momento;
	}

	public TypeSituacao getSituacao() {
		return this.situacao;
	}

	public void setSituacao(TypeSituacao situacao) {
		this.situacao = situacao;
	}
	
	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	@Override
	public UUID getMMId() {
		return this.situacaoId;
	}

}