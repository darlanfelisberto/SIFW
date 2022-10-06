package br.edu.iffar.fw.classBag.db.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.relatorios.RelatoriosPath;

@Entity
@Table(name = "imagen")
public class Imagen extends Model<UUID>implements Serializable{

	private static final long serialVersionUID = 22021991L;
	
	public static final String SEMIMAGEN_PNG = "semfoto";
	
	public static final Imagen SEMIMAGEN = new Imagen(SEMIMAGEN_PNG);
	
	public static final byte[] SEM_IMAGE_BYTE = Base64.getDecoder().decode(
			"iVBORw0KGgoAAAANSUhEUgAAATsAAAE7CAYAAACi3CbHAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH5QsEETgSiy2CIwAAFZtJREFUeNrt3etum2oWxvHFwUAwwZQib3/o/V9NL6MaWZFlsS2CMMV4Psxe7yaetE185PD/SVGnu5o2AfOw3rP1/fv3owDAyNlcAgCEHQAQdgBA2AEAYQcAhB0AEHYAQNgBAGEHgLADAMIOAAg7ACDsAICwAwDCDgAIOwAg7ACAsANA2AEAYQcAhB0AEHYAQNgBAGEHAIQdABB2AEDYASDsAICwAwDCDgAIOwAg7ACAsAMAwg4ACDsAIOwAEHYAQNgBAGEHAIQdABB2AEDYAQBhBwCEHQAQdgAIOwAg7ACAsAMAwg4ACDsAIOwAgLADAMIOAAg7AIQdABB2AEDYAQBhBwCEHQAQdgBA2AEAYQcAhB0Awg4ACDsAIOwAgLADAMIOAAg7ACDsAICww2Mcj8eH/H+BX3G5BLhV2B0Oh/PewLYtlmVxEUHY4fGapnkTTm3biuu64rqu2LYttm2/+/tf/V1t20rbttI0zbu/13/DfHBdProg7HCDKu14PErbtia4PM8zAdb936fh1v2zP4Vn27ZS1/Uvw+/0z/RLK0GqQRB2uIhlWXI8Ht8EWBRFEgSB+XJdVxzHOfvf8H3/l392OBykaRqpqsp8FUVhwk+/R4Cww9lN1G6gacB1qzdtxt4ybBzHMUEbRdGbqk+DrxuENHVB2OGPIee6rgk3z/MkDEMTcEEQXFS9XVpd6r+tv/q+b75XDb6yLKWua6mqyjSBCT0QdpDj8WiqMg23KIpMFfe75mUfOI4j8/lcREQWi4Xs93tT7RVFIWVZmoGN7s8Kwg4TY1mWqdqSJDEhN9TpH77vmz5FDb08z02lB8IOE2uu6oiqVnFRFJnBh6FXP9rkDcPQ9PNppacDGzpVBoQdRthc1RHV7oCDfj2qL+7Woef7/pu+vSAIzICGTmNh2gphh5E9+MfjUVzXlSzLJEkS09c1BY7jyGKxkMViIa+vr5LnuWw2G6nrmqAj7DCmZmsYhpIkiSRJIp7nied5k70e2ryN41jyPJc8z6UsS5q1hB2GSvulNOSSJOn96Oq9m7ca/Bp42p8Jwg4DogMQWZaNtl/uUr7vS5ZlEgSBbDYbKYqCUVvCDoO6sf/0zWVZNooR1ltyHEfiODaBt9lsCDzCDn2mTbAwDM0gBM3WzzVtsywT13Vls9mYCcn05RF26AmdVqIrIJIkkSzLaLZe0KwVkTf9eExPIezQk6rEsixJkkSWy+WkppTcqln7119/SRRF8vLyItvtlosyAgw7jaTpmmUZQXdl8/lclsulZFkmtm3Tj0dlh0dp29asac2yTMIw5KJcmfZ/arO2rmumphB2uDfP80zQUdHdrouge23zPKfCI+xwbwTdfZu0arPZcEEIO9xa0zRUdA8OvLZtTZOWaSnDQefDQOiBN7pl0XK5pI/uAcIwlOVyabbEatuWc24JO1yTZVnStq1EUSRpmsp8Pmfe14Puw3w+lzRNzXkY3AeasbhBVZFlmXz9+pWL8WB6D+q6lrquuSBUdriGtm3F8zxZLpeSJAkXpCd0ErfneW8O8AZhh3NvUme9K0vA+sNxHDO/kbl3hB0u1DSN2aaJB6qfL6IsyySOY6q7nqPPrsdNV20q6X50dIT3j2VZ5iSz7iHdvJio7PDJqiFJEonjmOZrz5uzcRxLkiSEHGGHT5fc/8yn0/lc6LenpyczHYX7RdjhE01YXSXBLsPDEQSBGZ1l/Sxhhw82X3U3k9lsxgUZWHNWTy8DYYc/0N2G2VJ92IEHwg6/0TSNOfYQw6T3j6YsYYffiKJIwjBk9HXg1V0YhhJFEReDsMMp3dEkTVMJgoALMnBBEEiapmZnFBB26HBdl766kfB9X5IkYaCCsMN7QRcEgXiex8UYCc/zJAgCAo+wQ7cJqyOw9NWNh+M4kiSJhGFIU5awgwqCQOI45kKMTBzH9MESdjht7jCBeHxmsxndE4Qd9FyJMAx5+4+8atemLOdVEHaTDTvbtiWKIsJu5GEXRZHYtk3YEXbT5bquhGHIdJMR832f9bKE3cQvvm2bKScYf3XHNBTCbpKapjF71rHh4zRebHpWBWtmCbtJNmEJu2mFHZUdYTfZsAuCgLCbSNjRjCXsJh12YRiyE/EEWJZFZUfYTTfoXNcl6CYWeHrfQdhNho7OgfsOwm78F5++Ou45CLsxa5pGPM9jveQE6X1n+glhN7kPPbjvIOxG6Xg8mk5qmjTTbMbq/WedLGE3CYzKcd9B2E3qLQ/uOQi70dKTxJhjNz06145t2gk73vDg3oOwAwDCDgAIOwCEHQAQdgBA2AEAYQcAhB0AEHYAQNgBAGE3JKyN5N6DsAMAwm4UF922pW1bNm+coMPhIE3TsBkAYTcdTdNwDsFEm7A0Ywm7SbAsywQdH/pphp3ef/YzJOyo7MB9B2E3dHrgDs2ZaTdjOXCHsJtEM1ZEpK5rqaqKCzIxVVVJXddvPgsg7Eb/oSfsuO8g7Eavrmvzhgf3HYTdqLVtK/v9ngsxEfv9nn5awm66b/mqquRwOHAxRu5wOLzprwNhNylN00hZllyIiSjLkmknhN30uK7LAzDRF5veexB2k3sACDvuNQi70T8A9NuNX7e/jrAj7KZ58f/Z/aQoCuZejVhVVVIUhbRty24nhN10w862bcJuImGn9xuE3aQfhqqqWCs5QsfjkVUThB1U0zRSVRXTUEaoLEupqoq+OsIOp00dqrtxVXV0URB26HBdV6qqkjzPqQBGVrHneS5VVTG3jrDDaVN2t9sxDWUEDoeD7HY7mrCEHd6r7kTEVAIYNq3Uu/cWhB061Z1WAxh+2O12O6o6wg6/Ute1FEUhr6+vXIyBen19laIo2OGEsMOf7HY7KYqCCzFQRVHIbrfjQhB2+B0dmd3tdrLf75mKMiDH41H2+73piqCvjrDDB5RlKev1mqbQgNR1Lev1msnhhB0+Sg/S1pFZpqL0n+5sonMlOTmMsMMHw07k7aRU9NvppHDCrn/oVOi5zWYjrutKEATiOA4XpKdVXZ7nstlsuBhUdjiXVnc8SP1+IbHUj7DDpaX3P6Oz2+1WXl9fGZ3tkePxKK+vr7Ldbhl9JexwDW3bSlmW8uPHD0b6eqR7TzgPlrDDFViWZbZvz/Ocg7V7YL/fS57nZrt1BiQIO1wp7GzbNv13eZ4zHeWBdEBC++ls2ybsCDtck+u6Upal6RAn8B4XdJvNRsqypJ+OsMMtq7yqqmS9XrOz8Z3pzsPr9VqqqqKaI+xw67DTQ1zW67XkeU7g3Sno8jw3QXc8Hgm7obWMuATDDDyR/230qcfzRVHEpOMbNl2LojDdB67rnhV0BCSV3STpBFTP885/U7mu7HY7+fHjB314Nwy6PM/lx48fstvtLuqj0zXPTD6mshs9nYtl27YkSSJxHEsYhmZKSVEUn36Y2rY1TdqmaSRJEvF9n4t9BTq9ZLPZSFVV0rbtpw+51mouCALJskyappGiKMyUFf08gLAbvG7TJQxD8TxPgiCQKIokiiKZzWYSBIGp9uq6FsuyPtzc0QelLEvZbrfStq2kaSqe59FkuuCe1XUt2+1W8jyXsixNd8E5f9fT05OkaSpZlknbthKGoYRhKFVVSV3XZqMHmrmE3aBZlmUW8idJ8m7l5fu+pGkqbdvKy8vLWZNUbds2W4HXdS1ZlkkYhjw8Z4RTd3pPXdcXVV6e50mSJLJcLsVxHHEcR758+SJfvnwxlaPubEPzlrAbZHO1bVtxXVeiKJLlcilhGIrrur98cJ6eniTLMqnr2kxW/WyT1rIsqetaNpuNNE0jWZbJYrHghnzCbrczQXfJyght8mZZJlmWvTt45HmeZFkmSZJIWZby8vIiRVGYico0bwm73tI3szZTu83Vj4yUPj09yXK5NP0651SR+qBpYFZVJWmaymw24wb9xs+fP02ztbsE7Jywa5pGPM+TNE0lTdNf9qFalmWqPX0RFkUhVVWZX0U4ipGw61nTp9tcjeNY0jSV+Xz+6b/r+flZsiwTETk78E53OhYRieNYPM9jesqJw+EgdV3Lbrczc+guCZjj8Si2bUscx7JarT48WOQ4jiwWC1ksFmYnle6xmvTnXaFL6fv378xIvUKztdtkCcPwTaV1zgOjU0qutUtxFEWmyUTg/Rt0Otp6jdPcjsejHI9HMxhxbheCThLXvsPNZnPWSDCo7K4eclrJadP10jewZVkSRZGsVivzIJ7Th9f9PvXvKIpC4jiWOI4nG3qHw0F2u505svLcaSWnTVfXdeXLly+SZZlEUXTR/Rf53+i99vdut1vTvCb0CLu7h1wYhhJFkSRJcvUVDI7jSJIk5ve73e7sfiTdMUUfbJ3yMLWmbbfJqn1z2s96Sb+YDkbFcWyC7hrX1LIseXp6Es/zxPM88z1fI5wJO3w4PHQk7Zad/47jyNevX80DpZtEnlM5dh9mnTtWFIWpSMc+gPHz508pisL0hdV1La7rXqXzXyeJ32r0W/vztMJ7eXnhiM1zXh702X2+qaJ9c/c6BEfnfunC/0uatKcPqVaocRyPcvWFzmXb7XbmZXGtXYW7fbX60rj1IIIe2ah9edf6LFDZ4U3IaZM1SZKzRlovac7M53NZLpfiuq6Z7Hru1Ijuz9W2rVm5UVWVBEFgvoa4CkNXP2hzvTuN4xrz13QQom1bCYLATC+51wRux3HMZ08/C/qzEXqE3cUPj04pybJMlsvlwwLg+fnZzMfabrfSNM1FUxK6D76Ggoa6rtsNgsA8RH3ckVeDR8O7qiopy9IMPnRD4BphoC8Y7cZYLpcP6QKYz+dmorqu3WV6Cs3Yi9/iutynL6sRugvUtWl27bd6d+sorfSiKDJh++iHSkPudOBFRyxveQBOEASyWq0kjuNe9HX+/fff8vLyInmeX1ztU9lNuOn69etX0x/TF77vS5Ik4rqu6XC/9uicBkZRFGYhvFZ53aauhuyt+y51+yqt3vRLf6/f763Wl+qqCB1xDcOwN4M6+tnUzwNNWsLuUx9srWR0nlPf3pa+75vRRM/zzAjjtUNP+/W0qasj0adf+r1oRXhO0/e0SdoNMO1XPP3qVnHXnoqhlb1WuNpn27f1xo7jSBzH4rqueUFxji3N2A8/4KvV6lPLfR7pcDiYkdprrbj4yDXSgNENSLvVng5waPD96cHTQGvb1gwwdKs4ETHhplXMPbRtK57nybdv3wax+mS/38t6vZb1ek3YEXa//2Dbtm0+2EOahvHz508py/LNlkH3CIXTTnHbtt/tQ/xT1XXax6Yjp93/fs8O+KZpJAxDSdNUkiS52zSjawWe7q7M5GOase8+bL/bc67vZrOZacoEQWDmld26OXMaPt3pLJc6nSZy66DTgNU96HRZ3dA+C9qnq9uFafcDYQcR+XeTxdVqNdjVBDofT3fC1S2LtF/rHpXRkPdh0xeFbppwz7mUtwi81WolImIGLQi7idMA0FURY1g2ZVmWPD8/SxiGkiSJrNdrKctSmqZhWsI71ZyImAnCSZJIGIajqIRms5ksl0uxbVvW6/Xk5+G5U/+gu64rq9VqlEulHMeRKIrk27dvbybaTn1TyG6Vo6OsURSZM0LGtDGCNmlFxBzKNNX7Ptmw05uuC7jHeiKXLi/qzo0ry1LqujbTObQTe+z9Ot0+OT34KI5jiaJInp6eRvtzz+dzcV1Xqqq66tpqwm5AtI9uCkcPOo4jz8/P8vz8bPZz0z3SptKfo1NgoiiSNE0ntadftw9vs9lQ2U2BdtbrbrJD7oS+JPh07Wtd1+YcU6347j2X7ZZN1W7A6RQS/W9T27x0Pp+b4xy32+3kDvWZVNjpQ6yL+vu0BOwRgec4jvi+L57nmXNMu1/6Yuj7Yc7aPNXvsTupWX/VEeqpb0kfRZHZG/EWK24Iux6FnY66TXlb8veaOL7vy2KxkP1+/2ZbJF2W1a2KRf49J0FE7jrC996/212qpkvXdMAhCAJOVnunqk/TVLbb7aTm4E0q7HQhtx5YjPeDz/M8U/V2Kz2dpNxdL3tvp6s1tHrTL5020pfdWfoaeHpk5613iCHsHtTU0R1lpzAgcWmg6MtAqyNdu9odwX1vUf5pBdjtPviMbn+S9h12197+6ouA+/hLLU1TadtWNpvNJEZnJxN2uhRsyv10lwSf9u+JvN1uSRftd3cg6VYMp/1+Hw257gYC3SZqd4MBqvPLRFFk9gOcwoj86MNO9yFbrVZXPwFsys0g/dX3fXl+fjZ/djgc3qyPPQ2+334YOzsK69ZVrutSqd3wPuqRnf/5z3/MIUSE3VB/QNc1y6Zovt7nAdLm5yV9QTRH79ecTZLErKGmshtwVaeLugm6+zZ9uxUg+h94WZaZJu1Yq7tRh50uB+seNg3g/yVJ8maj1DEa5QQb7R/SBd5UGMCfux/0eRnrdJTRzibUw4sZfQU+Rrt8xjrJeHQ/lS5/0d0sqOqAz1V3cRz/35b4hF1Pw06XhHHgCPA5rutKmqYSBAFhN4SbpROIqeqAz1d33d1hCLue0hOhCDrg8sALw3BUKytGV9lpXx2A82nfHZVdT+nJUEwgBi7j+75EUSRBEBB2fWu+iojpawBwneJBJ+SPoTk7mspOR5EYgQV4pkYbdrZtmyYsAxPAdei8uyAIRjHRePA/QXcEFsD1jWVkdhSVnVZ1bAkEXJdlWaMZqBh82OkkYgYmgNsVE2OYZGyP5UbQVwfchuM4oygoBht2um5vbHOBgL4WFTpZf6hrZgdf2enpVwBuG3ZhGFLZPeQb/2e6Cae8A/dpymphMdRpKIMOOz2FCsDtdY+1JOzupGkasW1boigi7IA7hl0URWLb9iDn3NlDv/AA7mfIBcagw47+OuB+tN+OsLtz0AVBILPZjE8gcEez2WywE4wHG3ae5/HJAx7A8zzC7h6aphHP8wg74IFh53ne4AYp7KFebCYSA48RBMEgiw2biw1gCsXGYMOOwQngMXSQgrC7MVZNADyHkwi7sWwRDQyZrk0n7G5At5UZ8to8YExhp5XdULZ8GlzYMTAB9IM+i4TdDS8wfXbAYw1xYv8gBygA8CyOOux0BIjAA3gWRxt22iFKnx3QD9qlNJQBw8ENUNi2zWgs0IPiQ59DBihuWD5zGDbwWJZl0Wd3jzcKAJ5Fwg4Az+IYwg4ACDsAIOwAEHYAQNgBAGEHAIQdABB2AEDYAQBhBwCEHQAQdgAIOwAg7ACAsAMAwg4ACDsAIOwAgLADAMIOAAg7AIQdABB2AEDYAQBhBwCEHQAQdgBA2AEAYQcAhB0Awg4ACDsAIOwAgLADAMIOAAg7ACDsAICwAwDCDgBhBwCEHQAQdgBA2AEAYQcAhB0AEHYAQNgBAGEHgLADAMIOAAg7ACDsAICwAwDCDgAIOwAYUdhZlmW+APBMftZ/AQlq8R84r1sXAAAAAElFTkSuQmCC");

	public static final short WIDTH = 300;
	public static final short HEIGHT = 300;
	@Id
	@Column(name = "imagen_id",unique = true,updatable = false,insertable = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID imagenId;
	
	@Column(name = "file_name")
	private String fileName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="usuario_id")
	@XmlTransient
	private Usuario usuario;
	
	@Transient
	private byte[] image;
	
	public Imagen() {}
	
	public Imagen(String fileName, Usuario usuario,byte[] image) {
		super();
		this.image = image;
		this.fileName = fileName;
		this.usuario = usuario;
	}
	
	public Imagen(String fileName) {
		super();
		this.imagenId = UUID.randomUUID();
		this.fileName = fileName;
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
	
	public byte[] getImage64() {
	    try {
	        BufferedImage imagem = ImageIO.read(Files.newInputStream(Path.of(this.getFullPath())));
			BufferedImage new_img = new BufferedImage(64, 64, BufferedImage.TYPE_USHORT_555_RGB);

	        Graphics2D g = new_img.createGraphics();
	        g.drawImage(imagem, 0, 0, 64, 64, null);
	        g.dispose();
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(new_img, "png", baos);
	        
	        return baos.toByteArray();
	    } catch (IOException ex) {
			// ex.printStackTrace();
			System.out.println("Imagen não encontrada. Cuidado!");
	    }

		return SEM_IMAGE_BYTE;
	}
	
	public byte[] getImageForCatraca() {
	    try {	    	
			int widthI = Imagen.WIDTH;
			int heigthI = Imagen.HEIGHT;
			BufferedImage imagem = ImageIO.read(Files.newInputStream(Path.of(this.getFullPath())));
//			if(imagem.getHeight() < Imagen.HEIGHT || imagem.getWidth() < Imagen.WIDTH) {
			widthI = imagem.getWidth();
			heigthI = imagem.getHeight();
			float maior = 0;
			if(maior < widthI) {
				maior = widthI;
			}
			if(maior < heigthI) {
				maior = heigthI;
			}
			float aumento = Imagen.WIDTH / maior;
			
			widthI = (int) (widthI*aumento);
			heigthI = (int) (heigthI*aumento);
//			}
	    	
			BufferedImage new_img = new BufferedImage(widthI, heigthI, BufferedImage.TYPE_USHORT_555_RGB);

	        Graphics2D g = new_img.createGraphics();
	        g.drawImage(imagem, 0, 0, widthI, heigthI, null);
	        g.dispose();
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(new_img, "png", baos);
	        
	        return baos.toByteArray();
	    } catch (IOException ex) {
			System.out.println("Imagen nao encontrada");
	    }

		return SEM_IMAGE_BYTE;
	}

	@Override
	public UUID getMMId() {
		return this.imagenId;
	}

	public UUID getImagenId() {
		return imagenId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setImagenId(UUID imagenId) {
		this.imagenId = imagenId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getFullPath() {
		return RelatoriosPath.PATH_IMAGEN + this.fileName + RelatoriosPath.EXTENSAO;
	} 
	
	public byte[] getBytes() {
		try {
			if(this.image == null) {
				this.image = Files.readAllBytes(Path.of(this.getFullPath()));
			}
			return this.image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SEM_IMAGE_BYTE;
	}
	
	public void setBytes(byte[] img) {
		this.image = img;
	}
	
    public StreamedContent getImg() {
        try {
            return DefaultStreamedContent.builder()
                    .contentType("image/png")
                    .stream(() -> {
                        return new ByteArrayInputStream(getBytes());
                    })
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
