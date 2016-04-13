package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class WithdrawResponse {

	@JsonProperty
	private String response;
	@JsonProperty
	private int transactionNumber = 0;

	public WithdrawResponse() {
	}

	public WithdrawResponse(String response, int transactionNumber) {
		this.response = response;
		this.transactionNumber = transactionNumber;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

}
