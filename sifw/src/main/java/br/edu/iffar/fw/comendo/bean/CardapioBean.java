package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.util.List;

import br.edu.iffar.fw.classBag.db.dao.CardapioDAO;
import br.edu.iffar.fw.classBag.db.model.Cardapio;
import br.edu.iffar.fw.classBag.db.model.Cardapio.DiasDaSemana;
import br.edu.iffar.fw.classBag.util.BreadCrumb;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CardapioBean  implements Serializable{

	private static final long serialVersionUID = 22021991L;
	
	private BreadCrumb breadCrumb;
	
	private boolean rendTelaFiltro;
	private boolean rendTelaList;
	private boolean rendTelaCardapio;
	
	@Inject private CardapioDAO cardapioDAO;
	
	private Cardapio cardapio;
	
	
	private List<Cardapio> listLastCardapio;
	
	@PostConstruct
	public void init() {
		this.cardapio = this.cardapioDAO.findUltimoCardapio();
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



	public boolean isRendTelaFiltro() {
		return rendTelaFiltro;
	}

	public boolean isRendTelaList() {
		return rendTelaList;
	}

	public boolean isRendTelaCardapio() {
		return rendTelaCardapio;
	}


	public Cardapio getCardapio() {
		return cardapio;
	}

	public void setCardapio(Cardapio cardapio) {
		this.cardapio = cardapio;
	}

	public DiasDaSemana[] getFieldsCardapio() {
		return Cardapio.DiasDaSemana.values();
	}	
}
