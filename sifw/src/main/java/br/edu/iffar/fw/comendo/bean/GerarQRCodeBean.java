package br.edu.iffar.fw.comendo.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.StreamedContent;

import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.dao.UsuariosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import br.com.feliva.sharedClass.constantes.InitConstantes;
import br.edu.iffar.relatorios.RelatoriosPath;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

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
		map.put("SUB_MY_QRCODE", RelatoriosPath.PATH_JAR_JASPER_SUB_MY_QRCODE);
		map.put("PATH", InitConstantes.IMAGEM_PATH);
		map.put("EXTENSAO", InitConstantes.IMAGEM_EXTENSAO);
		
		return map;
		
	}
	
	public StreamedContent getAllQRCode() {
		Map<String, Object> map = this.getMap();

		if (this.curso != null) {
			map.put("WHERE", " c.curso_id = '" + curso.getMMId() + "'");
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
