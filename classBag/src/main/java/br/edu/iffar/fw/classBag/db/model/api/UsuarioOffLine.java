package br.edu.iffar.fw.classBag.db.model.api;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.edu.iffar.fw.classBag.db.Model;

@Entity
@Table(name = "usuario_off_line")
public class UsuarioOffLine extends Model<UUID> {

    public static final long serialVersionUID = 22021991L;

    @Id
    @Column(name = "usuario_off_line_id", insertable = true, updatable = false)
    private UUID usuarioOffLineId;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "hash")
    private String hash;

    @Column(name = "salt")
    private String salt;

	public UsuarioOffLine() {
	}

	public UsuarioOffLine(@NotNull String username) {
		super();
		this.usuarioOffLineId = UUID.randomUUID();
		this.username = username;
	}

	public UUID getUsuarioOffLineId() {
		return usuarioOffLineId;
	}

	public void setUsuarioOffLineId(UUID usuarioOffLineId) {
		this.usuarioOffLineId = usuarioOffLineId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public UUID getMMId() {
		return usuarioOffLineId;
	}

}
