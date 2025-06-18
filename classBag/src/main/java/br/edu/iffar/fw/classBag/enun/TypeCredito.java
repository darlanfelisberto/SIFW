package br.edu.iffar.fw.classBag.enun;

import java.math.BigDecimal;
//

import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;

public enum TypeCredito {
	ENTRADA			(0,"money-bill"),
	SAIDA			(1,"shopping-cart"),
	TRANS_SAIDA		(2,"send"),
	TRANS_ENTRADA	(3,"money-bill"),
	RETIRADA		(4,"exclamation-triangle");

    private int index;
	private String icon;

	private TypeCredito(int index, String icon) {
		this.index = index;
		this.icon = icon;
	}

	public void sumType(BigDecimal[] sum,Credito c) {
		sum[this.index] = sum[this.index].add(c.getValor());
	}

    public TipoCredito createIntance() {
		return new TipoCredito(this);
	}

	public int getIndex() {
		return index;
	}

	public String getIcon() {
		return icon;
	}
}
