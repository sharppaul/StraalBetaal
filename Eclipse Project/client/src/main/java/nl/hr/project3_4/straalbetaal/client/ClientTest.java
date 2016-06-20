package nl.hr.project3_4.straalbetaal.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import nl.hr.project3_4.straalbetaal.api.BalanceRequest;
import nl.hr.project3_4.straalbetaal.api.BalanceResponse;
import nl.hr.project3_4.straalbetaal.api.CheckPasRequest;
import nl.hr.project3_4.straalbetaal.api.CheckPasResponse;
import nl.hr.project3_4.straalbetaal.api.CheckPinRequest;
import nl.hr.project3_4.straalbetaal.api.CheckPinResponse;
import nl.hr.project3_4.straalbetaal.api.WithdrawRequest;
import nl.hr.project3_4.straalbetaal.api.WithdrawResponse;
import nl.hr.project3_4.straalbetaal.encryption.BlackBox;

public class ClientTest {
	public static void main(String[] args) {
		new ClientTest().testServer();
	}

	public String formatAmount(long amount) {
		String amountStr = (amount / 100) + ",";
		long decimals = amount - ((amount / 100) * 100);
		if (decimals > 9) {
			amountStr += decimals;
		} else {
			amountStr += "0" + decimals;	
		}

		return amountStr;
	}
	
	public void testEncryption(){
	}
	
	public void testFormat(){
		System.out.println("€"+formatAmount(1000L));
		System.out.println("€"+formatAmount(1009L));
		System.out.println("€"+formatAmount(1099L));
	}

	public void testServer() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter Bank ID\t");
			int bankid = Integer.parseInt(br.readLine());
			
			System.out.print("Enter Card ID\t");
			String cardid = BlackBox.b.encrypt(br.readLine());
			
			System.out.print("Enter PIN\t");
			String pin = BlackBox.b.encrypt(br.readLine());
			
			System.out.print("Enter withdraw amount\t");
			int amount = Integer.parseInt(br.readLine());

			System.out.println("Connecting...");
			ClientBackEnd backend = new ClientBackEnd();

			System.out.println("Checking if card exists...");
			
			CheckPasRequest pasRequest = new CheckPasRequest(bankid, cardid);
			CheckPasResponse pasResponse = backend.checkPas(pasRequest);
			if (pasResponse.doesExist()) {
				System.out.println("Card exists...");
			} else {
				throw new Exception("Card does not exist!");
			}

			System.out.println("Logging in...");
			CheckPinRequest pinRequest = new CheckPinRequest(bankid, cardid, pin);
			CheckPinResponse pinResponse = backend.checkPincode(pinRequest);
			if (pinResponse.isBlocked()) {
				throw new Exception("Card blocked!");
			}
			if (!pinResponse.isCorrect()) {
				throw new Exception("Wrong pin!");
			}
			
			System.out.println("Obtaining balance...");
			BalanceResponse balanceResponse = backend.checkBalance(new BalanceRequest(bankid, cardid));
			long saldo = balanceResponse.getBalance();

			System.out.println("Saldo: €" + formatAmount(saldo));

			System.out.println("Withdrawing...");
			
			WithdrawRequest rq = new WithdrawRequest(bankid, cardid, amount);
			WithdrawResponse rs = backend.withdrawMoney(rq);
			if (rs.isSucceeded()) {
				System.out.println("Successfully withdrawn €" + formatAmount(amount));
			} else {
				System.out.println("Withdraw failed!");
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
