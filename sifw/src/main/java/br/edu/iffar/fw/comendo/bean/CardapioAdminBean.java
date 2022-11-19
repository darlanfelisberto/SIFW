package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;

import br.edu.iffar.fw.classBag.db.dao.CardapioDAO;
import br.edu.iffar.fw.classBag.db.model.Cardapio;
import br.edu.iffar.fw.classBag.db.model.Cardapio.DiasDaSemana;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import br.edu.iffar.fw.classBag.util.BreadCrumbControl;
import br.edu.iffar.fw.classBag.util.MessagesUtil;

@Named
@ViewScoped
public class CardapioAdminBean  implements Serializable,BreadCrumbControl{

	private static final long serialVersionUID = 22021991L;
	
	private BreadCrumb breadCrumb;
	
	private boolean rendTelaFiltro;
	private boolean rendTelaList;
	private boolean rendTelaCardapio;
	
	@Inject private MessagesUtil mesUtil;
	@Inject private CardapioDAO cardapioDAO;
	
		
	private LocalDate data;
	private Cardapio cardapio;
	
	private List<Cardapio> listCardapio;
	
	private String textoEmEdicao;
	
	private DiasDaSemana diaDaSemana;
	
	@PostConstruct
	public void init() {
		this.createBreadCrumb();
		this.telaFiltro();
	}
	
	public void buscar() {
		this.listCardapio = (this.data != null ? this.cardapioDAO.listCardapioByData(this.data) : this.cardapioDAO.listAll());
		this.telaLista();
	}

	@Override
	public void createBreadCrumb() {
		this.breadCrumb = new BreadCrumb()
			.inicializa()
			.addItem("Publicar cardápio", "./cardapio_admin.xhtml",BreadCrumb.RAIZ)//1
			.addItem("Busca de Cardápio" ,"#{cardapioAdminBean.telaFiltro()}","frmMain", 1)//2
			.addItem("Lista de Cardápio","#{cardapioAdminBean.telaLista()}" ,"frmMain", 2)//3
			.addItem("Novo Cardápio"    ,"#{cardapioAdminBean.telaNewCardapio()}","frmMain", 1)//4
			.addItem("Editar Cardápio"    ,"#{cardapioAdminBean.telaEditarCardapio()}","frmMain", 3)//5
			;
	}
	
	public void editCardapio(){
		this.telaEditarCardapio();
		this.setDiaDaSemana(Cardapio.DiasDaSemana.SEGUNDA);
	}
	
	public void removerCardapio() {
		try {
			this.cardapioDAO.removeT(this.cardapio);
			this.mesUtil.addSuccess("Cardápio removido com sucesso.");
			this.buscar();
		} catch (RollbackException e) {
			this.mesUtil.addError(e);
			e.printStackTrace();
		}
	}
	
	public void seleciona() {
	}
	
	public void salvarCardapio() {

		if(cardapio.getDtFim().isBefore(this.cardapio.getDtInicio())) {
			this.mesUtil.addError("Data final não pode ser menor que data de início.");
			return;
		}

		try {
			this.cardapioDAO.updateT(this.cardapio);
			this.mesUtil.addSuccess("Cardápio salvo com sucesso.");
			this.telaFiltro();
		} catch (RollbackException e) {
			this.mesUtil.addError(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void newCardapio() {
		this.telaNewCardapio();
		this.setDiaDaSemana(Cardapio.DiasDaSemana.SEGUNDA);
	}
		
	public void telaFiltro() {
		this.rendTelaFiltro = true;
		this.rendTelaList = false;
		this.rendTelaCardapio = false;
		this.breadCrumb.setAtivo(2);
	}
	public void telaLista() {
		this.rendTelaFiltro = false;
		this.rendTelaList = true;
		this.rendTelaCardapio = false;
		this.breadCrumb.setAtivo(3);
	}
	public void telaNewCardapio() {
		this.rendTelaFiltro = false;
		this.rendTelaList = false;
		this.rendTelaCardapio = true;
		this.breadCrumb.setAtivo(4);
		this.cardapio = new Cardapio();
	}
	
	public void telaEditarCardapio() {
		this.rendTelaFiltro = false;
		this.rendTelaList = false;
		this.rendTelaCardapio = true;
		this.breadCrumb.setAtivo(5);
	}

	@Override
	public BreadCrumb getBreadCrumb() {
		return this.breadCrumb;
	}

	public boolean isRendTelaFiltro() {
		return rendTelaFiltro;
	}

	public boolean isRendTelaList() {
		return rendTelaList;
	}

	public boolean isRendTelaCardapio() {
		return rendTelaCardapio;
	}

	public List<Cardapio> getListCardapio() {
		return listCardapio;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Cardapio getCardapio() {
		return cardapio;
	}

	public void setCardapio(Cardapio cardapio) {
		this.cardapio = cardapio;
	}

	public String getTextoEmEdicao() {
		return textoEmEdicao;
	}

	public void setTextoEmEdicao(String textoEmEdicao) {
		this.textoEmEdicao = textoEmEdicao;
		this.cardapio.sendTexto(this.diaDaSemana, this.textoEmEdicao);
	}
	
	public DiasDaSemana[] getFieldsCardapio() {
		return Cardapio.DiasDaSemana.values();
	}

	public DiasDaSemana getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(DiasDaSemana diaDaSemana) {
		this.textoEmEdicao =  this.cardapio.pegaTexto((Cardapio.DiasDaSemana)diaDaSemana);
		this.diaDaSemana = diaDaSemana;
	}
	
	
}
