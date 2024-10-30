package br.edu.iffar.fw.comendo.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.Usuario;
import org.apache.commons.csv.CSVRecord;

import br.com.feliva.authClass.models.AuthUser;
import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.dao.BackgroudDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.SituacaoMatricula;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;

public class BackgroundUserCreate implements Runnable{

	@Inject private BackgroudDAO backDAO;

	private List<CSVRecord> listRescord;
	private StringBuffer saidaTextoImportUser;

	private HashMap<String, Permissao> cacheRoles = new HashMap<>();

	private Curso curso;
	private List<Matricula> listMatriculaHaInativar;
	private int line;
	private Integer colunaRole;
	private boolean inativarMatriculaAusentes = false;
	private LocalDateTime momento = LocalDateTime.now();
	private int firstRecord;

	private Map<String,Permissao> listAllPermissaos;

	public void setDados(List<CSVRecord> listRescord, StringBuffer saida,Curso curso,Integer coluna,boolean inativarMatriculaAusentes,int firstRecord) {
		this.listRescord = listRescord;
		this.saidaTextoImportUser = saida;
		this.curso = curso;
		this.colunaRole = coluna;
		this.inativarMatriculaAusentes = inativarMatriculaAusentes;
		this.firstRecord = firstRecord;
	}

	public void run() {
		try {
			this.listAllPermissaos = this.backDAO.listAllPermissaos();
			this.saidaInfo("processamento em background iniciando....");
			this.saidaInfo("");

			this.listMatriculaHaInativar =(this.inativarMatriculaAusentes? this.backDAO.listAllMatriculaByCurso(this.curso): new ArrayList<>());

			this.line = -1;
			for (CSVRecord record : listRescord) {
				this.line++;
				if(this.line <firstRecord) {
					continue;
				}
				this.saidaInfo(".");

				Set<Permissao> roles;
				if(this.colunaRole > 0) {
					roles =  new HashSet<>();
					String[] m = record.get(this.colunaRole).replace(" ", "").split("\\,");
					Arrays.stream(m).forEach(permissao ->{
						Permissao p = this.listAllPermissaos.get(permissao);
						if(p == null){
							throw new RuntimeException(permissao + ": não foi encontrada linha: " + this.line);
						}else{
							roles.add(p);
						}
					});
				} else {
					roles = null;
				}

				String cpf = record.get(1).replace(".", "").replace("-", "");
				Integer idMatricula = Integer.parseInt(record.get(0));

				Usuario usuario = this.modifyUser(this.backDAO.getUsuarioByCPF(cpf), record,roles);
				Matricula matricula = this.modifyMatricula(usuario.getMatriculaByIdMatricula(idMatricula), usuario, record);

				this.listMatriculaHaInativar.remove(matricula);

				try {
					this.backDAO.createUser(usuario, matricula);
					this.saidaSucess("Usuário " + usuario.getPessoa().getCpf() + " criado com sucesso.");
				} catch (Exception e) {
					this.saidaErro("Ocorreu um problema com o usuário: " + usuario.getPessoa().getCpf() + "." + e.getMessage());
					e.printStackTrace();
				}
			}
			if( this.inativarMatriculaAusentes) {
				LocalDateTime momento = LocalDateTime.now();
				this.listMatriculaHaInativar.forEach(mat->{
					try {
						mat.getUsuario().getPessoa().getAuthUser().setInativo(true);
						this.backDAO.mergeT(mat.getUsuario().getPessoa().getAuthUser());
						this.backDAO.persistT(new SituacaoMatricula(TypeSituacao.INATIVA, momento, mat));
						this.saidaInfo("Lançando situação: " + TypeSituacao.INATIVA.getDesc() + " para a matrícula: "+mat.getIdMatricula() + " para o usuário:"+mat.getUsuario().getPessoa().getCpf());
					} catch (RollbackException e) {
						e.printStackTrace();
						this.saidaErro("Erro ao Lançar situacão Inativa para o aluno: " + mat.getUsuario().getPessoa().getCpf()+"  --  "+e.getMessage());
					}
				});
				this.saidaSucess("Situacoes salvas com sucesso.");
			}
		} catch (Throwable e) {
			this.saidaErro(e.getMessage());
			e.printStackTrace();
		}
		this.saidaInfo("");
		this.saidaInfo("Processamento em background terminado.");
	}

	public Usuario modifyUser(Usuario u, CSVRecord record, Set roles) {
		if(u == null) {
			u = new Usuario();
			u.getPessoa().setCpf(record.get(1).trim().replace(".", "").replace("-", ""));
			u.setTokenRU(UUID.randomUUID().toString());
		}else {
			this.saidaInfo("Usuário "+u.getPessoa().getCpf()+" já existe na base.");
		}

		if(u.getPessoa().getAuthUser() == null){
			//TODO
//			AuthUser.createNew(u,roles);
		}else{
			u.getPessoa().getAuthUser().getSetPermissao().addAll(roles);
		}

		// em algumas planilhas, os campos nomes estao vindo em branco
		u.getPessoa().setNome(this.getNomePadrao((record.get(3).trim().length() > 0 ? record.get(3) : record.get(4))));

		//TODO ver questao do email
		u.getPessoa().getAuthUser().setEmail(record.get(5).trim().toLowerCase());
		u.getPessoa().setDtnasc(LocalDate.parse(record.get(9).trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		return u;
	}

	public Matricula modifyMatricula(Matricula m, Usuario u, CSVRecord record) throws NumberFormatException {
		if(m == null) {
			m = new Matricula();
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

	public String getNomePadrao(String nome) {
		String[] nn = nome.toLowerCase().trim().split(" ");
		StringBuilder s = new StringBuilder();
		for (String string : nn) {
			s.append(" "+(string.length()>2?string.substring(0, 1).toUpperCase()+string.substring(1, string.length()):string));
		}
		//esse trim remove o espaço colocado no for
		return s.toString().trim();
	}

	public void saidaErro(String msg) {
		String m = String.format("error  [line:%d] %s <br/>",this.line, msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}

	public void saidaSucess(String msg) {
		String m = String.format("sucess [line:%d] %s <br/>",this.line, msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}

	public void saidaInfo(String msg) {
		String m = String.format("info   [line:%d] %s <br/>",this.line, msg);
		System.out.println(m);
		this.saidaTextoImportUser.append(m);
	}
}