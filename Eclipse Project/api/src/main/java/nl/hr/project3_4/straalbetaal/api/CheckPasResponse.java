package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPasResponse {

	@JsonProperty
	private boolean doesExist;
	@JsonProperty
	private boolean blocked;

	public CheckPasResponse() {
	}

	public CheckPasResponse(boolean doesExist, boolean blocked) {
		this.setDoesExist(doesExist);
		this.setBlocked(blocked);
	}

	public boolean doesExist() {
		return doesExist;
	}

	public void setDoesExist(boolean doesExist) {
		this.doesExist = doesExist;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

}
