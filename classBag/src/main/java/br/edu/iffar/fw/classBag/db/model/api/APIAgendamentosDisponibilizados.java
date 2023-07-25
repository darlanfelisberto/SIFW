package br.edu.iffar.fw.classBag.db.model.api;

import java.time.LocalDateTime;
import java.util.UUID;

import br.edu.iffar.fw.classShared.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "agendamentos_disponibilizados")
public class APIAgendamentosDisponibilizados extends Model<UUID> {
	
	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "agendamento_disponibilizado_id",insertable = true,updatable = false,nullable = false)
	private UUID agendamentoDisponibilizadoId;

	@NotNull(message = "Informe a data da disponibilização do agendamento.")
	@Column(name = "dt_disponibilizado")
	private LocalDateTime dtDisponibilizado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_disponibilizou_id", referencedColumnName = "usuario_id")
	private APIUsuario usuarioDisponibilizou;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,optional = false)
	@JoinColumn(name = "agendamento_id")
	private APIAgendamento agendamento;
	
	@Column(name = "usuario_pegou_id")
	private UUID usuarioPegouId;
	
	private LocalDateTime sincronizado;

	public APIAgendamentosDisponibilizados() {//contrutor default
	}
	
	public APIAgendamentosDisponibilizados(APIUsuario usuarioDisponibilizou, APIAgendamento agendamento) {
		super();
		this.usuarioDisponibilizou = usuarioDisponibilizou;
		this.agendamento = agendamento;
		this.dtDisponibilizado = LocalDateTime.now();	
	}
	
	@Override
	public UUID getMMId() {
		return this.agendamentoDisponibilizadoId;
	}

	public LocalDateTime getDtDisponibilizado() {
		return dtDisponibilizado;
	}

	public void setDtDisponibilizado(LocalDateTime dtDisponibilizado) {
		this.dtDisponibilizado = dtDisponibilizado;
	}

	public APIUsuario getUsuarioDisponibilizou() {
		return usuarioDisponibilizou;
	}

	public void setUsuarioDisponibilizou(APIUsuario usuarioDisponibilizou) {
		this.usuarioDisponibilizou = usuarioDisponibilizou;
	}

	public APIAgendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(APIAgendamento agendamento) {
		this.agendamento = agendamento;
	}

	public UUID getAgendamentoDisponibilizadoId() {
		return agendamentoDisponibilizadoId;
	}

	public void setAgendamentoDisponibilizadoId(UUID agendamentoDisponibilizadoId) {
		this.agendamentoDisponibilizadoId = agendamentoDisponibilizadoId;
	}

	public UUID getUsuarioPegouId() {
		return usuarioPegouId;
	}

	public void setUsuarioPedouId(UUID usuarioPedou) {
		this.usuarioPegouId = usuarioPedou;
	}
	
	public void setUsuarioPedou(APIUsuario usuarioPedou) {
		this.usuarioPegouId = usuarioPedou.getMMId();
		this.agendamento.setUsuario(usuarioPedou);
	}

	public LocalDateTime getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(LocalDateTime sincronizado) {
		this.sincronizado = sincronizado;
		/**
		 * durante o bind do jakson, é chamado esse metodo com o "sincronizado", mesmo quando ele é null
		 * e quando isso acontece, credito tbm é null, pois ainda nao foi utilizado 
		 */
		if(getAgendamento().getCredito() != null) {
			getAgendamento().getCredito().setSincronizado(sincronizado);
		}
	}
}
