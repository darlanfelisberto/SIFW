package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.edu.iffar.fw.classBag.db.Model;

@Table(name = "log")
@Entity
public class Log extends Model<UUID> implements Serializable{

	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "log_id")
	private UUID logId = UUID.randomUUID();
	
	@Column(name = "tupla_id")
	private UUID tuplaId;
	
	private String entidade;
	
	private String operacao;
		
	@Column(name = "usuario_id")
	private String usuarioId;
	
	@Column(name = "dt_log")
	private LocalDateTime dtLog;
	
	public LocalDateTime getDtLog() {
		return dtLog;
	}

	public UUID getTuplaId() {
		return tuplaId;
	}

	public void setTuplaId(UUID tuplaId) {
		this.tuplaId = tuplaId;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public void setDtLog(LocalDateTime dtLog) {
		this.dtLog = dtLog;
	}

	@Override
	public UUID getMMId() {
		return this.logId;
	}
	
}
