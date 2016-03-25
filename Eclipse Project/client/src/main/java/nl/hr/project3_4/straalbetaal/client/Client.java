package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.WithdrawRequest;
import nl.hr.project3_4.straalbetaal.comm.*;
import nl.hr.project3_4.straalbetaal.gui.*;

public class Client {
	Frame frame;
	ArduinoData data;
	
	public static void main(String[] args) {

		Client client = new Client();
		
	}

	public Client() {
		frame = new Frame();
		
		while (true) {
			try {
				while (!cardInserted()) {
					// waits till card is inserted.
				}

				frame.setMode("login");

				while (!pinValid()) {

				}

				if (errorOccured())
					;

				frame.setMode("choice");

				while (!userIsReady()) {

				}

				if (frame.getMode().equals("quickpin")) {
					// execute quickpin() or something.
					frame.setMode("ticket");

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
			} catch(Exception e){
				
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
		// stuff that knows when card is inserted.
		return false;
	}

	public boolean pinValid() {
		// stuff that checks if pincode is valid.`
		return false;
	}

	public boolean userIsReady() {
		// stuff that checks if user is ready to go to next panel.
		return false;
	}

	public boolean errorOccured() {
		// stuff that checks if something is wrong.
		return false;
	}

	public float requestSaldo() {
		// request saldo
		// try { request } catch exc. { return (float)0.0 )
		return (float) 13.37;
	}

	public boolean shouldReset() {
		if(data.shouldReset()){
			data.reset();
			return true;
			
		} else {
			return false;
		}
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
