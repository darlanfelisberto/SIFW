package br.edu.iffar.fw.ru.bean.fragment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import br.edu.iffar.fw.classBag.bo.CreditosBO;
import br.edu.iffar.fw.classBag.db.model.*;
import br.edu.iffar.fw.classBag.enun.TypeCredito;
//import org.primefaces.model.charts.pie.PieChartModel;
import jakarta.enterprise.context.Dependent;
import org.primefaces.model.menu.BaseMenuModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuItem;

import br.edu.iffar.fw.classBag.db.dao.CreditosDAO;
import br.edu.iffar.fw.classBag.db.dao.MatriculaDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import software.xdev.chartjs.model.charts.LineChart;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.LineData;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.LineDataset;
import software.xdev.chartjs.model.dataset.PieDataset;
import software.xdev.chartjs.model.options.LineOptions;
import software.xdev.chartjs.model.options.Plugins;
import software.xdev.chartjs.model.options.Title;
import software.xdev.chartjs.model.options.elements.Fill;

@Dependent
public class SaldoUserFrament implements Serializable {

    private static final long serialVersionUID = 22021991L;

    @Inject
    private CreditosDAO creditosDAO;

    @Inject
    private MatriculaDAO matriculaDAO;

    private BaseMenuModel tabMenuModel;

    private int activeindex;
    private String activeIndexLabel;
    private String pieModel;

    private List<Credito> listCreditos;
    private List<Matricula> listMatricula;

    private Usuario user = null;

    private Saldo saldo;

    @PostConstruct
    public void gerarMeses() {
    }

    /**
     * @param user
     * @param beanPath é o path do bean + o nome do objeto do tipo SaldoUserFrament, infelismente isso foi necessario,
     *                 pois aparentemente ocorre a perda da referencia na renderização.
     */
    public void init(Usuario user, String beanPath) {
        this.tabMenuModel = new BaseMenuModel();
        this.user = user;
        this.saldo = this.creditosDAO.findSaldo(user);

        this.listMatricula = this.matriculaDAO.listAllMatriculaByUsuario(user);

        LocalDate hoje = LocalDate.now();

        int anoAtual = hoje.getYear();
        Locale portugese = new Locale("pt", "BR");

        this.selectTab(0, hoje.getMonthValue(), hoje.getYear());

        for (int i = 0; i < 12; ++i) {
            String mes = hoje.getMonth().getDisplayName(TextStyle.SHORT, portugese);
            String title = mes.substring(0, 1).toUpperCase().concat(mes.substring(1, 3)) + (hoje.getYear() == anoAtual ? "" : "/" + String.valueOf(hoje.getYear()).substring(2, 4));

            MenuItem m = DefaultMenuItem.builder()
                    .value(title)
//					.command("#{bean[saldoUserF][selectTabc]}")
                    .command(String.format("#{%s.selectTab(%d,%d,%d)}", beanPath, i, hoje.getMonthValue(), hoje.getYear()))
                    .update("frmMain:pnlMovimentacoes")
                    .process("@this")
                    .build();
            tabMenuModel.getElements().add(m);
            hoje = hoje.minusMonths(1);
        }
    }

    public void selectTabc() {
        System.out.println("passou");
    }

    public void selectTab(int index, int mes, int ano) {
        LocalDate data = LocalDate.of(ano, mes, 1);
        Locale portugese = new Locale("pt", "BR");
        this.activeindex = index;
        this.activeIndexLabel = data.getMonth().getDisplayName(TextStyle.SHORT_STANDALONE, portugese) + ano;

        this.listCreditos = creditosDAO.getCreditosByMesAno(data, this.user);

        this.pieModel = CreditosBO.chartPie(this.listCreditos);

    }

    public String getActiveIndexLabel() {
        return activeIndexLabel;
    }

    public int getActiveIndex() {
        return this.activeindex;
    }

    public BaseMenuModel getTabMenuModel() {
        return tabMenuModel;
    }

    public void setTabMenuModel(BaseMenuModel tabMenuModel) {
        this.tabMenuModel = tabMenuModel;
    }

    public int getActiveindex() {
        return activeindex;
    }

    public void setActiveindex(int activeindex) {
        this.activeindex = activeindex;
    }

    public String getPieModel() {
        return this.pieModel;
    }

    public List<Credito> getListCreditos() {
        return listCreditos;
    }

    public void setListCreditos(List<Credito> listCreditos) {
        this.listCreditos = listCreditos;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Saldo getSaldo() {
        return this.saldo;
    }

    public void setActiveIndexLabel(String activeIndexLabel) {
        this.activeIndexLabel = activeIndexLabel;
    }

    public boolean isRenSemInfo() {
        return (this.listCreditos == null || this.listCreditos.isEmpty());
    }

    public List<Matricula> getListMatricula() {
        return listMatricula;
    }
}
