package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.edu.iffar.relatorios.RelatoriosPath;

@Named
@RequestScoped
public class GerarQRCodeBean implements Serializable{

	private static final long serialVersionUID = 22021991L;
	
	@Inject private UsuariosDAO usuariosDAO;
	@Inject private RelatoriosPath path;
	@Inject private MessagesUtil messagesUtil; 
	@Inject private CursosDAO cursosDAO;

	private Curso curso;
	
	public List<Curso> getListaCursos() {
		return this.cursosDAO.listAllCursosComMatriculas();
	}
	
	public Map<String, Object> getMap(){
		int total = this.usuariosDAO.generatedTokenRUForAll(false);
		if(total>0) {
			this.messagesUtil.addWarn("Foram gerada(s) "+ total+" token ru de alunos carterinhas.", null);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SUB_MY_QRCODE", RelatoriosPath.JASPER_SUB_MY_QRCODE);
		map.put("PATH", RelatoriosPath.PATH_IMAGEN);
		map.put("EXTENSAO", RelatoriosPath.EXTENSAO);
		
		return map;
		
	}
	
	public StreamedContent getAllQRCode() {
//		Pbkdf2Hash c = new Pbkdf2Hash()
//		
//		Map<String, String> parameters =  new HashMap<>();
//		parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA256");
//		parameters.put("Pbkdf2PasswordHash.Iterations", "27500");
//		parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "128");
//		parameters.put("Pbkdf2PasswordHash.KeySizeBytes", "64");
//		c.initialize(parameters);
//		System.out.println(c.generate("teste".toCharArray()));
		
		Map<String, Object> map = this.getMap();

		if (this.curso != null) {
			map.put("WHERE", " and c.curso_id = '" + curso.getMMId() + "'");
		}

		try {

			return this.path.getJasper("AllQrCode", map, RelatoriosPath.JASPER_ALL_QRCODE);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
	
	
	public void getAllQRCodeB() {
		try {
			this.path.download(this.path.getJasper(getMap(), RelatoriosPath.JASPER_ALL_QRCODE), "AllQrCode");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}	

}
