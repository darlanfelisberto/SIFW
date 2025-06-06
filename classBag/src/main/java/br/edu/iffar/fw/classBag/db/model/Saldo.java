package br.edu.iffar.fw.classBag.db.model;

import java.util.UUID;

import br.com.feliva.sharedClass.db.Model;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
   CREATE OR REPLACE VIEW public.saldo
AS 
SELECT COALESCE(sum(c.valor * etc.mult::numeric), 0::numeric) AS saldo, u2.usuario_id as id
   FROM usuarios u2
     LEFT JOIN creditos c ON c.usuario_id = u2.usuario_id
     LEFT JOIN enum_tipo_credito etc ON c.tipo_credito::text = etc.tipo_credito_id::text
  GROUP BY u2.usuario_id, u2.cpf;

 *
 */

@Entity
/**
*##########
*##########    Esta entidade não deve ser persistida por isso o immutable, pois ela é uma view, e nao um tabela
*##########
*/
@Immutable
@Table(name = "saldo")
public class Saldo extends Model {

	private static final long serialVersionUID = 1L;

	@Column(name = "saldo")
	private float saldo;
	
	@Id
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name="usuario_id",referencedColumnName = "usuario_id",nullable = false,insertable = false,updatable = false)
//	private Usuario usuario;//= UUID.randomUUID();
	@Column(name = "usuario_id")
	private UUID usuarioId;

	@Override
	public UUID getMMId() {
		return this.usuarioId;
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}	
}
