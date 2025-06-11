package br.edu.iffar.fw.classBag.enun;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//
//import org.primefaces.model.charts.ChartData;
//import org.primefaces.model.charts.pie.PieChartDataSet;
//import org.primefaces.model.charts.pie.PieChartModel;

import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.impl.EntradaCredito;
import br.edu.iffar.fw.classBag.impl.SaidaCredito;
import br.edu.iffar.fw.classBag.interfaces.OperacaoCredito;

public enum TypeCredito {
	ENTRADA			(0,new EntradaCredito(),"money-bill"),
	SAIDA			(1,new SaidaCredito(),"shopping-cart"),
	TRANS_SAIDA		(2,new SaidaCredito(),"send"),
	TRANS_ENTRADA	(3,new EntradaCredito(),"money-bill"),
	RETIRADA		(4,new SaidaCredito(),"exclamation-triangle");

    private int index;
	private String icon;
	private OperacaoCredito operacao;

	private TypeCredito(int index,OperacaoCredito operacao,String icon) {
		this.index = index;
		this.icon = icon;
		this.operacao = operacao;
	}

	public void sumType(BigDecimal[] sum,Credito c) {
		sum[this.index] = sum[this.index].add(c.getValor());
	}

    public TipoCredito createIntance() {
		return new TipoCredito(this);
	}

	public void inicializaTipoCredito(Credito credito) {
		if(credito.getValor() == null) {
			throw new CreditoException("Informe o valor do credito antes de iniciar a operação.");
		}
		credito.setTipoCredito(new TipoCredito(this));
		this.operacao.ajuste(credito);
	}

	public int getIndex() {
		return index;
	}

	public String getIcon() {
		return icon;
	}
}
