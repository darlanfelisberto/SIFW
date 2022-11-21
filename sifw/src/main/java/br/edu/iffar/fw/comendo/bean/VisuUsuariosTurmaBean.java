package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import br.edu.iffar.fw.classBag.db.dao.ImagenDAO;
import br.edu.iffar.fw.classBag.db.dao.TurmaDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Imagen;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Turma;
import br.edu.iffar.fw.classBag.sec.HasRoleBean;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;

@Named
@ViewScoped
public class VisuUsuariosTurmaBean implements Serializable, BreadCrumbControl {
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private MessagesUtil messages;

	@Inject private UsuariosDAO usuariosDAO;
	@Inject private ImagenDAO imagenDAO;

	@Inject private HasRoleBean hasRoleBean;
	@Inject
	private TurmaDAO turmaDAO;

	private BreadCrumb breadCrumb;

	private List<Integer> listAnosDispniveis;
	private List<Turma> listTurma;
	private Turma turma;
	private int ano;
	
	private boolean rendListMatricula = false;
	private boolean rendDadosMatricula = false;
	private boolean rendAnoTurma = true;
	private boolean rendDadosTurma = false;

	private Integer rowColun;
	private Matricula matricula;
	private Imagen imagen;

	private List<Matricula> listaMatricula;

	@PostConstruct
	private void init() {
		this.createBreadCrumb();
		this.ano = LocalDate.now().getYear();

		this.listAnosDispniveis = this.turmaDAO.listAnosDisponiveis();
		this.listTurma = this.turmaDAO.listAllTurma(this.ano);
//		if(this.listTurma != null || !this.listTurma.isEmpty()) {
//			this.turma = this.turmaDAO.findTurma(this.listTurma.get(0));
//		}
		this.telaAnoTurma();
	}

	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
				.inicializa()
				.addItem("Visu. Alunos de Turma", "visu_aluno_turma.xhtml", BreadCrumb.RAIZ)// 1
				.addItem("Seleciona ano e turma", "#{visuUsuariosTurmaBean.telaAnoTurma()}", "frmMain", 1)// 2
				.addItem("Lista de alunos", "#{visuUsuariosTurmaBean.telaListMatricula()}", "frmMain", 2)// 3
				.addItem("Dados do aluno", "#{visuUsuariosTurmaBean.telaDadosMatricula()}", "frmMain", 3)// 4
		;
	}

	public void changeYear() {
		this.turma = null;
		this.listTurma = this.turmaDAO.listAllTurma(this.ano);
		this.telaAnoTurma();
	}

	public void changeTurma() {
		this.turma = this.turmaDAO.findTurma(this.turma);
		this.listaMatricula = this.turmaDAO.listAllMatricula(this.turma);

		this.telaListMatricula();
	}

	public void selecionaMatricula(int row) {
		this.rowColun = row;
		
		this.matricula = this.listaMatricula.get(this.rowColun);
		
		this.imagen = this.imagenDAO.findByUsuarioIfNullPattern(this.matricula.getUsuario());
		this.telaDadosMatricula();
	}

	public void telaListMatricula() {
		this.rendListMatricula = true;
		this.rendDadosMatricula = false;
		this.rendAnoTurma = false;
		this.rendDadosTurma = true;
		this.breadCrumb.setAtivo(3);
	}

	public void telaDadosMatricula() {
		this.rendListMatricula = false;
		this.rendDadosMatricula = true;
		this.rendAnoTurma = false;
		this.rendDadosTurma = true;
		this.breadCrumb.setAtivo(4);
	}

	public void telaAnoTurma() {
		this.rendListMatricula = false;
		this.rendDadosMatricula = false;
		this.rendAnoTurma = true;
		this.rendDadosTurma = false;
		this.breadCrumb.setAtivo(2);
	}

	public void proximo() {
		if(this.rowColun < (this.listaMatricula.size() - 1)) {
			this.rowColun++;
			this.matricula = this.listaMatricula.get(this.rowColun);
			this.imagen = this.imagenDAO.findByUsuarioIfNullPattern(this.matricula.getUsuario());
		} else {
			this.messages.addWarn("Última matrícula da turma foi atingida.", "");
		}
	}

	public void anterior() {
		if(this.rowColun == 0) {
			this.messages.addWarn("Essa é a primeira matrícula da turma.", "");
		} else {
			this.rowColun--;
			this.matricula = this.listaMatricula.get(this.rowColun);
			this.imagen = this.imagenDAO.findByUsuarioIfNullPattern(this.matricula.getUsuario());
		}
	}

	public List<Integer> getListAnosDispniveis() {
		return listAnosDispniveis;
	}

	public void setListAnosDispniveis(List<Integer> listAnosDispniveis) {
		this.listAnosDispniveis = listAnosDispniveis;
	}

	public List<Turma> getListTurma() {
		return listTurma;
	}

	public void setListTurma(List<Turma> listTurma) {
		this.listTurma = listTurma;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public boolean isRendListMatricula() {
		return rendListMatricula;
	}

	public boolean isRendDadosMatricula() {
		return rendDadosMatricula;
	}

	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public BreadCrumb getBreadCrumb() {
		return breadCrumb;
	}

	public boolean isRendAnoTurma() {
		return rendAnoTurma;
	}

	public boolean isRendDadosTurma() {
		return rendDadosTurma;
	}

	public List<Matricula> getListaMatricula() {
		return listaMatricula;
	}

}
