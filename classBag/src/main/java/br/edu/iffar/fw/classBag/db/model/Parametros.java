package br.edu.iffar.fw.classBag.db.model;

import br.edu.iffar.fw.classBag.enun.TypeParam;
import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "parametros")
@Cacheable
public class Parametros extends Model<TypeParam> {
	
	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "parametro_id",insertable = true,updatable = false,nullable = false)
	@Enumerated(EnumType.STRING)
	private TypeParam parametroId;

	@NotNull(message = "Informe o valor do parametro.")
	private String valor;

	public Parametros() {
	}

	@Override
	public TypeParam getMMId() {
		return this.parametroId;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}	
	
	public Object convertTo() throws Exception {
		try {
			return this.parametroId.getClasse().getConstructor(String.class).newInstance(this.valor);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
