package nl.hr.project3_4.straalbetaal.comm;

public class ArduinoData {
	private String pinCode;
	private String cardID;
	private String numbers = "\\d+";
	private boolean ready;
	
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
		if(this.ready){
			System.out.println("CardID: " + getCardID() + "\nPinCode: " + getPinCode());
		}
	}
	
	public String getCardID() {
		return cardID;
	}
	
	public void setCardID(String cardID) {
		this.cardID = cardID;
		System.out.println("Card read.");
	}
	
	public String getPinCode() {
		return pinCode;
	}
	
	public void setPinCode(String pinCode) {
		if(pinCode.length() == 4 && pinCode.matches(numbers) ){
			this.pinCode = pinCode.substring(0,4);
			System.out.println("Pincode received.");
			this.setReady(true);
		} else {
			System.err.println("Pincode is not 4 characters long. Or is not only numbers.");
		}
	}
}
