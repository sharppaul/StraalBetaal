package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.WithdrawRequest;
import nl.hr.project3_4.straalbetaal.comm.*;
import nl.hr.project3_4.straalbetaal.gui.*;

public class Client {

	public static void main(String[] args) {
		// DEAR READER, FOR NOW MYCLIENT ONLY HANDLES THE VISUAL ASPECT OF THE
		// CLIENT.
		// IT WILL ALSO HANDLE THE BACK-END PART OF THE CLIENT, BUT THE ARDUINO
		// READER PART SHOULD BE IN ANOTHER THREAD..
		Client myClient = new Client();
		if (myClient.toString().matches(""))
			;
	}

	public Client() {
		Frame f = new Frame();

		while (!cardInserted()) {
			// waits till card is inserted.
		}

		f.setMode("login");

		while (!pinValid()) {

		}

		if (errorOccured())
			;

		f.setMode("choice");

		while (!userIsReady()) {

		}

		if (f.getMode().equals("quickpin")) {
			// execute quickpin() or something.
			f.setMode("ticket");
			
		} else if (f.getMode().equals("pin")) {

			while (!userIsReady()) { // user hasn't entered amount etc.

			}
			f.setMode("billselect");
			
			while (!userIsReady()) { // user hasn't chosen options etc.

			}
			f.setMode("ticket");
			
		} else if (f.getMode().equals("saldo")) {
			
			f.setSaldo(this.requestSaldo());
			f.loadMenu();
			
		}
		
		while (!userIsReady()) { // user hasn't chosen options etc.
			
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
		//request saldo
		//try { request } catch exc. { return (float)0.0 )
		return (float)	13.37;
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
