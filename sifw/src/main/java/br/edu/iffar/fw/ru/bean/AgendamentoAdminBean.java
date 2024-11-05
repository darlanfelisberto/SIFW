package br.edu.iffar.fw.ru.bean;

import java.io.Serializable;
import java.util.List;

import br.edu.iffar.fw.classBag.db.dao.RefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.fw.classBag.util.fragmentos.FragmentoBuscaUsuario;
import br.edu.iffar.fw.classBag.util.fragmentos.SelecionaFragmentoInterface;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class AgendamentoAdminBean implements Serializable, BreadCrumbControl, SelecionaFragmentoInterface {

	private static final long serialVersionUID = 22021991L;
	
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private RefeicaoDAO refeicaoDAO;
	@Inject private HeaderBean headerBean;
	
	@Inject private MessagesUtil messages;
	
	@Inject FragmentoBuscaUsuario fragmentoBuscaUsuario;
		
	private Agendamento agendamento = null;
	
	private Usuario user = null;
	private BreadCrumb breadCrumb;
	private List<Refeicao> listRefeicao;
		
	@PostConstruct
	private void init() {
		this.createBreadCrumb();
		this.user = this.headerBean.getUsuarioLogado();
		this.fragmentoBuscaUsuario.init(this);
		this.telaBusca();
	}
	
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
			.addItem("Agendamento de Refeições", BreadCrumb.RAIZ)//1
			;
		this.breadCrumb.setAtivo(1);
	}	
	
	public void telaBusca() {
		this.fragmentoBuscaUsuario.telaBusca();
	}

	public void telaResultadoBusca() {
		this.fragmentoBuscaUsuario.telaResultadoBusca();
	}
	
	public void telaDadosUsuario() {
		this.fragmentoBuscaUsuario.telaDadosUsuario();
	}
		
    public Usuario getUser() {
		return user;
	}
	
	@Override
	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}

	@Override
	public void selecionaUsuario(Usuario user) {
		// TODO Auto-generated method stub
	}

	public FragmentoBuscaUsuario getFragmentoBuscaUsuario() {
		return fragmentoBuscaUsuario;
	}
}
