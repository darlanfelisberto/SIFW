package br.edu.iffar.fw.ru.bean;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import br.com.feliva.authClass.models.Pessoa;
import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.SituacaoMatricula;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import br.edu.iffar.fw.classBag.excecoes.ConfigException;
import br.edu.iffar.fw.classBag.impl.ConfigsCSVImpl;
import br.edu.iffar.fw.classBag.impl.FileInMemory;
import br.edu.iffar.fw.classBag.impl.GrupoProcessamento;
import br.edu.iffar.fw.classBag.impl.ImportarUsuariosImpl;
import br.edu.iffar.fw.classBag.interfaces.*;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.com.feliva.authClass.models.Permissao;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;

public class CSVImportUsuarios extends ImportarUsuariosImpl {

	private ConfigsCSVImpl config;

	public static final int COLUNA_MATRICULA = 0;
	public static final int COLUNA_CPF = 1;
	public static final int COLUNA_NOME = 2;
	public static final int COLUNA_EMAIL = 3;
	public static final int COLUNA_SEXO = 4;
	public static final int COLUNA_DT_NASC = 5;

	@Inject
	CursosDAO cursosDAO;

	public void processaAluno(GrupoProcessamento grupo, CSVRecord record) throws Exception {
		String cpf = this.cpf11(record.get(COLUNA_CPF).replace(".", "").replace("-", ""));
		Integer idMatricula = Integer.parseInt(record.get(COLUNA_MATRICULA));

		Pessoa pessoa = this.modifyPessoa(this.backDAO.getPessoaByCPF(cpf),record,grupo,cpf);
		Usuario usuario = this.modifyUser(this.backDAO.getUsuarioByCPF(cpf),pessoa, record, grupo);
		Curso curso = getCurso(grupo,record);
		Matricula matricula = this.modifyMatricula(this.backDAO.getMatricula(idMatricula,cpf), usuario, curso, record);
		this.backDAO.createUser(usuario,matricula,pessoa);
	}

	public Curso getCurso(GrupoProcessamento grupo,CSVRecord record){
		if(grupo.isUsarColunaCurso()){
			Integer cod = Integer.parseInt(record.get(grupo.getColunaCurso()).trim());
			return this.backDAO.getCursosDAO().findByCod(cod);
		}else{
			return grupo.getCurso();
		}
	}

	public void processaServidor(GrupoProcessamento grupo,CSVRecord record){

	}

	private void preparaInativarMariculas(GrupoProcessamento grupo){
		if(grupo.getInativarMatriculasAusentes()  && !grupo.isUsarColunaCurso()){
			this.listMatriculaHaInativar = this.backDAO.listAllMatriculaByCurso(grupo.getCurso());
		}
	}

	public void execute() {

		this.config.getListGrupoProcessamentos().forEach(grupo -> {
			this.preparaInativarMariculas(grupo);
			for (FileInMemory file : grupo.getListFile()) {
				this.line = -1;
				for (CSVRecord record : this.parseCSV(grupo, file)) {
					this.line++;
					this.saidaInfo("");
					if (this.line < grupo.getPrimeiroRegistro()) {
						continue;
					}

					if (!grupo.isServidor()) {
                        try {
                            this.processaAluno(grupo, record);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

				}
				this.saidaInfo("Fim processamento file: " + file.getFileName());
			}
		});
	}

	public Pessoa modifyPessoa(Pessoa p, CSVRecord record,GrupoProcessamento grupo,String cpf) {

		Set<Permissao> attPermissoes = (grupo.getUsarColunaPermissao()?
			this.backDAO.getPermissoesFromCache(record.get(grupo.getColunaPermissao()).trim().split(",")):new HashSet<>(grupo.getListPermissoes()));

		if(p == null){
			p = Pessoa.createNew();
			p.setCpf(cpf);
			p.getAuthUser().setSetPermissao( attPermissoes);
			p.getAuthUser().setUsername(cpf);
			p.getAuthUser().setPassword(cpf);
			this.saidaInfo("nova pessoa/login: " + p.getCpf());
		}else{
			p.getAuthUser().getSetPermissao().addAll(attPermissoes);
			this.saidaInfo("atualizado permissoes: " + p.getCpf());
		}

		p.setNome(this.getNomePadrao(record.get(COLUNA_NOME).trim()));
		p.getAuthUser().setEmail(record.get(COLUNA_EMAIL).trim().toLowerCase());
		p.setDtnasc(LocalDate.parse(record.get(COLUNA_DT_NASC).trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		return p;
	}

	public Usuario modifyUser(Usuario u, Pessoa p, CSVRecord record,GrupoProcessamento grupo) {
		if(u == null) {
			u = Usuario.createUsuario(p);
			this.saidaInfo("novo usuário: " + u.getPessoa().getCpf());
		}else {
			this.saidaInfo("base usuário: "+u.getPessoa().getCpf());
		}
		return u;
	}

	public Matricula modifyMatricula(Matricula m, Usuario u, Curso c, CSVRecord record) throws NumberFormatException {
		if(m == null) {
			m = Matricula.createMatricula(u,c);
			this.saidaInfo("nova matricula: " + m.getIdMatricula() + "/" + u.getPessoa().getCpf());
		}else {
			this.saidaInfo("base matricula: " + m.getIdMatricula() + "/" + u.getPessoa().getCpf());
		}

		if (m.getUltimaSituacao() == null || !m.getUltimaSituacao().getSituacao().equals(TypeSituacao.ATIVA)) {
			m.addSituacaoMatricula(new SituacaoMatricula(TypeSituacao.ATIVA, this.momento, m));
		}

		m.setIdMatricula(Integer.parseInt(record.get(COLUNA_MATRICULA).trim()));
		m.setCurso(c);
		//TODO removert tipo vinculo da matricula
		m.setTipoVinculo(c.getTipoVinculo());

		if(this.listMatriculaHaInativar != null) {
			this.listMatriculaHaInativar.remove(m);
		}
		return m;
	}

	public List<CSVRecord> parseCSV(GrupoProcessamento grupo, FileInMemory file) {
		this.saidaInfo("parceCSV: " + file.getFileName());
    	try {
	    	return CSVFormat.DEFAULT
	    			.withDelimiter(grupo.getDelimitadorColuna())
	    			.withQuote(grupo.getDelimitadorTexto())
					.parse(new StringReader(new String(file.getData())))
					.getRecords();
    	} catch (IOException e) {
			this.saidaErro("Erro no parce do csv. File: " + file.getFileName());
			this.saidaErro(e.getMessage());
			return null;
		}
	}

	public void onConfig(SelectEvent<Configs> event) {
		this.config = (ConfigsCSVImpl) event.getObject();
	}

	public void isOk()throws ConfigException {
		if(this.config == null){
			throw new ConfigException("Faça as configurações antes de iniciar.");
		}
		if(this.config.getListGrupoProcessamentos() == null ||this.config.getListGrupoProcessamentos().isEmpty()){
			throw new ConfigException("Não existe grupos de processamento.");
		}
	}

	public void setDados(Configs configs) {
		this.config = (ConfigsCSVImpl) configs;
	}

	public void initConfigs() {
		this.config =  new ConfigsCSVImpl();

		DialogFrameworkOptions options = DialogFrameworkOptions.builder()
				.modal(true)
				.closable(false)
				.contentHeight("100%")
				.contentWidth("100%")
				.width("95%")
				.height("95%")
				.build();
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("config", this.config);
		PrimeFaces.current().dialog().openDynamic("/configCSVFragment", options, null);
	}
}