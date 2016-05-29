package nl.hr.project3_4.straalbetaal.server.resources;

import nl.hr.project3_4.straalbetaal.apinew.*;
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

	// Bank ID = Ergens die checkt of de pas van onze bank is of niet, 
	// is het niet van onze bank dan wordt het doorgestuurd!
	// Is het wel van onze bank, wordt het met de pasID afgehandeld!
	@POST
	@Path("checkpas") // Check als de pas op onze bank voorkomt!
	public CheckPasResponse getUserID(CheckPasRequest request) {
		CheckPasResponse response = new CheckPasResponse(serv.getUserID(request.getBankID(), request.getPasID()));

		LOG.info("Get request for userID with IBAN: " + request.getBankID());

		return response;
	}

	@POST
	@Path("checkpin")
	public CheckPinResponse getUserpin(CheckPinRequest request) {
		CheckPinResponse response = new CheckPinResponse(serv.getPinConfirmed(request.getPinCode());

		LOG.info("Get request for userID with pincode: " + request.getPin());

		return response;
	}

	@GET
	@Path("test")
	public String testPage() {
		return "<h1>test</h1>";
	}

	/*
	 * This and the method above could have been done together, but because the
	 * DAO methods should have only 1 particular task, I spread the calls into 2
	 * different methods.
	 */
	@POST
	@Path("balance")
	public BalanceResponse getBalance() {
		BalanceResponse response = new BalanceResponse(serv.getBalance(iban));

		LOG.info("Get request for balance with IBAN: " + iban);

		return response;
	}

	@POST
	@Path("withdraw")
	public WithdrawResponse withdraw(String iban, WithdrawRequest request) {
		WithdrawResponse response = new WithdrawResponse();

		LOG.info("Post request for withdraw with IBAN: " + iban + " and amount: " + request.getAmount());

		// Beetje verwarrende benamingen, maar dus transactie is gefaald als
		// transactieBon = 0;
		int transactieBon = serv.withdraw(iban, request.getAmount());
		if (transactieBon > 0) {
			response.setResponse("succes");
			response.setTransactionNumber(transactieBon);
		}
		/*
		 * if (serv.withdraw(iban, request.getAmount())) {
		 * response.setResponse("succes"); response.setTransactionNumber(12345);
		 * // Dummy }
		 */else {
			response.setResponse("fail");
			// throw new
			// BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(response).build());
		}
		return response;
	}

}
