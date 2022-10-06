package br.edu.iffar.fw.classBag.db.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import br.edu.iffar.fw.classBag.db.Model;

@Entity
@Table(name = "alteracoes_agendamentos")
public class AlteracoesAgendamentos extends Model<UUID> {

	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "alteracoes_agendamentos_id", insertable = true, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID alteracoesAgendamentoId;

	@ManyToOne
	@JoinColumn(name = "usuario_origem_id", referencedColumnName = "usuario_id")
	@NotNull(message = "Informe o usuário de origem.")
	private Usuario usuarioOrigem;

	@ManyToOne
	@JoinColumn(name = "usuario_destino_id", referencedColumnName = "usuario_id")
	@NotNull(message = "Informe o usuário de destino.")
	private Usuario usuarioDestino;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agendamento_id")
	@NotNull(message = "Informe o agendamento.")
	private Agendamento agendamento;

	private Boolean aceito;

	@Column(name = "dt_transferencia")
	@NotNull(message = "Informe a data da transferência.")
	private LocalDateTime dtTranferencia;

	@Column(name = "dt_aceite")
	private LocalDateTime dtAceite;

	public AlteracoesAgendamentos() {
	}

	public AlteracoesAgendamentos(Usuario userOrigem) {
		this.usuarioOrigem = userOrigem;
	}

	@Override
	public UUID getMMId() {
		return this.alteracoesAgendamentoId;
	}

	public UUID getAlteracoesAgendamentoId() {
		return alteracoesAgendamentoId;
	}

	public void setAlteracoesAgendamentoId(UUID alteracoesAgendamentoId) {
		this.alteracoesAgendamentoId = alteracoesAgendamentoId;
	}

	public Usuario getUsuarioOrigem() {
		return usuarioOrigem;
	}

	public void setUsuarioOrigem(Usuario usuarioOrigem) {
		this.usuarioOrigem = usuarioOrigem;
	}

	public Usuario getUsuarioDestino() {
		return usuarioDestino;
	}

	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public Boolean getAceito() {
		return aceito;
	}

	public void setAceito(Boolean aceito) {
		this.dtAceite = LocalDateTime.now();
		this.aceito = aceito;
	}

	public LocalDateTime getDtTranferencia() {
		return dtTranferencia;
	}

	public void setDtTranferencia(LocalDateTime dtTranferencia) {
		this.dtTranferencia = dtTranferencia;
	}

	public LocalDateTime getDtAceite() {
		return dtAceite;
	}

	public void setDtAceite(LocalDateTime dtAceite) {
		this.dtAceite = dtAceite;
	}

	@XmlTransient
	public String label() {
		if(this.aceito == null) {
			return "Aguard.";
		}
		if(this.aceito) {
			return "Aceito";
		} else {
			return "Não aceito";
		}
	}
}
