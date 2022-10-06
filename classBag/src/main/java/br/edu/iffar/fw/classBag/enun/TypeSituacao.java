package br.edu.iffar.fw.classBag.enun;

public enum TypeSituacao {
		
	ATIVA			("Ativa"),
	INATIVA			("Inativa");
	
	private String desc;

	private TypeSituacao(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
