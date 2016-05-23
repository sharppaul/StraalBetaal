package nl.hr.project3_4.straalbetaal.apinew;

import org.codehaus.jackson.annotate.JsonProperty;

public class CheckPinResponse {
	@JsonProperty
	private boolean isCorrect;
	
	public CheckPinResponse(){
		
	}
	
	public CheckPinResponse(boolean isCorrect){
		this.setCorrect(isCorrect);
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
}
