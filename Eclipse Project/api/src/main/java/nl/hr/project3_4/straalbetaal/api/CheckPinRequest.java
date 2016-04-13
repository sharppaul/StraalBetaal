package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPinRequest {

	@JsonProperty
	private String pin;

	public CheckPinRequest() {
	}

	public CheckPinRequest(String pin) {
		this.pin = pin;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

}
