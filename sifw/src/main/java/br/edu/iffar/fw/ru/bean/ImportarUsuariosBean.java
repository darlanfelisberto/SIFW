package br.edu.iffar.fw.ru.bean;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.iffar.fw.classBag.interfaces.ImportarUsuarios;
import br.edu.iffar.fw.classBag.interfaces.ImportarUsuariosImpl;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.ValueChangeEvent;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.data.io.CSV;
import org.omnifaces.util.selectitems.SelectItemsBuilder;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.component.log.Log;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.sec.HasRoleBean;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.inject.Instance;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotNull;

@Named
@ViewScoped
public class ImportarUsuariosBean implements Serializable, BreadCrumbControl {

	private static final long serialVersionUID = 22021991L;

	@Inject private MessagesUtil messages;

	@Resource private ManagedExecutorService mes;
	@Inject private Instance<ImportarUsuariosImpl> insbuc;
	@Inject private CursosDAO cursosDAO;
	@Inject private HasRoleBean hasRoleBean;

	private StringBuffer saidaTextoImportUser = new StringBuffer();

	private UploadedFile file;
	private CSVParser parcer;
	private List<CSVRecord> listRescord;
	private List<Curso> listCurso;

	private boolean rendSeleciona;
	private boolean rendRunImpotacao;
	private boolean rendConfigTypeCSV;

	private int colunaRole;
	private boolean possuiRole = false;
	private boolean inativarMatriculasAusentes = false;
	private int tipoBusca = 3;

	private BreadCrumb breadCrumb;

	private char delimitadorColuna;
	private Character delimitadorTexto;
	private String codificacaoArquivo;

	private int firstRecrd = 1;


	private List<SelectItem> listSelectIten = new ArrayList<SelectItem>();
	private Class tipoImportacaoSelecionada;

	private ImportarUsuariosImpl importarUsuarios;


	@PostConstruct
	private void init() {

		this.listCurso = this.cursosDAO.listAllCursos();

		this.listSelectIten.add(new SelectItem(null,""));
		this.listSelectIten.add(new SelectItem(CSVImportUsuarios.class,CSVImportUsuarios.class.getCanonicalName()));

		this.createBreadCrumb();
		this.telaSelecionaTipoImportacao();

	}

	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
				.addItem("Busca de usuário", "#{importarUsuariosBean.telaFiltroBusca()}", "frmMain", BreadCrumb.RAIZ)// 1
				.addItem("Importar usuários", "#{importarUsuariosBean.telaShowDataFile()}", "frmMain", 1)// 2
				.addItem("Dados do usuário", "#{importarUsuariosBean.telaDadosUsuario()}", "frmMain", 1);// 3
			;
	}

	public void eventChange(AjaxBehaviorEvent event) {
		System.out.println(event);

		if(this.tipoImportacaoSelecionada == null) {
			this.messages.addError("Selecione um tipo de importação de usuários");
			return;
		}

        this.importarUsuarios = insbuc.select((Class<? extends ImportarUsuariosImpl>) this.tipoImportacaoSelecionada).get();

//		this.importarUsuarios.initConfigs();
//		iui.setDados(this.saidaTextoImportUser,this.curso,(this.possuiRole?this.colunaRole:-1),this.inativarMatriculasAusentes,this.firstRecrd);
//		mes.execute(iui);
	}

	public void telaSelecionaTipoImportacao() {
		this.rendSeleciona = true;
		this.rendConfigTypeCSV = false;
		this.breadCrumb.setAtivo(1);
	}

	public void telaConfigTipoImportacao() {
		this.importarUsuarios.initConfigs();
	}

	public void telaShowDataFile() {
		this.rendSeleciona = false;
		this.rendConfigTypeCSV = false;
		this.breadCrumb.setAtivo(2);
	}

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		//application code
	}

    public void upload(FileUploadEvent event) {

		UploadedFile upload = (UploadedFile) event.getFile();
//    	try {
//	    	this.parcer = CSVFormat.DEFAULT
//	    			.withDelimiter(this.delimitadorColuna)
//	    			.withQuote(delimitadorTexto)
//	    			.parse(new InputStreamReader(upload.getInputStream(),this.codificacaoArquivo));
//
//	        this.fileImport.listRescord = this.parcer.getRecords();
//			this.fileImport.nomeArquivo = upload.getFileName();
//			this.listFileImport.add(this.fileImport);
//			this.fileImport = new FileImport();
//			messages.addSuccess("Arquivo enviado.");
//    	} catch (IOException e) {
//    		messages.addSuccess("Erro de parse no arquivo");
//			e.printStackTrace();
//		}
    }

	public void initCreateUsers() {



		System.out.println("continuou");
    }

    public List<SelectItem> getListDelimitatadorColuna() {
    	return new SelectItemsBuilder().add(",", ",").add(";", ";").add("|", "|").buildList();
    }

    public List<SelectItem> getListCodificacaoArquivo() {
    	return new SelectItemsBuilder().add("WINDOWS-1252", "WINDOWS-1252").add("ISO-8859-1", "ISO-8859-1").add("UTF-8", "UTF-8").buildList();
    }

    public List<SelectItem> getListDelimitadorTexto() {
    	return new SelectItemsBuilder().add(null, "Não usar").add("\"", "\"").add("\'", "\'").buildList();
    }

	public int getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(int tipoBusca) {
		this.tipoBusca = tipoBusca;
	}

	public boolean isRendSeleciona() {
		return this.rendSeleciona;
	}
//
	public boolean isRendConfigTypeCSV() {
		return (this.tipoImportacaoSelecionada != null && this.tipoImportacaoSelecionada.equals(CSVImportUsuarios.class));
	}

	public String getSaidaTextoImportUser() {
		return this.saidaTextoImportUser.toString();
	}

	public int getSizeListRecord() {
		if(this.listRescord == null)
			return 0;
		return this.listRescord.size();
	}

	public List<Curso> getListCurso() {
		return listCurso;
	}

	public void setListCurso(List<Curso> listCurso) {
		this.listCurso = listCurso;
	}

	public int getColunaRole() {
		return colunaRole;
	}

	public void setColunaRole(int colunaRole) {
		this.colunaRole = colunaRole;
	}

	public boolean isPossuiRole() {
		return possuiRole;
	}

	public void setPossuiRole(boolean possuiRole) {
		this.possuiRole = possuiRole;
	}

	public boolean isInativarMatriculasAusentes() {
		return inativarMatriculasAusentes;
	}

	public void setInativarMatriculasAusentes(boolean inativarMatriculasAusentes) {
		this.inativarMatriculasAusentes = inativarMatriculasAusentes;
	}

	@Override
	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}

	public List<SelectItem> getListSelectIten() {
		return listSelectIten;
	}

	public char getDelimitadorColuna() {
		return delimitadorColuna;
	}

	public void setDelimitadorColuna(char delimitadorColuna) {
		this.delimitadorColuna = delimitadorColuna;
	}

	public Character getDelimitadorTexto() {
		return delimitadorTexto;
	}

	public void setDelimitadorTexto(Character delimitadorTexto) {
		this.delimitadorTexto = delimitadorTexto;
	}

	public void setCodificacaoArquivo(String codificacaoArquivo) {
		this.codificacaoArquivo = codificacaoArquivo;
	}

	public String getCodificacaoArquivo() {
		return codificacaoArquivo;
	}

	public int getFirstRecrd() {
		return firstRecrd;
	}

	public void setFirstRecrd(int firstRecrd) {
		this.firstRecrd = firstRecrd;
	}


	public Class getTipoimportacao() {
		return this.tipoImportacaoSelecionada;
	}

	public void setTipoimportacao(Class tipoimportacao) {
		this.tipoImportacaoSelecionada = tipoimportacao;
	}

	public boolean isRendRunImpotacao() {
		return rendRunImpotacao;
	}


}
