package br.edu.iffar.fw.classBag.enun;

/**
 * 
 * @author qwerty
 * 
-- DROP TYPE type_refeicao;

CREATE TYPE type_refeicao AS ENUM (
	'CAFE',
	'ALMOCO',
	'JANTA',
	'DESJEJUM',
	'LANCHE_TARDE',
	'LANCHE_MANHA',
	'LANCHE_NOITE',
	'CAFE_MANHA');

 *
 */

public enum TypeRefeicao {
	ALMOCO("Almoço","refeicao-almoco"),
	CAFE_MANHA("Café da manhã","refeicao-cafe"),
	JANTA("Janta","refeicao-janta");
	
	private String desc;
	private String styleClass;

	private TypeRefeicao(String desc,String style) {
		this.desc = desc;
		this.styleClass = style;
	}

	public String getDesc() {
		return desc;
	}

	public String getStyleClass() {
		return styleClass;
	}
	
	public String toString() {
		return desc;
	}
	
	
}
