package nl.hr.project3_4.straalbetaal.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DataAccessObject extends DbTemplate {

	private static final Logger LOG = Logger.getLogger(DataAccessObject.class.getName());

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public DataAccessObject() {
	}

	public boolean doesPasExist(String pasID) {
		String checkIban = "SELECT COUNT(card.IBAN) FROM card WHERE card.IBAN = ?";
		boolean doesExist = false;
		try {
			con = getConnection();
			stmt = con.prepareStatement(checkIban);
			stmt.setString(1, pasID);
			rs = stmt.executeQuery();
			if (rs.next())
				doesExist = rs.getInt(1) > 0;
			LOG.info("MYSQL Checking if card exists.");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("MYSQL ERROR when checking card! " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}
		return doesExist;
	}

	public String getPin(String pasID) {
		String sqlPin = null;
		String getUserSQL = "SELECT card.pincode FROM card WHERE card.IBAN = ?";

		try {
			con = getConnection();
			stmt = con.prepareStatement(getUserSQL);
			stmt.setString(1, pasID);
			rs = stmt.executeQuery();
			if (rs.next())
				sqlPin = rs.getString(1);
			LOG.info("MYSQL Request for pin.");
			if (rs.next()) // If the resultSet contains a second row.
				throw new SQLException("Multiple pincodes for one pasID!");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("MYSQL ERROR when getting pin! " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}

		return sqlPin;
	}

	public int getPinAttempts(String pasID) {
		int counter = 0;
		String getCounterSQL = "SELECT card.wrongPinCounter FROM card WHERE card.IBAN = ?";

		try {
			con = getConnection();
			stmt = con.prepareStatement(getCounterSQL);
			stmt.setString(1, pasID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
				LOG.info("MYSQL Checking pin attempts.");
			}
		} catch (SQLException e) {
			LOG.error("MYSQL ERROR when checking pincounter! " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}

		return counter;
	}

	public int incrementPinAttempts(String pasID) {
		int counter = 0;
		String updateIncrementedCounterSQL = "UPDATE card SET card.wrongPinCounter = ? WHERE IBAN = ?";

		counter = getPinAttempts(pasID) + 1;

		try {
			con = getConnection();
			stmt = con.prepareStatement(updateIncrementedCounterSQL);
			stmt.setInt(1, counter);
			stmt.setString(2, pasID);
			stmt.executeUpdate();
			LOG.info("MYSQL Incrementing pin attempt: " + counter);
		} catch (SQLException e) {
			LOG.error("MYSQL ERROR While incrementing pin attempts! " + e.getMessage());
		}

		closeResources(con, stmt, rs);

		return counter;
	}

	public void resetPinAttempts(String pasID) {
		String updateIncrementedCounterSQL = "UPDATE card SET card.wrongPinCounter = ? WHERE IBAN = ?";

		try {
			con = getConnection();
			stmt = con.prepareStatement(updateIncrementedCounterSQL);
			stmt.setInt(1, 0);
			stmt.setString(2, pasID);
			stmt.executeUpdate();
			LOG.info("MYSQL Resetting pin attempts.");
		} catch (SQLException e) {
			LOG.error("MYSQL ERROR When resetting pin attempts! " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}
	}

	public Long getBalance(String pasID) {
		long balance = 0;
		String getBalanceSQL = "SELECT saldo.cardSaldo FROM saldo WHERE saldo.IBAN = ?";

		try {
			con = getConnection();
			stmt = con.prepareStatement(getBalanceSQL);
			stmt.setString(1, pasID);
			rs = stmt.executeQuery();
			if (rs.next())
				balance = rs.getLong(1);
			else
				throw new SQLException("Saldo with given IBAN does not exist!");
		} catch (SQLException e) {
			LOG.error("MYSQL ERROR When getting balance from database! " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}
		return balance;
	}

	public int withdraw(String pasID, long amount, long currentSaldo) {
		String getMoneySQL = "UPDATE saldo SET saldo.cardSaldo = ? WHERE IBAN = ?";
		String betaalGeschiedenisSQL = "INSERT INTO betaalgeschiedenis (IBAN, afgeschreven, datum) VALUE( ?, ?, ?)";

		try {
			con = getConnection();
			stmt = con.prepareStatement(getMoneySQL);
			stmt.setLong(1, currentSaldo);
			stmt.setString(2, pasID);
			if (stmt.executeUpdate() == 1) {
				closeResources(null, stmt, rs);
				LOG.info("MYSQL Withdraw method, updated saldo.");
				stmt = con.prepareStatement(betaalGeschiedenisSQL);
				stmt.setString(1, pasID);
				stmt.setLong(2, amount);
				stmt.setTimestamp(3, java.sql.Timestamp.from(java.time.Instant.now()));
				if (stmt.executeUpdate() == 1)
					LOG.info("MYSQL Inserted data into betaalgeschiedenis.");
			}
		} catch (SQLException e) {
			LOG.error("MYSQL ERROR When withdrawing money! " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}

		return getTransactionNumber();
	}

	private int getTransactionNumber() {
		int transactieBon = 0;
		String getTransactieBonSQL = "SELECT transactieNummer FROM betaalgeschiedenis ORDER BY transactieNummer DESC LIMIT 1";
		Statement stmt = null;
		con = getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getTransactieBonSQL);
			if (rs.next())
				transactieBon = rs.getInt(1);
			LOG.info("MYSQL Getting transaction number.");
			stmt.close();
		} catch (SQLException e) {
			LOG.error("MYSQL ERROR When getting transaction number! " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}

		return transactieBon;
	}

}
