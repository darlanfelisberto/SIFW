package br.edu.iffar.fw.classBag.db.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import br.com.feliva.authClass.models.AuthUser;
import br.com.feliva.authClass.models.Pessoa;
import br.com.feliva.sharedClass.db.Model;
import jakarta.persistence.*;
import org.primefaces.model.charts.pie.PieChartModel;

import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="usuarios",schema = "public")
public class Usuario extends Model<UUID> implements Serializable {
	private static final long serialVersionUID = 22021991L;

	@Id
	@Column(name = "usuario_id",insertable = true,updatable = false,unique = true)
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID usurioId;

	@Column(name = "token_ru")
	private String tokenRU;

	@JoinColumn(name = "pessoa_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Pessoa pessoa;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	@OrderBy(value = "dtCredito desc")
	private List<Credito> listCreditos;
		
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<Matricula> listMatricula;
	
	@OneToMany(mappedBy="usuario",fetch = FetchType.LAZY)
	private List<Servidor> listServidor;

	public String getTokenRU() {
		return tokenRU;
	}

	public void setTokenRU(String tokenRU) {
		this.tokenRU = tokenRU;
	}
	
	public Usuario() {
		super();
	}
	public Matricula getMatriculaAtiva() {
		return this.listMatricula.get(0);
	} 

    @XmlTransient
	public List<Credito> getListCreditos() {
		if(this.listCreditos == null)
			this.listCreditos = new ArrayList<Credito>();
		return this.listCreditos;
	}

	public Credito addCredito(Credito credito) {
		getListCreditos().add(credito);
		credito.setUsuario(this);

		return credito;
	}

	public Credito removeCredito(Credito credito) {
		getListCreditos().remove(credito);
		credito.setUsuario(null);
		return credito;
	}

	public String labelIniciais() {
		String[] nome = this.pessoa.getNome().split(" ");
		StringBuilder iniciais = new StringBuilder();
		for (String string : nome) {
			iniciais.append(string.toUpperCase().charAt(0) + ".");
		}
		return iniciais.toString();
	}
    
	public void setEventLimit(boolean eventLimit) {
    }
		  
    public static PieChartModel getPieChartCredito(List<Credito> lc) {
    	
    	Set<TipoCredito> tiposCreditos = new HashSet<TipoCredito>();
    	
    	Float []sum = new Float[TypeCredito.values().length];
    	Arrays.fill(sum, 0f);
    	
    	if(lc != null) {
    		lc.forEach(c-> {
    			tiposCreditos.add(c.getTipoCredito());
    			c.getTipoCredito().getTipoCreditoId().sumType(c, sum);
    		});
//    		tiposCreditos.remove(new TipoCredito(TypeCredito.TRANS_ENTRADA));
    		return TypeCredito.getPieChartModel(sum,tiposCreditos);
    	}else {
    		return null;
    	}
    }

	public List<Matricula> getListMatricula() {
		return listMatricula;
	}
	
	public void setListMatricula(List<Matricula> listMatricula) {
		this.listMatricula = listMatricula;
	}

	public void addMatricula(Matricula m) {
		if (this.listMatricula == null) {
			this.listMatricula = new ArrayList<Matricula>();
		}

		if(!this.listMatricula.contains(m)) {
			m.setUsuario(this);
			this.listMatricula.add(0, m);
		}
	}
	
	public void addServidor(Servidor s) {
		if (this.listServidor == null) {
			this.listServidor = new ArrayList<Servidor>();
		}

		if(!this.listServidor.contains(s)) {
			s.setUsuario(this);
			this.listServidor.add(0, s);
		}
	}
	
	public Matricula getMatriculaByIdMatricula(Integer idMatricula) {
		if(this.listMatricula == null) {
			return null;
		}
		for (Matricula mat : listMatricula) {
			if(mat.getIdMatricula().equals(idMatricula)) {
				return mat;
			}
		}
		return null;
	}

	public boolean permitirTirarFoto() {
		if(this.listMatricula != null) {
			for (Matricula mat : this.listMatricula) {
				if(mat.getUltimaSituacao().getSituacao().equals(TypeSituacao.ATIVA) &&
						(mat.getTipoVinculo().isIntegrado() || mat.getTipoVinculo().isSubSequente())) {
					return false;
				}
			}
		}
		
		return true;
	}

	public List<Servidor> getListServidor() {
		return listServidor;
	}

	public void setListServidor(List<Servidor> listServidor) {
		this.listServidor = listServidor;
	}

	public UUID getUsurioId() {
		return usurioId;
	}

	public void setUsurioId(UUID usurioId) {
		this.usurioId = usurioId;
	}

	public UUID getMMId() {
		return this.usurioId;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public static Usuario createUsuario(Pessoa pessoa){
		Usuario u = new Usuario();
		u.setTokenRU(UUID.randomUUID().toString());
		u.setPessoa(pessoa);
		return u;
	}
}