package org.hr.project3_4.straalBetaal.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hr.project3_4.straalBetaal.entities.User;
import org.hr.project3_4.straalBetaal.services.UpdateSaldoService;

@Path("/saldos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

	private UpdateSaldoService service = new UpdateSaldoService();

	@GET
	public User getAllSaldos() {
		return service.updateSaldo();
	}

}
