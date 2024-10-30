package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.util.UUID;

import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.fw.classBag.util.ValidacaoPersonalizada;
import br.edu.iffar.fw.classBag.validation.ValidaCreditoPorTypo;
import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "altenacoes_creditos")
@ValidaCreditoPorTypo(typeCreditoPara = "getTypeCreditoPara",realizadoPorCredito = "getRealizadoPorCredito" )
public class AltenacoesCreditos extends Model<UUID> implements Serializable,ValidacaoPersonalizada {

	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "altenacoes_creditos_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID altenacoesCreditosId ;

	@OneToOne
	@JoinColumn(name = "realizado_por_id")
	@NotNull
	private Usuario realizadoPor;

	@OneToOne
	@JoinColumn(name = "para_id")
	@NotNull
	private Usuario para;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "realizado_por_credito_id")
	//é validado pela anotacao @ ValidaCreditoPorTypo
	private Credito realizadoPorCredito;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "para_credito_id")
	@NotNull
	private Credito paraCredito;

	public AltenacoesCreditos() {
	}
	
	public AltenacoesCreditos(Credito realizadoPorCredito, Credito paraCredito) {
		this.realizadoPorCredito = realizadoPorCredito;
		this.paraCredito = paraCredito;
	}
	
	public AltenacoesCreditos(Credito realizadoPorCredito, Usuario realizadoPor) {
		this.realizadoPorCredito = realizadoPorCredito;
		this.realizadoPor = realizadoPor;
	}


	public void setPara(Usuario para) {
		this.para = para;
		this.paraCredito.setUsuario(para);
	}

	public Credito getCreditoDe() {
		return realizadoPorCredito;
	}

	public TypeCredito getTypeCreditoPara() {
		return (this.paraCredito != null?this.paraCredito.getTipoCredito().getTipoCreditoId():null);
		
	}

	public void setCreditoPara(Credito creditoPara) {
		this.paraCredito = creditoPara;
		this.para = creditoPara.getUsuario();
	}

	@Override
	public UUID getMMId() {
		return this.altenacoesCreditosId;
	}

	public UUID getAltenacoesCreditosId() {
		return altenacoesCreditosId;
	}

	public void setAltenacoesCreditosId(UUID altenacoesCreditosId) {
		this.altenacoesCreditosId = altenacoesCreditosId;
	}

	public Usuario getRealizadoPor() {
		return realizadoPor;
	}

	public void setRealizadoPor(Usuario realizadoPor) {
		this.realizadoPor = realizadoPor;
	}

	public Credito getRealizadoPorCredito() {
		return realizadoPorCredito;
	}

	public void setRealizadoPorCredito(Credito realizadoPorCredito) {
		this.realizadoPorCredito = realizadoPorCredito;
	}

	public Credito getParaCredito() {
		return paraCredito;
	}

	public void setParaCredito(Credito paraCredito) {
		this.paraCredito = paraCredito;
	}

	public Usuario getPara() {
		return para;
	}

	@Override
	public boolean validar(MessagesUtil messages) {
		// TODO Auto-generated method stub
		return false;
	}


}