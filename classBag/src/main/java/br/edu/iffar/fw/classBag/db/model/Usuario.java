package br.edu.iffar.fw.classBag.db.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.primefaces.model.charts.pie.PieChartModel;

import br.edu.iffar.fw.classBag.enun.TypeCredito;
import br.edu.iffar.fw.classBag.enun.TypeSituacao;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.xml.bind.annotation.XmlTransient;
//
//@Entity
//@Table(name="usuarios")
public class Usuario extends br.edu.iffar.fw.authClassShared.models.Usuario{

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	@OrderBy(value = "dtCredito desc")
	private List<Credito> listCreditos;
		
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<Matricula> listMatricula;
	
	@OneToMany(mappedBy="usuario",fetch = FetchType.LAZY)
	private List<Servidor> listServidor;
		
	@Column(name = "token_ru")
	private String tokenRU;
	
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
		String[] nome = this.nome.split(" ");
		StringBuilder iniciais = new StringBuilder();
		for (String string : nome) {
			iniciais.append(string.toUpperCase().charAt(0) + ".");
		}
		return iniciais.toString();
	}
    
    public void setUsuarioId(UUID usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		StringBuilder s = new StringBuilder();
		for (String string : nome.split(" ")) {
			s.append(" " + (string.length() > 2 ? string.substring(0, 1).toUpperCase() + string.substring(1, string.length()).toLowerCase() : string));
		}
		// esse trim remove o espaço colocado no for
		this.nome = s.toString().trim();;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if(cpf != null) {
			this.cpf = cpf.replaceAll("[^0-9]", "");
		} else {
			this.cpf = cpf;
		}
	}
	
	public LocalDate getDtnasc() {
		return dtnasc;
	}

	public void setDtnasc(LocalDate dtnasc) {
		this.dtnasc = dtnasc;
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
	
	public boolean isNovo() {
		return this.novo;
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

	public String getNomeCpf() {
		return this.cpf + " - " + this.nome;
	}

}