package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.fw.classBag.util.ValidacaoPersonalizada;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alteracoes_creditos")
public class AlteracoesCreditos extends Model implements Serializable,ValidacaoPersonalizada {

	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "alteracoes_creditos_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID alteracoesCreditosId ;

	@OneToOne
	@JoinColumn(name = "usuario_logado_id")
	@NotNull
	private Usuario usuarioLogado;

	/**
	 * Atributo identifica o credito vindo de uma transferencia
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "de_credito_id")
	private Credito deCredito;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "para_credito_id")
	@NotNull
	private Credito paraCredito;

	public AlteracoesCreditos() {
	}

	@Override
	public UUID getMMId() {
		return this.alteracoesCreditosId;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Credito getDeCredito() {
		return deCredito;
	}

	public void setDeCredito(Credito deCredito) {
		this.deCredito = deCredito;
	}

	public Credito getParaCredito() {
		return paraCredito;
	}

	public void setParaCredito(Credito paraCredito) {
		this.paraCredito = paraCredito;
	}

	@Override
	public boolean validar(MessagesUtil messages) {
		return false;
	}
}