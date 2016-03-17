package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.WithdrawRequest;

public class Client {

	public static void main(String[] args) {
		/* 
		 * The helper class that gets the data from the arduino will be 
		 * instantiated in this main method (most likely).

		 * With 2 getters the values will then be put in the IBAN and
		 * pincode variables.

		 * The IBAN and pincode will then be used for the appropriate 
		 * requests and responses.
		 */
		// ArduinoHelperClass ahc = new ArduinoHelperClass();
		// MyClient client = new MyClient(ahc.getIBAN(),ahc.getPincode());
		ClientBackEnd client = new ClientBackEnd("123456789", 3025);		 

		boolean clientWantsToCheckBalance = true;
		boolean clientWantsToGet50Euros = false;

		WithdrawRequest request = null;
		if(!client.checkPincode().equals("Wrong pincode!")) {
			if(clientWantsToCheckBalance) {
				System.out.println("Uw actuele saldo is " + 
						client.checkBalance().getBalance() + " euro");
			}
			else if(clientWantsToGet50Euros) {
				request = new WithdrawRequest();
				request.setAmount(50);
	
				System.out.println("U wilt " + request.getAmount() + " euro pinnen.");
				System.out.println(client.withdrawMoney(request).getResponse().toString());
			}
		}
		// else retry!
			


	}

	/* 
	 * There should also follow logic here, for example
	 * if pincode correct, and user presses A (show saldo, the saldo method from
	 * ClientBackEnd needs to be called.

	 * Something like this if(pressed A == true)
	 * 							client.balance();
	 * 						else if(pressed B == true)
	 * 							client.withdraw();
	 * etc...
	 */
	
}
