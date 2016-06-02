package nl.hr.project3_4.straalbetaal.server.services;

import org.apache.log4j.Logger;

import nl.hr.project3_4.straalbetaal.server.dao.DataAccessObject;

public class Service {

	private static final Logger LOG = Logger.getLogger(Service.class.getName());

	private static DataAccessObject dao = new DataAccessObject();
	// Making pincodeChecked static, making sure 1 boolean instance is used for
	// all request!
	// When a withdraw is made I change this to false again, so that
	// pincodeChecked wont stay true for next user!
	private boolean pincodeChecked;
	private long balance = 0;

	public Service() {
		pincodeChecked = false;
	}

	public boolean checkPasExist(String pasID) {
		return dao.doesPasExist(pasID);
	}

	public boolean isPasBlocked(String pasID) {
		int counter = dao.getPinAttempts(pasID);

		if (counter > 2)
			return true;
		else
			return false;
	}

	public boolean isPinCorrect(String pasID, String pin) {
		String sqlPin = dao.getPin(pasID);

		if (!sqlPin.equals(pin)) {
			LOG.warn("Pincode DBRequest BLOCKED, Iban: " + pasID);
			dao.incrementPinAttempts(pasID);
			return false;
		} else {
			pincodeChecked = true;
			dao.resetPinAttempts(pasID);
			LOG.info("Pincode DBRequest SUCCESS, Iban: " + pasID);
			return true;
		}
	}

	public Long getBalance(String pasID) {
		if (pincodeChecked) {
			balance = dao.getBalance(pasID);
			LOG.info("Balance DBRequest SUCCESS, Iban: " + pasID + " Balance: " + balance + " Cause: Balance request");
		} else
			LOG.warn("Balance DBRequest BLOCKED, pincodeChecked:: " + pincodeChecked);

		return balance;
	}

	public int withdraw(String pasID, long amount) {
		int transactieBon = 0;

		if (pincodeChecked) {
			if (amount > 0) {
				long currentSaldo = dao.getBalance(pasID) - amount;
				LOG.info("Balance DBRequest SUCCESS Iban: " + pasID + " Balance: " + balance + " Cause: Withdraw");
				LOG.info("Withdraw DBRequest Iban: " + pasID + " Saldo left: " + currentSaldo);
				if (currentSaldo >= 0) {
					transactieBon = dao.withdraw(pasID, amount, currentSaldo);
					LOG.info("Withdraw DBRequest SUCCESS, Iban: " + pasID + " Amount: " + amount + " Transaction ID: "
							+ transactieBon);
				}
			}
		} else
			LOG.warn("Withdraw DBRequest BLOCKED, pincodeChecked: " + pincodeChecked);

		pincodeChecked = false;

		// return enoughMoneyInAccount;
		return transactieBon;
	}

}
