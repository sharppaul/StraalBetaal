package nl.hr.project3_4.straalbetaal.client;

import nl.hr.project3_4.straalbetaal.api.BalanceRequest;
import nl.hr.project3_4.straalbetaal.api.BalanceResponse;
import nl.hr.project3_4.straalbetaal.api.CheckPinRequest;
import nl.hr.project3_4.straalbetaal.api.CheckPinResponse;
import nl.hr.project3_4.straalbetaal.api.WithdrawRequest;
import nl.hr.project3_4.straalbetaal.api.WithdrawResponse;

public class BalanceTest {
	public static void main(String[] args) {
		System.out.println("Connecting...");
		ClientBackEnd backend = new ClientBackEnd("123456789");

		System.out.println("Logging in...");
		CheckPinRequest pinRequest = new CheckPinRequest(Client.BANKID, "123456789", "3025");
		CheckPinResponse pinResponse = backend.checkPincode(pinRequest);
		if (!pinResponse.isCorrect()) {
			System.out.println("Wrong pin.");
		}
		if (pinResponse.isBlocked()) {
			System.out.println("Card Blocked.");
		}

		System.out.println("Obtaining balance...");
		BalanceResponse balanceResponse = backend.checkBalance(new BalanceRequest(Client.BANKID, "123456789"));
		long saldo = balanceResponse.getBalance();

		String saldoString = Long.toString(saldo);
		System.out.println("Saldo: €" + saldoString.substring(0, saldoString.length() - 2) + ","
				+ saldoString.substring(saldoString.length() - 2, saldoString.length()));

		
		long donateAmount = saldo - (long) ((int) (saldo / 100) * 100);

		if (saldo >= 0) {
			System.out.println("Donating...");
			if (donateAmount == 0)
				donateAmount = 50;
			WithdrawRequest rq = new WithdrawRequest(Client.BANKID, "123456789", donateAmount);
			WithdrawResponse rs = backend.withdrawMoney(rq);
			if (rs.isSucceeded()) {
				System.out.println("Successfully donated " + donateAmount + " cents!");
			} else {
				System.out.println("Donating failed!");
			}
		}
	}
}
