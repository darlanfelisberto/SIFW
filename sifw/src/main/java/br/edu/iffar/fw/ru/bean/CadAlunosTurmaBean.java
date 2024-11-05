package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.util.List;

import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.dao.MatriculaDAO;
import br.edu.iffar.fw.classBag.db.dao.TurmaDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Turma;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;
import jakarta.validation.constraints.NotNull;

@Named
@ViewScoped
public class CadAlunosTurmaBean implements Serializable, BreadCrumbControl {
	
	private static final long serialVersionUID = 22021991L;
	
	@Inject private TurmaDAO turmaDAO;
	@Inject private MatriculaDAO matriculaDAO;
	@Inject private MessagesUtil messagesUtil;
	@Inject private CursosDAO cursosDAO;

	
	@NotNull(message = "Informe o ano") private Integer ano;
	
	@NotNull(message = "Informe uma turma") private Turma turma;
	
	private Turma novaTurma;
	
	@NotNull(message = "Informe uma Matricula") private Matricula matricula;
	
	private BreadCrumb breadCrumb;
	
	private List<Integer> listAnos;
	private List<Turma> listTurmas;
	
	private boolean rendBuscaTurma;
	private boolean rendListaAluno;
	private boolean rendCopiaTela;
	private boolean rendAddAluno = false;
	
	@PostConstruct
	public void init() {
		this.createBreadCrumb();
		this.listAnos = this.turmaDAO.listAnosDisponiveis();
		this.telaBuscaAluno();
	}
	
	public void mudaAno() {
		if(this.ano == null) {
			this.listTurmas = null;
			return;
		}
		this.listTurmas = this.turmaDAO.listAllTurma(this.ano);
	}
	
	public void telaBuscaAluno() {
		this.rendBuscaTurma = true;
		this.rendListaAluno = false;
		this.rendCopiaTela = false;
		this.breadCrumb.setAtivo(1);
	}
	
	public void telaListaALuno() {
		this.rendBuscaTurma = false;
		this.rendListaAluno = true;
		this.rendCopiaTela = false;
		this.breadCrumb.setAtivo(2);
		this.matricula = null;
		this.rendAddAluno = false;
	}
	public void telaCopiaTurma() {
		this.rendBuscaTurma = false;
		this.rendListaAluno = false;
		this.rendCopiaTela = true;
		this.breadCrumb.setAtivo(3);
	}
	
	public void buscarTurma() {
		this.turma = this.turmaDAO.buscaTurma(this.turma);
		this.telaListaALuno();
	}
	
	public List<Matricula> buscaMatriculaNome(String nome) {
		return this.matriculaDAO.listMatriculaByLikeNome(nome);
	}
	
	public void addAlunoTurma() {
		this.turma.addMatricula(this.matricula);
		try {
			this.turmaDAO.mergeT(this.turma);
			this.messagesUtil.addSuccess("Aluno adicionado na Turma com sucesso.");
			this.telaListaALuno();
		} catch (RollbackException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Um erro ocorreu.",""));
			this.messagesUtil.addError(e);			
		}
	}

	public void removerAlunoTurma(){
		this.turma.removeMatricula(matricula);
		try {
			this.turmaDAO.mergeT(turma);
			this.messagesUtil.addSuccess("Aluno " + matricula.getIdMatricula() + " removido com sucesso.");
		}catch (RollbackException e) {
			this.messagesUtil.addError("Um erro ocorreu.");
			this.messagesUtil.addError(e);
		}
	}
	
	public void copiaTurmaSelecionada() {
		this.novaTurma = this.turma.clone();
		this.telaCopiaTurma();
	}
	
	public void salvarNovaTurma() {
		try {
			this.turmaDAO.mergeT(novaTurma);
			this.init();
			this.messagesUtil.addSuccess("Turma salva com sucesso!");
		} catch (RollbackException e) {
			this.messagesUtil.addError(e);
		}
		
	}
	
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb().inicializa()//1
				.addItem("Busca Turma", "#{cadAlunosTurmaBean.telaBuscaAluno()}", "@form", 0)//1
				.addItem("Lista Aluno", "#{cadAlunosTurmaBean.telaListaALuno()}", "@form", 1)//2
				.addItem("Copia Turma", "#{cadAlunosTurmaBean.telaCopiaTurma()}", "@form", 2);//3
	}

	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Integer> getListAnos() {
		return listAnos;
	}

	public void setListAnos(List<Integer> listAnos) {
		this.listAnos = listAnos;
	}

	public List<Turma> getListTurmas() {
		return listTurmas;
	}

	public void setListTurmas(List<Turma> listTurmas) {
		this.listTurmas = listTurmas;
	}

	public boolean isRendBuscaTurma() {
		return rendBuscaTurma;
	}

	public boolean isRendListaAluno() {
		return rendListaAluno;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public boolean isRendAddAluno() {
		return rendAddAluno;
	}

	public void setRendAddAluno(boolean rendAddAluno) {
		this.rendAddAluno = rendAddAluno;
	}

	public Turma getNovaTurma() {
		return novaTurma;
	}

	public void setNovaTurma(Turma novaTurma) {
		this.novaTurma = novaTurma;
	}

	public boolean isRendCopiaTela() {
		return rendCopiaTela;
	}
	
	public List<Curso> getListCursos(){
		
		return this.cursosDAO.listAllCursos();
	}
	
	
}
