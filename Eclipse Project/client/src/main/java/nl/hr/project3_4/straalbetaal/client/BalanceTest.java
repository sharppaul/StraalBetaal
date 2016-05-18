package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.BalanceResponse;
import nl.hr.project3_4.straalbetaal.api.CheckPinRequest;
import nl.hr.project3_4.straalbetaal.api.CheckPinResponse;

public class BalanceTest {
	public static void main(String[] args) {
		System.out.println("Connecting...");
		ClientBackEnd backend = new ClientBackEnd("123456789");
		
		System.out.println("Logging in...");
		CheckPinRequest pinRequest = new CheckPinRequest("3025");
		CheckPinResponse pinResponse = backend.checkPincode(pinRequest);
		if (pinResponse.getUserID().equals("wrong")) {
			System.out.println("Wrong pin.");
		}
		if (pinResponse.getUserID().equals("blocked")) {
			System.out.println("Card blocked.");
		}
		
		System.out.println("Obtaining balance...");
		BalanceResponse balanceResponse = backend.checkBalance();
		
		long saldo = balanceResponse.getBalance();
		System.out.println("Saldo: " + saldo);
		
		
		String saldoString = Long.toString(saldo);
		System.out.println("Saldo: €" + saldoString.substring(0, saldoString.length()-2) + "," + saldoString.substring(saldoString.length()-2, saldoString.length()));
	}
}
