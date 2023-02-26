package br.edu.iffar.fw.classBag.db.dao;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.auth.models.Permissao;
import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;

@Dependent
public class BackgroudDAO  extends DAO<Model<?>> {
	
	@Inject private EntityManager em;
	
	@Transactional
	public Usuario getUsuarioByCPF(String cpf) {
		try {
//busca situacoes para o cache l1
			List<Matricula> lm = this.em.createQuery("""
					select m from Matricula m
					left join fetch m.curso c
					left join fetch m.usuario u
					left join fetch m.listSituacaoMatricula sm
					where u.cpf = :cpf
					""").setParameter("cpf", cpf).getResultList();

			Usuario u = (Usuario) this.em.createQuery("""
					select u from Usuario u
					left join fetch u.authUser au
					left join fetch au.setPermissao sp
					where u.cpf = :cpf
					""").setParameter("cpf", cpf).getSingleResult();
			return u;
		} catch (NoResultException e) {
			System.out.println("Nenum usuario encontrado.");
			return null;
		}
		
	}
//
//	public Matricula getMatriculaByNumero(Integer numero) {
//		try {
//			return (Matricula)this.em.createQuery("from Matricula m where m.idMatricula = :numero").setParameter("numero", numero).getSingleResult();
//		} catch (NoResultException e) {
//			System.out.println("Nenhuma matricula encontrada.");
//			return null;
//		}
//	}
//
//	public TipoVinculo getTipoVinculoByIdTipoVinculo(int vinculo) {
//		try {
//			return (TipoVinculo)this.em.createQuery("from TipoVinculo tv where tv.idTipoVinculo = :numero").setParameter("numero", vinculo).getSingleResult();
//		} catch (NoResultException e) {
//			System.out.println("Nenhuma matricula encontrada.");
//			return null;
//		}
//	}
	
	@SuppressWarnings("unchecked")
	public List<Matricula> listAllMatriculaByCurso(Curso curso){
		Query q = this.em.createQuery("""
				select m from Matricula m
				join fetch m.listSituacaoMatricula sm
				join SituacaoMatricula sm2 on sm2.matricula = m
				where m.curso = :curso and sm2.situacao = 'ATIVA'
				and ( sm2.matricula.matriculaId,sm2.momento) in 
				(select mm.matricula.matriculaId,max(mm.momento)  from SituacaoMatricula as mm group by mm.matricula)
		""");
		q.setParameter("curso", curso);
		
		return (List<Matricula> )q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Map<String,Permissao> listAllPermissaos(){
		Query q = this.em.createQuery("""
				select m from Permissao m
			""");
		List<Permissao> p = q.getResultList();
		return p.stream().collect(Collectors.toMap(Permissao::getNome, Function.identity()));
	}

	@Transactional
	public void createUser(Usuario usuario, Matricula matricula)
			throws RollbackException, IllegalStateException, SecurityException, SystemException {
		try {
			if(usuario.getAuthUser().isNovo()){
				this.em.persist(usuario.getAuthUser());
			}else {
				this.em.merge(usuario.getAuthUser());
			}
			if(usuario.isNovo()){
				this.em.persist(usuario);
			}else{
				this.em.merge(usuario);
			}
			if(matricula.isNovo()){
				this.em.persist(matricula);
			}else {
				this.em.merge(matricula);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Ocorreu um problema com o usuário: " + usuario.getNome() + "." + e.getMessage());
		}
	}

}