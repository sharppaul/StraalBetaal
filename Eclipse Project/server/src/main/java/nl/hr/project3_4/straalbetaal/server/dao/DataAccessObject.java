package nl.hr.project3_4.straalbetaal.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DataAccessObject extends DbTemplate {

	private static final Logger LOG = Logger.getLogger(DataAccessObject.class.getName());

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public DataAccessObject() {}

	public String getUserID(String iban, long pincode) throws Exception {
		String userID = null;
		String getUserSQL = "SELECT card.userID FROM card WHERE card.IBAN = ? AND card.pincode = ?";

		con = getConnection();
		stmt = con.prepareStatement(getUserSQL);
		stmt.setString(1, iban);
		stmt.setLong(2, pincode);
		rs = stmt.executeQuery();
		if (rs.next())
			userID = rs.getString(1);
		if (rs.next())
			throw new Exception("Multiple userID's for one user!");

		closeResources(con, stmt, rs);

		return userID;
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

	public int withdraw(String iban, long amount, long currentSaldo) throws Exception {
		// Ugly close Resources();
		int transactieNummer = 0;
		String getMoneySQL = "UPDATE saldo SET saldo.cardSaldo = ? WHERE saldo.IBAN = ?";
		String betaalGeschiedenisSQL = "INSERT INTO betaalgeschiedenis (IBAN, afgeschreven, datum) VALUE( ?, ?, ?)";
		String getInfoSQL = "SELECT betaalgeschiedenis.transactieNummer FROM betaalgeschiedenis ORDER BY betaalgeschiedenis.transactieNummer DESC";

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
			if (stmt.executeUpdate() == 1) {
				closeResources(null, stmt, rs);
				LOG.info("Withdraw method, inserted data into betaalgeschiedenis!");
				Statement stmt = con.createStatement();
				rs = stmt.executeQuery(getInfoSQL);
				if(rs.next())
					transactieNummer = rs.getInt(transactieNummer);
				closeResources(null, stmt, null);
				LOG.info("Withdraw method, transactienummer: " + transactieNummer);
			}
		}

		closeResources(con, null, rs);

		return transactieNummer;
	}

}
