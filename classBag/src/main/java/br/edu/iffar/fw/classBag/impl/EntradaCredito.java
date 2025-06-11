package br.edu.iffar.fw.classBag.impl;

import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.interfaces.OperacaoCredito;

import java.math.BigDecimal;

public class EntradaCredito implements OperacaoCredito {

	public BigDecimal calc(BigDecimal operado,BigDecimal operando) {
		return operado.add(operando);
	}

	@Override
	public void ajuste(Credito credito) {

	}
}
