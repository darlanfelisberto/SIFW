package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;

import br.edu.iffar.fw.classBag.db.dao.CardapioDAO;
import br.edu.iffar.fw.classBag.db.model.Cardapio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CardapioBean  implements Serializable{

	private static final long serialVersionUID = 22021991L;

	@Inject private CardapioDAO cardapioDAO;
	
	private Cardapio cardapio;
	
	@PostConstruct
	public void init() {
		this.cardapio = this.cardapioDAO.findUltimoCardapio();
	}

	public Cardapio getCardapio() {
		return cardapio;
	}

	public void setCardapio(Cardapio cardapio) {
		this.cardapio = cardapio;
	}
}
