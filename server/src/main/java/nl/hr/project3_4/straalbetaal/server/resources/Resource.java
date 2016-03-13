package nl.hr.project3_4.straalbetaal.server.resources;

import nl.hr.project3_4.straalbetaal.api.*;
import nl.hr.project3_4.straalbetaal.server.services.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Resource {

	private Service serv = new Service();


	@GET
	@Path("/{IBAN}&{pincode}")
	public String getUserID(@PathParam("IBAN") String iban,
						  @PathParam("pincode") long pincode) {
		return serv.getUserID(iban, pincode);
	}

	/*
	 * This and the method above could have been done together, but 
	 * because the DAO methods should have only 1 particular task, I 
	 * spread the calls into 2 different methods.
	 */
	@GET
	@Path("/{IBAN}&{pincode}/balance")
	public Long getBalance(@PathParam("IBAN") String iban) {
		return serv.getBalance(iban);
	}

	@POST
	@Path("/{IBAN}&{pincode}/withdraw")
	public WithdrawResponse withdraw(@PathParam("IBAN") String iban, WithdrawRequest request) {
		WithdrawResponse response = new WithdrawResponse();

		if(serv.withdraw(iban, request.getAmount())) {
			response.setResponse("Dank u voor pinnen.");
			response.setTransactionNumber(12345L); // Dummy
			return response;
		} else {
			response.setResponse("Saldo ontoereikend");
			// throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
		}
		return response; // CHANGE!
	}

}
