package br.edu.iffar.fw.classBag.db.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.com.feliva.sharedClass.db.DAO;
import br.com.feliva.sharedClass.db.Model;
import br.edu.iffar.fw.classBag.interfaces.VinculosAtivosUsuarios;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;

@RequestScoped
@SuppressWarnings("unchecked")
public class RefeicaoDAO extends DAO<Refeicao> {
	
	private static final long serialVersionUID = 22021991L;

	@SuppressWarnings("unchecked")
	public List<Refeicao> listAll(){
		return this.em.createQuery("from Refeicao").getResultList();
	}
	
	public Refeicao findbyId(UUID refeicaoId) {
		try {
			return (Refeicao) this.em.createQuery("from Refeicao r where r.refeicaoId = :refeicaoId").setParameter("refeicaoId", refeicaoId).getSingleResult();
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			return null;
		}catch (NoResultException e) {
			return null;
		}
	}
	

	public List<Refeicao> listRefeicaoByVinculo(VinculosAtivosUsuarios vinculo,TipoRefeicao tipoRefeicao){
		// TODO verificar a situação dos usuários
		String hql = """
				select r from Refeicao r 
				join fetch r.tipoRefeicao tr
				join fetch r.grupoRefeicoes gr 
				left join gr.tipoVinculo tv 
				left join gr.listMatricula m
				left join Servidor s on s.tipoVinculo = tv
				left join Matricula mm on mm.tipoVinculo = tv
				where (s.servidorId = :id
				or m.matriculaId = :id
				or mm.matriculaId = :id)
				$tipoRefeicao
				order by r.valor asc,tr.descricao 
				""".replace("$tipoRefeicao",(tipoRefeicao!=null?" and tr = :tipoRefeicao ":""));
		Query q = this.em.createQuery(hql);
		q.setParameter("id", ((Model)vinculo).getMMId());
		
		if(tipoRefeicao != null) {
			q.setParameter("tipoRefeicao", tipoRefeicao);
		}
		
		return q.getResultList();
	}
	
	public Set<TipoRefeicao> listTipoRefeicaoByVinculo(VinculosAtivosUsuarios vinculo){
		Set<TipoRefeicao> st =  new HashSet<TipoRefeicao>();
		
		for (Refeicao refeicao : this.listRefeicaoByVinculo(vinculo, null)) {
			st.add(refeicao.getTipoRefeicao());
		}
				
		return st;
	}
	
	public boolean foiUtilizada(Refeicao refeicao) {
		try {
			Query q = this.em.createNativeQuery("""
					select count(r.refeicao_id) from refeicao r
					 inner join agendamentos a on a.refeicao_id  = r.refeicao_id
					 where r.refeicao_id = :idRefeicao
					 group by r.refeicao_id

					""").setParameter("idRefeicao", refeicao.getMMId());
			Number cont = ((Number) q.getSingleResult()).intValue();
			if (cont != null && cont.intValue() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NoResultException e) {
			return false;
		}

	}

}