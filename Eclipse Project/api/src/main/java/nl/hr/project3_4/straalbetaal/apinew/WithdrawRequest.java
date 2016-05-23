package nl.hr.project3_4.straalbetaal.apinew;

import org.codehaus.jackson.annotate.JsonProperty;

public class WithdrawRequest {
	@JsonProperty
	private
	long pinAmount;
	@JsonProperty
	private int bankID;
	@JsonProperty
	private String pasID;

	public WithdrawRequest() {
	}

	public WithdrawRequest(int bankID, String pasID, long pinAmount) {
		this.setBankID(bankID);
		this.setPasID(pasID);
		this.setPinAmount(pinAmount);
	}

	public int getBankID() {
		return this.bankID;
	}

	public void setBankID(int bankID) {
		this.bankID = bankID;
	}
	
	public String getPasID(){
		return this.pasID;
	}
	
	public void setPasID(String pasID){
		this.pasID = pasID;
	}

	public long getPinAmount() {
		return pinAmount;
	}

	public void setPinAmount(long pinAmount) {
		this.pinAmount = pinAmount;
	}
}
