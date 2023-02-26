package br.edu.iffar.fw.classBag.db.model.log;

import java.time.LocalDateTime;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leituras",schema = "log")
public class LogLeitura extends Model<UUID> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "leitura_id",updatable = false,unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID leituraID;
	
	private String qrcode;
	
	@Column(name = "dt_leitura")
	private LocalDateTime dtLeitura;
	
//	mappeamento foi removido por questao de desempenho, no insert
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "usuario_lido_id")
	@Column(name = "usuario_lido_id")
	private UUID usuarioLido;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "usuario_operador_id")
	@Column(name = "usuario_operador_id")
	private UUID usuarioOperador;

	public LogLeitura() {}
	
	public LogLeitura(Usuario lido,Usuario operador, String valorLido) {
		this.dtLeitura = LocalDateTime.now();
		this.qrcode = valorLido;
		if(lido != null) {
			this.usuarioLido = lido.getMMId();
		}
		this.usuarioOperador = operador.getMMId();
	}
	
	public UUID getMMId() {
		return leituraID;
	}

	public UUID getLeituraID() {
		return leituraID;
	}

	public void setLeituraID(UUID leituraID) {
		this.leituraID = leituraID;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public LocalDateTime getDtLeitura() {
		return dtLeitura;
	}

	public void setDtLeitura(LocalDateTime dtLeitura) {
		this.dtLeitura = dtLeitura;
	}

	public UUID getUsuarioLido() {
		return usuarioLido;
	}

	public void setUsuarioLido(UUID usuarioLido) {
		this.usuarioLido = usuarioLido;
	}

	public UUID getUsuarioOperador() {
		return usuarioOperador;
	}

	public void setUsuarioOperador(UUID usuarioOperador) {
		this.usuarioOperador = usuarioOperador;
	}

//	public Usuario getUsuarioLido() {
//		return usuarioLido;
//	}
//
//	public void setUsuarioLido(Usuario usuarioLido) {
//		this.usuarioLido = usuarioLido;
//	}
//
//	public Usuario getUsuarioOperador() {
//		return usuarioOperador;
//	}
//
//	public void setUsuarioOperador(Usuario usuarioOperador) {
//		this.usuarioOperador = usuarioOperador;
//	}
	
	
}
