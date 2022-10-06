package br.edu.iffar.fw.classBag.db.model.api;

import java.time.LocalDate;
import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-05-27T18:13:45.017-0300")
@StaticMetamodel(APIAgendamento.class)
public class APIAgendamento_ {
	public static volatile SingularAttribute<APIAgendamento, UUID> agendamentoId;
	public static volatile SingularAttribute<APIAgendamento, LocalDate> dtAgendamento;
	public static volatile SingularAttribute<APIAgendamento, Boolean> editavel;
	public static volatile SingularAttribute<APIAgendamento, APIUsuario> usuario;
	public static volatile SingularAttribute<APIAgendamento, APIRefeicao2> refeicao;
}
