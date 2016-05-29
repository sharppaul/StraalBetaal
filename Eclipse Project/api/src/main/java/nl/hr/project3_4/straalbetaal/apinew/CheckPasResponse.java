package nl.hr.project3_4.straalbetaal.apinew;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPasResponse {

	@JsonProperty
	private boolean doesExist;

	public CheckPasResponse() {
	}

	public CheckPasResponse(boolean doesExist) {
		this.setDoesExist(doesExist);
	}

	public boolean doesExist() {
		return doesExist;
	}

	public void setDoesExist(boolean doesExist) {
		this.doesExist = doesExist;
	}

}
