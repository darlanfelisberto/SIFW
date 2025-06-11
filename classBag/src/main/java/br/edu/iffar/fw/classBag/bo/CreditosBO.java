package br.edu.iffar.fw.classBag.bo;

import br.edu.iffar.fw.classBag.db.dao.AltenacoesCreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.CreditosDAO;
import br.edu.iffar.fw.classBag.db.model.*;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.excecoes.CreditoException;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Transactional;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.PieDataset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RequestScoped
@Transactional
public class CreditosBO {

    @Inject private AltenacoesCreditosDAO altenacoesCreditosDAO;
    @Inject private CreditosDAO creditosDAO;
    @Inject private MessagesUtil mesUtil;

    Credito credito = new Credito();
    AlteracoesCreditos altenacoesCreditos = new AlteracoesCreditos();

    public CreditosBO valor(BigDecimal valor) {
        this.credito.setValor(valor);
        this.credito.setDtCredito(LocalDateTime.now());
        return this;
    }

    public CreditosBO para(Usuario para) {
        this.credito.setUsuario(para);
        this.altenacoesCreditos.setParaCredito(credito);
        return this;
    }

    public CreditosBO usuarioLogado(Usuario usuarioLogado) {
        this.altenacoesCreditos.setUsuarioLogado(usuarioLogado);
        return this;
    }

    public CreditosBO retirada(boolean total) throws CreditoException {
        Saldo saldo = this.creditosDAO.findSaldo(this.credito.getUsuario());

        if(!(saldo.getSaldo().floatValue() > 0)) {
            throw new CreditoException("Saldo deve ser maior que zero.");
        }
        if(total) {
            credito.setValor(saldo.getSaldo());
        }else {
            //retirada parcial
            if ((saldo.getSaldo().compareTo(this.credito.getValor())) < 0) {
                throw new CreditoException("Saldo insufíciente para retirada.");
            }
        }

        TypeCredito.RETIRADA.inicializaTipoCredito(credito);

        return this;
    }
//
//    public CreditosBO criaAltenacoesCreditos(Usuario usuarioLogado,Usuario usuarioPara) throws CreditoException {
//        altenacoesCreditos.setUsuarioLogado(usuarioLogado);
//        altenacoesCreditos.setParaCredito(this.credito);
//        return this;
//    }

    public CreditosBO entrada() throws CreditoException {
        TypeCredito.ENTRADA.inicializaTipoCredito(credito);
        return this;
    }

    public void saveAltenacoesCreditos() throws CreditoException {
        try {
            this.altenacoesCreditosDAO.persistT(altenacoesCreditos);
            mesUtil.addSuccess("Os créditos foram retirados com sucesso.");
        }catch (RollbackException e) {
            mesUtil.addError(e);
            e.printStackTrace();
            throw new CreditoException("Lamento, mas não consegui adicionar os créditos nesse instante, tente novamente mais tarde.");
        }catch (Exception e) {
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
