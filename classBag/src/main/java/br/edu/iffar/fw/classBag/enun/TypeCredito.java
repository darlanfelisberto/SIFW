package br.edu.iffar.fw.classBag.enun;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;

public enum TypeCredito {
	ENTRADA			(TypeCredito.E,"money-bill"),
	SAIDA			(TypeCredito.S,"shopping-cart"),
	RETIRADA		(TypeCredito.R,"exclamation-triangle"),
	TRANS_SAIDA		(TypeCredito.TS,"send"),
	TRANS_ENTRADA	(TypeCredito.TE,"money-bill");
	
	private int index;
	private String icon;
	
	public static final int E = 0;
	public static final int S = 1;
	public static final int R = 4;
	
	public static final int TS = 2;
	public static final int TE = 3;


	private TypeCredito(int index,String icon) {
		this.index = index;
		this.icon = icon;
	}

	public void sumType(Credito c,Float[] sum) {
		sum[index]+= c.getValor();
	}
	
	public int getIndex() {
		return this.index;
	}
		
	
	public TipoCredito createIntance() {
		return new TipoCredito(this);
	}
	

	public String getIcon() {
		return icon;
	}

	public static PieChartModel getPieChartModel(Float[] sum,Set<TipoCredito> listTipoCredito) {
//		float total = Arrays.asList(sum).stream().reduce(0f,Float::sum);
    	
    	PieChartModel pieModel = new PieChartModel();
        ChartData data = new ChartData();
        
        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        listTipoCredito.forEach(tc ->{
        	values.add(sum[tc.getTipoCreditoId().getIndex()]);
        	bgColors.add(tc.getCor());
        	labels.add(tc.getDescricao());
        });
        
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);
        
        data.addChartDataSet(dataSet);
        data.setLabels(labels);
        
        pieModel.setData(data);
        
        return pieModel;
			
	}
	
	
}
