package nl.hr.project3_4.straalbetaal.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class DataAccessObject extends DbTemplate {

	private static final Logger LOG = Logger.getLogger(DataAccessObject.class.getName());

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public DataAccessObject() {
	}

	public String getUserID(String iban, String pincode) throws Exception {
		String userID = null;
		String getUserSQL = "SELECT card.userID FROM card WHERE card.IBAN = ? AND card.pincode = ?";

		con = getConnection();
		stmt = con.prepareStatement(getUserSQL);
		stmt.setString(1, iban);
		stmt.setString(2, pincode);
		rs = stmt.executeQuery();
		if (rs.next())
			userID = rs.getString(1);
		if (rs.next()) {// If the resultSet contains a second row.
			LOG.warn("Multiple userID's for one user! IBAN: " + iban + " & Pincode: " + pincode + "!!!");
			throw new Exception("Multiple userID's for one user!"); // With this line here, the closeResources will never be executed (just saying).
		}

		closeResources(con, stmt, rs);

		return userID;
	}

	public int checkWrongPinCounter(String iban) throws Exception {
		int counter = 0;
		String getCounterSQL = "SELECT card.wrongPinCounter FROM card WHERE card.IBAN = ?";

		con = getConnection();
		stmt = con.prepareStatement(getCounterSQL);
		stmt.setString(1, iban);
		rs = stmt.executeQuery();
		if (rs.next()) {
			counter = rs.getInt(1);
			LOG.info("checkWrongPinCounter method!");
		}

		closeResources(con, stmt, rs);

		return counter;
	}

	public int incrementWrongPinCounter(String iban) throws Exception {
		int counter = 0;
		String insertIncrementedCounterSQL = "UPDATE card SET card.wrongPinCounter = ? WHERE IBAN = ?";

		counter = checkWrongPinCounter(iban) + 1;

		con = getConnection();
		stmt = con.prepareStatement(insertIncrementedCounterSQL);
		stmt.setInt(1, counter);
		stmt.setString(2, iban);
		stmt.executeUpdate();

		LOG.info("insertWrongPincodeCounter method, inserted counter++ into cards!");

		closeResources(con, stmt, rs);
		
		return counter;
	}

	public Long getUserBalance(String iban) throws Exception {
		long balance;
		String getBalanceSQL = "SELECT saldo.cardSaldo FROM saldo WHERE saldo.IBAN = ?";

		con = getConnection();
		stmt = con.prepareStatement(getBalanceSQL);
		stmt.setString(1, iban);
		rs = stmt.executeQuery();
		if (rs.next())
			balance = rs.getLong(1);
		else
			throw new Exception("Saldo with given IBAN does not exist!");

		closeResources(con, stmt, rs);

		return balance;
	}

	public boolean withdraw(String iban, long amount, long currentSaldo) throws Exception {
		boolean successfulWithdraw = false;
		String getMoneySQL = "UPDATE saldo SET saldo.cardSaldo = ? WHERE IBAN = ?";
		String betaalGeschiedenisSQL = "INSERT INTO betaalgeschiedenis (IBAN, afgeschreven, datum) VALUE( ?, ?, ?)";

		con = getConnection();
		stmt = con.prepareStatement(getMoneySQL);
		stmt.setLong(1, currentSaldo);
		stmt.setString(2, iban);
		if (stmt.executeUpdate() == 1) {
			closeResources(null, stmt, rs); // You dont have to close connection!
			LOG.info("Withdraw method, updated saldo!");
			stmt = con.prepareStatement(betaalGeschiedenisSQL);
			stmt.setString(1, iban);
			stmt.setLong(2, amount);
			stmt.setTimestamp(3, java.sql.Timestamp.from(java.time.Instant.now()));
			if (stmt.executeUpdate() == 1)
				LOG.info("Withdraw method, inserted data into betaalgeschiedenis!");
			successfulWithdraw = true;
		}

		closeResources(con, stmt, rs);

		return successfulWithdraw;
	}

}
