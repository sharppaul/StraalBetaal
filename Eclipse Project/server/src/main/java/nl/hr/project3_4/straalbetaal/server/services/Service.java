package nl.hr.project3_4.straalbetaal.server.services;

import org.apache.log4j.Logger;

import nl.hr.project3_4.straalbetaal.server.dao.DataAccessObject;


public class Service {

	private static final Logger LOG = Logger.getLogger(Service.class.getName());

	private static DataAccessObject dao = new DataAccessObject();
	private boolean pincodeChecked; // (Testing with true for now) -> Mostlikely this whole class is instantiated every time a request is sent, so it will stay on false.
	private long balance = 0;


	public Service() {
		this.pincodeChecked = false;
	}


	public String getUserID(String iban, long pincode) {
		String user = null;
		try {
			user = dao.getUserID(iban, pincode);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		if(user == null) {
			LOG.warn("Database operation performed for userID, but incorrect credentials."
						+ "Iban: " + iban + " and pincode: " + pincode + " used!");
			return "Wrong pincode!";
		}
		else {
			this.pincodeChecked = true;
			LOG.info("Database operation performed for userID sucessfully for userId:" + user); 
			return user;	
		}
	}

	public Long getBalance(String iban) {
		try {
			if(this.pincodeChecked) {
				balance = dao.getUserBalance(iban);
				LOG.info("Database operation performed for balance. Iban: " + iban + " and balance: " + balance);
			}
			else
				LOG.warn("Get request for balance asked, without first checking pincode!!!");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return balance;
	}

	public boolean withdraw(String iban, long amount) {
		boolean enoughMoneyInAccount = false;
		try {
			if(this.pincodeChecked) {
				long currentSaldo = balance - amount;
				LOG.info("Withdraw amount. Iban: " + iban + " and current Saldo: " + currentSaldo);
				if(currentSaldo < 0) {
					enoughMoneyInAccount = dao.withdraw(iban, amount, currentSaldo);
					LOG.info("Database operation performed for withdraw. Iban: " + iban + " and amount: " + amount);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return enoughMoneyInAccount;
	}

}
