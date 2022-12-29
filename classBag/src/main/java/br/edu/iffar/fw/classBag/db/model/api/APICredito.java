package br.edu.iffar.fw.classBag.db.model.api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="creditos")
//@JsonIgnoreProperties({"tipoCredito"})
public class APICredito extends Model<UUID> implements Serializable {
	
	private static final long serialVersionUID = 22021991L;
	
	public static final int E = 0;
	public static final int S = 1;
	public static final int T = 2;
	public static final int EI = 3;

	//mapeamento alterado para gerar o id manualmente e manter o id com a catraca.
	//o metodo persisty nao ira mais funcionar com essa classe!
	@Id
	@Column(name="credito_id")
	private UUID creditoId = UUID.randomUUID();

	@Column(name="dt_credito")
	@NotNull
	private LocalDateTime dtCredito;

	@Max(value = 99)
	@NotNull
	private Float valor;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="usuario_id")
	private APIUsuario usuario;
	
	@XmlTransient
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = true,mappedBy = "credito")
	private APIAgendamento agendamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_credito_id")
	@NotNull
	private TipoCredito tipoCredito;
	
	private LocalDateTime sincronizado;
	
	public APICredito() {
	}
	
	public APICredito(@NotNull LocalDateTime dtCredito, APIAgendamento agendamento,
			@Max(99) Float valor, @NotNull APIUsuario usuario,TipoCredito tipoCredito) {
		super();
		this.dtCredito = dtCredito;
		this.valor = valor;
		this.usuario = usuario;
		this.agendamento = agendamento;
		this.tipoCredito = tipoCredito;
	}

	public UUID getCreditoId() {
		return creditoId;
	}

	public LocalDateTime getDtCredito() {
		return dtCredito;
	}

	public void setDtCredito(LocalDateTime dtCredito) {
		this.dtCredito = dtCredito;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public APIUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(APIUsuario usuario) {
		this.usuario = usuario;
	}

	public TipoCredito getTipoCredito() {
		return tipoCredito;
	}

	public void setTipoCredito(TipoCredito tipoCredito) {
		this.tipoCredito = tipoCredito;
	}

	public void setCreditoId(UUID creditoId) {
		this.creditoId = creditoId;
	}

	public Credito converteForCredito(Usuario u, Agendamento a) {
		Credito c = new Credito();
		c.setSincronizado(this.sincronizado);
		c.setUsuario(u);
		c.setValor(this.valor);
		c.setDtCredito(this.dtCredito);
		c.setCreditoId(this.creditoId);
		c.setTipoCredito(this.tipoCredito);
		c.setAgendamento(a);
		return c;
	}
	
	@Override
	public UUID getMMId() {
		return this.creditoId;
	}

	public LocalDateTime getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(LocalDateTime sincronizado) {
		this.sincronizado = sincronizado;
	}

	public APIAgendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(APIAgendamento agendamento) {
		this.agendamento = agendamento;
	}

	@XmlTransient
	@Transient
	public String getDataString() {
		return this.dtCredito.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));
	}
}