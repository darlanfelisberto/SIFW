package br.edu.iffar.fw.classBag.db.model.api;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class APIUsuarioRefeicaoPK  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name ="refeicao_id")
	private UUID refeicaoId;	

	@Column(name ="usuario_id")
	private UUID usuarioId;
//	
//	@Column(name ="grupo_refeicao_id")
//	private UUID grupoRefeicaoId;

	public UUID getRefeicaoId() {
		return refeicaoId;
	}

	public void setRefeicaoId(UUID refeicaoId) {
		this.refeicaoId = refeicaoId;
	}

	public UUID getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(UUID usuarioId) {
		this.usuarioId = usuarioId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(refeicaoId, usuarioId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		APIUsuarioRefeicaoPK other = (APIUsuarioRefeicaoPK) obj;
		return Objects.equals(refeicaoId, other.refeicaoId) && Objects.equals(usuarioId, other.usuarioId);
	}	
	
	
}
