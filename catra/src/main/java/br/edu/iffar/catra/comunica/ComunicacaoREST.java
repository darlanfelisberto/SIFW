package br.edu.iffar.catra.comunica;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.persistence.RollbackException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.adapters.ServerRequest.HttpFailure;
import org.keycloak.common.VerificationException;

import br.edu.iffar.catra.dao.APIAgendamentosDisponibilizadosDAO;
import br.edu.iffar.catra.dao.APISaldoDAO;
import br.edu.iffar.catra.dao.AgendamentoAPIDAO;
import br.edu.iffar.catra.forms.FormCatra;
import br.edu.iffar.catra.util.Log;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APIImagen;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuario;
import br.edu.iffar.fw.classBag.db.model.api.APIUsuarioRefeicao;

/**
 *
 * @author qwerty
 */
public class ComunicacaoREST extends ComunicacaoLocal implements CatracaComunicacao{
	
	private APISaldoDAO saldoDAO =  new APISaldoDAO();

	public ComunicacaoREST() {
		super();

	}

	private static class NullHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private static class NullX509TrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}
	
	public static Client createClient() {
		SSLContext context;
		try {
			context = SSLContext.getInstance("TLSv1.2");
			final TrustManager[] trustManagerArray = {new NullX509TrustManager()};
			context.init(null, trustManagerArray, null);
			
			return ClientBuilder.newBuilder()
					.hostnameVerifier(new NullHostnameVerifier())
					.sslContext(context)
					.register(ObjectMapperContextResolver.class)
					.build();
		}
		catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}
		
		return ResteasyClientBuilder.newClient();
	}

	public List<APIUsuario> getList() throws IOException, HttpFailure, VerificationException {
		List<APIUsuario> lista = null;
		Client client = createClient();
		lista = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API + "/listAllUser", new GenericType<List<APIUsuario>>() {
		});
		client.close();
		return lista.parallelStream().filter(e -> e != null).collect(Collectors.toList());
	}

	
	
	public List<TipoVinculo> listALLTipoVinculo() throws IOException, HttpFailure, VerificationException {
		List<TipoVinculo> lista = null;
		Client client = createClient();
		lista = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API +"/listAllTipoVinculo", new GenericType<List<TipoVinculo>>() {
		});
		client.close();
		return lista.parallelStream().filter(e -> e != null).collect(Collectors.toList());

	}
	
	public List<APISaldo> listALLSaldos()throws IOException, HttpFailure, VerificationException  {
		List<APISaldo> lista = null;
		Client client = createClient();
		lista = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API +"/listAllSaldo", new GenericType<List<APISaldo>>() {
		});
		client.close();
		return lista.parallelStream().filter(e -> e != null).collect(Collectors.toList());

	}
	

	@Override
	public APISaldo findSaldoByUsername(String username) throws Exception{
		APISaldo u = null;
		try {
			Client client = createClient();
			u = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API +"/getByUsername/" + username, APISaldo.class);
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ocorreu um problema de comunicação com o web service!");
		}
				
		if(u != null) {
	    	try {
	        	this.saldoDAO.salvar(u);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else {
    		throw new Exception("Usuário não foi encontrado!");
    	}
		return u;
	}

	public List<TipoRefeicao> listAllTipoRefeicao() throws IOException, HttpFailure, VerificationException {
		List<TipoRefeicao> lista = null;
		Client client = createClient();
		lista = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API +"/listAllTipoRefeicao", new GenericType<List<TipoRefeicao>>() {
		});
		client.close();
		
		return lista.parallelStream().filter(e -> e != null).collect(Collectors.toList());
	}
	
	public List<APIImagen> listAllImages() throws IOException, HttpFailure, VerificationException {
		List<APIImagen> lista = null;
		Client client = createClient();
		lista = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API + "/allImages", new GenericType<List<APIImagen>>() {
		});
		client.close();
		
		return lista.parallelStream().filter(e -> e != null).collect(Collectors.toList());
	}
	
	public List<APIUsuarioRefeicao> listAllAPIRefeicoes() throws IOException, HttpFailure, VerificationException {
		List<APIUsuarioRefeicao> list = null;

		Client client = createClient();
		list = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API +"/listAllRefeicoes2", new GenericType<List<APIUsuarioRefeicao>>() {
		});
		client.close();
		
		return list;
	}
	
	public List<APIAgendamento> listAgendamentoByDateStart(LocalDate dateStart) throws IOException, HttpFailure, VerificationException {
		List<APIAgendamento> list = null;
		String data = dateStart.format(DateTimeFormatter.ISO_LOCAL_DATE);
		Client client = createClient();
		list = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API +"/listAgendamentoByDateStart/" + data, new GenericType<List<APIAgendamento>>() {
		});
		client.close();
		return list;
	}
	
	public List<APIAgendamentosDisponibilizados> listAgeDisponibilizadosByDateStart(LocalDate dateStart) throws IOException, HttpFailure, VerificationException {
		List<APIAgendamentosDisponibilizados> list = null;
		String data = dateStart.format(DateTimeFormatter.ISO_LOCAL_DATE);
		Client client = createClient();
		list = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API +"/listAgeDisponibilidadeByDateStart/" + data, new GenericType<List<APIAgendamentosDisponibilizados>>() {
		});
		client.close();
		return list;
	}
		
	public void sendListCredito(List<APIAgendamento> listAgendamento) throws FalhaNaComunicacao {
		
		Client clients = createClient();
		AgendamentoAPIDAO agenDAO = new AgendamentoAPIDAO();
		try {
			Log.info("---------- INICIO SYNC AGENDAMENTO ----------");
			listAgendamento.forEach(agendamento -> {
				agendamento.getCredito().setSincronizado(LocalDateTime.now());
			});
			
			Response r = KeycloakSession.getCatracaInstance().post(clients, KeycloakSession.PATH_CATRA_AUTH_API +"/updateListAgendamento", Entity.entity(listAgendamento, MediaType.APPLICATION_JSON_TYPE));
			Log.info("Response online:" + r.getStatusInfo());
			if(r.getStatus() == 200) {
				List<APIAgendamento> listFail = r.readEntity(new GenericType<List<APIAgendamento>>() {});
				
				if(!listFail.isEmpty()) {
					Log.info(FALHA_SALVAR_LISTA_ONLINE);
					FormCatra.getInstance().showMessageError(FALHA_SALVAR_LISTA_ONLINE+" Contate o admin.");
				}
				
				listAgendamento.forEach(agen -> {
					if(!listFail.contains(agen)) {
						try {
							agenDAO.salvar(agen);
						} catch (Exception e) {
							e.printStackTrace();
							Log.info(FALHA_SALVAR_LOCAL);
						}
						Log.info(SUCESSO_SALVAR_ONLINE, agen);
					}else {
						Log.info(FALHA_SALVAR_LISTA_ONLINE, agen);
					}
				});
			}
			clients.close();
			
		} catch (IOException | HttpFailure| VerificationException e) {
			e.printStackTrace();
			clients.close();
			throw new FalhaNaComunicacao("Falha na cominicação!");
		}finally {
			Log.info("---------- FIM SYNC AGENDAMENTO ----------");
		}
	}
	
	
	public void syncAgeDisp(List<APIAgendamentosDisponibilizados> listAgeDisp) throws FalhaNaComunicacao {
		Client clients = createClient();
		APIAgendamentosDisponibilizadosDAO ageDispDAO = new APIAgendamentosDisponibilizadosDAO();
		try {
			Log.info("---------- INICIO SYNC AGENDAMENTOS DISPONIBILIZADOS ----------");
			listAgeDisp.forEach(ageDisp -> {
				LocalDateTime now = LocalDateTime.now();
				ageDisp.setSincronizado(now);
				ageDisp.getAgendamento().getCredito().setSincronizado(now);
			});
			
			Response r = KeycloakSession.getCatracaInstance().post(clients, KeycloakSession.PATH_CATRA_AUTH_API +"/updateListAgendamentoDisponibilizado", Entity.entity(listAgeDisp, MediaType.APPLICATION_JSON_TYPE));
			Log.info("Response online:" + r.getStatusInfo());
			if(r.getStatus() == 200) {
				List<APIAgendamentosDisponibilizados> listFail = r.readEntity(new GenericType<List<APIAgendamentosDisponibilizados>>() {});
				
				if(!listFail.isEmpty()) {
					Log.info(FALHA_SALVAR_LISTA_ONLINE);
					FormCatra.getInstance().showMessageError(FALHA_SALVAR_LISTA_ONLINE+" Contate o admin.");
				}
				
				listAgeDisp.forEach(ageDisp -> {
					if(!listFail.contains(ageDisp)) {
						try {
							ageDispDAO.salvar(ageDisp);
							Log.info(SUCESSO_SALVAR_LOCAL, ageDisp);
						} catch (Exception e) {
							e.printStackTrace();
							Log.info(FALHA_SALVAR_LOCAL, ageDisp);
						}
					}else {
						Log.info(FALHA_SALVAR_ONLINE, ageDisp);
					}
				});
			}
			clients.close();
		} catch (IOException | HttpFailure| VerificationException e) {
			e.printStackTrace();
			clients.close();
			throw new FalhaNaComunicacao("Falha Durante a sincronização dos Agendamentos Disponibilizados.");
		}finally {
			Log.info("---------- FIM SYNC AGENDAMENTOS DISPONIBILIZADOS ----------");
		}
	}
	
	@Override
	public APISaldo findSaldoByTokenRU(String token) throws Exception {
		APISaldo u = null;
		try {
			Client client = createClient();
			u = KeycloakSession.getCatracaInstance().get(client, KeycloakSession.PATH_CATRA_AUTH_API + "/getByToken/" + token, APISaldo.class);
			client.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Ocorreu um problema de comunicação com o web service!");
		}

		if(u != null) {
			try {
				this.saldoDAO.salvar(u);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new Exception("Usuário não foi encontrado!");
		}
		return u;
	}
	
	public void updateAgendamento(APIAgendamentosDisponibilizados apiAgeDisp)  throws FalhaNaComunicacao, RollbackException, Exception {
		Client clients = createClient();
		try {
			super.updateAgendamento(apiAgeDisp);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new FalhaPassagemBloqueada("Falha ao salvar dados do usuário!");
		}
		
		LocalDateTime now = LocalDateTime.now();
		apiAgeDisp.getAgendamento().getCredito().setSincronizado(now);
		apiAgeDisp.setSincronizado(now);
		
		Response response;
		try {
			response = KeycloakSession.getCatracaInstance().post(clients, KeycloakSession.PATH_CATRA_AUTH_API +"/updateAgendamentoDisponibilizado", Entity.json(apiAgeDisp));
			clients.close();
		} catch (IOException | HttpFailure| VerificationException e) {
			e.printStackTrace();
			clients.close();
			Log.info(FALHA_SALVAR_ONLINE, apiAgeDisp);
			throw new FalhaPassagemPermitida("Falha ao sincronizar os dados do usuário com o WebService!");
		}
				
		if(response.getStatus() == 200) {
			try {
				Log.info(SUCESSO_SALVAR_ONLINE,apiAgeDisp);
				super.updateAgendamento(apiAgeDisp);				
			} catch (Exception e1) {
				e1.printStackTrace();
				Log.info(FALHA_SALVAR_LOCAL, apiAgeDisp,"Falha ao salvar reposta(200) do WebService!");
				throw new FalhaPassagemPermitida("Falha ao salvar reposta(200) do WebService!");
			}
		}else {
			Log.info(FALHA_SALVAR_ONLINE, apiAgeDisp,"Status:"+response.getStatus());
			throw new FalhaPassagemPermitida("Falha ao sincronizar os dados do usuário com o WebService!");
		}
	}
	
	public void updateAgendamento(APIAgendamento apiAngedamento)  throws FalhaNaComunicacao, RollbackException, Exception{
		Client clients = createClient();
		try {
			super.updateAgendamento(apiAngedamento);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new FalhaPassagemBloqueada("Falha ao salvar dados do usuário!");
		}
		
		LocalDateTime now = LocalDateTime.now();
		apiAngedamento.getCredito().setSincronizado(now);		

		Response response;
		try {
			response = KeycloakSession.getCatracaInstance().post(clients, KeycloakSession.PATH_CATRA_AUTH_API +"/updateAgendamento", Entity.json(apiAngedamento));
			clients.close();
		} catch (IOException | HttpFailure| VerificationException e) {
			Log.info(FALHA_SALVAR_ONLINE, apiAngedamento);
			e.printStackTrace();
			clients.close();
			throw new FalhaPassagemPermitida("Falha ao sincronizar os dados do usuário com o WebService!");
		}
			
		if(response.getStatus() == 200) {
			try {
				Log.info(SUCESSO_SALVAR_ONLINE, apiAngedamento);
				super.updateAgendamento(apiAngedamento);
			} catch (Exception e1) {
				e1.printStackTrace();
				Log.info(FALHA_SALVAR_LOCAL, apiAngedamento,"Falha ao salvar reposta(200) do WebService!");
				throw new FalhaPassagemPermitida("Falha ao salvar reposta(200) do WebService!");
			}
		}else {
			Log.info(FALHA_SALVAR_ONLINE, apiAngedamento,"Status:"+response.getStatus());
			throw new FalhaPassagemPermitida("Falha ao sincronizar os dados do usuário com o WebService!");
		}
	}
}
