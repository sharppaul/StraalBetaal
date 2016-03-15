package nl.hr.project3_4.straalbetaal.server.services;

import nl.hr.project3_4.straalbetaal.server.dao.DataAccessObject;


public class Service {

	private static DataAccessObject dao = new DataAccessObject();
	private boolean pincodeChecked = true; // (Testing with true for now) -> Mostlikely this whole class is instantiated every time a request is sent, so it will stay on false.
	private long balance = 0;


	public Service() {}


	public String getUserID(String iban, long pincode) {
		String user = null;
		try {
			user = dao.getUserID(iban, pincode);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		if(user == null)
			return "Wrong pincode!";
		else {
			pincodeChecked = true;
			return user;	
		}
	}

	public Long getBalance(String iban) {
		try {
			if(pincodeChecked)
				balance = dao.getUserBalance(iban);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return balance;
	}

	public boolean withdraw(String iban, long amount) {
		boolean enoughMoneyInAccount = false;
		try {
			if(pincodeChecked) {
				long currentSaldo = balance - amount;
				if(currentSaldo < 0)
					enoughMoneyInAccount = dao.withdraw(iban, amount, currentSaldo);
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return enoughMoneyInAccount;
	}

}
