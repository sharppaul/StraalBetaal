package nl.hr.project3_4.straalbetaal.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import org.glassfish.jersey.jackson.JacksonFeature;

import nl.hr.project3_4.straalbetaal.api.*;

public class ClientBackEnd {

	private static final Logger LOG = Logger.getLogger(ClientBackEnd.class.getName());
	private ClientBuilder cBuilder = ClientBuilder.newBuilder();
	private Client client;
	/*
	CostaMonday:	private static final String TARGET = "http://145.24.222.211:8025";
	GetBenkt:		private static final String TARGET = "http://178.62.244.124:8000";
	*/
	// BD96FA35E4E4
	// private static final String TARGET = "http://145.24.222.211:8025";
	private static final String TARGET = "http://145.24.222.208:8025";
	// private static final String TARGET = "http://localhost:8025";
	// private static final String TARGET = "http://178.62.244.124:8000";
	

	public ClientBackEnd() {
		cBuilder.register(JacksonFeature.class);
		client = cBuilder.build();
	}

	public CheckPasResponse checkPas(CheckPasRequest request) {
		String path = "/checkpas";

		LOG.info("Client - CheckPasRequest send to server!");
		CheckPasResponse response = client.target(TARGET).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), CheckPasResponse.class);
		LOG.info("Client - CheckPasResponse received from server!");
		return response;
	}

	public CheckPinResponse checkPincode(CheckPinRequest request) {
		String path = "/checkpin";

		LOG.info("Client - CheckPincodeRequest send to server!");
		CheckPinResponse response = client.target(TARGET).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), CheckPinResponse.class);
		LOG.info("Client - CheckPincodeResponse received from server!");
		return response;
	}

	public BalanceResponse checkBalance(BalanceRequest request) {
		String path = "/balance";

		LOG.info("Client - BalanceRequest send to server!");
		BalanceResponse response = client.target(TARGET).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), BalanceResponse.class);
		LOG.info("Client - BalanceResponse received from server!");
		return response;
	}

	public WithdrawResponse withdrawMoney(WithdrawRequest request) {
		String path = "/withdraw";
		LOG.info("Client - WithdrawRequest send to server!");
		WithdrawResponse response = client.target(TARGET).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), WithdrawResponse.class);
		LOG.info("Client - WithdrawResponse received from server!");
		return response;
	}

}
