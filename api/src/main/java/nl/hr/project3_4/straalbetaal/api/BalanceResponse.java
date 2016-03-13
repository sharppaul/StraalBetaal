package nl.hr.project3_4.straalbetaal.api;

public class BalanceResponse {

	private final long balance;


	public BalanceResponse(long balance) {
		this.balance = balance;
	}

	public long getBalance() {
		return balance;
	}

}
