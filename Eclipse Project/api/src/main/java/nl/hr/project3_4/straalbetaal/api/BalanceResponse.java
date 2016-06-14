package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class BalanceResponse {

	@JsonProperty
	private long balance;

	public BalanceResponse() {
	}

	public BalanceResponse(long balance) {
		this.setBalance(balance);
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

}
