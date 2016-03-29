package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPincodeResponse {

	@JsonProperty
	private final String userID;


	public CheckPincodeResponse(String userID) {
		this.userID = userID;
	}


	public String getUserID() {
		return userID;
	}

}
