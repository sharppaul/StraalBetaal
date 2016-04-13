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
		frame = new Frame();
		data = new ArduinoData();
		reader = new Read(data);

		while (true) {
			try {
				frame.setMode("start");
				while (!cardInserted()) {
					sleep(100);
				}

				shouldReset();

				frame.setMode("login");
				int dots = 0;
				while (!pinReceived()) {
					sleep(100);
					shouldUpdateGUI = true;
					frame.addDotToPin(data.getPinLength());
					pinErrorOccured();
					shouldReset();
				}

				checkPinValid();
				
				boolean userNotDone = true;
				while (userNotDone) {
					data.setPressedBack(false);
					shouldReset();
					frame.setMode("choice");

					while (!userMadeChoice()) {
						// WAIT FOR USER
						sleep(100);
						shouldReset();
					}

					shouldReset();

					if (data.getChoice().equals("a")) {
						// QUICKPIN
						userNotDone = false;
					} else if (data.getChoice().equals("c")) {

						frame.setMode("pin");
						while (!userEnteredAmount()) { // user hasn't entered amount
							sleep(100);
							shouldReset();
						}

						shouldReset();
						data.chooseBill(null);
						calculateBills();
						frame.setMode("billselect");
						while (!billSelected()) { // user hasn't chosen bills
							sleep(100);
							shouldReset();
						}

						shouldReset();
						frame.setMode("ticket");
						while (!ticketIsChosen()) { // user hasn't chosen ticket
							sleep(100);
							shouldReset();
						}
						
						userNotDone = false;

					} else if (data.getChoice().equals("b")) {
						frame.setMode("loading");
						frame.setSaldo(this.requestSaldo());
						frame.setMode("saldo");
						while(!data.isPressedBack()){
							sleep(100);
							shouldReset();
						}
					}
				}
				
				shouldReset();
				frame.setMode("success");
				while (true) {
					sleep(100);
					finished();
				}

			} catch (ResetException e) {
				frame.setError(e.getMessage());
				frame.setMode("error");
				
				System.out.println(e);
				try {
					Thread.sleep(2500);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			} catch (SuccessException e) {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	private void calculateBills() {
		int amount = data.getAmount();
		// calculates some stuff, and will return a string with few amounts.
		// quite an interesting piece of software to make.
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

		if (false) {
			throw new ResetException("Pincode foutief. Verwijder pas.");
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
			frame.addDotToPin(0);
			this.shouldUpdateGUI = false;
		}
	}

	public void shouldReset() throws ResetException {
		if (data.isReset()) {
			throw new ResetException("Pinsessie afgebroken. Verwijder alstublieft uw pas.");
		}
	}

	public float requestSaldo() throws ResetException {
		// request saldo
		// try { request } catch exc. { return (float)0.0 )
		return (float) 13.37;
	}
}
