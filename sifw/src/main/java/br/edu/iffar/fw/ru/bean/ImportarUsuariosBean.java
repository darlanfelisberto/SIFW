package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.edu.iffar.fw.classBag.interfaces.ImportarUsuariosImpl;
import jakarta.faces.event.AjaxBehaviorEvent;
import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
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


	private boolean rendSeleciona;
	private boolean rendRunImpotacao;
	private boolean rendConfigTypeCSV;


	private BreadCrumb breadCrumb;

	private List<SelectItem> listSelectIten = new ArrayList<SelectItem>();
	private Class tipoImportacaoSelecionada;

	private ImportarUsuariosImpl importarUsuarios;


	@PostConstruct
	private void init() {
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


	public ImportarUsuariosImpl getImportarUsuarios() {
		return importarUsuarios;
	}

	public boolean isRendRunImpotacao() {
		return rendRunImpotacao;
	}

	public boolean isRendSeleciona() {
		return rendSeleciona;
	}

	public List<SelectItem> getListSelectIten() {
		return listSelectIten;
	}

	public void initCreateUsers() {

		System.out.println("continuou");
    }
	public boolean isRendConfigTypeCSV() {
		return (this.tipoImportacaoSelecionada != null && this.tipoImportacaoSelecionada.equals(CSVImportUsuarios.class));
	}

	public String getSaidaTextoImportUser() {
		return this.saidaTextoImportUser.toString();
	}

	@Override
	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}


	public Class getTipoimportacao() {
		return this.tipoImportacaoSelecionada;
	}

	public void setTipoimportacao(Class tipoimportacao) {
		this.tipoImportacaoSelecionada = tipoimportacao;
	}

}
