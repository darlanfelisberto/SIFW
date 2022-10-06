package br.edu.iffar.fw.classBag.db.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
//import br.edu.iffar.fw.classBag.db.model.api.APIRefeicao;
import br.edu.iffar.fw.classBag.db.model.api.APIRefeicao2;
import br.edu.iffar.fw.classBag.db.model.interfaces.VinculosAtivosUsuarios;

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

//	@SuppressWarnings({ "unchecked", "deprecation", "serial","rawtypes" })
//	public List<APIRefeicao> listAllAPIRefeicao(){
//		
//		Session s = this.em.unwrap(Session.class);
//		
//		
//		
//		String sql = """
//				select
//				u.*,
//				r.*,
//				tr.*,
//				g.descricao as descricao_grupo
//				 from refeicao r 
//				 inner join tipo_refeicao tr on r.tipo_refeicao_id=tr.tipo_refeicao_id 
//				 inner join grupo_refeicoes g on r.grupo_refeicoes_id=g.grupo_refeicoes_id 
//				 left outer join tipo_vinculo tv on g.tipo_vinculo_id=tv.tipo_vinculo_id 
//				 left outer join matricula_grupo mg  on g.grupo_refeicoes_id=mg.grupo_refeicoes_id 
//				 left outer join matricula m  on mg.matricula_id=m.matricula_id 
//				 left outer join servidor s on ( s.tipo_vinculo_id=tv.tipo_vinculo_id ) 
//				 left outer join matricula m2  on ( m2.tipo_vinculo_id=tv.tipo_vinculo_id )  
//				 inner join usuarios u on u.usuario_id = coalesce(coalesce(m2.usuario_id,m.usuario_id),s.usuario_id)
//				 order by u.usuario_id,r.valor
//
//				""";
//		
//		return (List<APIRefeicao>) s.createNativeQuery(sql)
//				.addEntity("refeicao", APIRefeicao.class)
//				.addJoin("u", "refeicao.usuario")
//				.addJoin("tr", "refeicao.tipoRefeicao")
//				.setResultTransformer(new ResultTransformer() {
//					
//					@Override
//					public Object transformTuple(Object[] tuple, String[] aliases) {
//						return tuple[0];
//					}
//					
//					@Override
//					public List transformList(List collection) {
//						return collection;
//					}
//				})
//				.list();
//		
////		Query q = this.em.createNativeQuery(sql,APIRefeicao.class);
////		return (List<APIRefeicao>) q.getResultList();
//	}
	
	public List<APIRefeicao2> listAllRefeicoes2(){
		Query q = this.em.createQuery("""
					from APIUsuarioRefeicao ur
					join fetch ur.refeicao r
					join fetch r.tipoRefeicao tr
					join fetch ur.usuario u
				""");
		
		return q.getResultList();
	}
}