package br.edu.iffar.fw.classBag.interfaces.credito.impl;

import br.edu.iffar.fw.classBag.db.model.AlteracoesCreditos;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.interfaces.credito.OperacoesCredito;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transferencia implements OperacoesCredito<Transferencia> {

    @Getter
    private final Credito saida = new Credito(new TipoCredito(TypeCredito.TRANS_SAIDA));;

    @Getter
    private final Credito entrada = new Credito(new TipoCredito(TypeCredito.TRANS_ENTRADA));;
    private BigDecimal saldo;
    private BigDecimal valor;

    public Transferencia valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(){
        this.saida.setValor(this.valor.negate());
        this.entrada.setValor(this.valor);
        this.saida.setParent(this.entrada);
//        this.entrada.setParent(this.saida);//causa erro ao salvar
    }

    @Override
    public Transferencia saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public Transferencia para(Usuario para) {
        this.entrada.setUsuario(para);
        return this;
    }

    public Transferencia realizadoPor(Usuario usuario) {
        this.saida.setUsuario(usuario);
        return this;
    }

    public AlteracoesCreditos getAltenacoesCreditos(){
        throw new CreditoException("Operacao não suporta esta funcionalidade");
    }

    @Override
    public Transferencia builder() {
        if(this.valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CreditoException("Informe um valor para a transferência maior que 0.");
        }
        if(this.saldo.compareTo(this.valor) < 0) {
            throw new CreditoException("Saldo insuficiente.");
        }
        if(this.entrada.getUsuario() == null) {
            throw new CreditoException("Informe para quem você vai realizar a transferência.");
        }
        if(this.entrada.getUsuario().equals(this.saida.getUsuario())) {
            throw new CreditoException("Você não pode transferir valores para você mesmo.");
        }

        this.setValor();

        return this;
    }

}
