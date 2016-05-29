package nl.hr.project3_4.straalbetaal.apinew;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPinRequest {

	@JsonProperty
	private String pinCode;
	@JsonProperty
	private int bankID;
	@JsonProperty
	private String pasID;

	public CheckPinRequest() {
	}

	public CheckPinRequest(int bankID, String pasID, String pinCode) {
		this.setBankID(bankID);
		this.setPasID(pasID);
		this.setPinCode(pinCode);
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

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

}
