//package br.edu.iffar.api;
//
//import java.io.Serializable;
//
//import jakarta.inject.Inject;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.core.Context;
//import jakarta.ws.rs.core.SecurityContext;
//
//import br.edu.iffar.fw.classBag.db.dao.api.CatraSyncAPIDAO;
//
////import br.edu.iffar.fw.classBag.db.dao.W_SaldoUsuarioDAO;
//
////import br.edu.iffar.fw.classBag.db.dao.W_SaldoUsuarioDAO;
//
//
//@Path("/public")
//public class PublicAPI implements Serializable{
//	
//	private static final long serialVersionUID = 22021991L;
//	@Inject private CatraSyncAPIDAO catraSync;
//	
//	@Context
//    protected SecurityContext securityContext;
////	KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) securityContext.getUserPrincipal();
////  System.out.println(keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername());
//	
////	@GET                                                             
////    @Path("/listAgeDisponibilidadeByDateStart/{dateStart}")
////    public Response listAgeDisponibilidadeByDateStart(@PathParam("dateStart") String dateStart) {		
////		List<APIAgendamentosDisponibilizados> l = new ArrayList<APIAgendamentosDisponibilizados>();
////		try {
////			l = this.catraSync.listAgendamentoDisponibiliadoFromDateStar(new SimpleDateFormat("yyyy-MM-dd").parse(dateStart));
////		} catch (ParseException e) {
////			e.printStackTrace();
////		}
////		return Response.ok(l,MediaType.APPLICATION_JSON).build();
////	}
//    
//}
//
//
//
//
//
//
//
//
//
//
