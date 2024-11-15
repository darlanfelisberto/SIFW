package br.edu.iffar.fw.ru.bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import br.edu.iffar.fw.classBag.db.model.Usuario;
import br.edu.iffar.fw.classBag.interfaces.Configs;
import br.edu.iffar.fw.classBag.interfaces.ConfigsCSVImpl;
import br.edu.iffar.fw.classBag.interfaces.ImportarUsuariosImpl;
import jakarta.faces.context.FacesContext;
import org.apache.commons.csv.CSVRecord;

import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.SituacaoMatricula;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import org.primefaces.model.file.UploadedFile;

public class CSVImportUsuarios extends ImportarUsuariosImpl{

	private ConfigsCSVImpl config;

	private Map<String,Permissao> listAllPermissaos;
	protected List<CSVRecord> listRescord;

	public void run() {}
//		try {
//			this.listAllPermissaos = this.backDAO.listAllPermissaos();
//			this.saidaInfo("processamento em background iniciando....");
//			this.saidaInfo("");
//			if(true)
//				return;
//
//			this.listMatriculaHaInativar =(this.inativarMatriculaAusente? this.backDAO.listAllMatriculaByCurso(this.curso): new ArrayList<>());
//
//			this.line = -1;
//			for (CSVRecord record : listRescord) {
//				this.line++;
//				if(this.line <firstRecord) {
//					continue;
//				}
//				this.saidaInfo(".");
//
//				Set<Permissao> roles;
//				if(this.colunaRole > 0) {
//					roles =  new HashSet<>();
//					String[] m = record.get(this.colunaRole).replace(" ", "").split("\\,");
//					Arrays.stream(m).forEach(permissao ->{
//						Permissao p = this.listAllPermissaos.get(permissao);
//						if(p == null){
//							throw new RuntimeException(permissao + ": não foi encontrada linha: " + this.line);
//						}else{
//							roles.add(p);
//						}
//					});
//				} else {
//					roles = null;
//				}
//
//				String cpf = record.get(1).replace(".", "").replace("-", "");
//				Integer idMatricula = Integer.parseInt(record.get(0));
//
//				Usuario usuario = this.modifyUser(this.backDAO.getUsuarioByCPF(cpf), record,roles);
//				Matricula matricula = this.modifyMatricula(usuario.getMatriculaByIdMatricula(idMatricula), usuario, record);
//
//				this.listMatriculaHaInativar.remove(matricula);
//
//				try {
//					this.backDAO.createUser(usuario, matricula);
//					this.saidaSucess("Usuário " + usuario.getPessoa().getCpf() + " criado com sucesso.");
//				} catch (Exception e) {
//					this.saidaErro("Ocorreu um problema com o usuário: " + usuario.getPessoa().getCpf() + "." + e.getMessage());
//					e.printStackTrace();
//				}
//			}
//			if( this.inativarMatriculaAusente) {
//				this.inativaMatriculasAusentes();
//			}
//		} catch (Throwable e) {
//			this.saidaErro(e.getMessage());
//			e.printStackTrace();
//		}
//		this.saidaInfo("");
//		this.saidaInfo("Processamento em background terminado.");
//	}
//
//	public Usuario modifyUser(Usuario u, CSVRecord record, Set roles) {
//		if(u == null) {
//			u = new Usuario();
//			u.getPessoa().setCpf(record.get(1).trim().replace(".", "").replace("-", ""));
//			u.setTokenRU(UUID.randomUUID().toString());
//		}else {
//			this.saidaInfo("Usuário "+u.getPessoa().getCpf()+" já existe na base.");
//		}
//
//		if(u.getPessoa().getAuthUser() == null){
//			//TODO
////			AuthUser.createNew(u,roles);
//		}else{
//			u.getPessoa().getAuthUser().getSetPermissao().addAll(roles);
//		}
//
//		// em algumas planilhas, os campos nomes estao vindo em branco
//		u.getPessoa().setNome(this.getNomePadrao((record.get(3).trim().length() > 0 ? record.get(3) : record.get(4))));
//
//		//TODO ver questao do email
//		u.getPessoa().getAuthUser().setEmail(record.get(5).trim().toLowerCase());
//		u.getPessoa().setDtnasc(LocalDate.parse(record.get(9).trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//		return u;
//	}
//
//	public Matricula modifyMatricula(Matricula m, Usuario u, CSVRecord record) throws NumberFormatException {
//		if(m == null) {
//			m = new Matricula();
//			m.addSituacaoMatricula(new SituacaoMatricula(TypeSituacao.ATIVA,this.momento,m));
//		}else {
//			this.saidaInfo("Matricula " + m.getIdMatricula() + " já existe na base.");
//		}
//
//		if (m.getUltimaSituacao() == null || !m.getUltimaSituacao().getSituacao().equals(TypeSituacao.ATIVA)) {
//			m.addSituacaoMatricula(new SituacaoMatricula(TypeSituacao.ATIVA, this.momento, m));
//		}
//
//		m.setIdMatricula(Integer.parseInt(record.get(0).trim()));
//		m.setUsuario(u);
//		u.addMatricula(m);
//		//TODO remover tipo de vinculo da matricula
//		m.setTipoVinculo(this.curso.getTipoVinculo());
//		m.setCurso(this.curso);
//		return m;
//	}

	public void upload(FileUploadEvent event) {

		UploadedFile upload = (UploadedFile) event.getFile();
//    	try {
//	    	this.parcer = CSVFormat.DEFAULT
//	    			.withDelimiter(this.delimitadorColuna)
//	    			.withQuote(delimitadorTexto)
//	    			.parse(new InputStreamReader(upload.getInputStream(),this.codificacaoArquivo));
//
//	        this.fileImport.listRescord = this.parcer.getRecords();
//			this.fileImport.nomeArquivo = upload.getFileName();
//			this.listFileImport.add(this.fileImport);
//			this.fileImport = new FileImport();
//			messages.addSuccess("Arquivo enviado.");
//    	} catch (IOException e) {
//    		messages.addSuccess("Erro de parse no arquivo");
//			e.printStackTrace();
//		}
	}


	public void onConfig(SelectEvent<Configs> event) {
		this.config = (ConfigsCSVImpl) event.getObject();
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