package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPinResponse {

	@JsonProperty
	private String userID;

	public CheckPinResponse() {
	}

	public CheckPinResponse(String userID) {
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
