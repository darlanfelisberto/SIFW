package br.edu.iffar.relatorios;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RequestScoped
public class RelatoriosPath {
	
	//open
	public static final String JASPER_ALL_QRCODE = "RU/carterinha/principal.jasper";
	public static final String JASPER_SUB_MY_QRCODE = "br/edu/iffar/relatorios/RU/carterinha/subCarterinha.jasper";
	
	//open
	public static final String JASPER_MOVIMENTACOES_DIARIAS = "RU/admin/movimentacoes_diarias.jasper";
	public static final String JASPER_TOTAL_ENTRADAS = "RU/admin/totalEntradas/total_entradas.jasper";
	public static final String JASPER_ENTRADAS = "RU/admin/entradas.jasper";
	
	public static final String JASPER_AGENDAMENTOS_INTEGRALIZADO = "RU/admin/integralizado/agendamentos_integralizados.jasper";
	public static final String JASPER_AGENDAMENTOS_FRUSTRADOS = "RU/admin/agendamentos_frustrados.jasper";
	public static final String JASPER_TRANSFERENCIA_AGENDAMENTO = "RU/admin/alteracoesAgendamentos.jasper";
	
	public static final String JASPER_CHAMADA_MORADIA = "RU/moradia/relatorioPresencasAlunosMoradia.jasper";	
	
	static public final String PATH_IMAGEN = "/opt/app/imagens/";// aqui é no servidor
	static public final String EXTENSAO =".png";
	static public final String JASPER_SUB_HEADER ="br/edu/iffar/relatorios/header.jasper";
	static public final String JASPER_SUB_HEADER_SMALL ="br/edu/iffar/relatorios/header_small.jasper";
		
	@Inject private Connection connection;
	
	static public InputStream get(String name) {
		return RelatoriosPath.class.getResourceAsStream(name);
	} 
	
	private byte[] getByte(Map<String, Object> properts,InputStream jasper) throws Throwable {
		JasperPrint jp = JasperFillManager.fillReport(jasper, properts,this.connection);

		byte[] bytes = JasperExportManager.exportReportToPdf(jp);

		if(!this.connection.isClosed()) {
			this.connection.close();
		}

		return bytes;
	}
	
	public StreamedContent getJasper(String nome, Map<String, Object> properts, String pathJasper ) throws Throwable{
		InputStream jasper =RelatoriosPath.get(pathJasper);
		InputStream b =  new ByteArrayInputStream(this.getByte(properts, jasper));
		StreamedContent relatorio =  DefaultStreamedContent.builder()
		.contentType("application/pdf")
		.name(nome + ".pdf")
		.stream(()->b)
		.build();	
		
		jasper.close();
		b.close();
		return relatorio;
	}
	
	public byte[] getJasper(Map<String, Object> properts, String pathJasper ) throws Throwable {
		InputStream jasper = RelatoriosPath.get(pathJasper);
		byte[] relatorio = this.getByte(properts, jasper); 
		jasper.close();
		return relatorio;
	}
	
	
	public void download(byte[] arquivo,String nome) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
		    ExternalContext externalContext = facesContext.getExternalContext();
		    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		    response.reset();
		    response.setContentType("application/pdf");
		    response.setHeader("Content-disposition", "attachment; filename=\""+nome+".pdf\"");


		    OutputStream output = response.getOutputStream();
		    output.write(arquivo);
		    output.close();

		    facesContext.responseComplete();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

}
