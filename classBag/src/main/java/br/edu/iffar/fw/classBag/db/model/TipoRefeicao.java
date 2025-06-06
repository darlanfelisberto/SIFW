package br.edu.iffar.fw.classBag.db.model;

import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "tipo_refeicao")
@Cacheable
public class TipoRefeicao extends Model {

	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name ="tipo_refeicao_id",insertable = true)
	private UUID tipoRefeicaoId;	
	
	private String descricao;
	
	@Column(name ="style_class")
	private String styleClass;
	
	@Column(name ="cor_fonte")
	private String corFonte;
	
	@Column(name ="background_color")
	private String backgroundColor;
	
	@Column(name = "hora_inicio")
	private LocalTime horaInicio;
	
	@Column(name = "hora_fim")
	private LocalTime horaFim;
	
	public TipoRefeicao() {
	}

	public UUID getTipoRefeicaoId() {
		return tipoRefeicaoId;
	}

	public String getDescricao() {
		return descricao;
	}

//	public String getStyleClass() {
//		return styleClass;
//	}

	public void setTipoRefeicaoId(UUID tipoRefeicaoId) {
		this.tipoRefeicaoId = tipoRefeicaoId;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

//	public void setStyleClass(String styleClass) {
//		this.styleClass = styleClass;
//	}

	@Override
	public UUID getMMId() {
		return this.tipoRefeicaoId;
	}

	@Override
	public String toString() {
		return descricao;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getCorFonte() {
		return corFonte;
	}

	public void setCorFonte(String corFonte) {
		this.corFonte = corFonte;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}
}
