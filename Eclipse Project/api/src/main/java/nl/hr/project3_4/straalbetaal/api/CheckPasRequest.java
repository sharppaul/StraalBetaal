package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

import nl.hr.project3_4.straalbetaal.encryption.BlackBox;

public class CheckPasRequest {
	@JsonProperty
	private int bankID;
	@JsonProperty
	private String pasID;

	
	public CheckPasRequest() {
	}

	public CheckPasRequest(int bankID, String pasID) {
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
		return BlackBox.b.decrypt(this.pasID);
		
	}

	public void setPasID(String pasID) {
		this.pasID = BlackBox.b.encrypt(pasID);
	}

}
