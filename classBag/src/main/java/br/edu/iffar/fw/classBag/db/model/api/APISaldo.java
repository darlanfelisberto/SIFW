package br.edu.iffar.fw.classBag.db.model.api;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.edu.iffar.fw.classBag.db.Model;

/**
 * Esta classe é uma VIEW
 * 
 * @author darlan
 *
 */

@Entity
@Table(name = "api_saldo")
public class APISaldo extends Model<String> {

	private static final long serialVersionUID = 1L;

	@Column(name = "saldo")
	private Float saldo;

	@Id
	@Column(name = "username", insertable = true, updatable = false)
	private String userName;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
	private APIUsuario usuario;

	public String getMMId() {
		return userName;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public APIUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(APIUsuario usuario) {
		this.usuario = usuario;
	}
}
