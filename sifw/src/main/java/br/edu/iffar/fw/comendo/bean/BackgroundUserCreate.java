package br.edu.iffar.fw.comendo.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.core.Response;

import org.apache.commons.csv.CSVRecord;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.wildfly.security.http.oidc.OidcSecurityContext;

import com.google.gson.JsonObject;

import br.edu.iffar.fw.classBag.db.dao.BackgroudDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.SituacaoMatricula;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import br.edu.iffar.fw.classBag.excecoes.KeyCloakFacilitExeption;
import br.edu.iffar.fw.classBag.sec.KeycloakAdminUtil;

public class BackgroundUserCreate implements Runnable{

	private List<CSVRecord> listRescord;
	private StringBuffer saidaTextoImportUser;
	
	private HashMap<String, RoleRepresentation> cacheRoles = new HashMap<String, RoleRepresentation>();

	@Inject private OidcSecurityContext oidcContext;
	/**
	 * por problemas no escopo de execução a entyty manager teve de ser injetada aqui
	 */
	@Inject protected EntityManager em;
	/**
	 * TODO remover o backgroud, para nao causar problemas no futuro, devido a concorrencia
	 */
	@Inject private BackgroudDAO backDAO;
	@Inject private UserTransaction transaction;
			
	private Keycloak keycloak;
	private RealmResource realmResource;
	private UsersResource usersResource;
	private Curso curso;
	private List<Matricula> listMatriculaHaInativar;
	
	private int line;
	private Integer colunaRole;
	private boolean inativarMatriculaAusentes = false;
	private LocalDateTime momento = LocalDateTime.now();
	private int firstRecord;
	
	private List<String> listIdKeyNewUser = new ArrayList<String>();
	
	public void setDados(List<CSVRecord> listRescord, StringBuffer saida,JsonObject keycloakInfo,Curso curso,Integer coluna,boolean inativarMatriculaAusentes,int firstRecord) {
		this.listRescord = listRescord;
		this.saidaTextoImportUser = saida;
		this.curso = curso;
		this.colunaRole = coluna;
		this.inativarMatriculaAusentes = inativarMatriculaAusentes;
		this.firstRecord = firstRecord;
	}
	
	public void run() {
		try {
			this.saidaInfo("processamento em background iniciando....");
			this.saidaInfo("");
			this.listMatriculaHaInativar = this.backDAO.listAllMatriculaByCurso(this.curso);
			
			this.initKeyCloak();
			this.line = 0;
			for (CSVRecord record : listRescord) {
				this.line++;
				if(this.line <firstRecord) {
					continue;
				}
				this.saidaInfo(".");
				
				//CSVRecord record = csvIterator.next();
				String cpf = record.get(1).replace(".", "").replace("-", "");
				Integer idMatricula = Integer.parseInt(record.get(0));
				
				Usuario usuario = this.modifyUser(this.backDAO.getUsuarioByCPF(cpf), record);
				Matricula matricula = this.modifyMatricula(usuario.getMatriculaByIdMatricula(idMatricula), usuario, record);
				
//				if(matricula.getIdMatricula().equals(2019315106)) {
//					System.out.println(usuario.getCpf());
//				}
				
				Matricula mm = this.backDAO.getMatriculaByNumero(matricula.getIdMatricula());
				
				if(mm != null) {
					if(!mm.getUsuario().equals(usuario)) {
						this.saidaErro("Matricula: "+matricula.getIdMatricula() +" já esta cadastrada para o usuario: "+usuario.getUserName() + " ");
						continue;
					}
				}
				
				this.listMatriculaHaInativar.remove(matricula);

				List<String> roles = null;
				if(this.colunaRole > 0) {
					String[] m = record.get(this.colunaRole-1).replace(" ", "").split("\\,");
					roles= Arrays.asList(m);
				}else {
					roles = new ArrayList<String>();
				}
				
				try {
					this.createUser(usuario, matricula.getUltimaSituacao().getSituacao().equals(TypeSituacao.ATIVA), matricula, roles);
					
				} catch (KeyCloakFacilitExeption e) {
					this.saidaErro(e.getMessage());
				} catch (Exception e) {
					this.saidaErro(e.getMessage());
					e.printStackTrace();
				}
			}
			if(!this.listMatriculaHaInativar.isEmpty() && this.inativarMatriculaAusentes) {
				try {
					this.transaction.begin();
					LocalDateTime momento = LocalDateTime.now();
					this.listMatriculaHaInativar.forEach(mat->{
						this.em.persist(new SituacaoMatricula(TypeSituacao.INATIVA, momento, mat));
						this.saidaInfo("Lançando situacao: " + TypeSituacao.INATIVA.getDesc() + " para a matricula: "+mat.getIdMatricula() + " para o usuario:"+mat.getUsuario().getUserName()+", aguardando commit.");
						UserResource userResource = this.usersResource.get(mat.getUsuario().getMMId().toString());
						UserRepresentation userRepresentaton = userResource.toRepresentation();
						userRepresentaton.setEnabled(false);
						userResource.update(userRepresentaton);
						this.saidaInfo("keycloak:user: " + userRepresentaton.getUsername() + " disable.");
					});
					this.transaction.commit();
					this.saidaSucess("Situacoes salvas com sucesso.");
				} catch (Exception e) {
					e.printStackTrace();
					this.saidaErro("Erro ao Lançar situacoes, inativar alunos.<br/> "+e.getMessage());
					this.transaction.rollback();
				}
			}
			
		} catch (Throwable e) {
			this.saidaErro(e.getMessage());
			e.printStackTrace();
		}
		this.saidaInfo("");
		this.saidaInfo("Processamento em background terminado.");
		this.close();
	}
	
	public void unickRun(Usuario usuario, boolean ativo, Matricula matricula, List<String> roles) {
		try {
			this.saidaTextoImportUser = new StringBuffer();
			this.initKeyCloak();
			try {
				this.createUser(usuario, ativo, matricula, roles);
			} catch (KeyCloakFacilitExeption e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		this.close();
	}

	@SuppressWarnings("unused")
	public void validaDados(CSVRecord record) throws Exception {
		try {
			int i = Integer.parseInt(record.get(0));
		} catch (NumberFormatException e) {
			this.saidaErro("linha " + record.getRecordNumber() +": matricula deve se inteiro.");
			throw new Exception("");
		}
		try {
			int i = Integer.parseInt(record.get(1).replace(".", "").replace("-", ""));
		} catch (NumberFormatException e) {
			this.saidaErro("linha " + record.getRecordNumber() +": cpf deve se inteiro.");
			throw new Exception("");
		}
	}
	
	public Usuario modifyUser(Usuario u, CSVRecord record) {
		if(u == null) {
			u = new Usuario();
			u.setCpf(record.get(1).trim().replace(".", "").replace("-", ""));
			u.setUserName(u.getCpf());
			u.setTokenRU(UUID.randomUUID().toString());
		}else {
			this.saidaInfo("Usuario "+u.getUserName()+" já existe na base.");
		}
		
		// em alguma planilhas, os campos nome estao vindo em branco
		u.setNome(this.getNomePadrao((record.get(3).trim().length() > 0 ? record.get(3) : record.get(4))));

		u.setEmail(record.get(5).trim().toLowerCase());
		u.setDtnasc(LocalDate.parse(record.get(9).trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		return u;
	}
	
	public Matricula modifyMatricula(Matricula m, Usuario u, CSVRecord record) throws NumberFormatException {
		if(m == null) {
			m =  new Matricula();
			m.addSituacaoMatricula(new SituacaoMatricula(TypeSituacao.ATIVA,this.momento,m));
		}else {
			this.saidaInfo("Matricula " + m.getIdMatricula() + " já existe na base.");
		}

		if (m.getUltimaSituacao() == null || !m.getUltimaSituacao().getSituacao().equals(TypeSituacao.ATIVA)) {
			m.addSituacaoMatricula(new SituacaoMatricula(TypeSituacao.ATIVA, this.momento, m));
		}

		m.setIdMatricula(Integer.parseInt(record.get(0).trim()));
		m.setUsuario(u);
		u.addMatricula(m);
		//TODO remover tipo de vinculo da matricula
		m.setTipoVinculo(this.curso.getTipoVinculo());
		m.setCurso(this.curso);
		return m;
	}
	
	public void initKeyCloak() {
		this.keycloak = KeycloakAdminUtil.getKeyCloak(this.oidcContext);
		this.realmResource = this.keycloak.realm(this.oidcContext.getRealm());
		this.usersResource = this.realmResource.users();
		
//		if(this.colunaRole != null) {
		this.realmResource.roles().list().forEach(role -> {
			this.saidaCache("Role " + role + ".");
			cacheRoles.put(role.getName(), role);
		});
//		}
	}
	
	public void close() {
		this.realmResource = null;
		this.usersResource = null;
		this.keycloak.close();
	}

	public void createUser(Usuario newUser, boolean ativo, Matricula matricula, List<String> ifRoles)
			throws RollbackException, KeyCloakFacilitExeption, IllegalStateException, SecurityException, SystemException {
		try {
			this.transaction.begin();
			
//			this.initKeyCloak();
			List<UserRepresentation> existeUserName = this.usersResource.search(newUser.getUserName(), true);
			UserRepresentation keyUser = null;

			if(!existeUserName.isEmpty()) {
				this.saidaCuidado("Usuario "+newUser.getUserName() + " já existe no keycloak e não será atualizado.");

				keyUser = existeUserName.get(0);
				UserResource userResource = this.usersResource.get(keyUser.getId());

//				ativa ou desativa o usuario no keycloak
				if(ativo != keyUser.isEnabled()) {
					keyUser.setEnabled(ativo);
					userResource.update(keyUser);
				}

				this.updateRoleOfKeycloakuser(userResource, ifRoles);
			}else {
				keyUser = this.createUserKeycloak(newUser, ifRoles, null);
			}
			
			Usuario uExistente = this.getUsuarioByUserName(newUser.getUserName());
			if(uExistente == null) {
				//assim os ids dos usuarios nos sistemas serão os mesmo
				newUser.setUsuarioId(UUID.fromString(keyUser.getId()));
			}else {
				this.saidaCuidado("Usuário "+newUser.getUserName() + " já existe na base, dados serão atualizados.");
			}
			
//			newUser = 
			this.em.merge(newUser);
			
			if(matricula != null) {
				this.em.merge(matricula);
			}

			this.transaction.commit();
			this.saidaSucess("Usuário " + newUser.getUserName() + " criado com sucesso.");
			
		}catch (Exception e) {
			e.printStackTrace();
			this.transaction.rollback();
			throw new KeyCloakFacilitExeption("Ocorreu um problema com o usuário: " + newUser.getUserName() + "." + e.getMessage());
		}
	}
	
	private UserRepresentation createUserKeycloak(Usuario newUser,List<String> ifRoles,UserRepresentation keyUser) {
		
		Response response = this.usersResource.create(newUser.createUserRepresentation());
		
		this.saidaInfo(String.format("keycloak:api:create:user Response: %s %s", response.getStatus(), response.getStatusInfo()));
//		System.out.printf("Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());
		String userId = response.getLocation().getPath().replaceFirst(".*/([^/?]+).*", "$1");
		this.saidaInfo("keycloak:user:id "+ userId);
		this.listIdKeyNewUser.add(userId);
		
		UserResource u = this.usersResource.get(userId);
		u.resetPassword(newUser.createCredentialRepresentation());
		
		this.updateRoleOfKeycloakuser(u, ifRoles);
		
		return u.toRepresentation();
	}
	
	public void updateRoleOfKeycloakuser(UserResource userResource,List<String> ifRoles) {
		List<RoleRepresentation> rolesListForUser =  new ArrayList<RoleRepresentation>();
		for (String ifRole : ifRoles) {
			RoleRepresentation roleR = cacheRoles.get(ifRole);
			if(roleR == null) {
				this.saidaCuidado("Role " + ifRole + " não foi encontrada.");
			}else {
				rolesListForUser.add(roleR);
			}
		}
		userResource.roles().realmLevel().add(rolesListForUser);
	}
	
	public void deleteusers() {
		for (String id : listIdKeyNewUser) {
			Response response = this.usersResource.delete(id);
			System.out.printf("delete Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());	
		}
	}
	
	/**
	 * por problemas no escopo de execução este metodo esta aqui
	 */
	public Usuario getUsuarioByUserName(String userName) {
		try {
			return (Usuario) this.em.createQuery("from Usuario u where u.userName = :userName").setParameter("userName", userName).getSingleResult();
		} catch (NoResultException e) {
			System.out.println("nenhum resultado encontrado, isso não é um problema!!");
			//e.printStackTrace();
		}
		return null;
	}
	
	public String getNomePadrao(String nome) {
		String[] nn = nome.toLowerCase().trim().split(" ");
		StringBuilder s = new StringBuilder(); 
		for (String string : nn) {
			s.append(" "+(string.length()>2?string.substring(0, 1).toUpperCase()+string.substring(1, string.length()):string));
		}
		//esse trim remove o espaço colocado no for
		return s.toString().trim();
	}
	
	public void saidaCuidado(String msg) {
		String m = String.format("warn   [line:%d] %s <br/>",this.line, msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}
	
	public void saidaErro(String msg) {
		String m = 		String.format("error  [line:%d] %s <br/>",this.line, msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}
	
	public void saidaSucess(String msg) {
		String m = 		String.format("sucess [line:%d] %s <br/>",this.line, msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}
	
	public void saidaInfo(String msg) {
		String m = 		String.format("info   [line:%d] %s <br/>",this.line, msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}
	
	public void saidaCache(String msg) {
		String m = 		String.format("cache  [       ] %s <br/>", msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}

	public HashMap<String, RoleRepresentation> getCacheRoles() {
		return cacheRoles;
	}

}