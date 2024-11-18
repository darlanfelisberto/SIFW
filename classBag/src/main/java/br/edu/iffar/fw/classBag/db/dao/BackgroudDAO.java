package br.edu.iffar.fw.classBag.db.dao;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.feliva.authClass.dao.PermissaoDAO;
import br.com.feliva.authClass.models.Permissao;
import br.com.feliva.authClass.models.Pessoa;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.com.feliva.sharedClass.db.DAO;
import br.com.feliva.sharedClass.db.Model;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import lombok.Getter;

/**
 * Este dao é injetado em threds que execuca em background, por isso nao tem contexto e nao funciona outros escopos,
 * por isso que ela possuimetodos copiados de outros daos.
 */
@Dependent
@Getter
public class BackgroudDAO  extends DAO<Model<?>> {
	
	@Inject private EntityManager em;

	CursosDAO cursosDAO;
	PermissaoDAO permissaoDAO;

	protected Map<String,Permissao> cacheAllPermissaos = new HashMap<>();

	@PostConstruct
	private void init(){
		this.cursosDAO = new CursosDAO().noContext(this.em);
		this.permissaoDAO = new PermissaoDAO().noContext(this.em);


		this.permissaoDAO.listAll().forEach(permissao->{
			this.cacheAllPermissaos.put(permissao.getNome(),permissao);
		});
	}

	public Set<Permissao> getPermissoesFromCache(String[] role){
		Set<Permissao> permissoes = new HashSet<>();

		for (String permissao : role){
			Permissao p = this.cacheAllPermissaos.get(permissao);
			if(p == null){
				throw new RuntimeException("Permissao " + permissao + " não foi encontrada.");
			}
			permissoes.add(p);
		}

		return permissoes;
	}

	public Usuario getUsuarioByCPF(String cpf) {
		try {
			Usuario u = (Usuario) this.em.createQuery("""
					select u from Usuario u
					left join fetch u.pessoa p
					left join fetch p.authUser au
					left join fetch au.setPermissao sp
					where p.cpf = :cpf
					""").setParameter("cpf", cpf).getSingleResult();
			return u;
		} catch (NoResultException e) {
			return null;
		}
	}

	public Matricula getMatricula(Integer matricula,String cpf) {
		try {
			return (Matricula) this.em.createQuery("""
				select m from Matricula m
				left join m.usuario u 
				left join u.pessoa p 
				where p.cpf = :cpf and m.idMatricula = :matricula
			""").setParameter("cpf", cpf)
					.setParameter("matricula", matricula)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Pessoa getPessoaByCPF(String cpf) {
		try {
			Pessoa p = (Pessoa) this.em.createQuery("""
			select p from Pessoa p
			left join fetch p.authUser au
			left join fetch au.setPermissao sp
			where p.cpf = :cpf
			""").setParameter("cpf", cpf).getSingleResult();
			return p;
		} catch (NoResultException e) {
			return null;
		}
	}

	
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

	public Curso findByCod(Integer cod) {
		return (Curso) this.em.createQuery("select c from Curso c where c.codigo = :cod")
				.setParameter("cod",cod)
				.getSingleResult();
	}

	@Transactional
	public void createUser(Usuario usuario, Matricula matricula,Pessoa pessoa) throws Exception  {
		try {
			if(pessoa.isNovo()){
				this.em.persist(pessoa);
			}else {
				this.em.merge(pessoa);
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
			throw new RuntimeException("Ocorreu um problema com o usuário: " + usuario.getPessoa().getNome() + "." + e.getMessage());
		}
	}

}