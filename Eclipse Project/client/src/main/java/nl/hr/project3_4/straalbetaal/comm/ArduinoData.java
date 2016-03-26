package nl.hr.project3_4.straalbetaal.comm;

public class ArduinoData {
	private boolean cardReceived = false;
	private int pinLength = 0;
	private boolean wantsBon;
	private String pinEncrypted;
	private String cardId;
	private String billOption;
	private int pinAmount;
	private boolean amountDone = false;
	private boolean firstDigitAmount;
	private String selectedChoice = "none";
	private boolean isReset;
	private String errorMessage;
	private boolean errored;

	public ArduinoData() {
		// stuff here
	}

	public void reset() {
		this.cardReceived = false;
		this.pinLength = 0;
		this.pinEncrypted = "";
		this.cardId = "";
		this.billOption = "";
		this.pinAmount = 0;
		this.amountDone = false;
		this.firstDigitAmount = true;
		this.selectedChoice = "none";
		
	}


	////////////////////// CARD RECEIVED::
	public void cardReceived() {
		this.cardReceived = true;
		this.isReset = false;
	}

	public boolean isCardReceived() {
		return this.cardReceived;
	}

	////////////////////// PIN STUFF:
	public void resetPin() {
		this.pinLength = 0;
		this.pinEncrypted = "";
	}

	public int getPinLength() {
		return this.pinLength;
	}

	public void addToPinLength() {
		this.pinLength++;
	}

	public void receivePin(String pin, String kaartid) {
		this.pinEncrypted = pin;
		this.cardId = kaartid;
	}

	public String getPinEncrypted() {
		return this.pinEncrypted;
	}

	public String getCard() {
		return this.cardId;
	}

	////////////////////// BON STUFF:
	public void setBon(boolean option) {
		this.wantsBon = option;
	}

	public boolean getBon() {
		return this.wantsBon;
	}

	////////////////////// BILL STUFF:
	public void chooseBill(String option) {
		this.billOption = option;
	}

	public String getBillOption() {
		return this.billOption;
	}

	////////////////////// AMOUNT STUFF:
	public void amountDone() {
		this.amountDone = true;
	}

	public boolean isAmountDone() {
		return this.amountDone;
	}

	public void amountReset() {
		this.pinAmount = 0;
	}

	public void amountKey(String key) {
		if (key.equals("a")) {
			this.pinAmount = 50;
			this.firstDigitAmount = true;
		} else if (key.equals("b")) {
			this.pinAmount = 100;
			this.firstDigitAmount = true;
		} else if (key.equals("c")) {
			this.pinAmount = 200;
			this.firstDigitAmount = true;
		} else {
			if (firstDigitAmount) {
				this.pinAmount = 0;
				firstDigitAmount = false;
			}
			this.pinAmount = Integer.parseInt((Integer.toString(this.pinAmount) + key));
		}
	}

	public void setChoice(String option) {
		this.selectedChoice = option;
	}

	public String getChoice() {
		return this.selectedChoice;
	}
	//NON FATAL ERROR HANDLING:
	public void error(String error) {
		this.errorMessage = error;
		this.errored = true;
	}
	
	public boolean isErrored(){
		return this.errored;
	}
	
	public String getError(){
		return this.errorMessage;
	}
	//////////////////////RESET STUFF:
	public void resetSession() {
		reset();
		this.isReset = true;
	}
	
	public boolean isReset(){
		return this.isReset;
	}
}
