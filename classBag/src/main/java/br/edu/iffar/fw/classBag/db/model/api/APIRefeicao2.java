package br.edu.iffar.fw.classBag.db.model.api;

import java.time.LocalTime;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classShared.db.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "api_refeicao")
public class APIRefeicao2 extends Model<UUID>{
	
	public static class SelecioneAPIRefeicao extends APIRefeicao2  {
		private static final long serialVersionUID = 1L;

		public String toString() {
			return "Selecione";
		}
	}
	
	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name ="refeicao_id",unique = true,nullable = false, insertable = true, updatable = false)
	private UUID refeicaoId;	
		
	@NotNull(message = "Informe o valor da refeição.")
	private Float valor;
	
	@NotNull(message = "Informe a hora de inicio em dias de semana.")
	@Column(name ="hora_inicio_util")
	private LocalTime horaInicioUtil;
	
	@NotNull(message = "Informe a hora de inicio aos finais de semana.")
	@Column(name ="hora_inicio_find")
	private LocalTime horaInicioFind;

	@NotNull(message = "Informe o tempo em horas para bloquear o agendamento de refeições.")
	private Integer bloquear;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tipo_refeicao_id",nullable = false,referencedColumnName = "tipo_refeicao_id")
	private TipoRefeicao tipoRefeicao;
	
	@Column(name = "descricao_grupo")
	private String descricaoGrupo;
	
	@Column(name = "grupo_refeicoes_id")
	private UUID grupoRefeicoesId;
	
	public String toString() {
		return this.tipoRefeicao.getDescricao() +", R$ "+this.valor +", "+ this.descricaoGrupo;
	}	
	
	public Refeicao convertToRefeicao() {
		return new Refeicao(this.refeicaoId);
	}

	public UUID getRefeicaoId() {
		return refeicaoId;
	}

	public void setRefeicaoId(UUID refeicaoId) {
		this.refeicaoId = refeicaoId;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public LocalTime getHoraInicioUtil() {
		return horaInicioUtil;
	}

	public void setHoraInicioUtil(LocalTime horaInicioUtil) {
		this.horaInicioUtil = horaInicioUtil;
	}

	public LocalTime getHoraInicioFind() {
		return horaInicioFind;
	}

	public void setHoraInicioFind(LocalTime horaInicioFind) {
		this.horaInicioFind = horaInicioFind;
	}

	public Integer getBloquear() {
		return bloquear;
	}

	public void setBloquear(Integer bloquear) {
		this.bloquear = bloquear;
	}

	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}

	public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
		this.tipoRefeicao = tipoRefeicao;
	}

	public String getDescricaoGrupo() {
		return descricaoGrupo;
	}

	public void setDescricaoGrupo(String descricaoGrupo) {
		this.descricaoGrupo = descricaoGrupo;
	}

	public UUID getGrupoRefeicoesId() {
		return grupoRefeicoesId;
	}

	public void setGrupoRefeicoesId(UUID grupoRefeicoesId) {
		this.grupoRefeicoesId = grupoRefeicoesId;
	}

	@Override
	public UUID getMMId() {
		return this.refeicaoId;
	}
	
	
}