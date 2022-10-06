package br.edu.iffar.fw.classBag.db.model.api;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.relatorios.RelatoriosPath;

@Entity
@Table(name = "imagen")
public class APIImagen extends Model<UUID>implements Serializable{

	private static final long serialVersionUID = 22021991L;
	
	public static final String SEMIMAGEN_PNG = "semfoto";
	public static final String CHECK_PNG = Paths.get("").toAbsolutePath().toString() + File.separator + "imagens" + File.separator + "check" + RelatoriosPath.EXTENSAO;
	public static final String CANCEL_PNG = Paths.get("").toAbsolutePath().toString() + File.separator + "imagens" + File.separator + "cancel" + RelatoriosPath.EXTENSAO;
	
	public static final APIImagen SEMIMAGEN = new APIImagen(SEMIMAGEN_PNG);

	@Id
	@Column(name = "imagen_id", unique = true, insertable = true)
	private UUID imagenId;
	
	@Column(name = "file_name")
	private String fileName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="usuario_id")
	private APIUsuario usuario;
	
	@Transient
	private byte[] image;
	
	public static void init() {
		if(!Files.exists(Paths.get(RelatoriosPath.PATH_IMAGEN))) {
			try {
				Files.createDirectories(Paths.get(RelatoriosPath.PATH_IMAGEN));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public APIImagen() {}
	
	public APIImagen(String fileName) {
		this.fileName = fileName;
	}
	
	public APIImagen(String fileName, APIUsuario usuario,byte[] image) {
		super();
		this.image = image;
		this.fileName = fileName;
		this.usuario = usuario;
	}
	
	public void salveFileInDisck() throws IOException {
		Path path = Paths.get(getFullPath());
        if(!Files.exists(path)) {
			Files.write(path, this.image, StandardOpenOption.CREATE_NEW);
        }
	}
	
	public void deleteFileInDisck() throws IOException {
		Path path = Paths.get(getFullPath());
        if(Files.exists(path)) {
        	Files.delete(path);
        }
	}

	@Override
	public UUID getMMId() {
		return this.imagenId;
	}
	
	@XmlTransient
	public String getFullPath() {
		return RelatoriosPath.PATH_IMAGEN + File.separator + this.fileName + RelatoriosPath.EXTENSAO;
	} 
	
	public byte[] getBytes() {
		try {
			if(this.image == null) {
				this.image = Files.readAllBytes(Path.of(this.getFullPath()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.image;
	}
	
	public void setBytes(byte[] img) {
		this.image = img;
	}
	

	public UUID getImagenId() {
		return imagenId;
	}

	public String getFileName() {
		return fileName;
	}

	public APIUsuario getUsuario() {
		return usuario;
	}

	public void setImagenId(UUID imagenId) {
		this.imagenId = imagenId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setUsuario(APIUsuario usuario) {
		this.usuario = usuario;
	}
}
