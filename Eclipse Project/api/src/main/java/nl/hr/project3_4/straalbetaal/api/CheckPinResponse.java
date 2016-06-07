package nl.hr.project3_4.straalbetaal.api;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPinResponse {
	@JsonProperty
	private boolean correct;
	@JsonProperty
	private boolean blocked;
	
	public CheckPinResponse(){
		
	}
	
	public CheckPinResponse(boolean correct){
		this.setCorrect(correct);
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
