package br.edu.iffar.catra.comunica;

public class FalhaPassagemPermitida extends Exception{

	public FalhaPassagemPermitida(String string) {
		super("Passagem autorizada! "+string+" Comunique o Admin dos Sistemas!");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
