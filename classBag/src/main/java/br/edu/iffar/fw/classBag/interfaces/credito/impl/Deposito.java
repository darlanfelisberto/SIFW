package br.edu.iffar.fw.classBag.interfaces.credito.impl;

import br.edu.iffar.fw.classBag.db.model.AlteracoesCreditos;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.interfaces.credito.OperacoesCredito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Deposito implements OperacoesCredito<Deposito> {

    private final Credito entrada = new Credito(new TipoCredito(TypeCredito.ENTRADA));
    private final AlteracoesCreditos altenacoesCreditos = new AlteracoesCreditos();
    private BigDecimal saldo;

    public Deposito  valor(BigDecimal valor) {
        this.entrada.setValor(valor);
        return this;
    }

    public Deposito saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public Deposito para(Usuario para) {
        this.entrada.setUsuario(para);
        this.altenacoesCreditos.setParaCredito(this.entrada);
        return this;
    }

    public Deposito realizadoPor(Usuario usuarioLogado) {
        this.altenacoesCreditos.setUsuarioLogado(usuarioLogado);
        return this;
    }

    @Override
    public Credito getSaida() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Credito getEntrada() {
        return this.entrada;
    }

    public AlteracoesCreditos getAltenacoesCreditos() {
        return altenacoesCreditos;
    }

    @Override
    public Deposito builder() {
        if(this.entrada.getValor().compareTo(BigDecimal.ZERO) > 0){
            throw new CreditoException("Informe um valor maior que 0.");
        }
        return this;
    }
}
