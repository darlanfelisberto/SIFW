package br.edu.iffar.fw.classBag.interfaces.credito.impl;

import br.edu.iffar.fw.classBag.db.model.*;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.interfaces.credito.OperacoesCredito;
import lombok.Getter;

import java.math.BigDecimal;

public class Pagamento implements OperacoesCredito<Pagamento> {

    private final Credito saida = new Credito(new TipoCredito(TypeCredito.SAIDA));
    private final AlteracoesCreditos altenacoesCreditos = new AlteracoesCreditos();
    private BigDecimal saldo;
    private BigDecimal valor;

    public Pagamento() {
    }

    public Pagamento(Agendamento agendamento) {
        agendamento.setCredito(this.saida);
        this.saida.setAgendamento(agendamento);
        this.saida.setValor(agendamento.getRefeicao().getValor().negate());
        this.valor = agendamento.getRefeicao().getValor();
    }

    public Pagamento valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    @Override
    public Pagamento saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public Pagamento para(Usuario para) {
        this.saida.setUsuario(para);
        this.altenacoesCreditos.setParaCredito(this.saida);
        return this;
    }

    public Pagamento realizadoPor(Usuario usuarioLogado) {
        this.altenacoesCreditos.setUsuarioLogado(usuarioLogado);
        return this;
    }

    public Credito getSaida() {
        return this.saida;
    }

    public Credito getEntrada() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AlteracoesCreditos getAltenacoesCreditos() {
        return altenacoesCreditos;
    }

    public Pagamento builder() {
        if(this.valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new CreditoException("Informe um valor maior que 0.");
        }

        if(!(this.saldo.compareTo(BigDecimal.ZERO) >= 0)) {
            throw new CreditoException("Saldo deve ser maior que zero.");
        }

        if(this.saldo.compareTo(this.valor) < 0) {
            throw new CreditoException("Saldo insuficiente.");
        }

        return this;
    }
}
