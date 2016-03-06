package org.hr.project3_4.straalBetaal.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hr.project3_4.straalBetaal.dao.dbUtil.UserInfoRowMapper;
import org.hr.project3_4.straalBetaal.dao.dbUtil.dbTemplate;
import org.hr.project3_4.straalBetaal.entities.User;

public class UpdateSaldoDAO extends dbTemplate {

	private String userID;
	private int saldo;

	private int saldos[] = new int[5];

	private final String CHECK_SALDO = "SELECT saldo.userID, saldo.userSaldo FROM saldo";
	//private final String AFSCRHIJVEN = "UPDATE saldo.userSaldo FROM saldo WHERE "


	public UpdateSaldoDAO() {}

	public UpdateSaldoDAO(String userID, int saldo) {
		this.userID = userID;
		this.saldo = saldo;
	}


	public User checkBalance() throws ClassNotFoundException, SQLException {
		Connection con = getConnection();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(CHECK_SALDO);

		User user = null;
		UserInfoRowMapper mapper = new UserInfoRowMapper();
		for(int i = 0; rs.next(); i++) {
			user = mapper.mapSqlToObject(rs);
			System.out.println(rs);
		}

		closeResources(con, stmt, rs);

		return user;
	}


	public int[] getSaldos() {
		return saldos;
	}


/*
	protected void afschrijven() {
		Connection con = getConnection();
		SqlParameterValues values = new SqlParameterValues();
		values.addValue("userID", userID);

		this.insertItem(CHECK_SALDO, values);
	}
*/

}
