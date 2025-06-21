package br.edu.iffar.fw.classBag.bo;

import br.edu.iffar.fw.classBag.db.dao.AgendamentosDAO;
import br.edu.iffar.fw.classBag.db.dao.AltenacoesCreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.CreditosDAO;
import br.edu.iffar.fw.classBag.db.model.*;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.interfaces.credito.OperacoesCredito;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Deposito;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Pagamento;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Retirada;
import br.edu.iffar.fw.classBag.interfaces.credito.impl.Transferencia;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.PieDataset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RequestScoped
public class CreditosDepositoBO {

    @Inject private AltenacoesCreditosDAO altenacoesCreditosDAO;
    @Inject private CreditosDAO creditosDAO;
    @Inject private AgendamentosDAO agendamentosDAO;
    @Inject private MessagesUtil mesUtil;
	@Inject private UserTransaction userTransaction;

    @Transactional
    public void saveOperacaoCredito(OperacoesCredito<?> operacoesCredito) throws CreditoException {
        try {
            this.altenacoesCreditosDAO.persistT(operacoesCredito.getAltenacoesCreditos());
            mesUtil.addSuccess("Os créditos foram salvos com sucesso.");
        }catch (RollbackException e) {
            mesUtil.addError(e);
            e.printStackTrace();
            throw new CreditoException("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
        }catch (Exception e) {
            e.printStackTrace();
            throw new CreditoException("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
        }
    }

    @Transactional
    public void savePagamento(Pagamento pagamento) throws CreditoException,RollbackException {
        this.creditosDAO.persist(pagamento.getSaida());
        //foi optado por não bloquear quem nao tenha agendamento, mas tbm de naão permitir,kkkk
        if(!pagamento.getSaida().getAgendamento().isNovo()) {
            this.agendamentosDAO.merge(pagamento.getSaida().getAgendamento());
        }else{
            this.agendamentosDAO.persist(pagamento.getSaida().getAgendamento());
        }
        this.altenacoesCreditosDAO.persistT(pagamento.getAltenacoesCreditos());
    }

    public void saveOperacaoCredito(Transferencia trans) throws CreditoException, SystemException {
        try {
            this.userTransaction.begin();

				creditosDAO.persist(trans.getEntrada());
//
				creditosDAO.persist(trans.getSaida());
                trans.getEntrada().setParent(trans.getSaida());
                creditosDAO.merge(trans.getEntrada());
//
//				this.transSub.entrada.setParent(this.transSub.saida);
//				creditosDAO.merge(this.transSub.entrada);

            this.userTransaction.commit();
            mesUtil.addSuccess("Os créditos foram salvos com sucesso.");
        }catch (RollbackException e) {
            this.userTransaction.rollback();
            mesUtil.addError(e);
            e.printStackTrace();
            throw new CreditoException("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
        }catch (Exception e) {
            this.userTransaction.rollback();
            e.printStackTrace();
            throw new CreditoException("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
        }
    }

    public static String chartPie(List<Credito> listCreditos){
        BigDecimal[] sum = new BigDecimal[TypeCredito.values().length];

        Set<TipoCredito> tiposCreditos = new HashSet<TipoCredito>();

        Arrays.fill(sum, BigDecimal.ZERO);

        if (listCreditos != null) {
            listCreditos.forEach(c -> {
                tiposCreditos.add(c.getTipoCredito());
                c.getTipoCredito().getTipoCreditoId().sumType(sum, c);
            });
        } else {
            return "";
        }

        List<String> labels = new ArrayList<>();
        List<Number> somatorio = new ArrayList<>();
        List<RGBAColor> colors = new ArrayList<>();
        tiposCreditos.forEach(tipo -> {
            labels.add(tipo.getDescricao());

            somatorio.add(sum[tipo.getTipoCreditoId().getIndex()]);

            String[] rgb = tipo.getCor().split(",");
            colors.add(new RGBAColor(Integer.valueOf(rgb[0]), Integer.valueOf(rgb[1]), Integer.valueOf(rgb[2])));
        });

        return new PieChart()
                .setData(new PieData()
                        .addDataset(new PieDataset()
                                .setData(somatorio)
                                .setLabel("Movimentações")
                                .addBackgroundColors(colors)
                        )
                        .setLabels(labels))
                .toJson();
    }
}
