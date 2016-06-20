package nl.hr.project3_4.straalbetaal.server.services;

import org.apache.log4j.Logger;

import nl.hr.project3_4.straalbetaal.encryption.BlackBox;
import nl.hr.project3_4.straalbetaal.server.dao.DataAccessObject;
import nl.hr.project3_4.straalbetaal.server.mail.MailService;

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
		pasID = BlackBox.b.decrypt(pasID);
		return dao.doesPasExist(pasID);
	}

	public boolean isPasBlocked(String pasID) {
		pasID = BlackBox.b.decrypt(pasID);
		int counter = dao.getPinAttempts(pasID);

		if (counter > 2)
			return true;
		else
			return false;
	}

	public boolean isPinCorrect(String pasID, String pin) {
		pasID = BlackBox.b.decrypt(pasID);
		pin = BlackBox.b.decrypt(pin);
		
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
		pasID = BlackBox.b.decrypt(pasID);
		if (pincodeChecked) {
			balance = dao.getBalance(pasID);
			LOG.info("Balance DBRequest SUCCESS, Iban: " + pasID + " Balance: " + balance + " Cause: Balance request");
		} else
			LOG.warn("Balance DBRequest BLOCKED, pincodeChecked:: " + pincodeChecked);

		return balance;
	}

	public int withdraw(String pasID, long amount) {
		pasID = BlackBox.b.decrypt(pasID);
		int transactieBon = 0;

		if (pincodeChecked) {
			if (amount > 0) {
				long currentSaldo = dao.getBalance(pasID) - amount;
				LOG.info("Balance DBRequest SUCCESS Iban: " + pasID + " Balance: " + balance + " Cause: Withdraw");
				LOG.info("Withdraw DBRequest Iban: " + pasID + " Saldo left: " + currentSaldo);
				if (currentSaldo >= 0) {
					transactieBon = dao.withdraw(pasID, amount, currentSaldo);
					String emailUser = dao.getMailAdres(pasID);
					if (emailUser != null) {
						new MailService(emailUser).sendMailContainingTransactiebon(transactieBon, pasID, amount);
						LOG.info("Mail send to user with transactiebon!");
					}
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
