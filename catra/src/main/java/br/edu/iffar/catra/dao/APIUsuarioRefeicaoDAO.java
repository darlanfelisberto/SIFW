package br.edu.iffar.catra.dao;

import java.util.List;

import javax.persistence.Query;

import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.api.APIRefeicao2;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuario;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuarioRefeicao;

@SuppressWarnings("unchecked")
public class APIUsuarioRefeicaoDAO extends APIDAO<APIUsuarioRefeicao>{
	

	public APIRefeicao2[] listRefeicaoByUsuarioETipoRefeicao(APIUsuario usuario,TipoRefeicao tipoRefeicao){
		this.begin();
		String hql = """
				select r from APIRefeicao2 r
				join fetch r.tipoRefeicao tr
				join APIUsuarioRefeicao ar on ar.refeicao = r
				where ar.usuario = :usuario 
				and tr = :tipoRefeicao
				and ar.ativa = true 
				order by r.valor asc,tr.descricao 
				""";
		Query q = this.em.createQuery(hql);
		q.setParameter("usuario", usuario);
		q.setParameter("tipoRefeicao", tipoRefeicao);
		
		List<APIRefeicao2> l = q.getResultList();

		this.commit();
		
		if(l == null || l.isEmpty()) {
    		return null;
    	}
		l.add(0, new APIRefeicao2.SelecioneAPIRefeicao());
		APIRefeicao2[] r = new APIRefeicao2[l.size()];		
		l.toArray(r);
		
    	return r;
	}
	
	public List<APIUsuarioRefeicao> listAllRefeicaoUsuarioAtivas(){
		this.begin();
		String hql = """
				from APIUsuarioRefeicao ar
				join fetch ar.refeicao r
				join fetch ar.usuario u
				where ar.ativa = true
				""";
		Query q = this.em.createQuery(hql);
		
		List<APIUsuarioRefeicao> l = q.getResultList();

		this.commit();
				
    	return l;
	}
	
	public void disableAllRefeicoes() {
		this.begin();
		this.em.createNativeQuery("""
				UPDATE public.api_usuario_refeicao SET ativo=true;
				""")
		.executeUpdate();
		this.commit();
		
	}

}
