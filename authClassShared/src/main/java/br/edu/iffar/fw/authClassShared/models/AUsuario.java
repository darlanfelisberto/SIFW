package br.edu.iffar.fw.authClassShared.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import br.edu.iffar.fw.classShared.db.Model;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="ausuarios",schema = "auth")
@PrimaryKeyJoinColumn(name = "auth_user_id")
public class AUsuario extends AuthUser {
	private static final long serialVersionUID = 22021991L;

	@Column(name="usuario_id")
	private UUID usuarioId;

	@Column(name="nome")
	@NotNull(message = "Informe o nome do usuario.")
	private String nome;

	@Column(name = "dt_nasc")
	@NotNull(message = "Informe a data de nascimento.")
	private LocalDate dtnasc;
	
	@NotEmpty(message = "Informe o cpf")
	private String cpf;
    
//	/**
//	 * nao ativar o cascade no authUser, se for necessario, inverta o dono da relação
//	 * */
//	@JoinColumn(name = "auth_user_id")
//	@OneToOne(fetch = FetchType.LAZY)
//	private AuthUser authUser;
	
	public AUsuario() {
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

	public String getNomeCpf() {
		return this.cpf + " - " + this.nome;
	}

//	public AuthUser getAuthUser() {
//		return authUser;
//	}
//
//	public void setAuthUser(AuthUser authUser) {
//		this.authUser = authUser;
//		//cuidado com o loop dentro dos set
//	}
}