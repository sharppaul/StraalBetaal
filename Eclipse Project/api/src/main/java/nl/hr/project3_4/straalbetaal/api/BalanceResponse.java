package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class BalanceResponse {

	@JsonProperty
	private final long balance;


	public BalanceResponse(long balance) {
		this.balance = balance;
	}

	public long getBalance() {
		return balance;
	}

}
