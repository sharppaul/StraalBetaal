package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.*;
import nl.hr.project3_4.straalbetaal.comm.*;
import nl.hr.project3_4.straalbetaal.gui.*;

@SuppressWarnings("unused")
public class Client {
	Frame frame;
	ArduinoData data;
	Read reader;
	private String errorMessage;
	private boolean shouldUpdateGUI;

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

				while (!pinValid()) {
					// WAIT TILL PIN IS VALID
					pinErrorOccured();
					shouldReset();
				}

				shouldReset();
				frame.setMode("choice");

				while (!userMadeChoice()) {
					// WAIT FOR USER
					shouldReset();
				}

				if (data.getChoice().equals("quickpin")) {

				} else if (frame.getMode().equals("pin")) {

					while (!userIsReady()) { // user hasn't entered amount etc.

					}
					frame.setMode("billselect");

					while (!userIsReady()) { // user hasn't chosen options etc.

					}
					frame.setMode("ticket");

				} else if (frame.getMode().equals("saldo")) {

					frame.setSaldo(this.requestSaldo());
					frame.loadMenu();

				}

				while (!userIsReady()) { // user hasn't chosen options etc.

				}
			} catch (ResetException e) {

			}
		}

		/**
		 * ClientBackEnd client = new ClientBackEnd("123456789", 3025);
		 * 
		 * boolean clientWantsToCheckBalance = true; boolean
		 * clientWantsToGet50Euros = false;
		 * 
		 * WithdrawRequest request = null; if(!client.checkPincode().equals(
		 * "Wrong pincode!")) { if(clientWantsToCheckBalance) {
		 * System.out.println("Uw actuele saldo is " +
		 * client.checkBalance().getBalance() + " euro"); } else
		 * if(clientWantsToGet50Euros) { request = new WithdrawRequest();
		 * request.setAmount(50);
		 * 
		 * System.out.println("U wilt " + request.getAmount() + " euro pinnen."
		 * ); System.out.println(client.withdrawMoney(request).getResponse().
		 * toString()); } } // else retry!
		 **/
	}

	public boolean cardInserted() {
		return data.isCardReceived();
	}

	public boolean pinValid() {
		// stuff that checks if pincode is valid.`
		return false;
	}

	public boolean userMadeChoice() {
		if (data.getChoice().equals("none")) {
			return false;
		} else {
			return true;
		}
	}

	public void pinErrorOccured() {
		if (data.isErrored()) {
			this.errorMessage = data.getError();
			this.shouldUpdateGUI = true;
		}
	}

	public void shouldReset() throws ResetException {
		if (data.isReset()) {
			throw new ResetException();
		}
	}

	public float requestSaldo() {
		// request saldo
		// try { request } catch exc. { return (float)0.0 )
		return (float) 13.37;
	}

	/*
	 * There should also follow logic here, for example if pincode correct, and
	 * user presses A (show saldo, the saldo method from ClientBackEnd needs to
	 * be called.
	 * 
	 * Something like this if(pressed A == true) client.balance(); else
	 * if(pressed B == true) client.withdraw(); etc...
	 */

}
