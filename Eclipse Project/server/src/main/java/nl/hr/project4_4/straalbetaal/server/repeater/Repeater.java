package nl.hr.project4_4.straalbetaal.server.repeater;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import nl.hr.project3_4.straalbetaal.api.BalanceRequest;
import nl.hr.project3_4.straalbetaal.api.BalanceResponse;
import nl.hr.project3_4.straalbetaal.api.CheckPasRequest;
import nl.hr.project3_4.straalbetaal.api.CheckPasResponse;
import nl.hr.project3_4.straalbetaal.api.CheckPinRequest;
import nl.hr.project3_4.straalbetaal.api.CheckPinResponse;
import nl.hr.project3_4.straalbetaal.api.WithdrawRequest;
import nl.hr.project3_4.straalbetaal.api.WithdrawResponse;
import nl.hr.project3_4.straalbetaal.server.dao.DataAccessObject;

public class Repeater {
	private static final Logger LOG = Logger.getLogger(Repeater.class.getName());
	private ClientBuilder cBuilder = ClientBuilder.newBuilder();
	private Client client;
	public DataAccessObject dao;

	private String target = null;

	public Repeater() {
		cBuilder.register(JacksonFeature.class);
		client = cBuilder.build();
		dao = new DataAccessObject();

	}

	
	public boolean target(int bankID) {
		this.target = dao.getBankIP(bankID);
		return (this.target != null);
	}

	public CheckPasResponse checkPas(CheckPasRequest request) {
		String path = "/checkpas";
		if (target(request.getBankID())) {
			LOG.info("Repeater - CheckPasRequest send to server!");
			CheckPasResponse response = client.target(target).path(path).request()
					.post(Entity.entity(request, MediaType.APPLICATION_JSON), CheckPasResponse.class);
			LOG.info("Repeater - CheckPasResponse received from server!");
			return response;
		} else {
			System.out.println("IP IS NULL");
			return new CheckPasResponse(false, false);
		}
	}

	public CheckPinResponse checkPincode(CheckPinRequest request) {
		String path = "/checkpin";

		if (target(request.getBankID())) {
			LOG.info("Repeater - CheckPincodeRequest send to server!");
			CheckPinResponse response = client.target(target).path(path).request()
					.post(Entity.entity(request, MediaType.APPLICATION_JSON), CheckPinResponse.class);
			LOG.info("Repeater - CheckPincodeResponse received from server!");
			return response;
		} else {
			CheckPinResponse response = new CheckPinResponse(false);
			response.setBlocked(false);
			return response;
		}
	}

	public BalanceResponse checkBalance(BalanceRequest request) {
		String path = "/balance";

		if (target(request.getBankID())) {
			LOG.info("Repeater - BalanceRequest send to server!");
			BalanceResponse response = client.target(target).path(path).request()
					.post(Entity.entity(request, MediaType.APPLICATION_JSON), BalanceResponse.class);
			LOG.info("Repeater - BalanceResponse received from server!");
			return response;
		} else {
			BalanceResponse response = new BalanceResponse(0L);
			return response;
		}
	}

	public WithdrawResponse withdrawMoney(WithdrawRequest request) {
		String path = "/withdraw";

		if (target(request.getBankID())) {
			LOG.info("Repeater - WithdrawRequest send to server!");
			WithdrawResponse response = client.target(target).path(path).request()
					.post(Entity.entity(request, MediaType.APPLICATION_JSON), WithdrawResponse.class);
			LOG.info("Repeater - WithdrawResponse received from server!");
			return response;
		} else {
			WithdrawResponse response = new WithdrawResponse(false,0);
			return response;
		}
	}
}
