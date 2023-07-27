package br.edu.iffar.fw.comendo.bean;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.omnifaces.util.selectitems.SelectItemsBuilder;
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
	@Inject private Instance<BackgroundUserCreate> insbuc;
	@Inject private CursosDAO cursosDAO;
	@Inject private HasRoleBean hasRoleBean;

	private StringBuffer saidaTextoImportUser = new StringBuffer();

	private UploadedFile file;
	private CSVParser parcer;
	private List<CSVRecord> listRescord;
	private List<Curso> listCurso;

	private boolean rendBusca;
	private boolean rendCreateUsers;
	private int colunaRole;
	private boolean possuiRole = false;
	private boolean inativarMatriculasAusentes = false;
	private int tipoBusca = 3;
	private Curso curso;
	private BreadCrumb breadCrumb;

	private char delimitadorColuna;
	private Character delimitadorTexto;
	private String codificacaoArquivo;

	private int firstRecrd = 1;

	private List<SelectItem> listSelectIten = new ArrayList<SelectItem>();

	@PostConstruct
	private void init() {
		if(hasRoleBean.isHasIffarAdmin()) {
			this.listSelectIten.add(new SelectItem(2, "Importar Usuários"));
			this.listSelectIten.add(new SelectItem(3, "Gerar Carterinhas"));
		}

		this.createBreadCrumb();
		this.telaFiltroBusca();
	}

	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
				.addItem("Busca de usuário", "#{importarUsuariosBean.telaFiltroBusca()}", "frmMain", BreadCrumb.RAIZ)// 1
				.addItem("Importar usuários", "#{importarUsuariosBean.telaShowDataFile()}", "frmMain", 1)// 2
				.addItem("Dados do usuário", "#{importarUsuariosBean.telaDadosUsuario()}", "frmMain", 1);// 3
			;
	}

	public void telaFiltroBusca() {
		this.rendBusca = true;
		this.rendCreateUsers = false;
		this.breadCrumb.setAtivo(1);
	}

	public void telaShowDataFile() {
		this.rendBusca = false;
		this.rendCreateUsers = true;
		this.breadCrumb.setAtivo(2);
	}

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
    	try {
	    	this.parcer = CSVFormat.DEFAULT
	    			.withDelimiter(this.delimitadorColuna)
	    			.withQuote(delimitadorTexto)
	    			.parse(new InputStreamReader(this.file.getInputStream(),this.codificacaoArquivo));

	        this.listRescord = this.parcer.getRecords();
	        this.listCurso = this.cursosDAO.listAllCursos();
	        this.telaShowDataFile();
    	} catch (IOException e) {
    		messages.addSuccess("Erro de parse no arquivo");
			e.printStackTrace();
			this.telaFiltroBusca();
		}
    }

	public void initCreateUsers() {

		BackgroundUserCreate buc =  insbuc.get();
		buc.setDados(this.listRescord,this.saidaTextoImportUser,this.curso,(this.possuiRole?this.colunaRole:-1),this.inativarMatriculasAusentes,this.firstRecrd);
		mes.execute(buc);
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

	public boolean isRendBusca() {
		return rendBusca;
	}

	public boolean isRendCreateUsers() {
		return rendCreateUsers;
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

	@NotNull(message = "Seleciona um curso.")
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
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


}
