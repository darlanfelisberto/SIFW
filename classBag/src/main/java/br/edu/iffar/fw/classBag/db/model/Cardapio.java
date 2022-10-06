package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.edu.iffar.fw.classBag.db.Model;

@Entity
@Table(name = "cardapio")
public class Cardapio extends Model<UUID>implements Serializable{
	
	public enum DiasDaSemana{
		SEGUNDA("Segunda"),
		TERCA("Terca"),
		QUARTA("Quarta"),
		QUINTA("Quinta"),
		SEXTA("Sexta"),
		SABADO("Sabado"),
		DOMINGO("Domingo");
		
		private String descricao;
		
		DiasDaSemana(String dia){
			this.descricao = dia;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}

	private static final long serialVersionUID = 22021991L;
	
	@Id
	@Column(name = "cardapio_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID cardapioId;

	@NotNull
	@Column(name = "dt_inicio")
	private LocalDate dtInicio;
	
	@NotNull
	@Column(name = "dt_fim")
	private LocalDate dtFim;
	
	private String segunda = new String("");
	private String terca = new String("");
	private String quarta = new String("");
	private String quinta = new String("");
	private String sexta = new String("");
	private String sabado = new String("");
	private String domingo = new String("");
	
	public Cardapio() {}

	@Override
	public UUID getMMId() {
		return this.cardapioId;
	}

	public UUID getCardapioId() {
		return cardapioId;
	}

	public void setCardapioId(UUID cardapioId) {
		this.cardapioId = cardapioId;
	}

	public String getSegunda() {
		return segunda;
	}

	public void setSegunda(String segunda) {
		this.segunda = segunda;
	}

	public String getTerca() {
		return terca;
	}

	public void setTerca(String terca) {
		this.terca = terca;
	}

	public String getQuarta() {
		return quarta;
	}

	public void setQuarta(String quarta) {
		this.quarta = quarta;
	}

	public String getQuinta() {
		return quinta;
	}

	public void setQuinta(String quinta) {
		this.quinta = quinta;
	}

	public String getSexta() {
		return sexta;
	}

	public void setSexta(String sexta) {
		this.sexta = sexta;
	}

	public String getSabado() {
		return sabado;
	}

	public void setSabado(String sabado) {
		this.sabado = sabado;
	}

	public String getDomingo() {
		return domingo;
	}

	public void setDomingo(String domingo) {
		this.domingo = domingo;
	}

	public LocalDate getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(LocalDate dtInicio) {
		this.dtInicio = dtInicio;
	}

	public LocalDate getDtFim() {
		return dtFim;
	}

	public void setDtFim(LocalDate dtFim) {
		this.dtFim = dtFim;
	}
	
	public String getSemana() {
		if(this.dtInicio != null) {
			return String.valueOf(this.dtInicio.get(WeekFields.of(Locale.ENGLISH).weekOfWeekBasedYear()));
		}
		return "-";
	}
	
	public String pegaTexto(DiasDaSemana dia) {
		switch (dia) {
			case SEGUNDA:  return this.segunda;
			case TERCA: return this.terca;
			case QUARTA: return this.quarta;
			case QUINTA: return this.quinta;
			case SEXTA: return this.sexta;
			case SABADO: return this.sabado;
			default: return this.domingo;		
		}
	}
	
	public void sendTexto(DiasDaSemana dia,String texto) {
		switch (dia) {
			case SEGUNDA:  
				this.segunda = texto;
				break;
			case TERCA: 
				this.terca = texto;
				break;
			case QUARTA: 
				this.quarta = texto;
				break;
			case QUINTA: 
				this.quinta = texto;
				break;
			case SEXTA: 
				this.sexta = texto;
				break;
			case SABADO: 
				this.sabado = texto;
				break;
			default: 
				this.domingo = texto;
				break;
		}
	}
	
	public String descricao(){
		StringBuffer desc = new StringBuffer();
		desc.append("Período do cardápio de ");
		if(this.dtInicio != null ) {
			desc.append(this.dtInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		desc.append(" até ");
		if(this.dtFim != null) {
			desc.append(this.dtFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}
		return desc.toString();
	}
	
}
