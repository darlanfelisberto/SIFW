package br.edu.iffar.fw.authClassShared.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import br.edu.iffar.fw.classShared.db.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="usuarios")
public class Usuario extends Model<UUID> implements Serializable {
	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name="usuario_id")
	protected UUID usuarioId;

	@Column(name="nome")
	@NotNull(message = "Informe o nome do usuario.")
	protected String nome;

	@Column(name = "dt_nasc")
	@NotNull(message = "Informe a data de nascimento.")
	protected LocalDate dtnasc;
	
	@NotEmpty(message = "Informe o cpf")
	protected String cpf;
    
	/**
	 * nao ativar o cascade no authUser, se for necessario, inverta o dono da relação
	 * */
	@JoinColumn(name = "auth_user_id")
	@OneToOne(fetch = FetchType.LAZY)
	protected AuthUser authUser;

	@Transient
	protected boolean novo;

	public Usuario() {
	}

	public UUID getUsuarioId() {
		return this.usuarioId;
	}

	public void setIdUsuario(UUID usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	@Override
	public UUID getMMId() {
		return this.usuarioId;
	}

	public String labelIniciais() {
		String[] nome = this.nome.split(" ");
		StringBuilder iniciais = new StringBuilder();
		for (String string : nome) {
			iniciais.append(string.toUpperCase().charAt(0) + ".");
		}
		return iniciais.toString();
	}
    
    public void setUsuarioId(UUID usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		StringBuilder s = new StringBuilder();
		for (String string : nome.split(" ")) {
			s.append(" " + (string.length() > 2 ? string.substring(0, 1).toUpperCase() + string.substring(1, string.length()).toLowerCase() : string));
		}
		// esse trim remove o espaço colocado no for
		this.nome = s.toString().trim();;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if(cpf != null) {
			this.cpf = cpf.replaceAll("[^0-9]", "");
		} else {
			this.cpf = cpf;
		}
	}
	
	public LocalDate getDtnasc() {
		return dtnasc;
	}

	public void setDtnasc(LocalDate dtnasc) {
		this.dtnasc = dtnasc;
	}

	public void setEventLimit(boolean eventLimit) {
    }
		public boolean isNovo() {
		return this.novo;
	}

	public String getNomeCpf() {
		return this.cpf + " - " + this.nome;
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
		//cuidado com o loop dentro dos set
	}

	public static Usuario createNew(){
		Usuario u = new Usuario();
		u.novo = true;
		u.setIdUsuario(UUID.randomUUID());
		u.setAuthUser(AuthUser.createNew(u, new HashSet<Permissao>()));
		return u;
	}
}