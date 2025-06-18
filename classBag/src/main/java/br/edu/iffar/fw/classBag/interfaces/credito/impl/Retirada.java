package br.edu.iffar.fw.classBag.interfaces.credito.impl;

import br.edu.iffar.fw.classBag.bo.CreditosDepositoBO;
import br.edu.iffar.fw.classBag.db.model.*;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.interfaces.credito.OperacoesCredito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Retirada implements OperacoesCredito {

    Credito credito = new Credito(new TipoCredito(TypeCredito.RETIRADA));
    AlteracoesCreditos altenacoesCreditos = new AlteracoesCreditos();
    BigDecimal saldo;

    public Retirada retirada(boolean total,BigDecimal valor) throws CreditoException {

        this.valor(valor);

        if(saldo == null) {
            throw new CreditoException("Informe o saldo do usuário.");
        }

        if(!(saldo.compareTo(BigDecimal.ZERO) > 0)) {
            throw new CreditoException("Saldo deve ser maior que zero.");
        }

        if(total) {
            credito.setValor(saldo.negate());
        }else {
            //retirada parcial
            if ((saldo.add(this.credito.getValor())).compareTo(BigDecimal.ZERO) <= 0) {
                throw new CreditoException("Saldo insufíciente para retirada.");
            }
        }

        return this;
    }
    public OperacoesCredito valor(BigDecimal valor) {
        this.credito.setValor(valor.negate());
        this.credito.setDtCredito(LocalDateTime.now());
        return this;
    }

    @Override
    public Retirada saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public OperacoesCredito para(Usuario para) {
        this.credito.setUsuario(para);
        this.altenacoesCreditos.setParaCredito(credito);
        return this;
    }

    public OperacoesCredito realizadoPor(Usuario usuarioLogado) {
        this.altenacoesCreditos.setUsuarioLogado(usuarioLogado);
        return this;
    }

    public AlteracoesCreditos getAltenacoesCreditos() {
        return altenacoesCreditos;
    }
}
