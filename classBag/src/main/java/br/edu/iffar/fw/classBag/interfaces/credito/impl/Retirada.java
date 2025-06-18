package br.edu.iffar.fw.classBag.interfaces.credito.impl;

import br.edu.iffar.fw.classBag.bo.CreditosDepositoBO;
import br.edu.iffar.fw.classBag.db.model.*;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.interfaces.credito.OperacoesCredito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Retirada implements OperacoesCredito<Retirada> {

    Credito credito = new Credito(new TipoCredito(TypeCredito.RETIRADA));
    AlteracoesCreditos altenacoesCreditos = new AlteracoesCreditos();
    BigDecimal saldo;
    BigDecimal valor;
    private boolean total;


    public Retirada() {
    }

    public Retirada(boolean total) {
        this.total = total;
    }

    public Retirada valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }
    public void setValor(){
        this.credito.setValor(valor.negate());
    }

    @Override
    public Retirada saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public Retirada para(Usuario para) {
        this.credito.setUsuario(para);
        this.altenacoesCreditos.setParaCredito(credito);
        return this;
    }

    public Retirada realizadoPor(Usuario usuarioLogado) {
        this.altenacoesCreditos.setUsuarioLogado(usuarioLogado);
        return this;
    }

    public AlteracoesCreditos getAltenacoesCreditos() {
        return altenacoesCreditos;
    }

    @Override
    public Retirada builder() {
        if(this.valor.compareTo(BigDecimal.ZERO) > 0) {
            throw new CreditoException("Informe um valor para a transferência maior que 0.");
        }

        if(!(saldo.compareTo(BigDecimal.ZERO) > 0)) {
            throw new CreditoException("Saldo deve ser maior que zero.");
        }

        if(total) {
            valor(saldo);
        }else {
            //retirada parcial
            if (saldo.subtract(this.valor).compareTo(BigDecimal.ZERO) < 0) {
                throw new CreditoException("Saldo insufíciente para retirada.");
            }
        }

        this.setValor();

        return this;
    }
}
