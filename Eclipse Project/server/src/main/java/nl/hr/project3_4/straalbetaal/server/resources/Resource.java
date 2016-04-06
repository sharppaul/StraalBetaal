package nl.hr.project3_4.straalbetaal.server.resources;

import nl.hr.project3_4.straalbetaal.api.*;
import nl.hr.project3_4.straalbetaal.server.services.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Resource {

	private static final Logger LOG = Logger.getLogger(Resource.class.getName());

	private static Service serv = new Service();


	@GET
	@Path("/{IBAN}&{pincode}")
	public CheckPincodeResponse getUserID(@PathParam("IBAN") String iban,
						  @PathParam("pincode") long pincode) {
		CheckPincodeResponse response = new CheckPincodeResponse(serv.getUserID(iban, pincode));
		LOG.info("Get request for userID with IBAN: " + iban + " and pincode: " + pincode);
		// JSON format -> Probably gonna use a Json format for clarity

		return response;	// ("{\"userid\":\"" + serv.getUserID(iban, pincode) + "\"}"); --> This is the old JSON object, but we should use API!
	}

	/*
	 * This and the method above could have been done together, but 
	 * because the DAO methods should have only 1 particular task, I 
	 * spread the calls into 2 different methods.
	 */
	@GET
	@Path("/{IBAN}&{pincode}/balance")
	public BalanceResponse getBalance(@PathParam("IBAN") String iban) {
		BalanceResponse response = new BalanceResponse(serv.getBalance(iban));
		LOG.info("Get request for balance with IBAN: " + iban);

		return response;
	}

	@POST
	@Path("/{IBAN}&{pincode}/withdraw")
	public WithdrawResponse withdraw(@PathParam("IBAN") String iban, WithdrawRequest request) {
		WithdrawResponse response = new WithdrawResponse();

		LOG.info("Post request for withdraw with IBAN: " + iban + " and amount: " + request.getAmount());

		if(serv.withdraw(iban, request.getAmount())) {
			response.setResponse("Dank u voor pinnen.");
			response.setTransactionNumber(12345); // Dummy
			return response;
		} else {
			response.setResponse("Saldo ontoereikend");
			// throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
		}
		return response;
	}

}
