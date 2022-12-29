package br.edu.iffar.fw.classBag.db.model.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sincronizado")
public class APISincronizado  extends Model<UUID> {

	private static final long serialVersionUID = 19910222L;
	
	public enum TipoSinc{
		TOTAL("Total"),PARCIAL("Parcial");
		
		private String descricao;
		
		TipoSinc(String desc){
			this.descricao = desc;
		}

		public String getDescricao() {
			return descricao;
		}
	}

	@Id
	@Column(name = "sincronizado_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID sincronizadoId;
	
	@Column(name = "dt_sincronizado")
	private LocalDateTime dtSincronizado;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_sincronizacao")
	private TipoSinc tipoSincronizacao;
	
	public APISincronizado() {}
	
	public APISincronizado(LocalDateTime dtSincronizado, TipoSinc tipoSincronizacao) {
		super();
		this.dtSincronizado = dtSincronizado;
		this.tipoSincronizacao = tipoSincronizacao;
	}

	@Override
	public UUID getMMId() {
		return sincronizadoId;
	}
        
        public String getLabel(){
            if(this.dtSincronizado == null){
                return "-";
            }
            
            return "Sinc.:" + this.dtSincronizado.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")) + " - "+this.tipoSincronizacao.getDescricao();
        }

	public UUID getSincronizadoId() {
		return sincronizadoId;
	}

	public void setSincronizadoId(UUID sincronizadoId) {
		this.sincronizadoId = sincronizadoId;
	}

	public LocalDateTime getDtSincronizado() {
		return dtSincronizado;
	}

	public void setDtSincronizado(LocalDateTime dtSincronizado) {
		this.dtSincronizado = dtSincronizado;
	}

	public TipoSinc getTipoSincronizacao() {
		return tipoSincronizacao;
	}

	public void setTipoSincronizacao(TipoSinc tipoSincronizacao) {
		this.tipoSincronizacao = tipoSincronizacao;
	}

	
	
}
