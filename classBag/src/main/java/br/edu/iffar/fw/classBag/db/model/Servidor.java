package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import br.edu.iffar.fw.classBag.interfaces.VinculosAtivosUsuarios;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "servidor")
public class Servidor extends Model implements Serializable, VinculosAtivosUsuarios {

	private static final long serialVersionUID = 22021991L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "servidor_id")
	private UUID servidorId;

	@NotNull(message = "informe o usuário!")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_vinculo_id",referencedColumnName = "tipo_vinculo_id")
	@NotNull(message = "Informe o tipo do vinculo da matricula.")
	private TipoVinculo tipoVinculo;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TypeSituacao situacao;
	
	private Integer siape; 
	
	public Servidor() {
	}
	
	public Servidor(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public UUID getMMId() {
		return this.servidorId;
	}

	public UUID getServidorId() {
		return servidorId;
	}

	public void setServidorId(UUID servidorId) {
		this.servidorId = servidorId;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoVinculo getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(TipoVinculo tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}
	
	public String getLabel() {
		return "Servidor, " + this.tipoVinculo.getDescricao();
	}

	public Integer getSiape() {
		return siape;
	}

	public void setSiape(Integer siape) {
		this.siape = siape;
	}

	public TypeSituacao getSituacao() {
		return situacao;
	}

	public void setSituacao(TypeSituacao situacao) {
		this.situacao = situacao;
	}
	
}