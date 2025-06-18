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

    Credito credito = new Credito(new TipoCredito(TypeCredito.ENTRADA));
    AlteracoesCreditos altenacoesCreditos = new AlteracoesCreditos();
    BigDecimal saldo;

    public Deposito  valor(BigDecimal valor) {
        this.credito.setValor(valor);
        return this;
    }

    public Deposito saldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public Deposito para(Usuario para) {
        this.credito.setUsuario(para);
        this.altenacoesCreditos.setParaCredito(credito);
        return this;
    }

    public Deposito realizadoPor(Usuario usuarioLogado) {
        this.altenacoesCreditos.setUsuarioLogado(usuarioLogado);
        return this;
    }

    public AlteracoesCreditos getAltenacoesCreditos() {
        return altenacoesCreditos;
    }

    @Override
    public Deposito builder() {
        if(this.credito.getValor().compareTo(BigDecimal.ZERO) > 0){
            throw new CreditoException("Informe um valor maior que 0.");
        }
        return this;
    }
}
