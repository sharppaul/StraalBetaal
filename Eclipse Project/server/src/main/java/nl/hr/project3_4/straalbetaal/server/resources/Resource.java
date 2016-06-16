package nl.hr.project3_4.straalbetaal.server.resources;

import nl.hr.project3_4.straalbetaal.api.*;
import nl.hr.project3_4.straalbetaal.encryption.BlackBox;
import nl.hr.project3_4.straalbetaal.server.services.Service;
import nl.hr.project4_4.straalbetaal.server.repeater.Repeater;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Resource {

	private static final Logger LOG = Logger.getLogger(Resource.class.getName());

	private static Service serv = new Service();
	private static Repeater repeater = new Repeater();
	public static final int BANKID = 0;

	@POST
	@Path("checkpas") // Check if pas exists
	public CheckPasResponse checkPas(CheckPasRequest request) {
		LOG.info("POST: CheckPasRequest, bankID: " + request.getBankID() + " pasID: " + BlackBox.b.decrypt(request.getPasID()));
		if (request.getBankID() != BANKID) {
			return repeater.checkPas(request);
		}
		CheckPasResponse response = new CheckPasResponse(serv.checkPasExist(request.getPasID()), serv.isPasBlocked(request.getPasID()));
		return response;
	}

	@POST
	@Path("checkpin")
	public CheckPinResponse checkPinCorrect(CheckPinRequest request) {
		LOG.info("POST: CheckPinRequest, CARD: " + request.getPasID());
		if (request.getBankID() != BANKID) {
			return repeater.checkPincode(request);
		}
		CheckPinResponse response = new CheckPinResponse();
		response.setBlocked(serv.isPasBlocked(request.getPasID()));

		if (response.isBlocked()) {
			response.setCorrect(false);
			return response;
		} else {
			response.setCorrect(serv.isPinCorrect(request.getPasID(), request.getPinCode()));
			return response;
		}
	}

	@GET
	@Path("test")
	public String testPage() {
		return "{\"test\":\"test\"}";
	}

	@POST
	@Path("balance")
	public BalanceResponse getBalance(BalanceRequest request) {
		LOG.info("POST: BalanceRequest with CARD: " + request.getPasID());
		if (request.getBankID() != BANKID) {
			return repeater.checkBalance(request);
		}
		BalanceResponse response = new BalanceResponse(serv.getBalance(request.getPasID()));
		return response;
	}

	@POST
	@Path("withdraw")
	public WithdrawResponse withdraw(WithdrawRequest request) {
		LOG.info("POST: WithdrawRequest, CARD: " + request.getPasID() + " AMOUNT: " + request.getPinAmount());
		if (request.getBankID() != BANKID) {
			return repeater.withdrawMoney(request);
		}
		WithdrawResponse response = new WithdrawResponse();
		long transactieNummer = serv.withdraw(request.getPasID(), request.getPinAmount());
		if (transactieNummer > 0) {
			response.setSucceeded(true);
			response.setTransactieNummer(transactieNummer);
		} else {
			response.setSucceeded(false);
		}
		return response;
	}

}
