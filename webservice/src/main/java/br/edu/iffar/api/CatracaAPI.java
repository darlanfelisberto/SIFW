package br.edu.iffar.api;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import br.edu.iffar.fw.classBag.db.dao.RefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoRefeicaoDAO;
import br.edu.iffar.fw.classBag.db.dao.TipoVinculoDAO;
import br.edu.iffar.fw.classBag.db.dao.api.CatraSyncAPIDAO;
import br.edu.iffar.fw.classBag.db.dao.api.JAXImagenDAO;
import br.edu.iffar.fw.classBag.db.dao.api.JAXSaldoDAO;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamento;
import br.edu.iffar.fw.classBag.db.model.api.APIAgendamentosDisponibilizados;
import br.edu.iffar.fw.classBag.db.model.api.APIImagen;
import br.edu.iffar.fw.classBag.db.model.api.APIRefeicao2;
import br.edu.iffar.fw.classBag.db.model.api.APISaldo;


//http://localhost:8080/catraca/auth/catracaAPI/listAllGrupos
@Path("/auth/catracaAPI")
@RequestScoped
public class CatracaAPI implements Serializable{
	
	private static final long serialVersionUID = 22021991L;

	@Inject private JAXSaldoDAO jaxSaldoDAO;
	@Inject private JAXImagenDAO jaxImagenDAO;
	@Inject private RefeicaoDAO refeicaoDAO;
	@Inject private TipoRefeicaoDAO tipoRefeicaoDAO;
	@Inject private CatraSyncAPIDAO catraSync;
	@Inject private TipoVinculoDAO tipoVinculoDAO;
	
	@Inject private UpdateMethods updateMethods;
	
	@Context
    protected SecurityContext securityContext;
	
	// catraca/auth/catracaAPI/ping
	@GET                                                             
    @Path("/ping")
    public Response ping() {
		return Response.ok("On-line",MediaType.APPLICATION_JSON).build();
	}
	
	@GET                                                             
    @Path("/listAllUser")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listAllUser() {
		return Response.ok(this.jaxSaldoDAO.getListAllAPIUsuario(),MediaType.APPLICATION_JSON).build();
	}
		
	@GET                                                             
    @Path("/listAllTipoVinculo")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listAllTipoVinculo() {
		return Response.ok(this.tipoVinculoDAO.listAll(),MediaType.APPLICATION_JSON).build();
	}
	
	@GET                                                             
    @Path("/listAllSaldo")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listAllSaldo() {
		return Response.ok(this.jaxSaldoDAO.listAllAPISaldo(),MediaType.APPLICATION_JSON).build();
	}
	
	@GET                                                             
    @Path("/listAllTipoRefeicao")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listTipoRefeicao() {
		return Response.ok(this.tipoRefeicaoDAO.listAll(),MediaType.APPLICATION_JSON).build();
	}
	
	@GET                                                             
    @Path("/listAllRefeicoes2")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listAllRefeicoes2() {

		List<APIRefeicao2> l = this.refeicaoDAO.listAllRefeicoes2();
		
		return Response.ok(l,MediaType.APPLICATION_JSON).build();
	}
	
	@GET                                                             
    @Path("/allImages")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listImages() {
		List<APIImagen> l = null;
		try {
			List<APIImagen> lImagen = this.jaxImagenDAO.listAll();

			for (APIImagen apiImagen : lImagen) {
				apiImagen.getBytes();
			}
			l = lImagen;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok(l, MediaType.APPLICATION_JSON).build();
	}
	
	@GET                                                             
    @Path("/listAgendamentoByDateStart/{dateStart}")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listAgendamentoPorDataInicio(@PathParam("dateStart") String dateStart) {		
		List<APIAgendamento> l = this.catraSync.listFromDateStar(LocalDate.parse(dateStart, DateTimeFormatter.ISO_LOCAL_DATE));
		return Response.ok(l,MediaType.APPLICATION_JSON).build();
	}	

	@GET
	@Path("/getByUsername/{id}")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
	public Response getByUsername(@PathParam("id") String id) {
		APISaldo s = this.jaxSaldoDAO.findUserId(id);
		return Response.ok(s,MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/getByToken/{token}")
	@RolesAllowed({
			"IFFAR_ADMIN",
			"IFFAR_RU_CATRACA"
	})
	public Response getByToken(@PathParam("token") String id) {
		APISaldo s = this.jaxSaldoDAO.findUserByToken(id);
		return Response.ok(s, MediaType.APPLICATION_JSON).build();
	}
		
	@GET
	@Path("/existe_saldo/{id}")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
	public Response existeSaldo(@PathParam("id") String id) {
		APISaldo u = this.jaxSaldoDAO.findUserId(id);
//		Usuario user = this.usuarioDAO.getUsuarioByUserName(id);
		return Response.ok(u,MediaType.APPLICATION_JSON).build();
	}
	
	@GET                                                             
    @Path("/listAgeDisponibilidadeByDateStart/{dateStart}")
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
    public Response listAgeDisponibilidadeByDateStart(@PathParam("dateStart") String dateStart) {		
		List<APIAgendamentosDisponibilizados> l = new ArrayList<APIAgendamentosDisponibilizados>();
		try {
			l = this.catraSync.listAgendamentoDisponibiliadoFromDateStar(new SimpleDateFormat("yyyy-MM-dd").parse(dateStart));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Response.ok(l,MediaType.APPLICATION_JSON).build();
	}
		
	@POST
	@Path("/updateAgendamentoDisponibilizado")
	@Consumes({ MediaType.APPLICATION_JSON})
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
	public Response updateAgendamentoDisponibilizado(APIAgendamentosDisponibilizados apiAgeDisp) {
		try {
			this.updateMethods.updateAgendamento(apiAgeDisp);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (InternalServerErrorException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
		return Response.ok().build();
	}
	
	
	@POST
	@Path("/updateListAgendamentoDisponibilizado")
	@Consumes({ MediaType.APPLICATION_JSON})
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
	public Response syncListAgeDisponibilizado(List<APIAgendamentosDisponibilizados> listAgeDisp) {
		List<APIAgendamentosDisponibilizados> listOfFail = new ArrayList<APIAgendamentosDisponibilizados>();
		
		if(listAgeDisp != null) {
			listAgeDisp.forEach(apiAgeDisp->{
				try {
					this.updateMethods.updateAgendamento(apiAgeDisp);
				}catch (Exception e) {
					e.printStackTrace();
					listOfFail.add(apiAgeDisp);
				}
			});
		}
		return Response.ok(listOfFail,MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("/updateAgendamento")
	@Consumes({ MediaType.APPLICATION_JSON})
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
	public Response updateAgendamento(APIAgendamento apiAgendamento) {

		try {
			this.updateMethods.updateAgendamento(apiAgendamento);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (InternalServerErrorException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/updateListAgendamento")
	@Consumes({ MediaType.APPLICATION_JSON})
	@RolesAllowed({"IFFAR_ADMIN","IFFAR_RU_CATRACA"})
	public Response updateListAgendamento(List<APIAgendamento> listAgendamentos) {
		List<APIAgendamento> listOfFail = new ArrayList<APIAgendamento>();
		
		if(listAgendamentos != null) {
			listAgendamentos.forEach(agen->{
				try {
					this.updateMethods.updateAgendamento(agen);
				} catch (Exception e) {
					e.printStackTrace();
					listOfFail.add(agen);
				}
			});
		}
//		if(listOfFail.isEmpty()) {
//			return Response.ok().build();
//		}else {
		return Response.ok(listOfFail,MediaType.APPLICATION_JSON).build();
//		}
	}
}
