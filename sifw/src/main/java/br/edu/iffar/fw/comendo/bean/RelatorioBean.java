package br.edu.iffar.fw.comendo.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;

import br.edu.iffar.fw.classBag.db.dao.AgendamentosDAO;
import br.edu.iffar.fw.classBag.db.dao.GrupoRefeicoesDAO;
import br.edu.iffar.fw.classBag.db.dao.RelatoriosDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoRefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoVinculoDAO;
import br.edu.iffar.fw.classBag.db.model.GrupoRefeicoes;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.relatorios.RelatoriosPath;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@RequestScoped
public class RelatorioBean implements Serializable{
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private AgendamentosDAO agendamentosDAO; 
	@Inject private TipoRefeicaoDAO tipoRefeicaoDAO;
	@Inject private RelatoriosPath relatoriosPath;	
	@Inject private MessagesUtil messagesUtil;
	@Inject private RelatoriosDAO relatoriosDAO;
	
	@Inject private TipoVinculoDAO tipoVinculoDAO;
	@Inject private GrupoRefeicoesDAO grupoRefeicoesDAO;
	
	private LocalDate dtInicio;
	private LocalDate dtFim;
	
	List<TipoVinculo> listTipoVinculo;
	List<TipoRefeicao> listTipoRefeicao; 
	List<GrupoRefeicoes> listGrupoRefeicoes;
	
	private GrupoRefeicoes grupoRefeicoes;
	private List<TipoVinculo> selecTipoVinculos;
	private TipoRefeicao tipoRefeicao;
	private TipoVinculo tipoVinculo;
	
	
	@PostConstruct
	public void init() {
		LocalDate date = LocalDate.now();
		DayOfWeek day = date.getDayOfWeek();
		int first = day.getValue();
		
		//java trata os dias da semana direfente, domingo seria o 0 
		int last = DayOfWeek.SATURDAY.getValue() - day.getValue();
		
		this.dtInicio = date.minusDays(first);
		this.dtFim = date.plusDays(last);
	}
	
	public void novoPediodo() {
		if(this.getDtFim().isBefore(this.getDtInicio())) {
			this.messagesUtil.addError("Data final não pode ser menor que data de início.");
			return;
		}
	}
	
    public BarChartModel getBarModel() {


		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	BarChartModel barModel = new BarChartModel();
        ChartData data = new ChartData();

        List<String> labels = new ArrayList<String>();
        
        List<TipoRefeicao> tipo = this.tipoRefeicaoDAO.listAll();

        boolean datas = true;
        for (TipoRefeicao tipoRefeicao : tipo) {
        	BarChartDataSet barDataSetJanta = new BarChartDataSet();
            barDataSetJanta.setLabel(tipoRefeicao.getDescricao());
            barDataSetJanta.setBackgroundColor(tipoRefeicao.getBackgroundColor());

            List<Number> days = new ArrayList<Number>();
            barDataSetJanta.setData(days);
            for (Object[] a : agendamentosDAO.getAgendamentoEmDataPeriodo(this.dtInicio, this.dtFim,tipoRefeicao)) {
            	days.add(((Number)a[1]).intValue());
            	if(datas) {
            		labels.add(sdf.format(((Instant)a[0]).atOffset(ZoneOffset.UTC)));
            	}
     		}
            datas = false;
            data.addChartDataSet(barDataSetJanta);
		}
          
        data.setLabels(labels);
        
        barModel.setData(data);
        
		return barModel;
    }

	private Map<String, Object> getMap(){		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header", RelatoriosPath.PATH_JAR_JASPER_SUB_HEADER);
		map.put("dataInicio", Date.valueOf(this.dtInicio));
		map.put("dataFim", Date.valueOf(this.dtFim));
		return map;
	}
    
	public StreamedContent getPdfMovimentacoesDiarias() {
		if(this.dtFim.isBefore(dtInicio)) {
			this.messagesUtil.addError("A data final não pode ser menor que a data inicial.");
			return null;
		}
		
		try {
			Map<String, Object> map = this.getMap();
			return this.relatoriosPath.getJasper("Movimentacoes diarias", map, RelatoriosPath.JASPER_MOVIMENTACOES_DIARIAS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public StreamedContent getPdfTransferenciaDeAgendamentos() {
		if(this.dtFim.isBefore(dtInicio)) {
			this.messagesUtil.addError("A data final não pode ser menor que a data inicial.");
			return null;
		}
		
		try {
			
			Map<String, Object> map = this.filtros("and tr", "and gr"); 
			map.putAll(getMap());
			return this.relatoriosPath.getJasper("transferencia_agendamentos", map, RelatoriosPath.JASPER_TRANSFERENCIA_AGENDAMENTO);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public StreamedContent getPdfTotalEntradasRefeicaoVinculo() {
		if(this.dtFim.isBefore(dtInicio)) {
			this.messagesUtil.addError("A data final não pode ser menor que a data inicial.");
			return null;
		}

		try {
			Map<String, Object> map = this.filtros("and tr", "and gr"); 
			map.putAll(getMap());
			return this.relatoriosPath.getJasper("Total de entrada por refeição e vínculo", map, RelatoriosPath.JASPER_TOTAL_ENTRADAS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public StreamedContent getPdfEntradas() {
		if(this.dtFim.isBefore(dtInicio)) {
			this.messagesUtil.addError("A data final não pode ser menor que a data inicial.");
			return null;
		}

		try {
			Map<String, Object> map = filtros("and tr","and gr"); 
			map.putAll(getMap());
			return this.relatoriosPath.getJasper("Entradas", map, RelatoriosPath.JASPER_ENTRADAS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public StreamedContent getPdfAgendamentosFrustrados() {
		if(this.dtFim.isBefore(dtInicio)) {
			this.messagesUtil.addError("A data final não pode ser menor que a data inicial.");
			return null;
		}
		String nome = "agendamento_frustrados " + dtInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a " + dtFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		try {
			Map<String, Object> map = filtros("and tr","and gr"); 
			map.putAll(this.getMap());
			return this.relatoriosPath.getJasper(nome, map, RelatoriosPath.JASPER_AGENDAMENTOS_FRUSTRADOS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public StreamedContent getPlanilhaAgendamentosFrustrados() {
		if(this.dtFim.isBefore(dtInicio)) {
			this.messagesUtil.addError("A data final não pode ser menor que a data inicial.");
			return null;
		}

		try {
			String nome = "Agendamento frustrados " + dtInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a " + dtFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(baos);
			
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Data","Nome","Tipo Refeição","Grupo", "Curso/Vínculo"));
			
			Map<String, Object> map = filtros("and tr","and gr"); 
			relatoriosDAO.listAllAgendamentoFrustrads(printer, dtInicio, dtFim,map);
			
			printer.printRecord(map.get("filtros"));
			printer.flush();
			
			InputStream i = new ByteArrayInputStream(baos.toByteArray());
			
			StreamedContent relatorio =  DefaultStreamedContent.builder()
					.contentType("application/csv")
					.name(nome + ".csv")
					.stream(() -> i)
					.build();	
			
			return relatorio;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public StreamedContent getRelatorioIntegralizados(){
		if(this.dtFim.isBefore(dtInicio)) {
			this.messagesUtil.addError("A data final não pode ser menor que a data inicial.");
			return null;
		}
		String nome = "age_integralizado_" + dtInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "_a_" + dtFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		try {
			Map<String, Object> map = filtros("where tipoR","and gr");
			map.put("header", RelatoriosPath.PATH_JAR_JASPER_SUB_HEADER_SMALL);
			map.put("dataInicio", Date.valueOf(this.dtInicio));
			map.put("dataFim", Date.valueOf(this.dtFim));
			return this.relatoriosPath.getJasper(nome, map, RelatoriosPath.JASPER_AGENDAMENTOS_INTEGRALIZADO);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Object> filtros(String aliasTipoRefeicao,String aliasGrupoRefeicao) {
		StringBuilder whereAdicional = new StringBuilder(" ");
		StringBuilder whereTipoRefeicao = new StringBuilder(" ");
		StringBuilder filtros = new StringBuilder("Filtros: ");
		
		if(this.tipoRefeicao!= null) {
			whereTipoRefeicao.append(" "+aliasTipoRefeicao + ".tipo_refeicao_id = '" + this.tipoRefeicao.getMMId() + "' ");
			filtros.append("Tipo Refeicao("+this.tipoRefeicao.getDescricao()+");");
		}
		
		if(this.tipoVinculo!= null) {
			whereAdicional.append(" and (tvM.tipo_vinculo_id ='" + this.tipoVinculo.getMMId()+ 
					"' or tvS.tipo_vinculo_id ='" + this.tipoVinculo.getMMId()+ "') ");
			filtros.append("Tipo Vínculo("+this.tipoVinculo.getDescricao()+");");
		}
		
		if(this.grupoRefeicoes != null) {
			whereAdicional.append( " "+aliasGrupoRefeicao + ".grupo_refeicoes_id = '" + this.grupoRefeicoes.getMMId() + "' ");
			filtros.append("Grupo de Refeição("+this.grupoRefeicoes.getDescricao()+");");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("where_adicional", whereAdicional.toString());
		map.put("where_tipo_refeicao", whereTipoRefeicao.toString());
		map.put("filtros", filtros.toString());
		
		return map;
	}
//
//	public void valida() {
//		if(this.dtFim != null && this.dtInicio != null) {
//			if(this.dtFim.isBefore(this.dtInicio)) {
//				this.messagesUtil.addError("Data de fim é maior que a data de Início");
//			}
//		}
//	}

	public LocalDate getDtInicio() {
		return dtInicio;
	}

	public LocalDate getDtFim() {
		return dtFim;
	}

	public void setDtInicio(LocalDate dtInicio) {
		this.dtInicio = dtInicio;
	}

	public void setDtFim(LocalDate dtFim) {
		this.dtFim = dtFim;
	}

	public List<TipoVinculo> getListTipoVinculo() {
		if(this.listTipoVinculo == null) {
			this.listTipoVinculo = this.tipoVinculoDAO.listAll();
		}
		return listTipoVinculo;
	}

	public List<GrupoRefeicoes> getListGrupoRefeicoes() {
		if(this.listGrupoRefeicoes == null) {
			this.listGrupoRefeicoes = this.grupoRefeicoesDAO.listAll();
		}
		return listGrupoRefeicoes;
	}
	
	public List<TipoRefeicao> getListTipoRefeicao() {
		if(this.listTipoRefeicao == null) {
			this.listTipoRefeicao = this.tipoRefeicaoDAO.listAll();
		}
		return listTipoRefeicao;
	}

	public GrupoRefeicoes getGrupoRefeicoes() {
		return grupoRefeicoes;
	}

	public void setGrupoRefeicoes(GrupoRefeicoes grupoRefeicoes) {
		this.grupoRefeicoes = grupoRefeicoes;
	}
	
//
//	public List<TipoVinculo> getSelecTipoVinculos() {
//
//		return selecTipoVinculos;
//	}

//	public void setSelecTipoVinculos(List<TipoVinculo> selecTipoVinculos) {
//		this.selecTipoVinculos = selecTipoVinculos;
//	}

	public TipoRefeicao getTipoRefeicao() {
		return tipoRefeicao;
	}

	public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
		this.tipoRefeicao = tipoRefeicao;
	}

	public TipoVinculo getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(TipoVinculo tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}
}