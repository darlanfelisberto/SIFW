package br.edu.iffar.fw.classBag.db.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

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

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.interfaces.TreeNodeSearch;


@Entity
@Table(name = "habitante_unidade", schema = "moradia_estudantil")
public class HabitanteUnidade extends Model<UUID> implements TreeNodeSearch {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "habitante_unidade_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID habitanteUnidadeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unidade_id")
	@NotNull(message = "Informe a unidade do habitante.")
	private Unidade unidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "matricula_id")
	@NotNull(message = "Informe a matricula do habitante.")
	private Matricula matricula;

	@Column(name = "dt_entrada")
	@NotNull(message = "Informe a data de entrada do habitante.")
	private LocalDate dtEntrada;

	@Column(name = "dt_saida")
	private LocalDate dtSaida;
	
	@Transient
	@XmlTransient
	private Presenca ausencia;

	public UUID getMMId() {
		return habitanteUnidadeId;
	}

	public UUID getHabitanteUnidadeId() {
		return habitanteUnidadeId;
	}

	public void setHabitanteUnidadeId(UUID habitanteUnidadeId) {
		this.habitanteUnidadeId = habitanteUnidadeId;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;

		if (this.unidade.getListHabitanteUnidade() == null) {
			this.unidade.setListHabitanteUnidade(new ArrayList<HabitanteUnidade>());
		}

		if (!this.unidade.getListHabitanteUnidade().contains(this)) {
			this.unidade.getListHabitanteUnidade().add(this);
		}
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public LocalDate getDtEntrada() {
		return dtEntrada;
	}

	public void setDtEntrada(LocalDate dtEntrada) {
		this.dtEntrada = dtEntrada;
	}

	public LocalDate getDtSaida() {
		return dtSaida;
	}

	public void setDtSaida(LocalDate dtSaida) {
		this.dtSaida = dtSaida;
	}

	public String getDtEntradaString() {
		return this.dtEntrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public String getDtSaidaString() {
		if(this.dtSaida != null) {
			return this.dtSaida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}else {
			return "-";
		}
		
	}

	public boolean isNovo() {
		return this.habitanteUnidadeId == null;
	}

	@Override
	public String label() {
		return this.matricula.juntaNomeMatricula();
	}

	@Override
	public boolean search(String termo) {
		if (this.matricula.getUsuario().getNome().toLowerCase().contains(termo)) {
			return true;
		}
		if (this.matricula.getIdMatricula().toString().contains(termo)) {
			return true;
		}
		return false;
	}

	public Presenca getAusencia() {
		return ausencia;
	}

	public void setAusencia(Presenca ausencia) {
		this.ausencia = ausencia;
	}
}
