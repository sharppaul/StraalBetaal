package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class WithdrawResponse {

	@JsonProperty
	private String response;
	@JsonProperty
	private Long transactionNumber = null;


	public WithdrawResponse() {}


	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}

	public Long getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(Long transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

}
