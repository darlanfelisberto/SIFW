package br.edu.iffar.fw.classBag.db.model.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classShared.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="agendamentos")
public class APIAgendamento extends Model<UUID> {
	
	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "agendamento_id",insertable = true,unique = true,nullable = false)
	private UUID agendamentoId;

//	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
//	@JsonSerialize(using = JsonLocalDateTimeSerializer.class)
//	@NotNull(message = "Imforme a data de inicio do agendamento.")
//	@Column(name = "dt_inicio")
//	private LocalDateTime dtInicio;

//	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
//	@JsonSerialize(using = JsonLocalDateTimeSerializer.class)
	@NotNull(message = "Imforme a data de agendamento.")
	@Column(name = "dt_agendamento", nullable = false)
	private LocalDate dtAgendamento;

	@NotNull(message = "Informe o usuario do agendamento.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id",referencedColumnName = "usuario_id")
	private APIUsuario usuario;

	@NotNull(message = "Informe o tipo da refeição.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "refeicao_id",referencedColumnName = "refeicao_id",nullable = false)
	private APIRefeicao2 refeicao;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = true)
	@JoinColumn(name = "credito_id")
	private APICredito credito;

	
	public APIAgendamento() {}
	
	public APIAgendamento(@NotNull(message = "Imforme a data do agendamento.") LocalDate dtAgendamento,
			@NotNull(message = "Informe o usuario do agendamento.") APIUsuario usuario,
			@NotNull(message = "Informe o tipo da refeição.") APIRefeicao2 refeicao) {
		super();
		this.dtAgendamento = dtAgendamento;
		this.usuario = usuario;
		this.refeicao = refeicao;
		this.agendamentoId = UUID.randomUUID();
	}

	@Override
	public UUID getMMId() {
		return this.agendamentoId;
	}

	public UUID getAgendamentoId() {
		return agendamentoId;
	}

	public void setAgendamentoId(UUID agendamentoId) {
		this.agendamentoId = agendamentoId;
	}

	public APIUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(APIUsuario usuario) {
		this.usuario = usuario;
	}

	public APIRefeicao2 getRefeicao() {
		return refeicao;
	}

	public void setRefeicao(APIRefeicao2 refeicao) {
		this.refeicao = refeicao;
	}

	public String periodScheduling() {
		return this.dtAgendamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
		
	public APICredito getCredito() {
		return credito;
	}

	public void setCredito(APICredito credito) {
		this.credito = credito;
	}

	public LocalDate getDtAgendamento() {
		return dtAgendamento;
	}

	public void setDtAgendamento(LocalDate dtAgendamento) {
		this.dtAgendamento = dtAgendamento;
	}

	public Agendamento converteAgendamento() {
		Agendamento a = new Agendamento();
		//TODO mudança de usuario para matricula/servidor
//		a.setUsuario(this.usuario.converteForUsuario());
		a.setDtAgendamento(dtAgendamento);
		a.setAgendamentoId(this.agendamentoId);
		a.setRefeicao(this.refeicao.convertToRefeicao());
//		a.setCredito(this.credito.converteForCredito());
		return a;
	}
	
	public void geraCreditoSaida() {
		this.setCredito( new APICredito(LocalDateTime.now(),
                this,
                getRefeicao().getValor(),
                getUsuario(),
                new TipoCredito(TypeCredito.SAIDA)
        ));
	}
	
}