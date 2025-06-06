package br.edu.iffar.fw.classBag.db.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "presenca", schema = "moradia_estudantil")
public class Presenca extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "presenca_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID presencaId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "habitante_unidade_id")
	@NotNull(message = "Informe o habitante.")
	private HabitanteUnidade habitanteUnidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "realizada_por_id")
	@NotNull(message = "Informe quem realizou a chamada.")
	private Usuario realizadoPor;

	@Column(name = "dt_referencia")
	@NotNull(message = "Informe a data de entrada do habitante.")
	private LocalDate dtReferencia;

	@Column(name = "dt_realizada")
	private LocalDateTime dtRealizada;
	
	@Size(max = 255,message = "A justificativa deve ter no maximo 255 caracteres.")
	private String justificativa;
	
	@NotNull(message = "Informe a presença.")
	private Boolean presente;
	
	public Presenca() {}
	
	public Presenca(HabitanteUnidade habitanteUnidade, Usuario realizadoPor, LocalDate dtReferencia) {
		this.dtRealizada =  LocalDateTime.now();
		this.realizadoPor = realizadoPor;
		this.dtReferencia = dtReferencia;
		this.habitanteUnidade = habitanteUnidade;
		this.presente = true;
	}

	public UUID getMMId() {
		return this.presencaId;
	}

	public UUID getPresencaId() {
		return presencaId;
	}

	public void setPresencaId(UUID presencaId) {
		this.presencaId = presencaId;
	}

	public HabitanteUnidade getHabitanteUnidade() {
		return habitanteUnidade;
	}

	public void setHabitanteUnidade(HabitanteUnidade habitanteUnidade) {
		this.habitanteUnidade = habitanteUnidade;
	}

	public Usuario getRealizadoPor() {
		return realizadoPor;
	}

	public void setRealizadoPor(Usuario realizadoPorId) {
		this.realizadoPor = realizadoPorId;
	}

	public LocalDate getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(LocalDate dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	public LocalDateTime getDtRealizada() {
		System.out.println(dtRealizada);
		return dtRealizada;
	}

	public void setDtRealizada(LocalDateTime dtRealizada) {
		this.dtRealizada = dtRealizada;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Boolean getPresente() {
		return presente;
	}

	public void setPresente(Boolean presente) {
		this.presente = presente;
	}

}
