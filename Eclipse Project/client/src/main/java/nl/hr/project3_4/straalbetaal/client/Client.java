package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.*;
import nl.hr.project3_4.straalbetaal.comm.*;
import nl.hr.project3_4.straalbetaal.gui.*;
import nl.hr.project3_4.straalbetaal.exceptions.*;

@SuppressWarnings("unused")
public class Client {
	Frame frame;
	ArduinoData data;
	Read reader;

	private boolean shouldUpdateGUI = true;

	public static void main(String[] args) {

		Client client = new Client();

	}

	public Client() {
		data = new ArduinoData();
		reader = new Read(data);
		frame = new Frame();

		while (true) {
			try {
				while (!cardInserted()) {
					// WAIT FOR CARD.
					shouldReset();
				}

				frame.setMode("login");
				int dots = 0;
				shouldUpdateGUI = true;
				while (!pinReceived()) {
					if (dots < data.getPinLength()) {
						frame.addDotToPin();
						dots++;
					}
					pinErrorOccured();
					shouldReset();
				}

				checkPinValid();

				shouldReset();
				frame.setMode("choice");

				while (!userMadeChoice()) {
					// WAIT FOR USER
					shouldReset();
				}

				shouldReset();

				if (data.getChoice().equals("snelpinnen")) {
					// QUICKPIN
				} else if (data.getChoice().equals("pinnen")) {

					frame.setMode("pin");
					while (!userEnteredAmount()) { // user hasn't entered amount
						shouldReset();
					}

					shouldReset();
					data.chooseBill(null);
					calculateBills();
					frame.setMode("billselect");
					while (!billSelected()) { // user hasn't chosen bills
						shouldReset();
					}

					shouldReset();
					frame.setMode("ticket");
					while (!ticketIsChosen()) { // user hasn't chosen ticket
						shouldReset();
					}

				} else if (data.getChoice().equals("saldo")) {
					frame.setMode("loading");
					frame.setSaldo(this.requestSaldo());
					frame.setMode("saldo");
				}
				
				shouldReset();
				frame.setMode("success");
				while(true){
					finished();
				}

			} catch (ResetException e) {
				frame.setMode("error");
				try {
				    Thread.sleep(2500);                 
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			} catch (SuccessException e) {
				try {
				    Thread.sleep(2500);                 
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
		}
	}

	private void calculateBills() {
		int amount = data.getAmount();
		//calculates some stuff, and will return a string with few amounts. 
		//quite an interesting piece of software to make.
		String[] options = new String[3];
		
		options[0] = "Keuze 1";
		options[1] = "Keuze 2";
		options[2] = "Keuze 3";
		
		frame.setBillOption(options);		
	}

	private void finished() throws SuccessException {
		if (data.isReset()) {
			throw new SuccessException("Finished.");
		}
	}

	private boolean pinReceived() {
		if (data.getPinEncrypted() == null) {
			return false;
		}
		return true;
	}

	private boolean userEnteredAmount() {
		frame.setPinAmount(data.getAmount());
		if (data.isAmountDone()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean billSelected() {
		if (data.getBillOption() != null) {
			return true;
		}
		return false;
	}

	private boolean ticketIsChosen() {
		if (data.getBon() == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean cardInserted() {
		return data.isCardReceived();
	}

	public void checkPinValid() throws ResetException {
		// stuff that checks if pincode is valid.`

		if(false){
			throw new ResetException("Pin is invalid.");
		}

	}

	public boolean userMadeChoice() {
		if (data.getChoice().equals("none")) {
			return false;
		} else {
			return true;
		}
	}

	public void pinErrorOccured() {
		if (data.isErrored() && shouldUpdateGUI) {
			frame.setPinErr(data.getError());
			this.shouldUpdateGUI = false;
		}
	}

	public void shouldReset() throws ResetException {
		if (data.isReset()) {
			throw new ResetException("Something went wrong on pin terminal.");
		}
	}

	public float requestSaldo() throws ResetException {
		// request saldo
		// try { request } catch exc. { return (float)0.0 )
		return (float) 13.37;
	}
}
