package nl.hr.project3_4.straalbetaal.api;

public class CheckPincodeResponse {

	private final String userID;


	public CheckPincodeResponse(String userID) {
		this.userID = userID;
	}


	public String getUserID() {
		return userID;
	}

}
