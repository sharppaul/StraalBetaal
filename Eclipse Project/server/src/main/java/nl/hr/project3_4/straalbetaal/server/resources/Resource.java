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
	@Path("checkpas") // Check if pas is a StraalBetaal pas
	public CheckPasResponse checkPas(CheckPasRequest request) {
		CheckPasResponse response = new CheckPasResponse(
				serv.checkCorrectBank(request.getBankID(), request.getPasID()));

		LOG.info("Post request for checkpas with bankID: " + request.getBankID() + " + pasID: " + request.getPasID());

		return response;
	}

	@POST
	@Path("checkpin")
	public CheckPinResponse checkPinCorrect(CheckPinRequest request) {
		CheckPinResponse response = new CheckPinResponse();

		response.setBlocked(serv.isPasBlocked(request.getPasID()));

		if (response.isBlocked()) {
			LOG.info("Post request for correct pin, but pin is blocked!");
			return response;
		} else {
			response.setCorrect(serv.isPinCorrect(request.getPasID(), request.getPinCode()));
			LOG.info("Post request for checkpin with pincode: " + request.getPinCode());

			return response;
		}
	}

	@GET
	@Path("test")
	public String testPage() {
		return "<h1>test</h1>";
	}

	@POST
	@Path("balance")
	public BalanceResponse getBalance(BalanceRequest request) {
		BalanceResponse response = new BalanceResponse(serv.getBalance(request.getPasID()));

		LOG.info("Post request for balance with pasID: " + request.getPasID());

		return response;
	}

	@POST
	@Path("withdraw")
	public WithdrawResponse withdraw(WithdrawRequest request) {
		WithdrawResponse response = new WithdrawResponse();

		// Beetje verwarrende benamingen, maar dus transactie is gefaald als
		// transactieBon = 0;
		long transactieBon = serv.withdraw(request.getPasID(), request.getPinAmount());
		if (transactieBon > 0) {
			response.setSucceeded(true);
			response.setTransactieNummer(transactieBon);
		}
		else {
			response.setSucceeded(false);
		}

		LOG.info("Post request for withdraw with pasID: " + request.getPasID() + " and amount: " + request.getPinAmount());

		return response;
	}

}
