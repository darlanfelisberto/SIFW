package br.edu.iffar.fw.classBag.interfaces.credito;

import br.edu.iffar.fw.classBag.db.model.AlteracoesCreditos;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Deposito;

import java.math.BigDecimal;

public interface OperacoesCredito<T> {

    public T valor(BigDecimal valor);
    public T saldo(BigDecimal saldo);
    public T para(Usuario para);
    public T realizadoPor(Usuario usuario);

    public Credito getSaida();
    public Credito getEntrada();

    public AlteracoesCreditos getAltenacoesCreditos();

    public T builder();
}
