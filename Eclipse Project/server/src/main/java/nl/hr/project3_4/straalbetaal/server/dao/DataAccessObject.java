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

	public String getCorrectPin(String pasID) {
		String sqlPin = null;
		String getUserSQL = "SELECT card.pincode FROM card WHERE card.IBAN = ?";

		try {
			con = getConnection();
			stmt = con.prepareStatement(getUserSQL);
			stmt.setString(1, pasID);
			rs = stmt.executeQuery();
			if (rs.next())
				sqlPin = rs.getString(1);
			LOG.info("getCorrectPin method!");
			if (rs.next()) // If the resultSet contains a second row.
				throw new Exception("Multiple pincodes for one pasID!");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN GETTING PINCODE!!!");
			LOG.error("ERROR MESSAGE: " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}

		return sqlPin;
	}

	public int getWrongPin_Counter(String pasID) {
		int counter = 0;
		String getCounterSQL = "SELECT card.wrongPinCounter FROM card WHERE card.IBAN = ?";

		try {
			con = getConnection();
			stmt = con.prepareStatement(getCounterSQL);
			stmt.setString(1, pasID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
				LOG.info("checkWrongPinCounter method!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN CHECKING WRONG PIN COUNTER!!!");
		} finally {
			closeResources(con, stmt, rs);
		}

		return counter;
	}

	public int incrementWrongPinCounter(String pasID) {
		int counter = 0;
		String updateIncrementedCounterSQL = "UPDATE card SET card.wrongPinCounter = ? WHERE IBAN = ?";

		counter = getWrongPin_Counter(pasID) + 1;

		try {
			con = getConnection();
			stmt = con.prepareStatement(updateIncrementedCounterSQL);
			stmt.setInt(1, counter);
			stmt.setString(2, pasID);
			stmt.executeUpdate();
			LOG.info("incrementWrongPincodeCounter method, inserted counter: " + counter + " into cards!");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN INCREMENTING WRONG PIN COUNTER!!!");
		}

		closeResources(con, stmt, rs);

		return counter;
	}

	public void resetWrongPinCounter(String pasID) {
		String updateIncrementedCounterSQL = "UPDATE card SET card.wrongPinCounter = ? WHERE IBAN = ?";

		try {
			con = getConnection();
			stmt = con.prepareStatement(updateIncrementedCounterSQL);
			stmt.setInt(1, 0);
			stmt.setString(2, pasID);
			stmt.executeUpdate();
			LOG.info("resetWrongPinCounter method");
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN GETTING BALANCE!!!");
			LOG.error("ERROR MESSAGE: " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}
		return balance;
	}

	public void withdraw(String pasID, long amount, long currentSaldo) {
		String getMoneySQL = "UPDATE saldo SET saldo.cardSaldo = ? WHERE IBAN = ?";
		String betaalGeschiedenisSQL = "INSERT INTO betaalgeschiedenis (IBAN, afgeschreven, datum) VALUE( ?, ?, ?)";

		try {
			con = getConnection();
			stmt = con.prepareStatement(getMoneySQL);
			stmt.setLong(1, currentSaldo);
			stmt.setString(2, pasID);
			if (stmt.executeUpdate() == 1) {
				closeResources(null, stmt, rs);
				LOG.info("Withdraw method, updated saldo!");
				stmt = con.prepareStatement(betaalGeschiedenisSQL);
				stmt.setString(1, pasID);
				stmt.setLong(2, amount);
				stmt.setTimestamp(3, java.sql.Timestamp.from(java.time.Instant.now()));
				if (stmt.executeUpdate() == 1)
					LOG.info("Withdraw method, inserted data into betaalgeschiedenis!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error("DATABASE ERROR WHEN DOING WITHDRAW!!!");
			LOG.error("ERROR MESSAGE: " + e.getMessage());
		} finally {
			closeResources(con, stmt, rs);
		}
	}

	public int getTransactieBon() {
		int transactieBon = 0;
		String getTransactieBonSQL = "SELECT transactieNummer FROM betaalgeschiedenis ORDER BY transactieNummer DESC LIMIT 1";
		Statement stmt = null;
		con = getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(getTransactieBonSQL);
			if (rs.next())
				transactieBon = rs.getInt(1);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(con, stmt, rs);
		}

		LOG.info("getTransactionBon method");

		return transactieBon;
	}

}
