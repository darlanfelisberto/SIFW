//package br.edu.iffar.fw.classBag.db.model.api;
//
//import java.util.UUID;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotNull;
//
//import br.edu.iffar.fw.classBag.db.Model;
//import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
//
//@Entity
//@Table(name = "servidor")
//public class APIServidor extends Model<UUID> {
//	
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@Column(name = "servidor_id",insertable = true,updatable = false)
//	private UUID servidorId;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "usuario_id",referencedColumnName = "usuario_id")
//	private APIUsuario usuario;
//	
//	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
//	@JoinColumn(name = "tipo_vinculo_id",referencedColumnName = "tipo_vinculo_id")
//	@NotNull(message = "Informe o tipo do vinculo da matricula.")
//	private TipoVinculo tipoVinculo;
//
//	
//	public UUID getServidorId() {
//		return servidorId;
//	}
//
//	public void setServidorId(UUID servidorId) {
//		this.servidorId = servidorId;
//	}
//
//	public APIUsuario getUsuario() {
//		return usuario;
//	}
//
//	public void setUsuario(APIUsuario usuario) {
//		this.usuario = usuario;
//	}
//
//	@Override
//	public UUID getMMId() {
//		return this.servidorId;
//	}
//
//	public TipoVinculo getTipoVinculo() {
//		return tipoVinculo;
//	}
//
//	public void setTipoVinculo(TipoVinculo tipoVinculo) {
//		this.tipoVinculo = tipoVinculo;
//	}	
//	
//}
