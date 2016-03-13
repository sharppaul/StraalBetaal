package nl.hr.project3_4.straalbetaal.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.jackson.JacksonFeature;

import nl.hr.project3_4.straalbetaal.api.BalanceResponse;
import nl.hr.project3_4.straalbetaal.api.CheckPincodeResponse;
import nl.hr.project3_4.straalbetaal.api.WithdrawRequest;
import nl.hr.project3_4.straalbetaal.api.WithdrawResponse;

public class ClientBackEnd {

	private Client client = ClientBuilder.newClient().register(JacksonFeature.class);
	private String target = "http://145.24.222.208:8025";

	private String iban;
	private long pincode;

	private String path = "/" + iban + "&" + pincode;
	/*
	 * I did it this way - get the IBAN and pincode on initialization - ,
	 * and not get the IBAN and pincode per method, because that is how we
	 * set our arduino code to also send the data to the client!
	 */
	public ClientBackEnd(String iban, long pincode) {
		this.iban = iban;
		this.pincode = pincode;
	}


	public CheckPincodeResponse checkPincode() {
		return client.target(target).path(path)
				.request().get(CheckPincodeResponse.class);
	}

	public BalanceResponse checkBalance() {
		return client.target(target).path(path + "/balance")
				.request().get(BalanceResponse.class);
	}

	// DIDNT DO THIS YET!
	public WithdrawResponse withdrawMoney(WithdrawRequest request) {
		WithdrawResponse response = client.target(target).path(path + "/withdraw").request()
				.post(Entity.entity(request, MediaType.APPLICATION_JSON), WithdrawResponse.class);
		if(response.getTransactionNumber() == null) {
			response.setResponse("Pinnen is helaas mislukt. Hebt u voldoende saldo?");
		}
		return response;
	}

}
