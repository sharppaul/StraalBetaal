package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class WithdrawRequest {

	@JsonProperty
	private long amount;

	public WithdrawRequest() {
	}

	public WithdrawRequest(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

}
