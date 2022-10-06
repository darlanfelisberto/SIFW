package br.edu.iffar.fw.classBag.db.model.api;

import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-05-27T18:13:45.020-0300")
@StaticMetamodel(APIUsuario.class)
public class APIUsuario_ {
	public static volatile SingularAttribute<APIUsuario, UUID> usuarioId;
	public static volatile SingularAttribute<APIUsuario, String> username;
	public static volatile SingularAttribute<APIUsuario, String> cpf;
	public static volatile SingularAttribute<APIUsuario, String> tokenRu;
	public static volatile SingularAttribute<APIUsuario, String> nome;
	public static volatile SingularAttribute<APIUsuario, APISaldo> saldo;
}
