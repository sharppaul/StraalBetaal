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

	public boolean checkCorrectBank(int bankID, String pasID) {
		boolean onzeBank = true;
		if (onzeBank)
			return true;
		else
			return false;
	}

	public boolean isPasBlocked(String pasID) {
		int counter = dao.getWrongPin_Counter(pasID);

		if (counter > 2)
			return true;
		else
			return false;
	}

	public boolean isPinCorrect(String pasID, String pin) {
		String sqlPin = dao.getCorrectPin(pasID);

		if (sqlPin == null) {
			LOG.warn("Database operation performed for checkCorrectPin, but incorrect pincode. PasID: " + pasID
					+ " and pincode: " + pin + " was used!");

			dao.incrementWrongPinCounter(pasID);

			return false;
		} else {
			pincodeChecked = true;

			dao.resetWrongPinCounter(pasID);

			LOG.info("Database operation performed sucessfully for pasID: " + pasID);

			return true;
		}
	}

	public Long getBalance(String pasID) {
		if (pincodeChecked) {
			balance = dao.getBalance(pasID);
			LOG.info("Database operation performed for balance. PasID: " + pasID + " and balance: " + balance);
		} else
			LOG.warn("Database operation for get balance failed, pincodeChecked was: " + pincodeChecked);

		return balance;
	}

	public int withdraw(String pasID, long amount) {
		int transactieBon = 0;

		if (pincodeChecked) {
			if (amount > 0) {
				long currentSaldo = dao.getBalance(pasID) - amount;
				LOG.info("Database operation performed for balance *IN WITHDRAW*. Iban: " + pasID + " and balance: "
						+ balance);
				LOG.info("Withdraw attempt. Iban: " + pasID + " and current Saldo: " + currentSaldo);
				if (currentSaldo >= 0) {
					// enoughMoneyInAccount = dao.withdraw(iban, amount,
					// currentSaldo); // Dit kan ik void maken!
					dao.withdraw(pasID, amount, currentSaldo);
					transactieBon = dao.getTransactieBon();
					LOG.info("Database operation for withdraw succeeded. Iban: " + pasID + " and amount: " + amount);
				}
			}
		} else
			LOG.warn("Database Operation for withdraw failed, pincodeChecked was: " + pincodeChecked);

		pincodeChecked = false;

		// return enoughMoneyInAccount;
		return transactieBon;
	}

}
