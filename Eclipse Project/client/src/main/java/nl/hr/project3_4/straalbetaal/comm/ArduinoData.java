package nl.hr.project3_4.straalbetaal.comm;

public class ArduinoData {
	private boolean cardReceived = false;
	private int pinLength = 0;
	private Boolean wantsBon = null;
	private Boolean wantDonate = null;
	private String pinCode;
	private String cardId;
	private String billOption;
	private int pinAmount;
	private boolean amountDone = false;
	private String selectedChoice = "none";
	private boolean isReset;
	private String errorMessage;
	private boolean errored;
	private boolean pressedBack = false;
	private boolean pinReceived = false;
	private String language = "EN";
	private String dispenserAmounts;
	private int bankID;
	
	public ArduinoData() {
		reset();
	}

	public void reset() {
		this.cardReceived = false;
		this.pinLength = 0;
		this.wantsBon = null;
		this.setDonate(null);
		this.pinCode = null;
		this.cardId = null;
		this.billOption = null;
		this.pinAmount = 0;
		this.amountDone = false;
		this.selectedChoice = "none";
		this.isReset = false;
		this.errorMessage = null;
		this.errored = false;
		this.setPressedBack(false);
		this.setPinReceived(false);
		this.setLanguage("EN");
	}

	////////////////////// CARD RECEIVED::
	public boolean isCardReceived() {
		return this.cardReceived;
	}
	
	public void receiveCard(String kaartid){
		this.isReset = false;
		this.cardId = kaartid;
		this.cardReceived = true;
	}
	
	public String getCard() {
		return this.cardId;
	}
	
	public int getBankID() {
		return this.bankID;
	}
	
	public void setBankID(int bankid) {
		this.bankID = bankid;
	}

	////////////////////// PIN STUFF:
	public void resetPin() {
		this.pinLength = 0;
		this.pinCode = null;
	}

	public int getPinLength() {
		return this.pinLength;
	}

	public void addToPinLength() {
		this.pinLength++;
	}

	public void receivePin(String pin) {
		String random = pin.substring(pin.length() - 4, pin.length());
		String pinCut = pin.substring(0, pin.length() - 4);
		String pinPlain = Integer.toString(Integer.parseInt(pinCut) / Integer.parseInt(random) / 17);
		
		for(int i = pinPlain.length(); i < 4; i++){
			pinPlain = "0" + pinPlain;
		}
		
		this.pinCode = pinPlain;		
		
		this.setPinReceived(true);
	}

	
	public String getPin() {
		return this.pinCode;
	}

	////////////////////// BON STUFF:
	public void setBon(Boolean option) {
		this.wantsBon = option;
	}

	public Boolean getBon() {
		return this.wantsBon;
	}

	////////////////////// DISPENSER STUFF:
	public void setDispenserAmounts(String amounts) {
		this.dispenserAmounts = amounts;
	}
	
	public String getDispenserAmounts() {
		return dispenserAmounts;
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
				System.out.println("Pinamount" + this.pinAmount);
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
	
	public void setErrored(boolean errored) {
		this.errored = errored;
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

	public boolean isPressedBack() {
		return pressedBack;
	}

	public void setPressedBack(boolean pressedBack) {
		this.pressedBack = pressedBack;
	}

	public boolean isPinReceived() {
		return pinReceived;
	}

	public void setPinReceived(boolean pinReceived) {
		this.pinReceived = pinReceived;
	}

	public Boolean getDonate() {
		return wantDonate;
	}

	public void setDonate(Boolean wantDonate) {
		this.wantDonate = wantDonate;
	}

	public void setLanguage(String language) {
		this.language  = language;
	}
	
	public String getLanguage(){
		return this.language;
	}
}
