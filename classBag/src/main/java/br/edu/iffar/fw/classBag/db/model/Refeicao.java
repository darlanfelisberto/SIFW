package br.edu.iffar.fw.classBag.db.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;

@Entity
@Table(name = "refeicao")
public class Refeicao extends Model<UUID>{

	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name ="refeicao_id")
	//TO-DO refatorar tela de cadastro de grupos e refeições para deixar de exigir a criação de id manualmente
	private UUID refeicaoId = UUID.randomUUID();
		
	@NotNull(message = "Informe o valor da refeição.")
	private float valor;
	
	@NotNull(message = "Informe a hora de inicio em dias de semana.")
	@Column(name ="hora_inicio_util")
	private LocalTime horaInicioUtil;
	
	@NotNull(message = "Informe a hora de inicio aos finais de semana.")
	@Column(name ="hora_inicio_find")
	private LocalTime horaInicioFind;

	@NotNull(message = "Informe o tempo em horas para bloquear o agendamento de refeições.")
	private Integer bloquear;
	
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
	@JoinColumn(name="grupo_refeicoes_id",referencedColumnName ="grupo_refeicoes_id" ,nullable = false)
	@NotNull(message = "Informe o grupo ao qual essa refeição pertence.")
	private GrupoRefeicoes grupoRefeicoes;
	
	@NotNull(message = "Informe o tipo da refeição.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tipo_refeicao_id",nullable = false,referencedColumnName = "tipo_refeicao_id")
	private TipoRefeicao tipoRefeicao;
	
	@NotNull(message = "Informe o tempo para termino.")
	private Integer termino;

	public Refeicao() {
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public UUID getRefeicaoId() {
		return refeicaoId;
	}

	public LocalTime getHoraInicioUtil() {
		return horaInicioUtil;
	}

	public LocalTime getHoraInicioFind() {
		return horaInicioFind;
	}

	public Integer getBloquear() {
		return bloquear;
	}

	public void setRefeicaoId(UUID refeicaoId) {
		this.refeicaoId = refeicaoId;
	}

	public void setHoraInicioUtil(LocalTime horaInicioUtil) {
		this.horaInicioUtil = horaInicioUtil;
	}

	public void setHoraInicioFind(LocalTime horaInicioFind) {
		this.horaInicioFind = horaInicioFind;
	}

	public void setBloquear(Integer bloquear) {
		this.bloquear = bloquear;
	}

	public UUID getMMId() {
		return this.refeicaoId;
	}

	public GrupoRefeicoes getGrupoRefeicoes() {
		return grupoRefeicoes;
	}

	public void setGrupoRefeicoes(GrupoRefeicoes grupoReicoes) {
		this.grupoRefeicoes = grupoReicoes;
	}

	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}

	public Integer getTermino() {
		return termino;
	}

	public void setTermino(Integer termino) {
		this.termino = termino;
	}

	public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
		if (this.horaInicioFind == null) {
			this.horaInicioFind = tipoRefeicao.getHoraInicio();
		}
		
		if (this.horaInicioUtil == null) {
			this.horaInicioUtil = tipoRefeicao.getHoraInicio();
		}
		
		this.tipoRefeicao = tipoRefeicao;
	}
	
	public String label() {
		if(this.grupoRefeicoes.getTipoVinculo() == null) {
			return this.tipoRefeicao.getDescricao() +", R$ "+this.valor +", "+ this.grupoRefeicoes.getDescricao();
		}else {
			return this.tipoRefeicao.getDescricao() +", R$ "+this.valor +", "+ this.grupoRefeicoes.getTipoVinculo().getDesc();
		}
	}
	
	@XmlTransient
	@Transient
	public String getExemploBloqueioHoraLimitePara() {		
		return this.botaHoraInicio(LocalDateTime.now()).minusHours(this.bloquear).format(DateTimeFormatter.ofPattern("HH:mm 'do dia' dd/MM"));
	}
	
	
	public LocalDateTime botaHoraInicio(LocalDateTime instante) {
		
		instante = ((instante.getDayOfWeek().equals(DayOfWeek.SATURDAY) || instante.getDayOfWeek().equals(DayOfWeek.MONDAY))?
			//final off week
			 instante.with(getHoraInicioFind())
			:
			//day off week
			instante.with(getHoraInicioUtil())
		);
		return instante;
	}

	public LocalDateTime botaHoraFim(LocalDate instante) {

		LocalDateTime horaFim = ((instante.getDayOfWeek().equals(DayOfWeek.SATURDAY) || instante.getDayOfWeek().equals(DayOfWeek.MONDAY)) ?
		// final off week
				instante.atTime(getHoraInicioFind())
				:
				// day off week
				instante.atTime(getHoraInicioUtil()));

		horaFim = horaFim.plusMinutes(this.termino);

		return horaFim;
	}
}
