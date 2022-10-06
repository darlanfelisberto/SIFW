package br.edu.iffar.fw.classBag.enun;

public enum TypeParam{
	AGENDAMENTO_FUTURO_DIAS("Quantidade de dias para agendamento futuro",Long.class);
	
	private String descricao;
	private Class<?> classe;
	
	private TypeParam(String desc,Class<?> classe) {
		this.descricao = desc;
		this.classe = classe;
	}

	public String getDescricao() {
		return descricao;
	}

	public Class<?> getClasse() {
		return classe;
	}
	
}
