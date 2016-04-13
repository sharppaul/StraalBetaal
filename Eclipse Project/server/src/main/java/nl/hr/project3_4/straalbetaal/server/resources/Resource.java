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

	@POST
	@Path("/{IBAN}/check")
	public CheckPinResponse getUserID(@PathParam("IBAN") String iban, CheckPinRequest request) {
		CheckPinResponse response = new CheckPinResponse(serv.getUserID(iban, request.getPin()));

		LOG.info("Get request for userID with IBAN: " + iban + " and pincode: " + request.getPin());

		return response;
	}

	/*
	 * This and the method above could have been done together, but because the
	 * DAO methods should have only 1 particular task, I spread the calls into 2
	 * different methods.
	 */
	@POST
	@Path("/{IBAN}/balance")
	public BalanceResponse getBalance(@PathParam("IBAN") String iban) {
		BalanceResponse response = new BalanceResponse(serv.getBalance(iban));

		LOG.info("Get request for balance with IBAN: " + iban);

		return response;
	}

	@POST
	@Path("/{IBAN}/withdraw")
	public WithdrawResponse withdraw(@PathParam("IBAN") String iban, WithdrawRequest request) {
		WithdrawResponse response = new WithdrawResponse();

		LOG.info("Post request for withdraw with IBAN: " + iban + " and amount: " + request.getAmount());

		if (serv.withdraw(iban, request.getAmount())) {
			response.setResponse("succes");
			response.setTransactionNumber(12345); // Dummy
		} else {
			response.setResponse("fail");
			// throw new
			// BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
		}
		return response;
	}

}
