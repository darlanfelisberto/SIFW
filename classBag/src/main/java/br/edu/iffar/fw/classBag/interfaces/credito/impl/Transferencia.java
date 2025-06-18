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

public class Transferencia implements OperacoesCredito {

    Credito credito = new Credito(new TipoCredito(TypeCredito.TRANS_ENTRADA));
    AlteracoesCreditos altenacoesCreditos = new AlteracoesCreditos();
    BigDecimal saldo;

    public OperacoesCredito valor(BigDecimal valor) {
        this.credito.setValor(valor);
        this.credito.setDtCredito(LocalDateTime.now());
        return this;
    }

    @Override
    public OperacoesCredito saldo(BigDecimal saldo) {
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

    public OperacoesCredito operacaoRealizadoPor(Usuario usuario){
        return this;
    }

    public AlteracoesCreditos getAltenacoesCreditos(){
        throw new CreditoException("Operacao não suporta esta funcionalidade");
    }
}
