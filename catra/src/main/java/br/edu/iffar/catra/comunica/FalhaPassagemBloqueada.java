package br.edu.iffar.catra.comunica;

import br.edu.iffar.catra.util.Log;

public class FalhaPassagemBloqueada extends Exception{

	public FalhaPassagemBloqueada(String string) {
		super("Passagem NÃO autorizada! "+ string);
		Log.info( CatracaComunicacao.FALHA_SALVAR_LOCAL+ super.getMessage());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
