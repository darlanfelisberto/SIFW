package br.edu.iffar.fw.classBag.interfaces;

import br.edu.iffar.fw.classBag.db.model.Credito;

import java.math.BigDecimal;

public interface OperacaoCredito {

	public BigDecimal calc(BigDecimal operado, BigDecimal operando);

	/**
	 * Metodo serve para ajustar o valor a operação, se for retirada é negativo se for entrada é potitivo
	 *
	 * @param credito
	 */
	public void ajuste(Credito credito);
}
