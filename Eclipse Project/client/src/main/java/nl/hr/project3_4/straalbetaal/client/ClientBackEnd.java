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
	// private SSLContext s;
	private ClientBuilder cBuilder = ClientBuilder.newBuilder();
	private Client client;
	
	
	private static final String TARGET = "http://145.24.222.208:8025";


	/*
	 * I did it this way - get the IBAN and pincode on initialization - ,
	 * and not get the IBAN and pincode per method, because that is how we
	 * set our arduino code to also send the data to the client! (06-03-2016)
	 */
	
	public ClientBackEnd(String iban) {
		cBuilder.register(JacksonFeature.class);
		client = cBuilder.build();
	}


	/* This is for testing purposes, this main will be in the Client class,
	 * together with the ArduinoData and GUI.
	
	public static void main(String[] args) {
		// ClientBackEnd backEnd = new ClientBackEnd();
		ClientBackEnd backEnd = new ClientBackEnd("123456789", "3025");
		
		System.out.println("UserId: \t\t" + backEnd.checkPincode().getUserID());
		System.out.println("Balance: \t\t" + backEnd.checkBalance().getBalance());

		WithdrawRequest request = new WithdrawRequest();
		request.setAmount(5);
		
		WithdrawResponse response = backEnd.withdrawMoney(request);
		
		System.out.println("Withdraw Response: \t" + response.getResponse());
		System.out.println("Transaction Number: \t" + response.getTransactionNumber());
		
		backEnd = new ClientBackEnd("123456789", "3025");
		backEnd.checkPincode().getUserID();
		System.out.println("Balance: \t\t" + backEnd.checkBalance().getBalance());
	}
	 */

	public CheckPinResponse checkPincode(CheckPinRequest request) {
		String path = "/checkpin";

		LOG.info("Client - CheckPincode Response send to server!");
		CheckPinResponse response = client.target(TARGET).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), CheckPinResponse.class);
		return response;
	}

	public BalanceResponse checkBalance(BalanceRequest request) {
		String path = "/balance";

		LOG.info("Client - Balance Response send to server!");
		BalanceResponse response = client.target(TARGET).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), BalanceResponse.class);
		return response;
	}

	// Not tested!
	public WithdrawResponse withdrawMoney(WithdrawRequest request) {
		String path = "/withdraw";
		LOG.info("Client - Withdraw Request send to server!");
		WithdrawResponse response = client.target(TARGET).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), WithdrawResponse.class);
		if(!response.isSucceeded()) {
			//response.setResponse("Pinnen is helaas mislukt. Hebt u voldoende saldo?");
		}
		LOG.info("Client - Withdraw response received from server!");
		return response;
	}

}
