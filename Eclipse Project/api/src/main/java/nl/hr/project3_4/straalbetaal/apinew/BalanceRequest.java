package nl.hr.project3_4.straalbetaal.apinew;

import org.codehaus.jackson.annotate.JsonProperty;

public class BalanceRequest {

	@JsonProperty
	private int bankID;
	@JsonProperty
	private String pasID;

	public BalanceRequest() {
	}

	public BalanceRequest(int bankID, String pasID) {
		this.setBankID(bankID);
		this.setPasID(pasID);
	}

	public int getBankID() {
		return this.bankID;
	}

	public void setBankID(int bankID) {
		this.bankID = bankID;
	}

	public String getPasID() {
		return this.pasID;
	}

	public void setPasID(String pasID) {
		this.pasID = pasID;
	}

}
