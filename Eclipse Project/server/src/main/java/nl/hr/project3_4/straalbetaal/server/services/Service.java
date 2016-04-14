package nl.hr.project3_4.straalbetaal.server.services;

import org.apache.log4j.Logger;

import nl.hr.project3_4.straalbetaal.server.dao.DataAccessObject;

public class Service {

	private static final Logger LOG = Logger.getLogger(Service.class.getName());

	private static DataAccessObject dao = new DataAccessObject();
	// Making pincodeChecked static, making sure 1 boolean instance is used for all request!
	// When a withdraw is made I change this to false again, so that pincodeChecked wont stay true for next user!
	private boolean pincodeChecked;
	private long balance = 0;

	public Service() {
		pincodeChecked = false;
	}

	public String getUserID(String iban, String pin) {
		String user = null;
		int counter = 0;

		try {
			user = dao.getUserID(iban, pin);
			counter = dao.checkWrongPinCounter(iban);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN GETTING USERID!!!");
			LOG.error("ERROR MESSAGE: " + e.getMessage());
		}

		if (counter > 2)
			return "blocked";

		if (user == null) {
			LOG.warn("Database operation performed for userID, but incorrect pincode. Iban: " + iban
					+ " and pincode: " + pin + " used!");

			try {
				dao.incrementWrongPinCounter(iban);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "wrong";
		} else {
			pincodeChecked = true;
			LOG.info("Database operation performed sucessfully for userID: " + user);

			return user;
		}
	}

	public Long getBalance(String iban) {
		try {
			if (pincodeChecked) {
				balance = dao.getUserBalance(iban);
				LOG.info("Database operation performed for balance. Iban: " + iban + " and balance: " + balance);
			} else
				LOG.warn("Database operation for get balance failed, pincodeChecked was: " + pincodeChecked);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN GETTING BALANCE!!!");
			LOG.error("ERROR MESSAGE: " + e.getMessage());
		}
		return balance;
	}

	public boolean withdraw(String iban, long amount) {
		boolean enoughMoneyInAccount = false;
		try {
			if (pincodeChecked) {
				long currentSaldo = dao.getUserBalance(iban) - amount;
				LOG.info("Database operation performed for balance *IN WITHDRAW*. Iban: " + iban + " and balance: "
						+ balance);
				LOG.info("Withdraw attempt. Iban: " + iban + " and current Saldo: " + currentSaldo);
				if (currentSaldo >= 0) {
					enoughMoneyInAccount = dao.withdraw(iban, amount, currentSaldo);
					LOG.info("Database operation for withdraw succeeded. Iban: " + iban + " and amount: " + amount);
				}
			} else
				LOG.warn("Database Operation for withdraw failed, pincodeChecked was: " + pincodeChecked);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN DOING WITHDRAW!!!");
			LOG.error("ERROR MESSAGE: " + e.getMessage());
		} finally {
			pincodeChecked = false;
		}
		return enoughMoneyInAccount;
	}

}
