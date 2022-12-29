package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.model.AlteracoesAgendamentos;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
@SuppressWarnings("unchecked")
public class AlteracoesAgendamentoDAO extends DAO<AlteracoesAgendamentos> {
	
	private static final long serialVersionUID = 22021991L;
	
	public List<AlteracoesAgendamentos> listAllAlteracoesAgendamentos(Usuario user) {
		return this.em.createQuery("""
					select aa from AlteracoesAgendamentos aa
					join fetch aa.agendamento a
					join fetch aa.usuarioDestino ud
					join fetch aa.usuarioOrigem uo
					join fetch a.refeicao r
					join fetch r.grupoRefeicoes gr
					where ud = :user or uo = :user
					order by aa.dtTranferencia desc
				""")
				.setParameter("user", user)
				.setMaxResults(6)
				.getResultList();
	}
}