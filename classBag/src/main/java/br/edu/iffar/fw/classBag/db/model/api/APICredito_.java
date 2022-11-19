package br.edu.iffar.fw.classBag.db.model.api;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-05-27T18:13:45.018-0300")
@StaticMetamodel(APICredito.class)
public class APICredito_ {
	public static volatile SingularAttribute<APICredito, UUID> creditoId;
	public static volatile SingularAttribute<APICredito, LocalDateTime> dtCredito;
	public static volatile SingularAttribute<APICredito, Float> valor;
	public static volatile SingularAttribute<APICredito, APIUsuario> usuario;
	public static volatile SingularAttribute<APICredito, APIAgendamento> agendamento;
	public static volatile SingularAttribute<APICredito, Boolean> sincronizado;
}
