package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPinRequest {

	@JsonProperty
	private long pin;

	public CheckPinRequest() {
	}

	public CheckPinRequest(long pin) {
		this.pin = pin;
	}

	public long getPin() {
		return pin;
	}

	public void setPin(long pin) {
		this.pin = pin;
	}

}
