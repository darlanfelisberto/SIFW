package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="creditos")
public class Credito extends Model implements Serializable {
	
	private static final long serialVersionUID = 22021991L;
	
	@Id
	@Column(name="credito_id",insertable = true,updatable = false,unique = true)
	private UUID creditoId = UUID.randomUUID();

	@Column(name="dt_credito")
	@NotNull
	private LocalDateTime dtCredito;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_credito_id")
	@NotNull
	private TipoCredito tipoCredito;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, mappedBy = "credito")
	private Agendamento agendamento;
	
	@Max(value = 200)
	@NotNull
	private Float valor;

	@NotNull
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	@OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "parent_id",referencedColumnName = "credito_id")
	private Credito parent;
	
	private LocalDateTime sincronizado;

	public Credito() {
	}
	
	public Credito(TipoCredito trans) {
		this.tipoCredito = trans;
		this.dtCredito = LocalDateTime.now();
	}
	
	public Credito(@NotNull LocalDateTime dtCredito, Agendamento agendamento,
				   @Max(99) Float valor, @NotNull Usuario usuario, TipoCredito tipoCredito, LocalDateTime sincronizado) {
		super();
		this.dtCredito = dtCredito;
		this.valor = valor;
		this.usuario = usuario;
		this.agendamento = agendamento;
		this.agendamento.setCredito(this);
		this.tipoCredito = tipoCredito;
		this.sincronizado = sincronizado;
	}

	public UUID getCreditoId() {
		return creditoId;
	}

	public void setCreditoId(UUID creditoId) {
		this.creditoId = creditoId;
	}

	public LocalDateTime getDtCredito() {
		return dtCredito;
	}

	public void setDtCredito(LocalDateTime dtCredito) {
		this.dtCredito = dtCredito;
	}


	public TipoCredito getTipoCredito() {
		return tipoCredito;
	}

	public void setTipoCredito(TipoCredito tipoCredito) {
		this.tipoCredito = tipoCredito;
	}
	
	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public UUID getMMId() {
		return this.creditoId;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public LocalDateTime getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(LocalDateTime sincronizado) {
		this.sincronizado = sincronizado;
	}

	public String getDataString() {
		return this.dtCredito.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));
	}

	public Credito getParent() {
		return parent;
	}

	public void setParent(Credito parent) {
		this.parent = parent;
	}
	
	
}