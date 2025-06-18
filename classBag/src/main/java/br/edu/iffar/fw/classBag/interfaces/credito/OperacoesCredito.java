package br.edu.iffar.fw.classBag.interfaces.credito;

import br.edu.iffar.fw.classBag.db.model.AlteracoesCreditos;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Deposito;

import java.math.BigDecimal;

public interface OperacoesCredito {

    public OperacoesCredito valor(BigDecimal valor);
    public OperacoesCredito saldo(BigDecimal saldo);
    public OperacoesCredito para(Usuario para);
    public OperacoesCredito realizadoPor(Usuario usuario);

    public AlteracoesCreditos getAltenacoesCreditos();
}
