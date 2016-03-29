package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPincodeResponse {

	@JsonProperty
	private String userID;


	public CheckPincodeResponse() {}

	public CheckPincodeResponse(String userID) {
		this.userID = userID;
	}


	public String getUserID() {
		return userID;
	}

}
