package br.edu.iffar.fw.classBag.db.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import org.primefaces.model.ScheduleDisplayMode;
//import org.primefaces.model.ScheduleDisplayMode;
import org.primefaces.model.ScheduleEvent;

import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.interfaces.VinculosAtivosUsuarios;
import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "agendamentos")
public class Agendamento extends Model<UUID> implements ScheduleEvent<Agendamento> {
	
	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "agendamento_id", insertable = true, updatable = false, unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID agendamentoId;

	@NotNull(message = "Imforme a data do agendamento.")
	@Column(name = "dt_agendamento")
	private LocalDate dtAgendamento;

	@Column(name = "editavel")
	private boolean editavel = true;
	
	@Column(name = "agendado")
	private boolean agendado = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "matricula_id")
	private Matricula matricula;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "servidor_id")
	private Servidor servidor;

	@NotNull(message = "Informe o tipo da refeição.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "refeicao_id",referencedColumnName = "refeicao_id",nullable = false)
	private Refeicao refeicao;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "credito_id")
	private Credito credito;
	
	@Transient
	private boolean novo = false;

	@Transient
	public boolean ehEditavelDtInicial = false;

	public Agendamento() {//contrutor default
	}
	
	public Agendamento(boolean novo) {
		this.novo = novo;
	}

	public Agendamento(LocalDate dtAgendamento, VinculosAtivosUsuarios vinculo) {
		this.setDtAgendamento(dtAgendamento);
		this.agendamentoId = UUID.randomUUID();
		this.novo = true;
		mudaVinculo(vinculo);
	}
	
	public Agendamento(LocalDate dtAgendamento, Matricula mat, Servidor serv) {
		this.setDtAgendamento(dtAgendamento);
		this.matricula = mat;
		this.servidor = serv;
		this.agendamentoId = UUID.randomUUID();
		this.novo = true;
	}
	
	public void mudaVinculo(VinculosAtivosUsuarios vinculo) throws RuntimeException{
		if(vinculo instanceof Matricula) {
			this.matricula = (Matricula) vinculo;
			this.servidor = null;
		}else if(vinculo instanceof Servidor) {
			this.servidor = (Servidor) vinculo;
			this.matricula = null;
		}else {
			throw new RuntimeException("Vinculo informado não é aluno e nem servidor.");
		}
	}

//	public Agendamento(LocalDateTime dtInicio, LocalDateTime dtFim, Usuario usuario) {
//		setStartDate(dtInicio);
//		setEndDate(dtFim);
//		this.usuario = usuario;
//		this.agendamentoId = UUID.randomUUID();
//		this.novo = true;
//	}
	
//	public Agendamento(LocalDate dtInicio, LocalDate dtFim, Usuario usuario) {
//		setStartDateD(dtInicio);
//		setEndDateD(dtFim);
//		this.usuario = usuario;
//		this.agendamentoId = UUID.randomUUID();
//		this.novo = true;
//	}
		
	public Agendamento(UUID agendamentoId, LocalDate dtAgendamento) {
		super();
		this.agendamentoId = agendamentoId;
		this.dtAgendamento = dtAgendamento;
		this.editavel = false;
		this.agendado = false;
		this.novo = true;
	}

	public Credito getCredito() {
		return credito;
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}
	public void setDtFim(LocalDateTime dtFim) {
		setEndDate(dtFim);
	}
	public String getId() {
		if(agendamentoId == null) {
			return "";
		}
		return agendamentoId.toString();
	}

	public void setId(String id) {
//		System.out.println("setId");
	}

	@Override
	public String getGroupId() {
		return this.refeicao.getTipoRefeicao().getMMId().toString();
	}

	@Override
	public String getTitle() {
		return this.refeicao.label();
	}

	@Override
	public LocalDateTime getStartDate() {
		return this.dtAgendamento.atTime(00, 01);
	}

	@Override
	public void setStartDate(LocalDateTime startDate) {
		this.dtAgendamento = startDate.toLocalDate();
	}

	public LocalDate getStartDateD() {
		return this.dtAgendamento;
	}

	public void setStartDateD(LocalDate startDate) {
		this.dtAgendamento = startDate;
	}
	
	//nao use esse metodos em
	@Override
	public LocalDateTime getEndDate() {
		// see https://github.com/primefaces/primefaces/issues/1164
		// https://github.com/fullcalendar/fullcalendar/issues/2985
//		if(isAllDay())
//			return this.dtAgendamento.plusDays(1).atTime(23, 59);
//		else
			return this.dtAgendamento.atTime(23, 59);
	}

	@Override
	public void setEndDate(LocalDateTime endDate) {
		this.dtAgendamento = endDate.toLocalDate();// with(LocalTime.of(23, 59));
	}

	public LocalDate getEndDateD() {
		return this.dtAgendamento;
	}

	public void setEndDateD(LocalDate endDate) {
		this.dtAgendamento = endDate;
	}

	@Override
	public boolean isAllDay() {
		return true;
	}

	public void setAllDay(boolean allDay) {
	}

	@Override
	public String getStyleClass() {
		return "testecalss" ;//this.refeicao.getTipoRefeicao().getStyleClass();
	}

	@Override
	public Agendamento getData() {
		return this;
	}

	public boolean isEditable() {
		boolean s = isEditDtAgendamento();
		return (!s);
	}
	
	//disable field?
	public boolean isEditDtAgendamento() {
		LocalDateTime agora = Agendamento.now();
		
		if(novo) {
			return false;
		}

		if (this.dtAgendamento == null) {
			return false;
		}
		
		if(getRefeicao() == null) {
			return false;
		}
		
		if(this.credito != null) {
			return true;
		}
		
		LocalDateTime block = this.diaHoraLimiteParaAlteracaoDtAgendamento();

		return block.isBefore(agora);
	}

	@Override
	public boolean isOverlapAllowed() {
		return false;
	}

	@Override
	public String getDescription() {
		StringBuffer desc = new StringBuffer();
		desc.append((this.refeicao!= null && this.refeicao.getTipoRefeicao() != null? this.refeicao.getTipoRefeicao().getDescricao() + ": ":""));
		desc.append(this.dtAgendamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		return desc.toString();
	}

	@Override
	public String getUrl() {
		return "";
	}

	@Override
	public Map<String, Object> getDynamicProperties() {
		return null;
	}

	@Override
	public String toString() {
		return "Agendamento{title=" + refeicao.getTipoRefeicao().getDescricao() + ",startDate=" + dtAgendamento + "}";// + ",endDate=" + dtFim + "}";
	}

	@Override
	public UUID getMMId() {
		return this.agendamentoId;
	}

	public Refeicao getRefeicao() {
		return refeicao;
	}

	public void setRefeicao(Refeicao tipoRefeicao) {
		this.refeicao = tipoRefeicao;
	}

	@Override
	public String getBackgroundColor() {
		return this.refeicao.getTipoRefeicao().getBackgroundColor();
	}

	@Override
	public String getBorderColor() {
		return "";
	}

	@Override
	public ScheduleDisplayMode getDisplay() {
		return ScheduleDisplayMode.AUTO;
	}

	@Override
	public String getTextColor() {
		return this.getRefeicao().getTipoRefeicao().getCorFonte();
	}

	@Override
	public Boolean isDraggable() {
		return false;
	}

	@Override
	public Boolean isResizable() {
		return false;
	}

	public UUID getAgendamentoId() {
		return agendamentoId;
	}

	public void setAgendamentoId(UUID agendamentoId) {
		this.agendamentoId = agendamentoId;
	}

	public boolean isAgendado() {
		return agendado;
	}

	public void setAgendado(boolean agendado) {
		this.agendado = agendado;
	}

	public LocalDateTime diaHoraLimiteParaAlteracaoDtAgendamento() {
		
		if(this.refeicao == null) {
			return null;
		}

		return this.refeicao.botaHoraInicio(this.getStartDate()).minusHours(this.refeicao.getBloquear());
	}
	
	/**
	 * @param dtFim indica se é para usar a data de fim sempre
	 */
	public LocalDateTime diaHoraLimiteAlteracaoDtFim(boolean dtFim) {
		
		if(this.refeicao == null) {
			return null;
		}
				
		LocalDateTime agora = Agendamento.now();
		LocalDateTime data;
		
		int dias = this.refeicao.getBloquear() / 24;
		int horas = this.refeicao.getBloquear() % 24;
		
		if (!dtFim && agora.toLocalDate().isBefore(this.dtAgendamento)) {
			if(dias > 0) {//remove um dia 
				dias--;
			}
			data = this.refeicao.botaHoraInicio(agora);
		}else {
			data = this.refeicao.botaHoraInicio(this.getStartDate());
		}
			
		return data.minusDays(dias).minusHours(horas);			
	}
	
	public LocalDateTime diaHoraLimiteAlteracaoData(LocalDateTime data) {
		return this.refeicao.botaHoraInicio(data).minusHours(this.refeicao.getBloquear());
	}

	public String mensagemAlteracaoDtAgendamento() {
		return 	getRefeicao().getTipoRefeicao().getDescricao() + ": "
				+ "Não é permitido "
				+ (novo ? "cadastro" : "alteração")
				+ ", pois agora(" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) + ") é maior que "
				+ this.diaHoraLimiteParaAlteracaoDtAgendamento().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) 
				+ "(data/hora mínima para o agendamento para " + this.dtAgendamento.format(DateTimeFormatter.ofPattern("dd/MM")) + ").";
	}
	
	public boolean isPermitdoRemover() {
		if(this.isNovo()) {
			return false;
		}
		
		return this.getCredito() == null && !this.isEditDtAgendamento();
	}

	public boolean isNovo() {
		return novo;
	}

	public void setNovo(boolean novo) {
		this.novo = novo;
	}
	
	public static LocalDateTime now() {
		//centraliza, pra realizar testes
    	return LocalDateTime.now();//.plusDays(1);
	}
	
	public LocalDate getDtAgendamento() {
		return dtAgendamento;
	}

	public void setDtAgendamento(LocalDate dtAgendamento) {
		this.dtAgendamento = dtAgendamento;
	}

	public Agendamento clone() {
		Agendamento clone = new Agendamento(this.dtAgendamento, this.matricula,this.servidor);
		clone.setRefeicao(this.refeicao);
		return clone;
	}
	
	public void updateFromApiAgendamento(APIAgendamento apiAgendamento) {
//		TODO rearrumar issoaqui depois
//		this.setRefeicao(apiAgendamento.getRefeicao().convertToRefeicao());
//		this.setUsuario(apiAgendamento.getUsuario().converteForUsuario());
//		this.setCredito(apiAgendamento.getCredito().converteForCredito(this.getUsuario(), this));
	}

	//removida a funcionalidade
//	public boolean isRendDisponizarAgendamento() {
//		//@formatter:off
//		LocalDateTime now = LocalDateTime.now();
//		return (!this.novo && this.credito == null 
//				&& now.isAfter(this.diaHoraLimiteParaAlteracaoDtAgendamento()) 
//				&& now.isBefore(this.refeicao.botaHoraInicio(this.getStartDate())));
//	}
	
	public boolean isAindaPodeTranferir() {
		//@formatter:off
		LocalDateTime now = LocalDateTime.now();
		return (!this.novo && this.credito == null 
				&& now.isAfter(this.diaHoraLimiteParaAlteracaoDtAgendamento()) 
				&& now.isBefore(this.refeicao.botaHoraFim(this.dtAgendamento)));
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
	
	@Transient
	@XmlTransient
	public VinculosAtivosUsuarios getVinculosAtivosUsuario() {
		if(this.matricula != null) {
			return this.matricula;
		}else {
			return this.servidor;
		}
	}
	
	@Transient
	@XmlTransient
	public String getDtAgendamentoString() {
		return this.dtAgendamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
}
