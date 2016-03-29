package nl.hr.project3_4.straalbetaal.comm;

public class ArduinoData {
	private boolean cardReceived = false;
	private int pinLength = 0;
	private Boolean wantsBon = null;
	private String pinEncrypted;
	private String cardId;
	private String billOption;
	private int pinAmount;
	private boolean amountDone = false;
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
		this.wantsBon = null;
		this.pinEncrypted = null;
		this.cardId = null;
		this.billOption = null;
		this.pinAmount = 0;
		this.amountDone = false;
		this.selectedChoice = "none";
		this.isReset = false;
		this.errorMessage = null;
		this.errored = false;
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
	public void setBon(Boolean option) {
		this.wantsBon = option;
	}

	public Boolean getBon() {
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
			amountDone();
		} else if (key.equals("b")) {
			this.pinAmount = 100;
			amountDone();
		} else if (key.equals("c")) {
			this.pinAmount = 200;
			amountDone();
		} else {
			if (this.pinAmount != 0) {
				StringBuilder _sb = new StringBuilder(Integer.toString(this.pinAmount));
				_sb.insert(0, key);

				this.pinAmount = Integer.parseInt(_sb.toString());
			}
		}
	}

	public int getAmount() {
		return this.pinAmount;
	}

	public void setChoice(String option) {
		this.selectedChoice = option;
	}

	public String getChoice() {
		return this.selectedChoice;
	}

	// NON FATAL ERROR HANDLING:
	public void error(String error) {
		this.errorMessage = error;
		this.errored = true;
	}

	public boolean isErrored() {
		return this.errored;
	}

	public String getError() {
		return this.errorMessage;
	}

	////////////////////// RESET STUFF:
	public void resetSession() {
		reset();
		this.isReset = true;
	}

	public boolean isReset() {
		return this.isReset;
	}
}
