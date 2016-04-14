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

	private Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
	private static final String target = "http://145.24.222.208:8025";

	private String iban;

	/*
	 * I did it this way - get the IBAN and pincode on initialization - ,
	 * and not get the IBAN and pincode per method, because that is how we
	 * set our arduino code to also send the data to the client! (06-03-2016)
	 */
	public ClientBackEnd(String iban) {
		this.iban = iban;
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
		String path = "/" + iban + "/check";

		LOG.info("Client - CheckPincode Response send to server!");
		CheckPinResponse response = client.target(target).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), CheckPinResponse.class);
		return response;
	}

	public BalanceResponse checkBalance() {
		String path = "/" + iban + "/balance";

		LOG.info("Client - Balance Response send to server!");
		BalanceResponse response = client.target(target).path(path).request()
				.post(Entity.entity(null, MediaType.APPLICATION_JSON), BalanceResponse.class);
		return response;
	}

	// Not tested!
	public WithdrawResponse withdrawMoney(WithdrawRequest request) {
		String path = "/" + iban + "/withdraw";

		WithdrawResponse response = client.target(target).path(path).request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), WithdrawResponse.class);
		if(response.getTransactionNumber() == 0) {
			response.setResponse("Pinnen is helaas mislukt. Hebt u voldoende saldo?");
		}
		LOG.info("Client - Withdraw Response send to server!");
		return response;
	}

}
